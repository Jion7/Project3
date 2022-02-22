import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.ArrayList;


public class flightPlan {

	public static void main(String[] args) throws IOException {
		
		/*It was what I wantted to use but I could not find any way to output with using ./flightPlan <FlightDataFile> <PathsToCalculateFile> <OutputFile> as the rubric said....
		so I just used the direct location......
		   Scanner FlightData = readFDF(args [0]);
		   Scanner pathsToCalc = readPTC(args [1]);
		   BufferedWriter writer = new BufferedWriter (new FileWriter(args[2]+ ".txt")); 
		create scanner objects for flight data, path to cal, and output
		*/

		Scanner FlightData = readFDF("FlightDataFile.txt");							
		Scanner pathsToCalc = readPTC("PathsToCalculateFile.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter("OutputFile.txt"));
		
		//It will make linked list of the cities 
		ArrayList<City> Cities = new ArrayList<City>();
		//Cities will read and create the list				
		Cities = readDataFile(FlightData, Cities);
		int maxNumPaths = 1;							
		//display
		DisplayCities(Cities);
		// it will calcualte everything
		flightCalculation(pathsToCalc, Cities, maxNumPaths, writer);
		//close
		writer.close();
	}
	
	//This will read FlightDataFile only
	public static Scanner readFDF(String name) {
		String FlightDataFileName = name;
		File FlightDatFile = new File(FlightDataFileName);
		//scanner
		Scanner FlightData;
		try{
			FlightData = new Scanner(FlightDatFile);
			return FlightData;
		}
		//error reading
		catch (IOException e) {
			System.out.print("\nError: Can't read the input file of FlightData properly");
			return null;
		}
	}
	//It will only read PathToCalculateFile
	public static Scanner readPTC(String name) {
		String pathsToCalculateFile = name;
		File pTCF = new File(pathsToCalculateFile);
		//scanner
		Scanner PathsToCalculateFile;
		try {
			PathsToCalculateFile = new Scanner(pTCF);
			return PathsToCalculateFile;
		}
		//error
		catch (IOException e) {
			System.out.print("\nError: Can't read the input file of Paths properly");
			return null;
		}
	}
	
	//It will Display the cities
	public static void DisplayCities(ArrayList<City> cityList) {
		for(City city : cityList) {
			city.print();
		}
	}
	//it will read the data files 
	public static ArrayList<City> readDataFile(Scanner flightData, ArrayList<City> Cities) {
		flightData.useDelimiter("(\\|)|(\\s+)");
		int numLines = Integer.parseInt(flightData.nextLine());
		for(int i = 0; i < numLines; i++) {
			//read the first city
			City cityA = addCity(flightData, Cities);
			String nameA = cityA.name;
			//second
			City cityB = addCity(flightData, Cities);
			String nameB = cityB.name;
			
			//cost and time
			int tempCost = Integer.parseInt(flightData.next());
			int tempTime = Integer.parseInt(flightData.next());
			
			//find the paths between the city A and B
			for(City c : Cities) {
				if(c.name.equals(nameA)) {
					c.destList.add(new Path(nameB, tempCost, tempTime));
				}
				else if(c.name.equals(nameB)) {
					c.destList.add(new Path(nameA, tempCost, tempTime));
				}
			}
			
		}
		return Cities;
	}
	
	// it will find out the total cost and time
	public static void flightCalculation(Scanner pathsToCalc, ArrayList<City> Cities, int maxNumPaths, BufferedWriter writer) {
		int numLines = Integer.parseInt(pathsToCalc.nextLine());
		//delimiter to prepare cal
		pathsToCalc.useDelimiter("(\\|)|(\\s+)");
		for(int i = 0; i < numLines; i++) {
			//start city
			City firstCity = getCity(pathsToCalc, Cities);
			//end city	
			City secondCity = getCity(pathsToCalc, Cities);
			ArrayList<SearchStack> paths = findAllPaths(Cities, firstCity, secondCity);
			//determine time and cost
			char var = pathsToCalc.next().charAt(0);
			//shortespath
			displayShortestPaths(paths, maxNumPaths, var, writer);
		}
	}
	
	public static City addCity(Scanner flightData, ArrayList<City> Cities) {
		String tempCityNameA = flightData.next();
		boolean cityInside = false;
		//search and return
		for (City c : Cities) {	
			if (c.name.equals(tempCityNameA))
				{cityInside = true; return c;}
		}
		if (!cityInside) {
			City newCity = new City(tempCityNameA, new LinkedList<Path>());
			//if the city doesnt exist add 
			Cities.add(newCity);
			return newCity;
		}
		return null;
	}
	
	public static City getCity(Scanner pathsToCal, ArrayList<City> Cities) {
		String cityName = pathsToCal.next();
		City theCity = null;
		// if the city exists on the file
		for (City x: Cities) {
			if(x.name.equals(cityName))
			{theCity = x; break;}
		}
		//if the city is not there
		if(theCity == null) {
			System.out.print("\nError, does not contain the city looking for");
			pathsToCal.nextLine();
		}
		return theCity;
	}
	// 
	public static void displayShortestPaths(ArrayList<SearchStack> paths, int maxNumPaths, char var, BufferedWriter writer) {
		// if it is empty
		if(paths.isEmpty())
			System.out.print("\nNot enough ifno to find the shortest path");
		//when enough info given
		else {
			for(int j = 0; j < maxNumPaths && !paths.isEmpty(); j++) {
				SearchStack minStack = null;
				int minimum = paths.get(0).getVar(var);
				for(SearchStack s: paths) {
					if (s.getVar(var) <= minimum)
					{minimum = s.getVar(var); minStack = s;}
				}
				minStack.displayFlightPath(j, var);
				try {
					minStack.writeFlightPath(j, var, writer);
				} catch (IOException e) {
					e.printStackTrace();
				}
				paths.remove(minStack);
			}
		}		
	}
	//it will find all paths
	public static ArrayList<SearchStack> findAllPaths(ArrayList<City> Cities, City sourceCity, City destCity) {
		ArrayList<SearchStack> paths = new ArrayList<SearchStack>();
		SearchStack stk = new SearchStack();
		stk.push(new Path(sourceCity.name, 0, 0));
		stk.peek().currentIndex = 0;
		while(!stk.isEmpty()) {
			//find the next destination
			for (City x : Cities) {	
				if(x.name.equals(stk.peek().name)) {
					if(stk.peek().currentIndex < x.destList.size()) {
						Path upNext = x.destList.get(stk.peek().currentIndex);
						//if we already hit this destination previously (skip it)
						if(stk.isIn(upNext)) {
							stk.peek().currentIndex++;
							break;
						}
						//if we now reached the final destination (path found)
						else if (upNext.name.equals(destCity.name)){
							stk.push(upNext);
							//found path
							paths.add(new SearchStack(stk));
							stk.pop();
							stk.peek().currentIndex++;
							break;
						}
						//if this is a new path node
						else {
							stk.push(upNext);
							stk.peek().currentIndex = 0;
							break;
						}
					}
					else {
						stk.pop();
						if(!stk.isEmpty())
							stk.peek().currentIndex++;
						break;
					}									

				}
			}
		}
		return paths;
	}
}

