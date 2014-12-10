package de.davidartmann.artmannwiki.android.model;

/**
 * 
 * This class stands for a Bank Account. E.g. for Sparkasse or DiBa.
 *
 */
public class Account extends SoftDeleteEntity {

	private String owner;
	
	private String iban;
	
	private String bic;
	
	private String pin;
	
	public Account(String o, String i, String b, String p) {
		this.owner = o;
		this.iban = i;
		this.bic = b;
		this.pin = p;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
	
}
