package Database.Managment.System.Tests;

import java.util.Scanner;

import Database.Managment.System.Services.QueryService;

public class TestQueries {
	
	public static void main(String args[]){
		QueryService q = new QueryService();
		/*q.runSqlCommand("CREATE TABLE Persons\r\n"
				+ "(\r\n"
				+ "PersonID int,\r\n"
				+ "LastName varchar(255),\r\n"
				+ "FirstName varchar(255),\r\n"
				+ "Address varchar(255),\r\n"
				+ "City varchar(255)\r\n"
				+ ");");*/
		
		//q.runSqlCommand("CREATE TABLE Persons ( PersonID int,LastName varchar(255),FirstName varchar(255),Address varchar(255),City varchar(255));");
		
		boolean run = true;
		 
		while (run == true) {
		    System.out.println("Veuillez saisir une requête :");
		    System.out.println("Query : ");
		    Scanner myObj = new Scanner(System.in);
		    
		    String query="";
		    while (myObj.hasNextLine()) {
		        String line = myObj.nextLine();
		        if (line != null) {
		            System.out.println("Output list : " + query);
		            query+=line;
		            break;
		        }

		    }
		    //String query = myObj.nextLine();
		    
		
		    System.out.println("Result final : "+query);
		    q.runSqlCommand(query, "TEST");
		}
		
	}
	
	
}
