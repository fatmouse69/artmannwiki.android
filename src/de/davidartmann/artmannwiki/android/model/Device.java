package de.davidartmann.artmannwiki.android.model;

/**
 * 
 * This class stands for a Device. E.g. for a Tablet or a Smartphone/Handy.
 *<br>
 * <u>Attributes: </u>
 * <ul>
 * 	<li>name ({@link String})</li>
 * 	<li>number ({@link String})</li>
 * 	<li>pin ({@link String})</li>
 * 	<li>puk ({@link String})</li>
 * </ul>
 */
public class Device extends SoftDeleteEntity {

	private String name;
	
	private String number;
	
	private String pin;
	
	private String puk;
	
	public Device(String na, String nu, String pi, String pu) {
		this.name = na;
		this.number = nu;
		this.pin = pi;
		this.puk = pu;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getPuk() {
		return puk;
	}

	public void setPuk(String puk) {
		this.puk = puk;
	}

}