package servlets;

import java.io.*;
import java.nio.charset.StandardCharsets;

import configs.GenericConfig;
import configs.Graph;
import configs.PlusAgent;
import graph.TopicManagerSingleton;
import server.RequestParser.RequestInfo;
import views.HtmlGraphWriter;

/**
 * The ConfLoader class implements the Servlet interface and handles the loading of configuration files.
 */
public class ConfLoader implements Servlet {

	/**
	 * Handles the HTTP request by processing the configuration file and generating an HTML response.
	 *
	 * @param ri the request information
	 * @param toClient the output stream to send the response to the client
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
		byte[] content = ri.getContent();

		// Writing the content to a new file
		String filePath = "config_files/graph_config.txt";
		try {
			File outputFile = new File(filePath);
			try (FileOutputStream fos = new FileOutputStream(outputFile)) {
				fos.write(content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Creating the topics for the new configuration
		TopicManagerSingleton.get().getTopics().clear();
		GenericConfig conf = new GenericConfig();
		conf.setConfFile(filePath);

		conf.create();

		Graph graph = new Graph();
		graph.createFromTopics();

		// Check for cycles
		if (graph.hasCycles()) {
			sendErrorResponse(toClient, "ERROR!! the graph has cycles.");
			return;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			int lineCount = 0;
			while (reader.readLine() != null) {
				lineCount++;
			}

			if (graph.size() != (TopicManagerSingleton.get().getTopics().size() + lineCount / 3)) {
				sendErrorResponse(toClient, "ERROR!! bad config file.");
				return;
			}
		}

		// Showing the graph as a POST response to the client
		String html = HtmlGraphWriter.getGraphHTML(graph);
		String htmlResponse = "HTTP/1.1 200 OK\n" + "Content-Type: text/html\n" + "Content-Length: " + html.length() + "\n\n" + html;
		toClient.write(htmlResponse.getBytes(StandardCharsets.UTF_8));
		toClient.flush();

		// Clean the file before writing
		File file = new File("html_files/graph.html");
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(""); // Write an empty string to clean the file
		} catch (IOException e) {
			System.err.println("Error while cleaning the file: " + e.getMessage());
		}

		// Write the new content to the file
		try (FileWriter fileWriter = new FileWriter(file, false)) {
			fileWriter.write(htmlResponse);
		} catch (IOException e) {
			System.err.println("Error while writing to the file: " + e.getMessage());
		}
	}

	/**
	 * Sends an error response to the client.
	 *
	 * @param toClient the output stream to send the response to the client
	 * @param message the error message
	 * @throws IOException if an I/O error occurs
	 */
	private void sendErrorResponse(OutputStream toClient, String message) throws IOException {
		String response = "HTTP/1.1 " + 400 + " " + message + "\r\n" +
				"Content-Type: text/plain\r\n" +
				"\r\n" +
				message;
		toClient.write(response.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Closes the servlet and releases any resources held by it.
	 */
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
	}
}
