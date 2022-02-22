import java.util.ArrayList;
import java.io.*;
//this will class will search the stack
//it will help to find built-in to the ArrayList type
public class SearchStack {
	ArrayList<Path> citiesSearched;
	int totalCost;
	int totalTime;
	
	SearchStack() {
		citiesSearched = new ArrayList<Path>();
		totalCost = 0;
		totalTime = 0;
	}
	SearchStack(ArrayList<Path> allCities) {
		citiesSearched = allCities;
		totalCost = 0;
		totalTime = 0;
	}
	
	SearchStack(SearchStack another) {
		this.citiesSearched = new ArrayList<Path>(another.citiesSearched);
		this.totalCost = another.totalCost;
		this.totalTime = another.totalTime;
	}
	
	//removes and returns the result
	public Path pop() {
		Path lastOne = citiesSearched.get(citiesSearched.size()-1);
		updatePathWeight(-lastOne.cost, -lastOne.time);
		citiesSearched.remove(citiesSearched.size()-1);
		return lastOne;
	}
	
	//returns index of pushed element
	public int push(Path destination) {
		citiesSearched.add(destination);
		updatePathWeight(destination.cost, destination.time);
		return citiesSearched.indexOf(destination);
	}
	
	public Path peek() {
		return citiesSearched.get(citiesSearched.size() - 1);
	}
	//check if the is in
	public boolean isIn(Path d) {
		for(Path x: citiesSearched) {
			if(x.name.equals(d.name))
				return true;
		}
		return false;
	}
	//This will read Cost or Time
	public int getVar(char var) {
		//totalCost
		if(var == 'C')
			return totalCost;
		//TotalTime
		else if(var == 'T')
			return totalTime;
		// Nothing
			else
			return -1;
	}
	
	//check it exists or not
	public boolean isEmpty() {
		if(citiesSearched.isEmpty())
			return true;
		else
			return false;
	}
	// display the path
	public void displayFlightPath(int index, char var) {
		System.out.print("\nFlight Path (");
		//when insertion was cost
		if (var == 'C')
				System.out.print("Cost");
		//when inerstion was time
		else if (var == 'T')
			System.out.print("Time");
		else 
			System.out.print("no given info");
		// print out -> and ; during the words
		System.out.print(") " + (index+1) + ": \n");
		for(int i = 0; i < citiesSearched.size(); i++) {
			System.out.print(citiesSearched.get(i).name );
			if(i != citiesSearched.size() - 1)
				System.out.print(" -> ");
			else
				System.out.print("; ");
		}
		if(var == 'C')
			displayCost();
		else if(var == 'T')
			displayTime();
		else
			System.out.print("\n Error displaying the path");
		System.out.print(". \n");
	}
	//write
	public void writeFlightPath(int index, char var, BufferedWriter writer) throws IOException {
		writer.write("\nFlight Path " + (index+1) + ": \n");
		for(int i = 0; i < citiesSearched.size(); i++) {
			writer.write(citiesSearched.get(i).name );
			if(i != citiesSearched.size() - 1)
				writer.write(" -> ");
			else
				writer.write("; ");
		}
		if(var == 'C')
			{writer.write("Total Cost: " + totalCost + "$"); }
		else if(var == 'T')
			{writer.write("Total Time: " + totalTime + " minutes"); }
		else
			writer.write("\n Error during writing paths");
		writer.write(". \n");
	}
	//display total cost
	public void displayCost() {
		System.out.print("Total Cost: " + totalCost + "$");
		
	}
	//display total time
	public void displayTime() {
		System.out.print("Total Time: " + totalTime + " minutes");
	}
	
	public void updatePathWeight(int cost, int time) {
		addCost(cost);
		addTime(time);
	}
	//adding cost
	public void addCost(int cost) {
		totalCost += cost;
	}
	//adding time
	public void addTime(int time) {
		totalTime += time;
	}
}