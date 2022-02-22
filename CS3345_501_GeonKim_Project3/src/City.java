import java.util.LinkedList;

public class City {
	String name;
	LinkedList<Path> destList;
	
	//constructor
	public City(String name, LinkedList<Path> destList) {
		this.name = name;
		this.destList = destList;
	}
	
	//print method
	public void print() {
		System.out.print(name + ": ");
		for(Path d : this.destList) {
			d.print();
			System.out.print(" | ");
		}
		System.out.print(".\n");
	}
}