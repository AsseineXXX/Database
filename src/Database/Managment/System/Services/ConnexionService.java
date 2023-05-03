package Database.Managment.System.Services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Database.Managment.System.Models.Connexion;
import Database.Managment.System.Models.Database;
import Database.Managment.System.Utils.Constants;
import Database.Managment.System.Services.DatabaseService;

public class ConnexionService {
	
	//Creer une nouvelle connexion pour une BDD et la stocker dans le fichier "Connexions.txt"
	public static Map<String, Object> createNewConnexion(String dbId) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//Lire (Deserialiser) le fichier contenant la liste des connexions 
			ArrayList<Connexion> connexions = new ArrayList<Connexion>();
			File file = new File(Constants.STORED_FILES_PATH+"Connections.txt");
			if(file.exists()) {
				FileInputStream fisConn = new FileInputStream(Constants.STORED_FILES_PATH+"Connections.txt");
				System.out.println("fisConn.available() : "+fisConn.available());
				if(fisConn.available() > 0) { 
					ObjectInputStream oisConn= new ObjectInputStream(fisConn);
					connexions = (ArrayList<Connexion>) oisConn.readObject();
					oisConn.close();
				}
			}
			
			String dbName = DatabaseService.getDBNameById(dbId);
			Connexion conn = new Connexion("admin", dbName+"-pwd", dbId);
			connexions.add(conn);
			FileOutputStream fosConn = new FileOutputStream(Constants.STORED_FILES_PATH+"Connections.txt");
			ObjectOutputStream osConn = new ObjectOutputStream(fosConn);
			osConn.writeObject(connexions);
			System.out.println("connexion serialisee avec succes");
			osConn.close();
			
			//Lire (Deserialiser) le fichier connexions.txt contenant toutes les connexions aux bases de données
			FileInputStream fisCon = new FileInputStream(Constants.STORED_FILES_PATH+"Connections.txt");
			ObjectInputStream oisCon = new ObjectInputStream(fisCon);
			ArrayList<Connexion> connList = (ArrayList<Connexion>) oisCon.readObject();
			System.out.println("connexion deserialisee avec success");
			for(int i=0; i<connList.size(); i++) {
				System.out.println("Connexion "+(i+1)+" : "+connList.get(i));
			}
			oisCon.close();
			
			System.out.println("Fermeture Connexion\n\n");
			
			result.put("error", false);
			result.put("message", null);
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			result.put("error", true);
			result.put("message", e.getMessage());
			return result;
		}
		
	}

}
