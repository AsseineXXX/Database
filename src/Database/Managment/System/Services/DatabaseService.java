package Database.Managment.System.Services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Database.Managment.System.Models.Connexion;
import Database.Managment.System.Models.Database;
import Database.Managment.System.Utils.Constants;

public class DatabaseService {
	public static ArrayList<String> dbNames = new ArrayList<String>();
	
	public static Map<String, Object> createDatabase(String query, String path) {
		Map<String, Object> result = new HashMap<String, Object>();
		String[] items = query.split(" ");
		String dbName = items[2].replace(";", " ");
		try {
			//Lire (Deserialiser) le fichier contenant la liste des bases de donnees
			ArrayList<Database> dbs = new ArrayList<Database>();
			Path path_2 = Paths.get(path);
			if (Files.exists(path_2)) {
				FileInputStream fisDB = new FileInputStream(path);
				if(fisDB.available() > 0) { 
					ObjectInputStream oisDB= new ObjectInputStream(fisDB);
					dbs = (ArrayList<Database>) oisDB.readObject();
					oisDB.close();
				} 
			} 
			
			//Creer une nouvelle base de donnees et la sauvegarder dans le fichier serialise "Databases"
			if(exist(dbs, dbName)) {
				result.put("error", true); 
				result.put("dbId", null);
				result.put("message", "Nom dupplique, veuillez choisir un autre nom pour la Base de donnees !");
				System.out.println("Nom de BDD dupliquee");
			}else {
				Database db = new Database(dbName); 
				dbs.add(db);
				FileOutputStream fosDB = new FileOutputStream(path);
				ObjectOutputStream osDB = new ObjectOutputStream(fosDB);
				osDB.writeObject(dbs);
				System.out.println("BDD-CREATE serialisee avec succes");
				osDB.close();
				
				//Lire (Deserialiser) le fichier contenant la liste des bases de donnees
				FileInputStream fisBDD = new FileInputStream(path);
				ObjectInputStream oisBDD = new ObjectInputStream(fisBDD);
				ArrayList<Database> bdds = (ArrayList<Database>) oisBDD.readObject();
				System.out.println("BDD deserialisee avec succes");
				System.out.println("La liste de toutes les bases de donnees :");
				for(int i=0; i<bdds.size(); i++) {
					System.out.println("BDD "+(i+1)+" : "+bdds.get(i));
				}
				oisBDD.close();
				System.out.println("Fermeture BDD-CREATE");
				
				result.put("error", false);
				result.put("dbId", db.getIdBDD());
				result.put("message", "La base de donnees a ete creee avec success !");
			}
			
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			result.put("error", true);
			result.put("dbId", null);
			result.put("message", e.getMessage());
			return result;
		}
		
	}
	
	
	private static Boolean exist(ArrayList<Database> dbList, String name) {
		Boolean exist = false;
		for(int i=0; i<dbList.size(); i++) {
			if(dbList.get(i).getNameBDD().equals(name)) {
				exist = true;
			}
		}
		return exist;
	}
	
	
	public static Map<String, Object> deleteDatabase(String query, String path) {
		Map<String, Object> result = new HashMap<String, Object>();
		String[] items = query.split(" ");
		String dbName = items[2].replace(";", " ");
		try {
			//Lire (Deserialiser) le fichier contenant la liste des bases de donnees
			ArrayList<Database> dbs = new ArrayList<Database>();
			FileInputStream fisDB = new FileInputStream(path);
			if(fisDB.available() > 0) { 
				ObjectInputStream oisDB= new ObjectInputStream(fisDB);
				dbs = (ArrayList<Database>) oisDB.readObject();
				oisDB.close();
			}
			
			//Supprimer la base de donnees du fichier serialise "Databases"
			if(exist(dbs, dbName)) {
				Database db = getDatabaseByName(dbs, dbName);
				dbs.remove(db);
				FileOutputStream fosDB = new FileOutputStream(path);
				ObjectOutputStream osDB = new ObjectOutputStream(fosDB);
				osDB.writeObject(dbs);
				System.out.println("BDD-DROP : serialisee avec succes");
				osDB.close();

				
				//Lire (Deserialiser) le fichier contenant la liste des bases de donnees
				FileInputStream fisBDD = new FileInputStream(path);
				ObjectInputStream oisBDD = new ObjectInputStream(fisBDD);
				ArrayList<Database> bdds = (ArrayList<Database>) oisBDD.readObject();
				System.out.println("BDD-DROP deserialisee avec succes");
				System.out.println("La liste de toutes les bases de donnees :");
				for(int i=0; i<bdds.size(); i++) {
					System.out.println("BDD "+(i+1)+" : "+bdds.get(i));
				}
				oisBDD.close();
				System.out.println("Fermeture BDD-DROP");
				
				result.put("error", false);
				result.put("message", "La base de donnees a ete cree avec succes !");
			}else {
				result.put("error", true);
				result.put("message", "Base de donnée introuvable, veuillez choisir un autre nom pour la Base de données !");
				System.out.println("BDD introuvable");
			}
			
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			result.put("error", true);
			result.put("message", e.getMessage());
			return result;
		}
	}
	
	private static Database getDatabaseByName(ArrayList<Database> dbList, String name) {
		Database db = new Database();
		for(int i=0; i<dbList.size(); i++) {
			if(dbList.get(i).getNameBDD().equals(name)) {
				db = dbList.get(i);
			}
		}
		return db;
	}

	
	public static String getDBNameById(String dbId) {
		String dbName = "";
		try {
			//Lire (Deserialiser) le fichier contenant la liste des bases de donnees
			ArrayList<Database> dbList = new ArrayList<Database>();
			FileInputStream fisDB = new FileInputStream(Constants.STORED_FILES_PATH+"Databases.txt");
			if(fisDB.available() > 0) { 
				ObjectInputStream oisDB= new ObjectInputStream(fisDB);
				dbList = (ArrayList<Database>) oisDB.readObject();
				oisDB.close();
			}
			for(Database db : dbList) {
				if(db.getIdBDD().equals(dbId)) {
					dbName = db.getNameBDD();
				}
			}
			return dbName;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
