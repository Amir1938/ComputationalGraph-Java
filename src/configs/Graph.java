package configs;

import java.util.ArrayList;
import graph.Agent;
import graph.Topic;
import graph.TopicManagerSingleton;

/**
 * The Graph class extends ArrayList to represent a graph structure consisting of nodes and edges.
 */
public class Graph extends ArrayList<Node> {
	/**
	 * Serial version UID for serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Checks if the graph contains any cycles.
	 *
	 * @return true if the graph has cycles, false otherwise
	 */
	public boolean hasCycles() {
		for (Node node : this) {
			if (node.hasCycles()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates the graph from the topics managed by the TopicManagerSingleton.
	 */
	public void createFromTopics() {
		for (Topic topic : TopicManagerSingleton.get().getTopics()) {
			Node topicNode = new Node("T" + topic.getName());
			this.add(topicNode);
			for (Agent agent : topic.getSubs()) {
				Node agentNode = findOrCreateNode("A" + agent.getName());
				topicNode.addEdge(agentNode);
			}
			for (Agent agent : topic.getPubs()) {
				Node agentNode = findOrCreateNode("A" + agent.getName());
				agentNode.addEdge(topicNode);
			}
		}
	}

	/**
	 * Finds a node by name or creates a new one if it does not exist.
	 *
	 * @param name the name of the node
	 * @return the existing or newly created node
	 */
	private Node findOrCreateNode(String name) {
		for (Node node : this) {
			if (node.getName().equals(name)) {
				return node;
			}
		}
		Node newNode = new Node(name);
		this.add(newNode);
		return newNode;
	}

	/**
	 * Prints the structure of the graph to the console.
	 */
	public void printGraph() {
		System.out.println("Graph structure:");
		for (Node node : this) {
			if (!node.getEdges().isEmpty()) {
				System.out.print(node.getName() + " -> ");
			}
			for (Node neighbor : node.getEdges()) {
				System.out.print(neighbor.getName() + ", ");
			}
			System.out.println();
		}
	}
}
