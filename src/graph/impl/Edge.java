package graph.impl;

public class Edge implements Comparable<Edge>{
	
	private String src;
	private String neigh;
	private int weight;
	
	public Edge(String src, String neigh, int weight){
		this.src = src;
		this.neigh = neigh;
		this.weight = weight;
	}
	
	public String src(){
		return src;
	}
	
	public String neigh(){
		return neigh;
	}
	
	public int weight(){
		return weight;
	}

	public int compareTo(Edge other) {
		return this.weight-other.weight;
	}

}
