package configs;

import java.util.ArrayList;
import java.util.List;

import graph.Message;

/**
 * The Node class represents a node in a graph, with a name, a list of edges, and an optional message.
 */
public class Node {
    private final String name;
    private final List<Node> edges;
    private Message msg;

    /**
     * Constructs a Node with the specified name.
     *
     * @param name the name of the node
     */
    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
    }

    /**
     * Returns the name of the node.
     *
     * @return the name of the node
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of edges (neighbors) of the node.
     *
     * @return the list of edges
     */
    public List<Node> getEdges() {
        return edges;
    }

    /**
     * Returns the message associated with the node.
     *
     * @return the message
     */
    public Message getMessage() {
        return msg;
    }

    /**
     * Sets the message associated with the node.
     *
     * @param message the message to set
     */
    public void setMessage(Message message) {
        this.msg = message;
    }

    /**
     * Adds an edge (arc) to another node.
     *
     * @param otherNode the node to add an edge to
     */
    public void addEdge(Node otherNode) {
        this.edges.add(otherNode);
    }

    /**
     * Checks if the graph containing this node has cycles.
     *
     * @return true if the graph has cycles, false otherwise
     */
    public boolean hasCycles() {
        return hasCycles(this, new ArrayList<>());
    }

    /**
     * Helper method to check for cycles in the graph.
     *
     * @param current the current node being visited
     * @param visited the list of visited nodes
     * @return true if a cycle is detected, false otherwise
     */
    private boolean hasCycles(Node current, List<Node> visited) {
        if (visited.contains(current)) {
            return true; // Cycle detected
        }
        visited.add(current);
        for (Node neighbor : current.getEdges()) {
            if (hasCycles(neighbor, visited)) {
                return true;
            }
        }
        visited.remove(current);
        return false;
    }
}
