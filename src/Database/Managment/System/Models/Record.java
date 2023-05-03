package Database.Managment.System.Models;

import java.io.Serializable;

public class Record implements Serializable {

	public String tableName;
	public String row;
	private static final long serialVersionUID = 6716583874467684985L;
	

	public String getRaw() {
		return row;
	}

	public void setRaw(String row) {
		this.row = row;
	}

	public String getTable() {
		return tableName;
	}

	public void setTable(String tableName) {
		this.tableName = tableName;
	}
	
	@Override
	public String toString() {
		return "RECORD : [ TableName: " +tableName+ ", ROW: " +row;
	}
	
	public Record() {
		
	}
	public Record(String tableName, String raw) {
		this.tableName = tableName;
		this.row = raw;
	}
}
