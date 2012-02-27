/**
 * 
 */
package contactsuite;

import java.io.Console;
import java.sql.*;
import java.util.*;

/**
 * @author Dominik Ferber
 *
 */
public class DatabaseConnection {
	private static DatabaseConnection instance = null;
	
	private final String dbBenutzername = "customers_s001";
	private final String dbPasswort = "dqJAMKPR2x5B5JV8";
	private final String dbName = "jdbc:mysql://pma.postopus.de/customers_s001";
	private final String tblBenutzer = "tblbenutzer";
	private final String tblKontakt = "tblkontakt";
	private Connection connection;
	
	private DatabaseConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(
					this.dbName, this.dbBenutzername, this.dbPasswort);
		} 
		catch (ClassNotFoundException err) {
			System.out.println("Datenbankverbindung kann nicht hergestellt werden.");
		} 
		catch (SQLException err) {
			System.out.println("Datenbankverbindung kann nicht hergestellt werden.");
		}
	}
	
	public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
	
	/**
	 * Methode zum speichern von Benutzern. Falls der �bergebene Benutzer schon
	 * vorhanden ist wird dieser geupdated. Sonst wird ein neuer Datensatz eingetragen.
	 * @author Dominik Ferber
	 * @param benutzer
	 * @return Anzahl der ver�nderten Datens�tze
	 */
	public int SpeicherDaten(Benutzer benutzer)
	{
		int geaenderteDatensaetze = 0;
		String sql = "SELECT benutzerID FROM "+tblBenutzer+" WHERE benutzerID = "+benutzer.getBenutzerID();
		try {
			Statement stmt = this.connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			//Benutzer ist schon vorhanden
			if(result.next()){				
				sql = "UPDATE "+tblBenutzer+";" +
						"SET email = '"+benutzer.getEmail()+"', " +
						"passwort = '"+benutzer.getPasswort()+"', " +
						"istAdmin = "+((benutzer.isIstAdmin())?"1":"0")+", " +
						"istFreigeschaltet = "+((benutzer.isIstFreigeschaltet())?"1":"0")+", " +
						"geaendertVon = -1, " +
						"geaendertAm = CURRENT_TIMESTAMP;";
			}
			//Neuer Benutzer
			else {
				sql = "INSERT INTO "+tblBenutzer+" " +
						"(email,passwort,istAdmin,istFreigeschaltet,erstelltVon) " +
						"VALUES " +
						"('"+benutzer.getEmail()+"'," +
						"'"+benutzer.getPasswort()+"'," +
						"false," +
						"false," +
						"-1);";
			}
			geaenderteDatensaetze = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return geaenderteDatensaetze;
	}
	
	/**
	 * Methode zum speichern von Firmenkontakten. Falls der �bergebene Firmenkontakt schon
	 * vorhanden ist wird dieser geupdated. Sonst wird ein neuer Datensatz eingetragen.
	 * @author Dominik Ferber
	 * @param firmenkontakt
	 * @return Anzahl der ver�nderten Datens�tze
	 */
	public int SpeicherDaten(Firmenkontakt firmenkontakt)
	{
		int geaenderteDatensaetze = 0;
		String sql = "SELECT kontaktID FROM "+tblKontakt+" WHERE kontaktID = "+firmenkontakt.getKontaktID();
		try {
			Statement stmt = this.connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			//Kontakt ist schon vorhanden
			if(result.next()){				
				sql = "UPDATE "+tblKontakt+
						"SET plz = '"+firmenkontakt.getPlz()+"', " +
						"strasse = '"+firmenkontakt.getStrasse()+"', " +
						"hausnummer = '"+firmenkontakt.getHausnummer()+"', " +
						"ort = '"+firmenkontakt.getOrt()+"', " +
						"email = '"+firmenkontakt.getEmail()+"', " +
						"telefonnummer = '"+firmenkontakt.getTelefonnummer()+"', " +
						"bildpfad = '"+firmenkontakt.getBildpfad()+"', " +
						"firmenname = '"+firmenkontakt.getFirmenname()+"', " +
						"ansprechpartner = '"+firmenkontakt.getAnsprechpartner()+"', " +
						"vorname = '', " +
						"nachname = '', " +
						"istFirmenkontakt = 1, " +
						"istOeffentlich = "+((firmenkontakt.isIstOeffentlich())?"1":"0")+", " +
						"geaendertVon = "+firmenkontakt.erstelltVon.getBenutzerID()+", " +
						"geaendertAm = CURRENT_TIMESTAMP";
			}
			//Neuer Benutzer
			else {
				sql = "INSERT INTO "+tblKontakt+" " +
						"(plz,strasse,hausnummer,ort,email,telefonnummer,bildpfad,firmenname,ansprechpartner,istFirmenkontakt,istOeffentlich,erstelltVon) " +
						"VALUES " +
						"('"+firmenkontakt.getPlz()+"'," +
						"'"+firmenkontakt.getStrasse()+"'," +
						"'"+firmenkontakt.getHausnummer()+"'," +
						"'"+firmenkontakt.getOrt()+"'," +
						"'"+firmenkontakt.getEmail()+"'," +
						"'"+firmenkontakt.getTelefonnummer()+"'," +
						"'"+firmenkontakt.getBildpfad()+"'," +
						"'"+firmenkontakt.getFirmenname()+"'," +
						"'"+firmenkontakt.getAnsprechpartner()+"'," +
						"1," +
						((firmenkontakt.isIstOeffentlich())?"1":"0")+"," +
						firmenkontakt.getErstelltVon().getBenutzerID()+");";
			}
			geaenderteDatensaetze = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return geaenderteDatensaetze;
	}
	
	/**
	 * Methode zum speichern von Privatkontakten. Falls der �bergebene Firmenkontakt schon
	 * vorhanden ist wird dieser geupdated. Sonst wird ein neuer Datensatz eingetragen.
	 * @author Dominik Ferber
	 * @param privatkontakt
	 * @return Anzahl der ver�nderten Datens�tze
	 */
	public int SpeicherDaten(Privatkontakt privatkontakt)
	{
		int geaenderteDatensaetze = 0;
		String sql = "SELECT kontaktID FROM "+tblKontakt+" WHERE kontaktID = "+privatkontakt.getKontaktID();
		try {
			Statement stmt = this.connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			//Kontakt ist schon vorhanden
			if(result.next()){
				sql = "UPDATE "+tblKontakt+
						"SET plz = '"+privatkontakt.getPlz()+"', " +
						"strasse = '"+privatkontakt.getStrasse()+"', " +
						"hausnummer = '"+privatkontakt.getHausnummer()+"', " +
						"ort = '"+privatkontakt.getOrt()+"', " +
						"email = '"+privatkontakt.getEmail()+"', " +
						"telefonnummer = '"+privatkontakt.getTelefonnummer()+"', " +
						"bildpfad = '"+privatkontakt.getBildpfad()+"', " +
						"firmenname = '', " +
						"ansprechpartner = '', " +
						"vorname = '"+privatkontakt.getVorname()+"', " +
						"nachname = '"+privatkontakt.getNachname()+"', " +
						"istFirmenkontakt = 0, " +
						"istOeffentlich = "+((privatkontakt.isIstOeffentlich())?"1":"0")+", " +
						"geaendertVon = "+privatkontakt.erstelltVon.getBenutzerID()+", " +
						"geaendertAm = CURRENT_TIMESTAMP";
			}
			//Neuer Benutzer
			else {
				sql = "INSERT INTO "+tblKontakt+" " +
						"(plz,strasse,hausnummer,ort,email,telefonnummer,bildpfad,vorname,nachname,istFirmenkontakt,istOeffentlich,erstelltVon) " +
						"VALUES " +
						"('"+privatkontakt.getPlz()+"'," +
						"'"+privatkontakt.getStrasse()+"'," +
						"'"+privatkontakt.getHausnummer()+"'," +
						"'"+privatkontakt.getOrt()+"'," +
						"'"+privatkontakt.getEmail()+"'," +
						"'"+privatkontakt.getTelefonnummer()+"'," +
						"'"+privatkontakt.getBildpfad()+"'," +
						"'"+privatkontakt.getVorname()+"'," +
						"'"+privatkontakt.getNachname()+"'," +
						"1," +
						((privatkontakt.isIstOeffentlich())?"1":"0")+"," +
						privatkontakt.getErstelltVon().getBenutzerID()+");";
			}
			geaenderteDatensaetze = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return geaenderteDatensaetze;
	}
	
	/**
	 * Methode pr�ft ob ein �bergebener Benutzer bereits in der Datenbank vorhanden ist.
	 * @author Dominik Ferber
	 * @param benutzer
	 * @return true falls Benutzer bereits vorhanden, sonst false.
	 */
	public boolean IstBenutzerVorhanden(Benutzer benutzer){
		boolean istVorhanden = false;
		
		String sql = "SELECT benutzerID FROM "+tblBenutzer+" WHERE email = '"+benutzer.getEmail()+"';";
		try {
			Statement stmt = this.connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			//Kontakt ist schon vorhanden
			if(result.next()){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return istVorhanden;		
	}
	
	/**
	 * Liest alle Privatkontakte aus der Datenbank aus.
	 * @author Dominik Ferber
	 * @return
	 */
	public List<Privatkontakt> getPrivatkontakte(){
		List<Privatkontakt> lstKontakte = new ArrayList<Privatkontakt>();
		Privatkontakt tmpKontakt = new Privatkontakt();;
		String sql = "SELECT * FROM "+tblKontakt+" WHERE istFirmenkontakt = 0 AND istGeloescht = 0 ORDER BY nachname;";
		try{
			Statement stmt = this.connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			System.out.println(result.toString());
			while(result.next()){
				tmpKontakt.setPlz(result.getString("plz"));
				tmpKontakt.setStrasse(result.getString("strasse"));
				tmpKontakt.setHausnummer(result.getString("hausnummer"));
				tmpKontakt.setOrt(result.getString("ort"));
				tmpKontakt.setEmail(result.getString("email"));
				tmpKontakt.setTelefonnummer(result.getString("telefonnummer"));
				tmpKontakt.setBildpfad(result.getString("bildpfad"));
				tmpKontakt.setVorname(result.getString("Vorname"));
				tmpKontakt.setNachname(result.getString("Nachname"));
				tmpKontakt.setIstOeffentlich(result.getBoolean("istOeffentlich"));
				tmpKontakt.setErstelltVon(getBenutzerById(result.getInt("erstelltVon")));
				tmpKontakt.setErstelltAm(result.getDate("erstelltAm"));
				lstKontakte.add(tmpKontakt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		return lstKontakte;
	}
	
	/**
	 * Liest einen Benutzer anhand von einer �bergebenen benutzerId aus.
	 * @author Dominik Ferber
	 * @param benutzerId
	 * @return Benutzer mit der benutzerId
	 */
	private Benutzer getBenutzerById(int benutzerId) {
		Benutzer benutzer = new Benutzer();
		String sql = "SELECT email FROM "+tblBenutzer+" WHERE benutzerId = "+benutzerId+" AND istGeloescht = 0;";
		try{
			Statement stmt = this.connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			while(result.next()){
				benutzer.setEmail(result.getString("email"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return benutzer;
	}
	
	public Benutzer getBenutzerByEmail(String email){
		Benutzer benutzer = new Benutzer();
		String sql = "SELECT email, passwort FROM "+tblBenutzer+" WHERE email = "+email+" AND istGeloescht = 0;";
		try{
			Statement stmt = this.connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			while(result.next()){
				benutzer.setEmail(result.getString("email"));
				benutzer.setPasswort(result.getString("passwort"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return benutzer;
	}

	public int executeUpdateQuery(String sqlCommand){
		int manipulierteDatensaetze = 0;
		try {
			Statement stmt = this.connection.createStatement();
			manipulierteDatensaetze = stmt.executeUpdate(sqlCommand);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return manipulierteDatensaetze;
	}
}