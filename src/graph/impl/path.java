package graph.impl;

public class path implements Comparable<path> {
	private String dst;
	private int cost;
	
	public path(String startNode, int cost){
		dst = startNode;
		this.cost = cost;
	}
	
	public String dst(){
		return dst;
	}
	
	public int cost(){
		return this.cost;
	}

	public int compareTo(path other) {
		return this.cost - other.cost;
	}

}
