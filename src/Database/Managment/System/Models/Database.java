package Database.Managment.System.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Database implements Serializable{
	private String nameBDD;
	private String idBDD;
	private ArrayList<DataTable> tables;
	private ArrayList<Connexion> connexions;
	
	private static final long serialVersionUID = -7443401478087545388L;
	
	
	public String getNameBDD() {
		return nameBDD;
	} 
	public void setNameBDD(String nameBDD) {
		this.nameBDD = nameBDD;
	}
	public String getIdBDD() {
		return idBDD;
	}
	public void setIdBDD(String idBDD) {
		this.idBDD = idBDD;
	}
	public ArrayList<DataTable> getTables() {
		return tables;
	}
	public void setTables(ArrayList<DataTable> tables) {
		this.tables = tables;
	}
	public ArrayList<Connexion> getConnexions() {
		return connexions;
	}
	public void setConnexions(ArrayList<Connexion> connexions) {
		this.connexions = connexions;
	}
	
	
	public String toString() {
		String s =  "Database : [ Name : "+nameBDD+", ID : "+idBDD;
		if(connexions != null) {
			s = s+", Connexion : \n";
			for(int i=0; i<connexions.size(); i++) {
				s = s + connexions.get(i).toString() + "\n";
			}
			s = s+ "]";
		}
		
		
		return s;
	}
	
	
	public Database() {
	}
	
	public Database(String name) {
	       this.nameBDD = name ;
	       this.idBDD =  UUID.randomUUID().toString();
	}
	
	public Database(String name, ArrayList<Connexion> connexions) {
	       this.nameBDD = name ;
	       this.idBDD =  UUID.randomUUID().toString();
	       this.connexions = connexions;
	}
	
}
