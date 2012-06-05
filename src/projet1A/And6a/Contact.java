package projet1A.And6a;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class Contact {
	
	private int id;
	private String name;
	private String lastName;
	private int promo;
	private String classe;
	private String year;
	private ArrayList<String> addrs;
	private ArrayList<String> emails;
	private ArrayList<String> tels;
	
	
	// Constructeur
	private Contact() {
		this.addrs = new ArrayList<String>();
		this.emails = new ArrayList<String>();
		this.tels = new ArrayList<String>();	
	}
	
	// Fonction pour créer un contacte à partir du flux simplifié
	public static Contact createSimpleContact(JSONObject jsonObject) {
		Contact c = new Contact();
		
		c.id = jsonObject.optInt("id");
		c.name = jsonObject.optString("name");
		c.lastName = jsonObject.optString("lastName");
		c.promo = jsonObject.optInt("promo");
		c.classe = jsonObject.optString("class");
		c.year = jsonObject.optString("year");
		
		c.emails.add(jsonObject.optString("email"));
		c.tels.add(jsonObject.optString("tel"));
		
		return c;
	}
	
	// Fonction pour créer un contacte à partir du flux détaillé
	public static Contact createDetailedContact(JSONObject jsonObject) {
		Contact c = new Contact();
		
		c.id = jsonObject.optInt("id");
		c.name = jsonObject.optString("name");
		c.lastName = jsonObject.optString("lastName");
		c.promo = jsonObject.optInt("promo");
		c.classe = jsonObject.optString("class");
		c.year = jsonObject.optString("year");
		
		// On récupère les addresses
		JSONArray jsonArray = jsonObject.optJSONArray("addrs");
		for (int i = 0; i<jsonArray.length(); i++) {
			c.addrs.add(jsonArray.optString(i));
		}
		
		// On récupère les emails
		jsonArray = jsonObject.optJSONArray("emails");
		if (jsonArray != null)
			for (int i = 0; i<jsonArray.length(); i++)
				c.emails.add(jsonArray.optString(i));
		
		// On récupère les téléphones
		jsonArray = jsonObject.optJSONArray("tels");
		if (jsonArray != null)
			for (int i = 0; i<jsonArray.length(); i++)
				c.tels.add(jsonArray.optString(i));					
		return c;
	}
	
	/*
	 * ------------------
	 * --- Accesseurs ---
	 * ------------------
	 */
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public int getPromo() {
		return this.promo;
	}
	
	public String getClasse() {
		return this.classe;
	}
	
	public String getYear() {
		return this.year;
	}
	
	public String getFirstEmail() {
		if (this.emails.size()>0)
			return this.emails.get(0);
		else
			return "";
	}
	
	public String getFirstTel() {
		if (this.tels.size()>0)
			return this.tels.get(0);
		else
			return "";
	}
	
	public Iterator<String> getAddrsIterator() {
		return this.addrs.iterator();
	}
	
	public Iterator<String> getEmailsIterator() {
		return this.emails.iterator();
	}
	
	public Iterator<String> getTelsIterator() {
		return this.tels.iterator();
	}

}