package Database.Managment.System.Services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Database.Managment.System.Models.Record;
import Database.Managment.System.Utils.Constants;


/**
 * 
 * @author Ahcene ALOUANE
 * Cette classe est le service de la jointure
 * Elle contient des fonctions permettant de traiter de selectionner des donnee avec de la jointure (creation/suppression d'une BDD par ex)
 */
public class JoinService {
	private static final Logger logger = LogManager.getLogger(JoinService.class.getName());

	/**
	 * Cette fonction permets de selectionner des enregistrements a l'aide de la requete saisie par l'utilisateur
	 * @param query : String (la requete de creation de BDD saisie par l'utilisateur
	 * @param currentDB : String (nom de la BDD en cours)
	 * @param path : String (le chemin vers le repertoire contenant les fichiers .txt serialises qui stocke la data et la metadata)
	 * @return Map<String, Object> : le resulat du select avec la jointure 
	 * Si tout est Ok alors renvoie un message de success avec error = false, et avec les records selectionnes s'il en existe
	 * Si la creation est KO : renvoie un message d'erreur avec error = true
	 */
	public static Map<String,Object> selectRecordsWithJoin(String query, String currentDB, String path) {
		query = query.replaceAll("\n", " ");
		query = query.replaceAll("\r", " ");
		int startIndex = query.indexOf("SELECT") + "SELECT".length();
		int endIndex = query.indexOf("FROM");
		String fields = query.substring(startIndex, endIndex).trim();
		logger.info("Les champs : "+fields);
		
		String[] tables = new String[3];
		String tab1 = query.substring(query.indexOf(" FROM ") + 6, query.indexOf(" INNER JOIN ")).replace("(" , "");
		tables[0] = tab1;
		
		 
		 String joinCondition1 = query.substring(query.indexOf(" ON ") + 4, query.indexOf(") INNER JOIN "));

	      // Récupération de la deuxième condition de jointure
	     String joinCondition2 = query.substring(query.lastIndexOf(" ON ") + 4, query.lastIndexOf(");"));
	     logger.info("joinCondition1 :"+joinCondition1);
		 logger.info("joinCondition2 : "+joinCondition2);
		 
		 Map<String,Object> tabsCondition1 = new HashMap<String,Object>();
		 tabsCondition1.put("tables", joinCondition1.split("=")[0].split("\\.")[0].trim()+"; "+joinCondition1.split("=")[1].split("\\.")[0].trim());
		 tabsCondition1.put("cols", joinCondition1.split("=")[0].split("\\.")[1].trim()+"; "+joinCondition1.split("=")[1].split("\\.")[1].trim());
		 logger.info("tabsCondition1 : "+tabsCondition1);
		 
		 if(!Arrays.asList(tables).contains(joinCondition1.split("=")[0].split("\\.")[0].trim())) {
			 tables[1] = joinCondition1.split("=")[0].split("\\.")[0].trim();
		 }
		 
		 if(!Arrays.asList(tables).contains(joinCondition1.split("=")[1].split("\\.")[0].trim())) {
			 tables[1] = joinCondition1.split("=")[1].split("\\.")[0].trim();
		 }
		 
		 Map<String,Object> tabsCondition2 = new HashMap<String,Object>();
		 tabsCondition2.put("tables", joinCondition2.split("=")[0].split("\\.")[0].trim()+"; "+joinCondition2.split("=")[1].split("\\.")[0].trim());
		 tabsCondition2.put("cols", joinCondition2.split("=")[0].split("\\.")[1].trim()+"; "+joinCondition2.split("=")[1].split("\\.")[1].trim());
		 logger.info("tabsCondition2 : "+tabsCondition2);
		 
		 if(!Arrays.asList(tables).contains(joinCondition2.split("=")[0].split("\\.")[0].trim())) {
			 tables[2] = joinCondition2.split("=")[0].split("\\.")[0].trim();
		 }
		 
		 if(!Arrays.asList(tables).contains(joinCondition2.split("=")[1].split("\\.")[0].trim())) {
			 tables[2] = joinCondition2.split("=")[1].split("\\.")[0].trim();
		 }
		 
		 logger.info(Arrays.asList(tables));
		 
		//Récupérer la liste de tous les records de la table
	      ArrayList<Record> recordsTab1 = SelectService.getTableRecords(tables[0], "Ecole", Constants.STORED_FILES_PATH);
	      logger.info("recordsTab1 " + tables[0]+" : "+recordsTab1);
	      ArrayList<Record> recordsTab2 = SelectService.getTableRecords(tables[1], "Ecole", Constants.STORED_FILES_PATH);
	      logger.info("recordsTab2 " + tables[1]+" : "+recordsTab2);
	      ArrayList<Record> recordsTab3 = SelectService.getTableRecords(tables[2], "Ecole", Constants.STORED_FILES_PATH);
	      logger.info("recordsTab3 " + tables[2]+" : "+recordsTab3);
	      
	      Map<String, Integer> mapColIndex1 = SelectService.getMapColumnIndex(recordsTab1.get(0).row);
	      Map<String, Integer> mapColIndex2 = SelectService.getMapColumnIndex(recordsTab2.get(0).row);
	      Map<String, Integer> mapColIndex3 = SelectService.getMapColumnIndex(recordsTab3.get(0).row);
	      
	      logger.info("@@@ mapColIndex1 : "+mapColIndex1);
	      logger.info("@@@ mapColIndex2 : "+mapColIndex2);
	      logger.info("@@@ mapColIndex3 : "+mapColIndex3);
	      
	      ArrayList<Map<String,Object>> lines = new ArrayList<Map<String,Object>>();
	      lines = innerJoin(recordsTab1,recordsTab2, recordsTab3, tabsCondition1, tabsCondition2, mapColIndex1, mapColIndex2, mapColIndex3, Arrays.asList(fields.split(",")));
	      
	      Map<String,Object> result = new HashMap<String,Object>();
	      if(!lines.isEmpty()) {
	    	  Map<String, Integer> mapColIndex = new HashMap<String, Integer>();
	    	  for(int i=0; i<fields.split(",").length; i++) {
	    		  mapColIndex.put(fields.split(",")[i], i);
	    	  }
	    	  result.put("error", false);
	    	  result.put("message", "Le nombre d'enregistrements trouves est : "+(lines.size()-1));
	    	  result.put("records", lines);
	    	  result.put("mapColIndex", mapColIndex);
	    	  result.put("columns", fields.split(","));
	      }
	      return result;
	}
	
	
	
	public static void testMethod() {
		
		ArrayList<Record> tab1 = new ArrayList<Record>();
		tab1.add(new Record("Order","2222, ggggg, hhhhhh"));
		tab1.add(new Record("Order","1111, 1212, 2323"));
		
		ArrayList<Record> tab2 = new ArrayList<Record>();
		tab2.add(new Record("Customer","AAAA, AAAAAA CUSTOMER"));
		tab2.add(new Record("Customer","1212, ZZZ CUSTOMER"));
		
		ArrayList<Record> tab3 = new ArrayList<Record>();
		tab3.add(new Record("Shipper","2323, RRRR SHIPPER"));
		tab3.add(new Record("Shipper","ZERT, YYYYY SHIPPER"));
		
		Map<String,Object> tabsCondition1 = new HashMap<String,Object>();
		tabsCondition1.put("tables", "Order; Customer");
		tabsCondition1.put("cols", "CustomerId; CustomerId");
		
		Map<String,Object> tabsCondition2 = new HashMap<String,Object>();
		tabsCondition2.put("tables", "Order; Shipper");
		tabsCondition2.put("cols", "ShipperId; ShipperId");
		
		Map<String, Integer> mapColumnIndex1 = new HashMap<String, Integer>();
		mapColumnIndex1.put("OrderId", 0);
		mapColumnIndex1.put("CustomerId", 1);
		mapColumnIndex1.put("ShipperId", 2);
		
		Map<String, Integer> mapColumnIndex2 = new HashMap<String, Integer>();
		mapColumnIndex2.put("CustomerId", 0);
		mapColumnIndex2.put("CustomerName", 1);
		
		Map<String, Integer> mapColumnIndex3 = new HashMap<String, Integer>();
		mapColumnIndex3.put("ShipperId", 0);
		mapColumnIndex3.put("ShipField", 1);
		
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("Order.OrderId");
		columnNames.add("Customer.CustomerName");
		
		innerJoin(tab1, tab2, tab3,tabsCondition1, tabsCondition2, mapColumnIndex1, mapColumnIndex2, mapColumnIndex3, columnNames);
	}

	
	/**
	 * Cette fonction permet de traiter la fonctionnalite inner join entre 3 tables
	 * @param tab1 : ArrayList<Record> (liste de tous les records de la table 1)
	 * @param tab2 : ArrayList<Record> (liste de tous les records de la table 2)
	 * @param tab3 : ArrayList<Record> (liste de tous les records de la table 3)
	 * @param tabsCondition1 : Map<String,Object> (la premiere condition de la jointure entre la table 1 et 2)
	 * @param tabsCondition2 : Map<String,Object> (la deuxieme condition de la jointure entre  deux tables)
	 * @param mapColIndex1 : Map<String,Integer> (map <Nom de la colonne de la table1, son index ou position dans la table1)
	 * @param mapColIndex2 : Map<String,Integer> (map <Nom de la colonne de la table2, son index ou position dans la table2)
	 * @param mapColIndex3 : Map<String,Integer> (map <Nom de la colonne de la table3, son index ou position dans la table3)
	 * @param columnNames : List<String> (liste des colonnes a renvoyer dans le resulat de la requete)
	 * @return ArrayList<Map<String,Object>> : si tout est ok renvoie la liste de tous les records respectant les conditions de la jointure, sinon renvoie une erreur
	 */
	public static ArrayList<Map<String,Object>> innerJoin(ArrayList<Record> tab1,ArrayList<Record> tab2, ArrayList<Record> tab3, Map<String,Object> tabsCondition1, Map<String,Object> tabsCondition2, Map<String, Integer> mapColIndex1, Map<String, Integer> mapColIndex2, Map<String, Integer> mapColIndex3, List<String> columnNames) {
		String tab1C1 = ((String) tabsCondition1.get("tables")).split(";")[0];
		String tab2C1 = ((String) tabsCondition1.get("tables")).split(";")[1];
		String col1C1 = ((String) tabsCondition1.get("cols")).split(";")[0];
		String col2C1 = ((String) tabsCondition1.get("cols")).split(";")[1];
		
		String tab1C2 = ((String) tabsCondition2.get("tables")).split(";")[0];
		String tab2C2 = ((String) tabsCondition2.get("tables")).split(";")[1];
		String col1C2 = ((String) tabsCondition2.get("cols")).split(";")[0];
		String col2C2 = ((String) tabsCondition2.get("cols")).split(";")[1];
		
		ArrayList<Map<String,Object>> lines = new ArrayList<Map<String,Object>>();
		for(int i=0; i<tab1.size(); i++) {
	    	  Record rec1 = tab1.get(i);
	    	  String[] row1 = rec1.row.split(",");
	    	  
	    	  for(int j=0; j<tab2.size(); j++) {
		    	  Record rec2 = tab2.get(j);
		    	  String[] row2 = rec2.row.split(",");
		    	  Map<String,Object> mapKeyValue = new HashMap<String,Object>();
		    	  for(int k=0; k<tab3.size(); k++) {
			    	  Record rec3 = tab3.get(k);
			    	  String[] row3 = rec3.row.split(",");
			    	  int index11 = mapColIndex1.get(col1C1.trim());
			    	  int index21 = mapColIndex2.get(col2C1.trim());
			    	  String value1 = row1[index11].trim();
			    	  String value2 = row2[index21].trim();
			    	  if(  value1.equals(value2) ) {
			    		  Map<String,Object>  mapKeyValue2 = getTabsOfconditions2(tab1C2.trim(), tab2C2.trim(), tab1.get(0).tableName.trim(), tab2.get(0).tableName.trim(), tab3.get(0).tableName.trim(), row1, row2, row3, col1C2.trim(), col2C2.trim(), mapColIndex1, mapColIndex2, mapColIndex3, columnNames);
			    		  if(!mapKeyValue2.isEmpty()) {
			    			  for(int h=0; h<columnNames.size(); h++) {
									String tab = columnNames.get(h).split("\\.")[0].trim();
									String col = columnNames.get(h).split("\\.")[1].trim();
									
									if(tab.equals(tab1.get(0).tableName)) {
										Integer index = mapColIndex1.get(col);
										mapKeyValue.put(columnNames.get(h), row1[index]);
									}else if(tab.equals(tab2.get(0).tableName)){
										Integer index = mapColIndex2.get(col);
										mapKeyValue.put(columnNames.get(h), row2[index]);
									}
								}
			    			 mapKeyValue.putAll(mapKeyValue2);
			    			 lines.add(mapKeyValue);
			    		  }
			    	  }
			    	  
				}
		    	  
			}
	    	  
		}
		System.out.println("Lines final : "+lines);
		return lines;
	}
	
	
	
	/**
	 * Cette fonction permet de traiter la deuxieme condition de la jointure entre 3 tables
	 * @param tab1 : ArrayList<Record> (liste de tous les records de la table 1)
	 * @param tab2 : ArrayList<Record> (liste de tous les records de la table 2)
	 * @param tabName1 : String ( nom de la premiere table )
	 * @param tabName2 : String ( nom de la deuxieme table )
	 * @param tabName3 : String ( nom de la troisieme table ) 
	 * @param row1 : String[] (liste correspondante a une ligne d'enregistrement dans la table 1)
	 * @param row2 : String[] (liste correspondante a une ligne d'enregistrement dans la table 2)
	 * @param row3 : String[] (liste correspondante a une ligne d'enregistrement dans la table 3)
	 * @param col1C2 : String (nom de la premiere colonne de la deuxieme condition de la jointure)
	 * @param col2C2 : String (nom de la deuxieme colonne de la deuxieme condition de la jointure)
	 * @param mapColIndex1 : Map<String,Integer> (map <Nom de la colonne de la table1, son index ou position dans la table1)
	 * @param mapColIndex2 : Map<String,Integer> (map <Nom de la colonne de la table1, son index ou position dans la table2)
	 * @param mapColIndex3 : Map<String,Integer> (map <Nom de la colonne de la table1, son index ou position dans la table3)
	 * @param columnNames : List<String> (liste des colonnes a renvoyer dans le resulat de la requete)
	 * @return
	 */
	public static Map<String,Object> getTabsOfconditions2(String tab1, String tab2, String tabName1, String tabName2, String tabName3, String[] row1, String[] row2, String[] row3, String col1C2, String col2C2, Map<String, Integer> mapColIndex1, Map<String, Integer> mapColIndex2, Map<String, Integer> mapColIndex3, List<String> columnNames){
		logger.info("@@@ tab1  : "+tab1);
		logger.info("@@@ tab2  : "+tab2);
		logger.info("@@@ tabName1  : "+tabName1);
		logger.info("@@@ tabName2  : "+tabName2);
		logger.info("@@@ tabName3  : "+tabName3);
		logger.info("@@@ row1  : "+row1);
		logger.info("@@@ row2  : "+row2);
		logger.info("@@@ row3  : "+row3);
		logger.info("@@@ col1C2  : "+col1C2);
		logger.info("@@@ col2C2  : "+col2C2);
		logger.info("@@@ mapColIndex1  : "+mapColIndex1);
		logger.info("@@@ mapColIndex2  : "+mapColIndex2);
		logger.info("@@@ mapColIndex3  : "+mapColIndex3);
		logger.info("@@@ mapColIndex3  : "+mapColIndex3);
		logger.info("@@@ columnNames  : "+columnNames);
		
		Map<String,Object> mapKeyValue = new HashMap<String,Object>();
		logger.info("@@@ mapKeyValue IN  : "+mapKeyValue);
		if(tab1.equals(tabName1) && tab2.equals(tabName2)) {
			int index12 = mapColIndex1.get(col1C2);
	  	  	int index22 = mapColIndex2.get(col2C2);
			String value1 = row1[index12].trim();
			String value2 = row2[index22].trim();
			if(  value1.equals(value2) ) {
				for(int i=0; i<columnNames.size(); i++) {
					String tab = columnNames.get(i).split("\\.")[0].trim();
					String col = columnNames.get(i).split("\\.")[1].trim();
					
					if(tab.equals(tabName1)) {
						Integer index = mapColIndex1.get(col);
						mapKeyValue.put(columnNames.get(i), row1[index]);
					}else if(tab.equals(tabName2)){
						Integer index = mapColIndex2.get(col);
						mapKeyValue.put(columnNames.get(i), row2[index]);
					}
				}
			}
		}
		if(tab1.equals(tabName1) && tab2.equals(tabName3)) {
			int index12 = mapColIndex1.get(col1C2);
	  	  	int index22 = mapColIndex3.get(col2C2);
			String value1 = row1[index12].trim();
			String value2 = row3[index22].trim();
			logger.info("@@@ value1 : "+value1);
			logger.info("@@@ value2 : "+value2);
			if(  value1.equals(value2) ) {
				for(int i=0; i<columnNames.size(); i++) {
					String tab = columnNames.get(i).split("\\.")[0].trim();
					String col = columnNames.get(i).split("\\.")[1].trim();
					logger.info("@@@ tab : "+tab);
					logger.info("@@@ col : "+col);
					if(tab.equals(tabName1)) {
						Integer index = mapColIndex1.get(col);
						mapKeyValue.put(columnNames.get(i), row1[index]);
					}else if(tab.equals(tabName3)){
						Integer index = mapColIndex3.get(col);
						mapKeyValue.put(columnNames.get(i), row3[index]);
					}
				}
			}
		}
		if(tab1.equals(tabName2) && tab2.equals(tabName1)) {
			int index12 = mapColIndex2.get(col1C2);
	  	  	int index22 = mapColIndex1.get(col2C2);
			String value1 = row2[index12].trim();
			String value2 = row1[index22].trim();
			if(  value1.equals(value2) ) {
				for(int i=0; i<columnNames.size(); i++) {
					String tab = columnNames.get(i).split("\\.")[0].trim();
					String col = columnNames.get(i).split("\\.")[1].trim();
					
					if(tab.equals(tabName1)) {
						Integer index = mapColIndex1.get(col);
						mapKeyValue.put(columnNames.get(i), row1[index]);
					}else if(tab.equals(tabName2)){
						Integer index = mapColIndex2.get(col);
						mapKeyValue.put(columnNames.get(i), row2[index]);
					}
				}
			}
		}
		if(tab1.equals(tabName3) && tab2.equals(tabName1)) {
			int index12 = mapColIndex3.get(col1C2);
	  	  	int index22 = mapColIndex1.get(col2C2);
			String value1 = row3[index12].trim();
			String value2 = row1[index22].trim();
			if(  value1.equals(value2) ) {
				for(int i=0; i<columnNames.size(); i++) {
					String tab = columnNames.get(i).split("\\.")[0].trim();
					String col = columnNames.get(i).split("\\.")[1].trim();
					
					if(tab.equals(tabName1)) {
						Integer index = mapColIndex1.get(col);
						mapKeyValue.put(columnNames.get(i), row1[index]);
					}else if(tab.equals(tabName3)){
						Integer index = mapColIndex3.get(col);
						mapKeyValue.put(columnNames.get(i), row3[index]);
					}
				}
			}
		}
		if(tab1.equals(tabName2) && tab2.equals(tabName3)) {
			int index12 = mapColIndex2.get(col1C2);
	  	  	int index22 = mapColIndex3.get(col2C2);
			String value1 = row2[index12].trim();
			String value2 = row3[index22].trim();
			if(  value1.equals(value2) ) {
				for(int i=0; i<columnNames.size(); i++) {
					String tab = columnNames.get(i).split("\\.")[0].trim();
					String col = columnNames.get(i).split("\\.")[1].trim();
					
					if(tab.equals(tabName3)) {
						Integer index = mapColIndex3.get(col);
						mapKeyValue.put(columnNames.get(i), row3[index]);
					}else if(tab.equals(tabName2)){
						Integer index = mapColIndex2.get(col);
						mapKeyValue.put(columnNames.get(i), row2[index]);
					}
				}
			}
		}
		if(tab1.equals(tabName3) && tab2.equals(tabName2)) {
			int index12 = mapColIndex3.get(col1C2);
	  	  	int index22 = mapColIndex2.get(col2C2);
			String value1 = row3[index12].trim();
			String value2 = row2[index22].trim();
			if(  value1.equals(value2) ) {
				for(int i=0; i<columnNames.size(); i++) {
					String tab = columnNames.get(i).split("\\.")[0].trim();
					String col = columnNames.get(i).split("\\.")[1].trim();
					
					if(tab.equals(tabName3)) {
						Integer index = mapColIndex3.get(col);
						mapKeyValue.put(columnNames.get(i), row3[index]);
					}else if(tab.equals(tabName2)){
						Integer index = mapColIndex2.get(col);
						mapKeyValue.put(columnNames.get(i), row2[index]);
					}
				}
			}
		}
		
		logger.info("@@@ mapKeyValue IN return  : "+mapKeyValue);
		return mapKeyValue;
	}
}
