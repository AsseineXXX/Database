package Database.Managment.System.Services;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.List;

import Database.Managment.System.Models.Record;

public class SelectService {

	public static ArrayList<Record> getTableRecords(String tabName, String currentDB, String path){
		try {
			//Lire (Deserialiser) le fichier contenant la liste des enregistrement de la table
			ArrayList<Record> recs = new ArrayList<Record>(); 
			String pathToRecs = path+currentDB+"_"+tabName+"_Records.txt";
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
			}
			return recs;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	
	
	public static boolean evaluateWhereClause(String[] row, String whereClause, Map<String, Integer> mapColIndex) {

        // Evaluer la condition par rapport � la ligne d'enregistrement
		
        String[] conditions = whereClause.split("\\s+(?i)(and|or)\\s+");
        System.out.println("@@@ conditions : "+Arrays.asList(conditions));
        boolean[] logicalOps = getOperators(whereClause, conditions.length);
        boolean[] results = evaluateAllConditions(row, conditions, conditions.length, mapColIndex);
        
        //Regrouper les resultats de chaque condition en une expression logique
        int i=0;
        boolean result = results[0];
        while(i<results.length-1) {
        	if(logicalOps[i]) {
        		result = result && results[i+1];
        	}else {
        		result = result || results[i+1];
        	}
        	System.out.println("@@@ --------- IN In result : "+result);
        	i++;
        }
        System.out.println("@@@ --------------- result : "+result);
        return result;
          
    }

	


	/**
	 * 
	 * @param columnLine
	 * @return Map<String, Integer> : map nomDeColonne --> Index
	 */
	public static Map<String, Integer> getMapColumnIndex(String columnLine){
		Map<String, Integer> mapColumnIndex = new HashMap<String, Integer>();
		String[] row = columnLine.split(",");
		for(int i=0; i<row.length; i++) {
			mapColumnIndex.put(row[i].trim(), i);
		}
		System.out.println("@@@ mapColumnIndex : "+mapColumnIndex);
		return mapColumnIndex;
	}

	


	
	/**
	 * Recuperer les operateurs logiques (AND,OR) entre les conditions dans la clause WHERE 
	 * @param whereClause
	 * @param numCondition
	 * @return boolean[]
	 */
	private static boolean[] getOperators(String whereClause, Integer numCondition) {
	    Pattern pattern = Pattern.compile("(AND|OR)");
	    Matcher matcher = pattern.matcher(whereClause.toUpperCase());

	    boolean[] operators = new boolean[numCondition - 1];
	    int currentIndex = 0;
	    while (matcher.find()) {
	    	String operator = matcher.group();
	        if (operator.equals("AND")) {
	          operators[currentIndex] = true;
	        } else {
	          operators[currentIndex] = false;
	        }

	      currentIndex++;
	    }
	    for(int i=0; i<operators.length; i++) {
	    	System.out.println("@@@  Operateur logique : "+operators[i]);
	    }
	    return operators;
	 }

	
	
	
	/**
	 * Verifier chaque condition de la clause where si elle est verifiee pour la ligne d enregistrement
	 * @param row
	 * @param conditions
	 * @param numCondition : nombre de conditions
	 * @param mapColIndex
	 * @return boolean[] : retourner un tableau de boolean contenant le resultat de chque condition
	 */
	private static boolean[] evaluateAllConditions(String[] row, String[] conditions,Integer numCondition, Map<String, Integer> mapColIndex) {
		boolean[] result = new boolean[numCondition];
		int i=0;
		for (String condition : conditions) {
        	System.out.println("---------------------------------  CONDITION   ------------------------------");
            // Diviser la condition en colonne et op�rateur
        	Pattern pattern = Pattern.compile("(\\w+)\\s*([<>!=]+)\\s*(\\S+)");
            Matcher matcher = pattern.matcher(condition);
            if (matcher.find()) {
                String colName = matcher.group(1).trim();
                String operator = matcher.group(2);
                String value = matcher.group(3).replace("'", "").trim();
                System.out.println("@@@ colName : "+colName);
                System.out.println("@@@ operator : "+operator);
                System.out.println("@@@ value : "+value);
                int columnIndex = mapColIndex.get(colName);
                if (columnIndex == -1) {
                    result[i] = false;;
                }
                boolean isOk = true;
                switch (operator) {
                    case ">":
                        if (Integer.parseInt(row[columnIndex].trim()) <= Integer.parseInt(value)) {
                        	result[i] = false;
                        	isOk = false;
                        }
                        break;
                    case "<":
                        if (Integer.parseInt(row[columnIndex].trim()) >= Integer.parseInt(value)) {
                        	result[i] = false;
                        	isOk = false;
                        }
                        break;
                    case "=":
                    	System.out.println("@@@ row[columnIndex] : "+row[columnIndex]);
                        if (!row[columnIndex].trim().equals(value)) {
                        	result[i] = false;
                        	isOk = false;
                        }
                        break;
                    case "!=":
                        if (row[columnIndex].trim().equals(value)) {
                        	result[i] = false;
                        	isOk = false;
                        }
                        break;
                    default:
                    	result[i] = false;
                    	isOk = false;
                }
                if(isOk) {
                	result[i] = true;
                }
                System.out.println("@@@@ result i : "+result[i]);
                i++;
                
            } 
            System.out.println("---------------------------------  CONDITION   ------------------------------");
        }
		for(int j=0; j<result.length; j++) {
	    	System.out.println("@@@  Operateur logique : "+result[j]);
	    }
        return result;
	}

	/**
	 * 
	 * @param row : List<String> liste contenant les valeurs des colonnes (l'enregirstrement)
	 * @param columnNames : List<String> contenant les nom de toutes les colonnes de la table
	 * @param mapColIndex : Map<String,Integer> map nomDeColonne --> index de la colonne dans la table
	 * @return Map<String,Object> : 
	 */
	public static Map<String,Object> getRecordWithFilter(List<String> row, List<String> columnNames, Map<String,Integer> mapColIndex){
		Map<String,Object> mapKeyValue = new HashMap<String,Object>();
		System.out.println("@@@ mapColIndex : "+mapColIndex);
		System.out.println("@@@ --------------row : "+row);
		if(columnNames.size()==1 && columnNames.get(0).equals("*")) {
			for (Map.Entry<String, Integer> entry : mapColIndex.entrySet()) {
			    String colName = entry.getKey();
			    Integer index = entry.getValue();
			    System.out.println("@@@ colName : "+colName);
			    System.out.println("@@@ index : "+index);
			    mapKeyValue.put(colName, row.get(index)); 
			}
		}else {
			for(int i=0; i<columnNames.size(); i++) {
				Integer index = mapColIndex.get(columnNames.get(i).replace(" ",""));
				System.out.println("@@@ columnNames.get(i) : "+columnNames.get(i));
			    System.out.println("@@@ index : "+index);
				mapKeyValue.put(columnNames.get(i), row.get(index));
			}
		}
		System.out.println("@@@ ligne a renvoyer : "+mapKeyValue);
		return mapKeyValue;
	}

}
