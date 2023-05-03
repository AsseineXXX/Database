package Database.Managment.System.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class DataTable implements Serializable{
	private String tableName;
	private String idTable;
	private String idBDD;
	private ArrayList<Colonne> columns;
	private ArrayList<Record> records;
	private static final long serialVersionUID = -2333223561803952133L;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getIdTable() {
		return idTable;
	}
	public void setIdTable(String idTable) {
		this.idTable = idTable;
	}
	public String getIdBDD() {
		return idBDD;
	}
	public void setIdBDD(String idBDD) {
		this.idBDD = idBDD;
	}
	public ArrayList<Colonne> getColumns() {
		return columns;
	}
	public void setColumns(ArrayList<Colonne> columns) { 
		this.columns = columns;
	}
	
	@Override
	public String toString() {
		String s = "Table : [\n Name: "+tableName+"\n Id : "+idTable+"\n Id BDD: "+idBDD+"\n Columns : {\n";
		for(int i=0; i<columns.size(); i++) {
			s += "	Name: "+columns.get(i).columnName+",  Type: "+columns.get(i).columnType+"\n";
		}
		s += " }\n]";
		return s;
	}
	
	
	public DataTable() {
		
	}
	public DataTable(String name, String idBDD, ArrayList<Colonne> columns) {
	       this.tableName = name ;
	       this.idBDD =  idBDD;
	       this.columns = columns;
	       this.idTable = UUID.randomUUID().toString();
	}
}
