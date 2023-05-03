package Database.Managment.System.Models;

import java.io.Serializable;

public class Colonne implements Serializable {
	public String idTable;
	public String columnName;
	public String columnType;
	public Boolean isUnique;
	public Boolean isPrimaryKey;
	public Boolean isForeignKey;
	public Integer position;
	
	private static final long serialVersionUID = 4752603475798334855L;
	
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public void setIdTable(String idTable) {
		this.idTable = idTable;
	}
	public String getIdTable() {
		return idTable;
	}
	public void setTable(String idTable) {
		this.idTable = idTable;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public Boolean getIsUnique() {
		return isUnique;
	}
	public void setIsUnique(Boolean isUnique) {
		this.isUnique = isUnique;
	}
	public Boolean getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(Boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public Boolean getIsForeignKey() {
		return isForeignKey;
	}
	public void setIsForeignKey(Boolean isForeignKey) {
		this.isForeignKey = isForeignKey;
	}
	
	@Override
	public String toString() {
		return "Colonne : [ Table: " +idTable+ ", Column name: " +columnName+ ", Column Type: " +columnType+ ", Column Position: " +position+ ", Is unique: " +isUnique
				+ ", Is primary key: " +isPrimaryKey+ ", Is foreign key: " +isForeignKey;
	}
	
	public Colonne() {
		
	}
	public Colonne(String name, String type, Boolean isUnique, Boolean isPrimaryKey, Boolean isForeignKey ) {
		this.columnName = name ;
		this.columnType =  type;
		this.isUnique = isUnique;
		this.isPrimaryKey = isPrimaryKey;
		this.isForeignKey = isForeignKey;
	}
}
