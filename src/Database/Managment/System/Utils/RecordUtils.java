package Database.Managment.System.Utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Database.Managment.System.Models.Colonne;
import Database.Managment.System.Models.DataTable;
import Database.Managment.System.Models.Record;

public class RecordUtils {

	public static Map<String, Object>  createRecord(String row[], DataTable tab, String path) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//Lire (Deserialiser) le fichier contenant la liste des enregistrement de la table
			ArrayList<Record> recs = new ArrayList<Record>(); 
			String tabName = tab.getTableName();
			String pathToRecs = path+tab.getIdBDD()+"_"+tabName+"_Records.txt";
			Path path_2 = Paths.get(pathToRecs);
			if (Files.exists(path_2)) {
				FileInputStream fisRec = new FileInputStream(pathToRecs);
				if(fisRec.available() > 0) {
					ObjectInputStream oisRec = new ObjectInputStream(fisRec);
					recs = (ArrayList<Record>) oisRec.readObject();
					System.out.println("Record deserialisee");
					for(int i=0; i<recs.size(); i++) {
						System.out.println(recs.get(i)); 
					}
					oisRec.close(); 
				}
			}else {
				//Ajouter la premiere ligne au fichier des records contenant le nom des colonnes
				String columns =  getColumnNames(tab);
				Record rec = new Record();
				rec.tableName = tab.getTableName();
				rec.row = columns;
				recs.add(rec);
				FileOutputStream fisR = new FileOutputStream(pathToRecs);
				ObjectOutputStream osR = new ObjectOutputStream(fisR);
				osR.writeObject(recs);
				System.out.println("RECORD-LINE-COLUMNS : serialisee avec succes");
				osR.close();
			}
			
			//Ajouter un record au fichier des record de la table en cours
			String line = getLine(row);
			Record rec = new Record();
			rec.row = line;
			rec.tableName = tab.getTableName();
			recs.add(rec);
			FileOutputStream fosRec = new FileOutputStream(pathToRecs);
			ObjectOutputStream osRec = new ObjectOutputStream(fosRec);
			osRec.writeObject(recs);
			System.out.println("RECORD-LINE : serialisee avec succes");
			osRec.close();

				
			//Lire (Deserialiser) le fichier contenant la liste des enregistrements
			FileInputStream fisRecord = new FileInputStream(pathToRecs);
			ObjectInputStream oisRecord = new ObjectInputStream(fisRecord);
			ArrayList<Record> records = (ArrayList<Record>) oisRecord.readObject();
			System.out.println("BDD-INSERT deserialisee avec succes");
			System.out.println("La liste de tous les enregistrements est :");
			for(int i=0; i<records.size(); i++) {
				System.out.println("RECORD "+(i+1)+" : "+records.get(i));
			}
			oisRecord.close();
			System.out.println("Fermeture BDD-INSERT");
				
			result.put("error", false);
			result.put("message", "Le record a ete insere avec succes !");
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			result.put("error", true);
			result.put("message","Erreur dans 'createRecord' :"+ e.getMessage());
			return result;
		}
		
	
	}
	
	private static String getLine(String[] row) {
		String line = "";
		if(row.length>0) {
			for(int i=0; i<row.length-1; i++) {
				line = line + row[i]+", ";
			}
			line = line + row[row.length-1];
			System.out.println("LINE : "+line);
		}
		return line;
	}
	
	private static String getColumnNames(DataTable table) {
		String columnNames = "";
		int n = table.getColumns().size();
		ArrayList<Colonne> columns = table.getColumns();
		for(int i=0; i<n-1; i++) {
			columnNames = columnNames + columns.get(i).getColumnName() +", ";
		}
		columnNames = columnNames + columns.get(n-1).getColumnName();
		System.out.println("columnNames String : "+columnNames);
		return columnNames;
	}
}
