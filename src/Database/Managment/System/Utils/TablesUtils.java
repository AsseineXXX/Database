package Database.Managment.System.Utils;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Database.Managment.System.Models.Colonne;
import Database.Managment.System.Models.DataTable;

public class TablesUtils {

	public static Map<String, Object> getTableByName(String tableName, String  currentDB) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			DataTable table = new DataTable();
			//Lire (Deserialiser) la Base de donnees stockee dans le fichier 
			FileInputStream fisTab_2 = new FileInputStream(Constants.STORED_FILES_PATH+currentDB+"_Tables.txt");
			
			ObjectInputStream oisTab_2 = new ObjectInputStream(fisTab_2);
			ArrayList<DataTable> tabs = (ArrayList<DataTable>) oisTab_2.readObject();
			Boolean tableFound = false;
			for(int i=0; i<tabs.size(); i++) {
				if(tableName.equals(tabs.get(i).getTableName())) {
					table = tabs.get(i);
					tableFound = true;
				}
			}
			oisTab_2.close(); 
			if(tableFound) {
				result.put("error", false);
				result.put("message", "Table trouvee");
				result.put("table", table); 
			}else {
				result.put("error", true);
				result.put("message", "La Table que vous cherchez est introuvable");
				result.put("table", null);
			}
		//System.out.println("@@@ Result get table : "+result);
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			result.put("error", true);
			result.put("message", e.getMessage()); 
			result.put("table", null);
			return result;
		}
		
	}

	public static Map<String, Object> addRaw(DataTable table, List<String> columnNames, List<String> values) {
		Map<String, Object> result = new HashMap<String, Object>();
		ArrayList<Colonne> allColumns = new ArrayList<Colonne>();
		allColumns = table.getColumns();
       
       Map<String, String> mapColsVals = getMapColumnsValues(columnNames, values);
       String row[] = new String[allColumns.size()];
       for(int i=0; i<allColumns.size(); i++) {
    	   String key = allColumns.get(i).getColumnName();
    	   String value = mapColsVals.get(key);
    	   if(mapColsVals.containsKey(key)) {
    		   System.out.println("not nulllllllllllll");
    		   Boolean valueOk = DataTypeUtils.checkValueType(value, allColumns.get(i).getColumnType());
    		   Boolean lengthOk = true;
    		   Integer size = DataTypeUtils.getColumnLength(allColumns.get(i).getColumnType());
    		   if(size == null) {
    			   lengthOk = true;
    		   }else {
    			   lengthOk = DataTypeUtils.checkLength(value, size);
    		   }
    		   result = DataTypeUtils.checkData(valueOk, lengthOk, row, value, i);
    		   
    	   }else {
    		   System.out.println("nulllllllllllll");
    		   row[i]= null;
    		   result.put("error", false);
			   result.put("message", null);
    		   result.put("row", row); 
    	   }
    	   
       }
       
       System.out.println("@@@ row : "+Arrays.asList(row).toString());
       return result;
		
	}
	
	private static Map<String, String> getMapColumnsValues(List<String> columnNames, List<String> values){
		Map<String, String> mapColsVals = new HashMap<String, String>();
		for(int i=0; i<values.size(); i++) {
			mapColsVals.put(columnNames.get(i).trim(), values.get(i).trim().replace("'", ""));
		}
		System.out.println("@@@ mapColsVals : "+mapColsVals);
		return mapColsVals;
	}
	
	
		
	}
	
