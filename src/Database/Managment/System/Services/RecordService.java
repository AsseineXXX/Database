package Database.Managment.System.Services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Database.Managment.System.Models.DataTable;
import Database.Managment.System.Models.Record;
import Database.Managment.System.Utils.RecordUtils;
import Database.Managment.System.Utils.TablesUtils;

public class RecordService {

	public static Map<String, Object> insertRecord(String query, String currentDB, String path) { 
		Map<String, Object> result = new HashMap<String, Object>();
			String[] items = query.split("\\([^0-9]");
			String[] commandItems = items[0].split(" "); 
			String tableName = commandItems[2];
			Map<String, Object> table = TablesUtils.getTableByName(tableName,  currentDB);
			if((boolean) table.get("error")) { 
				result.put("error", true);
				result.put("message", "La Table est introuvable, Veuillez inserer dans une autre table");
			}else {
				String[] columnNames = query.substring(query.indexOf("(") + 1, query.indexOf(")")).split(", ");
		        String[] columnValues = query.substring(query.lastIndexOf("(") + 1, query.lastIndexOf(")")).split(",");
				
				ArrayList<String> columns = new ArrayList<String>();
				ArrayList<String> values = new ArrayList<String>();
				
				for(int i=0; i<columnNames.length; i++) {
					columns.add(columnNames[i].trim());
				}
				for(int i=0; i<columnValues.length; i++) {
					values.add(columnValues[i].trim());
				}
				DataTable tab = (DataTable) table.get("table");  
				Map<String, Object> rowInfo = TablesUtils.addRaw(tab,columns,values );
				if((Boolean) rowInfo.get("error") == false) {
					String[] row = (String[]) rowInfo.get("row");
					result = RecordUtils.createRecord(row, tab, path);
				}else {
					result = rowInfo;
				}
				
			}
			
			return result;
		
		
	}
	
	
	
	public static void deleteRecord(String query) {
		
	}
	
	public static Map<String,Object> selectRecords(String query, String currentDB, String path) {
		Map<String,Object> result = new HashMap<String,Object>();
		query = query.replaceAll(", ",",");
		System.out.println("query : "+query);
		// D�couper la requ�te en parties distinctes
	      String[] words = query.split("\\s+");
	      System.out.println("@@@ words : "+Arrays.toString(words));

	      // Analyser les mots de la requ�te pour extraire les informations n�cessaires
	      String tableName = null;
	      String[] columnNames = null;
	      String whereClause = null;
	      boolean selectFlag = false;
	      boolean fromFlag = false;
	      boolean whereFlag = false;
	      for (String word : words) {
	         if (word.equalsIgnoreCase("SELECT")) {
	            selectFlag = true;
	         } else if (word.equalsIgnoreCase("FROM")) {
	            fromFlag = true;
	            tableName = words[Arrays.asList(words).indexOf("FROM") + 1];
	         } else if (word.equalsIgnoreCase("WHERE")) {
	            whereFlag = true;
	            whereClause = query.substring(query.indexOf("WHERE") + 6);
	         } else if (selectFlag && !fromFlag) {
	            columnNames = word.split(",");
	         }
		
	      }
	   // Afficher les informations extraites
	      System.out.println("Nom de la table : " + tableName);
	      System.out.println("Colonnes s�lectionn�es : " + Arrays.toString(columnNames));
	      System.out.println("Clause WHERE : " + whereClause);
	      
	     //R�cup�rer la liste de tous les records de la table
	      ArrayList<Record> records = SelectService.getTableRecords(tableName, currentDB, path);
	      System.out.println("records : " + records);
	      
	      if(!records.isEmpty()) {
		      //R�cup�rer la premi�re ligne (premier record) de cette liste, cette ligne contient les nom des colonnes de la table
		      Map<String, Integer> mapColIndex = SelectService.getMapColumnIndex(records.get(0).row);
		      
		      //Pour toutes les autres lignes (les autres records) evaluer la clause "where", si c'est ok ajouter le record � la liste des records � retourner (lines)
		      ArrayList<Map<String,Object>> lines = new ArrayList<Map<String,Object>>();
		      if(whereClause != null) {
			      for(int i=0; i<records.size(); i++) {
			    	  Record rec = records.get(i);
			    	  String[] row = rec.row.split(",");
			    	  System.out.println("@@@ row splited : "+ Arrays.asList(row));
			    	  System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ RECORD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");
			    	  Boolean recordOk = true;
			    	  if(i!=0) {
			    		  recordOk = SelectService.evaluateWhereClause(row, whereClause, mapColIndex);
			    	  }
			    	  
			    	  System.out.println("@@@ record OK OK : "+recordOk);
			    	  System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ RECORD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");
			    	  if(i==0) {
			    		  Map<String,Object> line = SelectService.getRecordWithFilter(Arrays.asList(row), Arrays.asList(columnNames), mapColIndex);
			    		  lines.add(line);
			    	  }else if(recordOk) {
				    		  //Si l'enregistrement respecte la clause where, avant de l'ajouter � la liste des enregistrements � renvoyer, filtrer les colonnes
				    		  //Retourner que les colone sp�cifier dans la clause where
				    		  Map<String,Object> line = SelectService.getRecordWithFilter(Arrays.asList(row), Arrays.asList(columnNames), mapColIndex);
				    		  lines.add(line);
			    	  }
			      }  
		      }else {
		    	  for(int i=0; i<records.size(); i++) {
		    		  Record rec = records.get(i);
		    		  String[] row = rec.row.split(",");
		    		  Map<String,Object> line = SelectService.getRecordWithFilter(Arrays.asList(row), Arrays.asList(columnNames), mapColIndex);
		    		  lines.add(line);
		    	  }
		      }
		      System.out.println("@@@ LE RETOUR DE SELECT : "+lines);
		      if(!lines.isEmpty()) {
		    	  result.put("error", false);
		    	  result.put("message", "Le nombre d'enregistrements trouves est : "+(lines.size()-1));
		    	  result.put("records", lines);
		    	  result.put("mapColIndex", mapColIndex);
		      }
	      }else {
	    	  result.put("error", true);
	    	  result.put("message", "Aucun enregistrement a ete trouvee dans cette table : ");
	    	  result.put("records", null);
	    	  result.put("mapColIndex", null);
	      }
	      return result;
}
	
	
	
}

