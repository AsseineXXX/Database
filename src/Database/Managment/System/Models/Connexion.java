package Database.Managment.System.Models;

import java.io.Serializable;

public class Connexion implements Serializable {
	private String login;
	private transient String pwd;
	private String dbId; 
	private static final long serialVersionUID = -7963192496290245564L;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public Connexion(String login, String pwd, String dbId ) {
	       this.login = login ;
	       this.pwd =  pwd;
	       this.dbId = dbId;
	}
	
	public String toString() {
		return "Connexion : [ Login : "+login+", Password : "+pwd+"]";
	}
}
