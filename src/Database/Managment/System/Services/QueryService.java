package Database.Managment.System.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Database.Managment.System.Utils.Constants;
import Database.Managment.System.Services.ConnexionService;
import Database.Managment.System.Services.DatabaseService;
import Database.Managment.System.Services.RecordService;
import Database.Managment.System.Services.TableService;


public class QueryService {
	//Read query
	
	public String query;

	public String getQuery() {
		return query; 
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public QueryService() {
	}
	public QueryService(String query) { 
		this.query = query;
	} 
	
	@Override
	public String toString() {
		return "[Query : "+query+" ]";
	}
	
	public Map<String, Object> runSqlCommand(String query, String currentDB) { 
		String[] items = query.split(" ");
		String first = items[0];
		String second = items[1];
		Map<String, Object> queryResult = new HashMap<String,Object>();
		Map<String, Object> connexionsMap = new HashMap<String,Object>(); 
		switch(first){
			case "CREATE":
							if(second.equals("TABLE")) {
								queryResult =TableService.createTable(query, currentDB, Constants.STORED_FILES_PATH+currentDB+"_Tables.txt");
								return queryResult;
							}else if(second.equals("DATABASE")){
								queryResult = DatabaseService.createDatabase(query, Constants.STORED_FILES_PATH+"Databases.txt");
								if((Boolean)queryResult.get("error") == false) {
									connexionsMap = ConnexionService.createNewConnexion((String)queryResult.get("dbId"));
								}
								if((boolean)queryResult.get("error") == false && (boolean)connexionsMap.get("error")==false) {
									queryResult.put("message", "La BDD a ete cree vec success !");
								}
							}
							break;
			case "DROP": 
							if(second.equals("TABLE")) {
								TableService.deleteTable(query);
							}else if(second.equals("DATABASE")){
								queryResult = DatabaseService.deleteDatabase(query, Constants.STORED_FILES_PATH+"Databases.txt");
							}
							break;
			case "INSERT" : queryResult = RecordService.insertRecord(query, currentDB, Constants.STORED_FILES_PATH);break;
			case "DELETE" : RecordService.deleteRecord(query);break;
			case "SELECT" : queryResult = RecordService.selectRecords(query, currentDB, Constants.STORED_FILES_PATH);break;
		}
		return queryResult;
	}
}
