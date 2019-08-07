package workShop;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class main {
	private static List<String> cities = new ArrayList<String>();
	private static int[] rewards = new int[10];
	private static int[][] connection ;
	private static String optimalPath = "";
	//list.indexOf(claude);
	

	 @SuppressWarnings("unchecked")
	 public static void readJson(String[] args) {
		 //JSON parser object to parse read file
	    JSONParser jsonParser = new JSONParser();
	     // System.out.println(new File(".").getAbsoluteFile());
	    try (FileReader reader = new FileReader("C:\\Users\\angel\\eclipse-workspace/workShop/src/workShop/ejercicio1.json")){
	     //Read JSON file
	    Object obj = jsonParser.parse(reader);
	 
	    JSONObject citiesObj = (JSONObject) obj;
	    System.out.println(citiesObj);
	    JSONArray citiesArray = (JSONArray) citiesObj.get("cities");
	             
	    //Iterate over cities array
        citiesArray.forEach( city -> parseCityObject( (JSONObject) city ) );
        connection= new int[cities.size()][cities.size()];
	    JSONArray connectionsArray = (JSONArray) citiesObj.get("connections");
	    connectionsArray.forEach( con -> parseConnectionObject( (JSONObject) con ) );

        } catch (FileNotFoundException e) {
        	e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	}
	 //2^n
	 private static void parseConnectionObject(JSONObject con) {
	    	
	    	
    	//Get city from
        String from = (String) con.get("from");   
        //Get city to
        String to = (String) con.get("to"); 
        //Get cost 
	    Long c = (Long) con.get("cost");
	    int cost = c.intValue();
	    int i = cities.indexOf(from);
	    int j = cities.indexOf(to);
	        
	    connection[i][j]=cost;
	    connection[j][i]=cost;

	}

	private static void parseCityObject(JSONObject city){
	        
	    //Get city name
	    String name = (String) city.get("name");   
	    cities.add(name);
	    //Get city reward
	    Long reward = (Long) city.get("reward"); 
	    int r = reward.intValue();
	    int i = cities.indexOf(name);
	    rewards[i]=r;
	         
	}
		
		
	//Finding the best option	
	static int pepi(int[][] graph, boolean[] v,int currPos, String path,int count, int cost, int ans, int days,int n){
        
	
		// If last node is reached and it has a link to the starting node i.e the source then
		// keep the minimum value out of the total cost of traversal and “ans”
		// Finally return to check for more possible values
		if (count == days && graph[currPos][0] > 0)
		{
		ans = Math.max(ans, cost - graph[currPos][0]);
		optimalPath = path + cities.get(currPos) + " - "+ cities.get(0);
		return ans;
		} 
		// BACKTRACKING STEP
		// Loop to traverse the adjacency list of currPos node and increasing the count 
		//by 1 and cost by graph[currPos][i] value
		for (int i = 0; i < n; i++) { 
			if (v[i] == false && graph[currPos][i] > 0){

				// Mark as visited
				v[i] = true;
				ans = pepi(graph, v, i, path + cities.get(currPos) + " - ", count + 1, cost - graph[currPos][i]+rewards[i], ans,days,n);

				// Mark ith node as unvisited
				v[i] = false;
			}
		}
		return ans;
	}
		
	        
			
		//NP-Hard - complexity - 2^n
	public static void main(String[] args) {
		//reading Json Example
		readJson(null);
		// n is the number of nodes i.e. V
		int n = cities.size();

		// Boolean array to check if a node has been visited or not
		boolean[] v = new boolean[n];

		// Mark 0th node as visited
		v[0] = true;
		//ans == answer
		int ans = 0; 
		Scanner myObj = new Scanner(System.in);

	    System.out.println("Enter number of travalling days for PEPI: ");

	    // String input
	    int days = myObj.nextInt();
		

		String path = "";
		// Find the best award
		ans = pepi(connection, v, 0, path, 1, 0, ans,days,n);

		System.out.println("Final Reward "+ans);
		System.out.println(optimalPath);

		}

}
