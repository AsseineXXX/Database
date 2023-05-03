package Database.Managment.System.Tests;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Database.Managment.System.Models.Colonne;
import Database.Managment.System.Models.Connexion;
import Database.Managment.System.Models.DataTable;
import Database.Managment.System.Models.Database;
import Database.Managment.System.Services.QueryService;
import Database.Managment.System.Utils.Constants;

public class Test {

	public static void main(String args[]){  
		try {
			//Creer et Serialiser une connexion
			// Dans le future on va verifier que l'utilisateur est bien connect� avant de faire n'import quelle action
			Connexion conn = new Connexion("admin", "test", "test bdd id");
			FileOutputStream fosConn = new FileOutputStream(Constants.STORED_FILES_PATH+"Connexions.txt");
			ObjectOutputStream osConn = new ObjectOutputStream(fosConn);
			osConn.writeObject(conn);
			System.out.println("connexion serialisee avec succes");
			osConn.close();
			
			//Lire (Deserialiser) la connexion stockee dans le fichier 
			FileInputStream fisConn = new FileInputStream(Constants.STORED_FILES_PATH+"Connexions.txt");
			ObjectInputStream oisConn = new ObjectInputStream(fisConn);
			Connexion c = (Connexion) oisConn.readObject();
			System.out.println("connexion deserialisee avec success");
			System.out.println(c.toString());
			oisConn.close();
			
			System.out.println("Fermeture Connexion\n\n");
			
			
			
			//Creer et Serialiser une BDD 
			ArrayList<Connexion> connexions = new ArrayList();
			connexions.add(c);
			Database db = new Database("BDD-Test", connexions);
			FileOutputStream fosBDD = new FileOutputStream(Constants.STORED_FILES_PATH+"Databases.txt");
			ObjectOutputStream osBDD = new ObjectOutputStream(fosBDD);
			osBDD.writeObject(db);
			System.out.println("BDD serialisee avec succes");
			osBDD.close();
			
			//Lire (Deserialiser) la Base de donnees stock�e dans le fichier 
			FileInputStream fisBDD = new FileInputStream(Constants.STORED_FILES_PATH+"Databases.txt");
			ObjectInputStream oisBDD = new ObjectInputStream(fisBDD);
			Database bdd = (Database) oisBDD.readObject();
			System.out.println("BDD deserialisee avec succes");
			System.out.println(bdd);
			oisBDD.close();
			
			System.out.println("Fermeture BDD\n\n");
			
			
			
			//Creer les colonnes (en dur dans le code pour l'instant, a modifier plustard)
			ArrayList<Colonne> columns = new ArrayList<Colonne>();
			Colonne col_1 = new Colonne ("FirstName", "String", false, false, false);
			Colonne col_2 = new Colonne ("LastName", "String", false, false, false);
			Colonne col_3 = new Colonne ("Date de Naissance", "Date", false, false, false);
			Colonne col_4 = new Colonne ("Numero Etudiant", "String", true, false, false);
			columns.add(col_1);
			columns.add(col_2);
			columns.add(col_3);
			columns.add(col_4);
			
			//Creer et serialiser une table
			DataTable table = new DataTable("Etudiant", bdd.getIdBDD(), columns);
			DataTable table2 = new DataTable("Personne", bdd.getIdBDD(), columns);
			ArrayList<DataTable> tabs = new ArrayList<DataTable>();
			tabs.add(table2);
			tabs.add(table);
			FileOutputStream fosTab = new FileOutputStream(Constants.STORED_FILES_PATH+bdd.getNameBDD()+"_Tables.txt");
			ObjectOutputStream osTab = new ObjectOutputStream(fosTab);
			osTab.writeObject(tabs);
			System.out.println("Table tabs serialisee avec succes");
			osTab.close();
			
			//Lire (Deserialiser) la Base de donnees stock�e dans le fichier 
			FileInputStream fisTab = new FileInputStream(Constants.STORED_FILES_PATH+bdd.getNameBDD()+"_Tables.txt");
			ObjectInputStream oisTab = new ObjectInputStream(fisTab);
			ArrayList<DataTable> ts = (ArrayList<DataTable>) oisTab.readObject();
			System.out.println("Table deserialisee");
			for(int i=0; i<ts.size(); i++) {
				System.out.println(ts.get(i));
			}
			
			oisTab.close();
			
			System.out.println("Fermeture Table");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}  
}
