package views;

import configs.Graph;
import configs.Node;
import graph.Topic;
import graph.TopicManagerSingleton;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The HtmlGraphWriter class provides methods to generate HTML representations of a graph.
 */
public class HtmlGraphWriter {

    /**
     * Generates an HTML representation of the given graph.
     *
     * @param graph the graph to generate HTML for
     * @return the HTML content as a string
     */
    public static String getGraphHTML(Graph graph) {

        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader("html_files/initialGraph.html"))) {
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str).append("\n");
            }
        } catch (IOException e) {
            // Handle any exceptions
            e.printStackTrace();
        }
        String content = contentBuilder.toString();

        // Assign positions to nodes
        Map<String, NodePosition> nodePositions = assignPositions(graph);

        StringBuilder nodesHtml = new StringBuilder();
        nodesHtml.append("[ ");
        for (Node n : graph) {
            NodePosition pos = nodePositions.get(n.getName());
            nodesHtml.append(" { x: ").append(pos.x)
                    .append(", y: ").append(pos.y)
                    .append(", label: '").append(n.getName())
                    .append("' },\n");
        }
        nodesHtml.append(" ];");

        StringBuilder edgesHtml = new StringBuilder();
        edgesHtml.append("[ ");
        for (Node n1 : graph) {
            for (Node n2 : n1.getEdges()) {
                edgesHtml.append(" [ '").append(n1.getName())
                        .append("', '").append(n2.getName())
                        .append("'],\n ");
            }
        }
        edgesHtml.append(" ]");

        StringBuilder msgHtml = new StringBuilder();
        msgHtml.append("[ ");
        for (Node n1 : graph) {
            if (n1.getName().startsWith("T")) {
                String name = n1.getName().substring(1);
                Topic t = TopicManagerSingleton.get().getTopic(name);
                if (t.getMsg() != null) {
                    msgHtml.append(" [ '").append(n1.getName())
                            .append("', '").append(t.getMsg().asDouble)
                            .append("'],\n ");
                }
            }
        }
        msgHtml.append(" ]");

        content = content.replace("NODES_PLACEHOLDER", nodesHtml);
        content = content.replace("EDGES_PLACEHOLDER", edgesHtml);
        content = content.replace("MSGS_PLACEHOLDER", msgHtml);

        // Check for cycles
        if (graph.hasCycles()) {
            return "cycles";
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("html_files/graph1.html"))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    /**
     * Assigns positions to nodes in the graph for visualization.
     *
     * @param graph the graph to assign positions to
     * @return a map of node names to their positions
     */
    private static Map<String, NodePosition> assignPositions(Graph graph) {
        Map<String, NodePosition> nodePositions = new HashMap<>();
        int width = 800;
        int height = 500;
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = height / 2 - 50; // 50 pixels padding

        int nodeCount = graph.size();
        int index = 0;

        for (Node n : graph) {
            double angle = 2 * Math.PI * index / nodeCount;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));

            nodePositions.put(n.getName(), new NodePosition(x, y));
            index++;
        }
        return nodePositions;
    }

    // Inner class to hold node positions
    private static class NodePosition {
        int x;
        int y;

        NodePosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
