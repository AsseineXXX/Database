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

import Database.Managment.System.Models.Colonne;
import Database.Managment.System.Models.DataTable;
import Database.Managment.System.Utils.Constants;

public class TableService { 
	
	private static Map<String, Colonne> mapCols = new HashMap<String, Colonne>();
	
	public static Map<String, Object> createTable(String query, String currentDB, String path) {
		Map<String, Object> result = new HashMap<String, Object>(); 
		String[] items = query.split("\\([^0-9]");
		String[] commandItems = items[0].split(" ");
		String tableName = commandItems[2]; 
		
		String[] columnItems = items[1].split(",");
		ArrayList<Colonne> columns = new ArrayList<Colonne>();
		System.out.println("@@@ columnItems.length : "+columnItems.length);
		if(columnItems.length>0) {
			for(int i=0; i<columnItems.length; i++) {
				System.out.println("@@@ columnItem i : "+columnItems[i].trim());
				Colonne col = createColumn(columnItems[i].trim(), i);
				columns.add(col);
			}
			System.out.println("@@@ COLONNES CCCC : "+columns);
			result = createTab(tableName, columns, path, currentDB);
		}else {
			result.put("error", true);
			result.put("message", "Erreur : la liste des colonnes est vide ou indefinie");
		}
		return result;
		
	} 
	
	public static void deleteTable(String query) { 
		
	} 
	
	
	public static Colonne createColumn(String column, Integer position){
		Colonne col = new Colonne();
		String[] items = column.split(" ");
		for(int i=0; i<items.length; i++) {
			System.out.println("items[i] : "+items[i]+i);
		}
		if(items[0].equals("PRIMARY")) {
			Colonne c = mapCols.get(items[2]);
			c.setIsPrimaryKey(true);
		}else if(items[0].equals("FOREIGN")) {
			Colonne c = mapCols.get(items[2]);
			c.setIsForeignKey(true);
		}else {
			if(items.length == 5) { // is Unique
				col = new Colonne(items[0], items[1], true, false, false);
			}else {
				col = new Colonne(items[0], items[1], false, false, false);
			}
		}
		col.setPosition(position);
		System.out.println(col.toString());
		mapCols.put(col.getColumnName(), col);
		return col;
	}
	
	public static Map<String, Object> createTab(String tableName, ArrayList<Colonne> columns, String path, String currentDB) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//Lire (Deserialiser) les tables stockees dans le fichier
			ArrayList<DataTable> ts = new ArrayList<DataTable>(); 
			Path path_2 = Paths.get(path);
			if (Files.exists(path_2)) {
				FileInputStream fisTab = new FileInputStream(path);
				if(fisTab.available() > 0) {
					ObjectInputStream oisTab = new ObjectInputStream(fisTab);
					ts = (ArrayList<DataTable>) oisTab.readObject();
					System.out.println("Table deserialisee");
					for(int i=0; i<ts.size(); i++) {
						System.out.println(ts.get(i));
					}
					oisTab.close();
				}
				
			}
			
			//Creer et serialiser une table
			DataTable table = new DataTable(tableName, currentDB, columns);
			ts.add(table);
			FileOutputStream fosTab = new FileOutputStream(path);
			ObjectOutputStream osTab = new ObjectOutputStream(fosTab);
			osTab.writeObject(ts);
			System.out.println("Table serialisee");
			osTab.close();
			
			//Lire (Deserialiser) la Base de donnees stockee dans le fichier 
			FileInputStream fisTab_2 = new FileInputStream(path);
			
			ObjectInputStream oisTab_2 = new ObjectInputStream(fisTab_2);
			ArrayList<DataTable> tabs = (ArrayList<DataTable>) oisTab_2.readObject();
			System.out.println("Table deserialisee");
			for(int i=0; i<tabs.size(); i++) {
				System.out.println(tabs.get(i));
			}
			oisTab_2.close();
			
			System.out.println("Fermeture Table");
			System.out.println("BDD-TABLE : Table cree");
			
			result.put("error", false);
			result.put("message", "La table a ete cree avec succes !");
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			result.put("error", true);
			result.put("message", e.getMessage());
			return result;
		}
	}
	
}
