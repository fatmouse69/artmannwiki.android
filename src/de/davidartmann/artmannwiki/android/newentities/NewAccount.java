package de.davidartmann.artmannwiki.android.newentities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.Choice;
import de.davidartmann.artmannwiki.android.backend.VolleyRequestQueue;
import de.davidartmann.artmannwiki.android.database.AccountManager;
import de.davidartmann.artmannwiki.android.model.Account;

public class NewAccount extends Activity {
	
	private EditText ownerEditText;
	private EditText ibanEditText;
	private EditText bicEditText;
	private EditText pinEditText;
	private Button saveButton;
	private AccountManager accountManager;
	private String pleaseFillField;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_account);
		// find components
		ownerEditText = (EditText) findViewById(R.id.activity_new_account_edittext_owner);
		ibanEditText = (EditText) findViewById(R.id.activity_new_account_edittext_iban);
		bicEditText = (EditText) findViewById(R.id.activity_new_account_edittext_bic);
		pinEditText = (EditText) findViewById(R.id.activity_new_account_edittext_pin);
		saveButton = (Button) findViewById(R.id.activity_new_account_button_save);
		pleaseFillField = "Bitte ausf�llen";
		
		checkIfUpdate();
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (getIntent().getBooleanExtra("update", false)) {
					updateAccount();
				} else {
					validate(ownerEditText, ibanEditText, bicEditText, pinEditText);
				}
			}
		});
	}

	/**
	 * Method to send the created account to the backend.
	 * @param a ({@link Account})
	 */
	private void createInBackend(final Account a) {
		String url = "http://213.165.81.7:8080/ArtmannWiki/rest/account/post/add";
		JSONObject jAccount = new JSONObject();
		try {
			jAccount.put("active", a.isActive());
			jAccount.put("owner", a.getOwner());
			jAccount.put("iban", a.getIban());
			jAccount.put("bic", a.getBic());
			jAccount.put("pin", a.getPin());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		System.out.println(jAccount.toString());
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jAccount, 
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
		               try {
		                   	VolleyLog.v("Response:%n %s", response.toString(4));
		                   	accountManager = new AccountManager(NewAccount.this);
		           			accountManager.openWritable(NewAccount.this);
		           			accountManager.addBackendId(a.getId(), response.getLong("id"));
		               } catch (JSONException e) {
		                   e.printStackTrace();
		               }
		           }
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());					
				}
			}) {
		       public Map<String, String> getHeaders() throws AuthFailureError {
		           HashMap<String, String> headers = new HashMap<String, String>();
		           headers.put("artmannwiki_headerkey", "blafoo");
		           headers.put("Content-Type", "application/json");
		           return headers;
		       }
		};
		VolleyRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
	}

	// TODO: validation necessary, because when text is removed null values could be stored
	protected void updateAccount() {
		Account a = (Account) getIntent().getSerializableExtra("account");
		a.setOwner(ownerEditText.getText().toString().trim());
		a.setIban(ibanEditText.getText().toString().trim());
		a.setBic(bicEditText.getText().toString().trim());
		a.setPin(pinEditText.getText().toString().trim());
		a.setLastUpdate(new Date());
		accountManager = new AccountManager(this);
		accountManager.openWritable(this);
		accountManager.updateAccount(a);
		accountManager.close();
		Toast.makeText(this, "Bankkonto erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();
		goBackToMain();
	}

	// fill the fields with the data of the account to update
	private void checkIfUpdate() {
		if (getIntent().getSerializableExtra("account") != null) {
			Account a = (Account) getIntent().getSerializableExtra("account");
			ownerEditText.setText(a.getOwner());
			ibanEditText.setText(a.getIban());
			bicEditText.setText(a.getBic());
			pinEditText.setText(a.getPin());
		}
	}

	protected void validate(EditText ownerEditText2, EditText ibanEditText2, EditText bicEditText2, EditText pinEditText2) {
		String owner = ownerEditText2.getText().toString().trim();
		String iban = ibanEditText2.getText().toString().trim();
		String bic = bicEditText2.getText().toString().trim();
		String pin = pinEditText2.getText().toString().trim();
		
		if (owner.isEmpty()) {
			ownerEditText2.setError(pleaseFillField);
		}
		if (iban.isEmpty()) {
			ibanEditText2.setError(pleaseFillField);
		}
		if (bic.isEmpty()) {
			bicEditText2.setError(pleaseFillField);
		}
		if (pin.isEmpty()) {
			pinEditText2.setError(pleaseFillField);
		}
		if (!owner.isEmpty() && !iban.isEmpty() && !bic.isEmpty() && !pin.isEmpty()) {
			Account a = new Account(owner,iban, bic, pin);
			a.setActive(true);
			a.setCreateTime(new Date());
			accountManager = new AccountManager(this);
			accountManager.openWritable(this);
			a = accountManager.addAccount(a);
			accountManager.close();
			createInBackend(a);
			Toast.makeText(this, "Bankkonto erfolgreich abgespeichert", Toast.LENGTH_SHORT).show();
			goBackToMain();
		}
		
	}

	private void goBackToMain() {
		Intent intent = new Intent(getBaseContext(), Choice.class);
		startActivity(intent);
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

}
