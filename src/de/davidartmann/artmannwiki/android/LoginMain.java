package de.davidartmann.artmannwiki.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.artmann.artmannwiki.R;

public class LoginMain extends Activity {
	
	private EditText passwordField;
	private EditText passwordField2;
	private Button loginButton;
	private TextView textView;
	private SharedPreferences sharedPreferences;
	private Editor editor;

    public static final String PREFS_NAME = "sprefsfile_artmannwiki";
    public static final String PREFS_ATTR = "pStr";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordField = (EditText) findViewById(R.id.login_password_field);
        passwordField2 = (EditText) findViewById(R.id.login_password_field_2);
        loginButton = (Button) findViewById(R.id.login_button);
        textView = (TextView) findViewById(R.id.login_textview);
        sharedPreferences = getSharedPreferences(PREFS_NAME, 0);    //0 == Activity.MODE_PRIVATE
        editor = sharedPreferences.edit();

        String passwd = sharedPreferences.getString(PREFS_ATTR, "");
        if(passwd.equals("")) {
            passwordField2.setVisibility(View.VISIBLE);
            textView.setText(R.string.prompt_register);
        }
        else {
            passwordField2.setVisibility(View.GONE);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String passwdSp = sharedPreferences.getString(PREFS_ATTR, "");
                String passField1 = passwordField.getText().toString().trim();
                String passField2 = passwordField2.getText().toString().trim();
                if(passwdSp.equals("")) {
                    if((passField1.equals(passField2)) && (passField1.length()>=6)) {
                        editor.putString(PREFS_ATTR, passField1).apply();
                        Toast.makeText(LoginMain.this, "Erfolgreiche erste Anmeldung", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getBaseContext(), Choice.class);
                        intent.putExtra("firstLogin", true);
                        startActivity(intent);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);
                    }
                    else if((!(passField1.equals(passField2))) && (passwordField.length()>=6)) {
                        Toast.makeText(LoginMain.this, R.string.prompt_password_unidentical, Toast.LENGTH_SHORT).show();
                   }
                    else if((passField1.equals(passField2)) && (passwordField.length()<6)) {
                        Toast.makeText(LoginMain.this, R.string.prompt_password_too_short, Toast.LENGTH_SHORT).show();
                    }
                    else if(!(passField1.equals(passField2)) && (passwordField.length()<6)) {
                        Toast.makeText(LoginMain.this, R.string.prompt_password_invalid+" und "
                        		+R.string.prompt_password_too_short, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(LoginMain.this, R.string.prompt_password_invalid, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(passField1.equals(passwdSp)) {
                        Intent intent = new Intent(getBaseContext(), Choice.class);
                        intent.putExtra("login", true);
                        startActivity(intent);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);
                    }
                    else {
                        Toast.makeText(LoginMain.this, R.string.prompt_password_invalid, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });//endOnClickListener
    }//endOnCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || id == R.id.action_exit || super.onOptionsItemSelected(item);
    }

    protected void onPause() {
        super.onPause();
    	exit();
    }

	private void exit() {
		finish();
        Runtime.getRuntime().addShutdownHook(new Thread() {
        	@Override
        	public void run() {
        		Intent intent = new Intent(Intent.ACTION_MAIN);
        		intent.addCategory(Intent.CATEGORY_HOME);
        		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		startActivity(intent);
        	}
        });
	}

	protected void onResume() {
		super.onResume();
	}
}
