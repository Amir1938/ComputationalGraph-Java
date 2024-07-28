package servlets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import configs.Graph;
import graph.Message;
import graph.Topic;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;
import server.RequestParser.RequestInfo;
import views.HtmlGraphWriter;

/**
 * The TopicDisplayer class implements the Servlet interface and handles displaying topics and their messages.
 */
public class TopicDisplayer implements Servlet {

    private static Map<String, Double> topics;

    /**
     * Handles the HTTP request by processing the topic and message parameters and generating an HTML response.
     *
     * @param ri the request information
     * @param toClient the output stream to send the response to the client
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {

        if (!ri.getHttpCommand().equalsIgnoreCase("get")) {
            sendErrorResponse(toClient, 405, "Method Not Allowed");
            return;
        }

        topics = new ConcurrentHashMap<>();
        TopicManager tm = TopicManagerSingleton.get();

        updateTable();

        Map<String, String> params = ri.getParameters();
        String topicName = params.get("topic").toUpperCase();
        String message = params.get("message");

        Message m = new Message(message);
        int flag = 0;

        // Check if topic exists in the topics map, and it's not an output of an agent and the message is double
        if (topics.containsKey(topicName) && tm.getTopic(topicName).getPubs().isEmpty() && !Double.isNaN(m.asDouble)) {
            tm.getTopic(topicName).publish(m);
            topics.put(topicName, m.asDouble);
            updateTable();
        } else {
            flag++;
            String errorMsg = null;
            if (!topics.containsKey(topicName)) {
                errorMsg = "Topic does not exist. Please choose another topic.";
            }
            if (!tm.getTopic(topicName).getPubs().isEmpty()) {
                errorMsg = "You have chosen a topic that cannot be written to.";
            }
            if (Double.isNaN(m.asDouble)) {
                errorMsg = "The message is not of double type. Please enter a new message.";
            }

            // Generate HTML for a popup window with an error message
            String popupHtml = "<script>"
                    + "window.onload = function() {"
                    + "    alert('Error: " + errorMsg + "');"
                    + "};"
                    + "</script>";

            // Send the popup HTML as part of the response
            StringBuilder htmlResponse = new StringBuilder();
            htmlResponse.append("HTTP/1.1 200 OK\r\n");
            htmlResponse.append("Content-Type: text/html; charset=UTF-8\r\n");
            htmlResponse.append("\r\n");
            htmlResponse.append(popupHtml);

            // Write the response to the client
            toClient.write(htmlResponse.toString().getBytes(StandardCharsets.UTF_8));
            toClient.flush();
        }

        Graph graph = new Graph();
        graph.createFromTopics();

        if (HtmlGraphWriter.getGraphHTML(graph).equals("cycles")) {
            sendErrorResponse(toClient, 400, "ERROR!! the graph has cycles.");
            return;
        }

        String htmlResponse = sendHtmlResponse(toClient, flag);
        saveHtmlToFile(htmlResponse);
    }

    /**
     * Sends an HTML response to the client.
     *
     * @param toClient the output stream to send the response to the client
     * @param flag a flag indicating whether an error occurred
     * @return the HTML response as a string
     * @throws IOException if an I/O error occurs
     */
    private String sendHtmlResponse(OutputStream toClient, int flag) throws IOException {
        StringBuilder htmlResponse = new StringBuilder();
        if (flag == 0) {
            htmlResponse.append("HTTP/1.1 200 OK\r\n");
            htmlResponse.append("Content-Type: text/html; charset=UTF-8\r\n");
            htmlResponse.append("\r\n");
        }

        htmlResponse.append("<!DOCTYPE html>\n");
        htmlResponse.append("<html lang=\"en\">\n");
        htmlResponse.append("<head>\n");
        htmlResponse.append("    <meta charset=\"UTF-8\">\n");
        htmlResponse.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        htmlResponse.append("    <title>Styled Table</title>\n");
        htmlResponse.append("    <style>\n");
        htmlResponse.append("        body {\n");
        htmlResponse.append("            font-family: candara, sans-serif;\n");
        htmlResponse.append("            margin: 0;\n");
        htmlResponse.append("            padding: 0;\n");
        htmlResponse.append("            display: flex;\n");
        htmlResponse.append("            justify-content: center;\n");
        htmlResponse.append("            align-items: center;\n");
        htmlResponse.append("            min-height: 100vh;\n");
        htmlResponse.append("        }\n");
        htmlResponse.append("        table {\n");
        htmlResponse.append("            border-collapse: collapse;\n");
        htmlResponse.append("            width: 50%;\n");
        htmlResponse.append("            background-color: #ffffff;\n");
        htmlResponse.append("            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n");
        htmlResponse.append("        }\n");
        htmlResponse.append("        th, td {\n");
        htmlResponse.append("            border: 1px solid #0e0e0e;\n");
        htmlResponse.append("            padding: 10px;\n");
        htmlResponse.append("            text-align: left;\n");
        htmlResponse.append("        }\n");
        htmlResponse.append("        th {\n");
        htmlResponse.append("            background-color: #f9b5d7;\n");
        htmlResponse.append("        }\n");
        htmlResponse.append("        tr:nth-child(even) {\n");
        htmlResponse.append("            background-color: #f9d9e9;\n");
        htmlResponse.append("        }\n");
        htmlResponse.append("    </style>\n");
        htmlResponse.append("</head>\n");
        htmlResponse.append("<body>\n");
        htmlResponse.append("    <table>\n");
        htmlResponse.append("        <tr>\n");
        htmlResponse.append("            <th>Topic</th>\n");
        htmlResponse.append("            <th>Message</th>\n");
        htmlResponse.append("        </tr>\n");

        // Iterate over the topics map
        for (Map.Entry<String, Double> entry : topics.entrySet()) {
            htmlResponse.append("        <tr>\n");
            htmlResponse.append("            <td>").append(entry.getKey()).append("</td>\n");
            htmlResponse.append("            <td>").append(entry.getValue()).append("</td>\n");
            htmlResponse.append("        </tr>\n");
        }

        htmlResponse.append("    </table>\n");
        htmlResponse.append("</body>\n");
        htmlResponse.append("</html>\n");

        // Write the response to the client
        toClient.write(htmlResponse.toString().getBytes(StandardCharsets.UTF_8));
        toClient.flush();

        return htmlResponse.toString();
    }

    /**
     * Saves the HTML response to a file.
     *
     * @param htmlResponse the HTML response as a string
     * @throws IOException if an I/O error occurs
     */
    private void saveHtmlToFile(String htmlResponse) throws IOException {
        File file = new File("html_files/temp.html");
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(htmlResponse);
        }
    }

    /**
     * Sends an error response to the client.
     *
     * @param toClient the output stream to send the response to the client
     * @param statusCode the HTTP status code
     * @param message the error message
     * @throws IOException if an I/O error occurs
     */
    private void sendErrorResponse(OutputStream toClient, int statusCode, String message) throws IOException {
        String response = "HTTP/1.1 " + statusCode + " " + message + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                message;
        toClient.write(response.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Updates the topics table with the latest messages.
     */
    public void updateTable() {
        TopicManager tm = TopicManagerSingleton.get();
        if (!tm.getTopics().isEmpty()) {
            // Get all topics and their last messages
            for (Topic topic : tm.getTopics()) {
                // Remove the 'T' prefix if present
                if (topic.getName().startsWith("T")) {
                    String newName = topic.getName().substring(1);
                    topics.put(newName, topic.getMsg().asDouble);
                } else {
                    topics.put(topic.getName(), topic.getMsg().asDouble);
                }
            }
        }
    }

    /**
     * Closes the servlet and releases any resources held by it.
     */
    @Override
    public void close() throws IOException {
        // Clean up resources if needed
    }
}
