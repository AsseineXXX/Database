package Database.Managment.System.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Database.Managment.System.Models.Colonne;
import Database.Managment.System.Models.DataTable;

public class ColonneUtils {

	public static Map<String, Object> getColumnByName(String columnName, String tableName, String currentDB){
		Map<String, Object> result = new HashMap<String, Object>(); 
		Map<String, Object> table = TablesUtils.getTableByName(tableName, currentDB);
		if(!(boolean) table.get("error")) {
			ArrayList<Colonne> columns = ((DataTable) table.get("table")).getColumns();
			for(int i=0; i<columns.size(); i++) {
				if(columns.get(i).columnName.equals(columnName)) {
					result.put("error", false);
					result.put("message", "La colonne recherchee est trouvee : "+columns.get(i));
					result.put("column", columns.get(i)); 
				}
			}
		}else {
			result.put("error", true);
			result.put("message", "Erreur dans de la recherche de la colonne : ");
			result.put("column",null);
		}
		System.out.println("Colonne trouvee : "+result);
		return result;
	}
}
