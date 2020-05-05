package graph.impl;

import java.util.*;

import graph.IGraph;
import graph.INode;
import graph.NodeVisitor;

/**
 * A basic representation of a graph that can perform BFS, DFS, Dijkstra,
 * and Prim-Jarnik's algorithm for a minimum spanning tree.
 * 
 * @author jspacco
 *
 */
public class Graph implements IGraph
{
    private Map<String, INode> nodes = new HashMap<>();
    /**
     * Return the {@link Node} with the given name.
     * 
     * If no {@link Node} with the given name exists, create
     * a new node with the given name and return it. Subsequent
     * calls to this method with the same name should
     * then return the node just created.
     * 
     * @param name
     * @return
     */
    public INode getOrCreateNode(String name) {
        if(nodes.containsKey(name)){
            return nodes.get(name);
        }
    
        INode node = new Node(name);
        nodes.put(name, node);
        return node;
    }

    /**
     * Return true if the graph contains a node with the given name,
     * and false otherwise.
     * 
     * @param name
     * @return
     */
    public boolean containsNode(String name) {
        return nodes.containsKey(name);
    }

    /**
     * Return a collection of all of the nodes in the graph.
     * 
     * @return
     */
    public Collection<INode> getAllNodes() {
        return nodes.values();
    }
    
    /**
     * Perform a breadth-first search on the graph, starting at the node
     * with the given name. The visit method of the {@link NodeVisitor} should
     * be called on each node the first time we visit the node.
     * 
     * 
     * @param startNodeName
     * @param v
     */
    public void breadthFirstSearch(String startNodeName, NodeVisitor v)
    {
        Set<INode> visited = new HashSet<INode>();
        Queue<INode> toVisit = new ArrayDeque<INode>();
        
        ((ArrayDeque<INode>) toVisit).addFirst(nodes.get(startNodeName));
        
        while (!toVisit.isEmpty()){
            INode x = toVisit.remove();
            
            if (visited.contains(x)){
                continue;
            }
            
            v.visit(x);
            visited.add(x);
            
            ArrayList<INode> n = new ArrayList<INode>();
            n.addAll(x.getNeighbors());
            for(int i = 0; i < n.size(); i++){
                if(!visited.contains(n.get(i))){
                    toVisit.add(n.get(i));
                }
            }
        }
    }

    /**
     * Perform a depth-first search on the graph, starting at the node
     * with the given name. The visit method of the {@link NodeVisitor} should
     * be called on each node the first time we visit the node.
     * 
     * 
     * @param startNodeName
     * @param v
     */
    public void depthFirstSearch(String startNodeName, NodeVisitor v)
    {
        Set<INode> visited = new HashSet<INode>();
        Stack<INode> toVisit = new Stack<INode>();
        
        toVisit.push(nodes.get(startNodeName));
        
        while (!toVisit.empty()){
        	INode x = toVisit.pop();
        	
        	if (visited.contains(x)){
        		continue;
        	}
        	
        	v.visit(x);
        	visited.add(x);
        	
        	ArrayList<INode> n = new ArrayList<INode>();
        	n.addAll(x.getNeighbors());
        	for (int i = 0; i < n.size(); i++){
        		if(!visited.contains(n.get(i))){
        			toVisit.push(n.get(i));
        		}
        	}
        }
    }

    /**
     * Perform Dijkstra's algorithm for computing the cost of the shortest path
     * to every node in the graph starting at the node with the given name.
     * Return a mapping from every node in the graph to the total minimum cost of reaching
     * that node from the given start node.
     * 
     * <b>Hint:</b> Creating a helper class called Path, which stores a destination
     * (String) and a cost (Integer), and making it implement Comparable, can be
     * helpful. Well, either than or repeated linear scans.
     * 
     * @param startName
     * @return
     */
    public Map<INode,Integer> dijkstra(String startName) {
        Map<INode, Integer> result = new HashMap<INode, Integer>();
        
        PriorityQueue<path> toDo = new PriorityQueue<path>();
        
        toDo.add(new path(startName, 0));
        
        while (result.size() < nodes.size()){
        	path nextPath = toDo.poll();
        	INode node = this.getOrCreateNode(nextPath.dst());
        	
        	if (result.containsKey(node))
        		continue;
        	
        	int cost = nextPath.cost();
        	
        	result.put(node, cost);
        	
        	ArrayList<INode> n = new ArrayList<INode>();
        	n.addAll(node.getNeighbors());
        	for (int i = 0; i < n.size(); i++){
        		toDo.add(new path(n.get(i).getName(), cost + node.getWeight(n.get(i))));
        	}
        }
        
        return result;
    }
    
    /**
     * Perform Prim-Jarnik's algorithm to compute a Minimum Spanning Tree (MST).
     * 
     * The MST is itself a graph containing the same nodes and a subset of the edges 
     * from the original graph.
     * 
     * @return
     */
    public IGraph primJarnik() {
        PriorityQueue<Edge> toPrim = new PriorityQueue<Edge>();
        IGraph result = new Graph();
        
        INode node = this.nodes.values().iterator().next(); //gets starting node
        
        result.getOrCreateNode(node.getName());
        
        for (INode n: node.getNeighbors()){
    		toPrim.add(new Edge(node.getName(), n.getName(), node.getWeight(n)));
    	}
        
        while (result.getAllNodes().size() < this.getAllNodes().size()){
        	Edge lightEdge = toPrim.poll(); //this takes the edge off ;)
        	
        	if(result.containsNode(lightEdge.neigh())){ //this ensures there are no duplicate nodes
        		continue;
        	}
        	
        	result.getOrCreateNode(lightEdge.neigh()).addUndirectedEdgeToNode(result.getOrCreateNode(lightEdge.src()), lightEdge.weight());
        	node = this.getOrCreateNode(lightEdge.neigh()); //updates node to newest node
        	
        	for (INode n: node.getNeighbors()){
        		toPrim.add(new Edge(node.getName(), n.getName(), node.getWeight(n)));
        	}
        }
        
        return result;
    }
}
