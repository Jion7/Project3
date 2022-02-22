public class Path {
	String name;
	int cost;
	int time;
	int currentIndex;
	
	//constructor
	public Path(String name, int cost, int time) {
		this.name = name;
		this.cost = cost;
		this.time = time;
	}
	
	public void print() {
		System.out.print(name + ", " + cost + "$, " + time + "min");
	}
}