package Database.Managment.System.Utils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataTypeUtils {
	public static String sqlToJavaType(String sqlType) {
	    String javaType = "";
	    switch(sqlType.toUpperCase()) {
	        case "INT":
	            javaType = "int";
	            break;
	        case "BIGINT":
	            javaType = "long";
	            break;
	        case "FLOAT":
	            javaType = "float";
	            break;
	        case "DOUBLE":
	            javaType = "double";
	            break;
	        case "DECIMAL": 
	            javaType = "java.math.BigDecimal";
	            break;
	        case "CHAR":
	            javaType = "char";
	            break;
	        case "VARCHAR":
	        case "TEXT":
	            javaType = "String";
	            break;
	        case "BOOLEAN":
	            javaType = "boolean";
	            break;
	        case "DATE":
	            javaType = "date";
	            break;
	        case "TIME":
	            javaType = "Time";
	            break;
	        case "TIMESTAMP":
	            javaType = "Timestamp";
	            break;
	        case "BLOB":
	            javaType = "byte[]";
	            break;
	        case "CLOB":
	            javaType = "String";
	            break;
	        default:
	            javaType = "undefined";
	            break;
	    }
	    return javaType;
	}


	public static Boolean checkValueType(String value, String type) {
		String sqlType = getColumnSqlType(type);
		String javaType = sqlToJavaType(sqlType);
		System.out.println("javaType : "+javaType);
		System.out.println("value : "+value);
		return isValueOfType(value, javaType);
	}
	
	
	public static String getColumnSqlType(String type) {
		String sqlType = "";
		if(type.contains("(")) {
			int index = type.indexOf("("); // Trouver l'index de la première occurrence de "("
			String texteAvantParenthese = type.substring(0, index);
			sqlType = texteAvantParenthese;
		}else {
			sqlType = type;
		}
		return sqlType;
	}

	
	public static boolean isValueOfType(String value, String typeName) {
	   return true;
	}
	
	
	public static boolean isDateValid(String dateStr, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	public static Integer getColumnLength(String Type) {
		System.out.println("@@@@@@ type : "+Type);
		Integer length = null;
		// Définir l'expression régulière
        String regex = "[a-z]\\((\\d+)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Type);
 
        // Extraire l'entier
        if (matcher.find()) {
            String intStr = matcher.group(1);
            length = Integer.parseInt(intStr);
        }
		System.out.println("La taille de la colonne est : "+length);
		return length;
	}
	
	public static Boolean checkLength(String value, int length) {
		if(value.length() <= length) {
			return true;
		}else {
			return false;
		}
	}
	
	public static Map<String, Object> checkData(Boolean valueOk, Boolean lengthOk, String row[], String value, Integer index) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(valueOk && lengthOk) {
			   row[index] = value;
			   result.put("error", false);
			   result.put("message", null);
			   result.put("row", row);
		}else if(valueOk == false) {
			   result.put("error", true);
			   result.put("message", "Erreur dans 'addRaw' : Valeur(s) incorrecte(s), veuillez vérifier le type des valeurs");
			   result.put("row", null);
			   //row.add(null);
		}else if(lengthOk == false) {
			   result.put("error", true);
			   result.put("message", "Erreur dans 'addRaw' : Valeur(s) incorrecte(s), veuillez vérifier le taille des valeurs");
			   result.put("row", null);
		}
		return result;
	}
	
}
