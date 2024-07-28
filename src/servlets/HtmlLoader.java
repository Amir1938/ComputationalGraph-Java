package servlets;

import server.RequestParser.RequestInfo;
import java.io.*;

/**
 * The HtmlLoader class implements the Servlet interface and handles loading and serving HTML files.
 */
public class HtmlLoader implements Servlet {
	// Class Variables
	private final String dir_name;

	/**
	 * Constructs an HtmlLoader with the specified directory name.
	 *
	 * @param dir_name the directory name where HTML files are located
	 */
	public HtmlLoader(String dir_name) {
		this.dir_name = dir_name;
	}

	/**
	 * Handles the HTTP request by serving the requested HTML file.
	 *
	 * @param ri the request information
	 * @param outToClient the output stream to send the response to the client
	 */
	@Override
	public void handle(RequestInfo ri, OutputStream outToClient) {
		try {
			PrintWriter out = new PrintWriter(outToClient, true);
			boolean success = false;
			StringBuilder htmlContent = new StringBuilder();
			String htmlFileName;

			// Handle GET requests
			if ("GET".equals(ri.getHttpCommand())) {
				String path = ri.getUri();
				if (ri.getUriSegments().length >= 2) {
					htmlFileName = ri.getUriSegments()[1]; // get the HTML file name we want to open
				} else {
					htmlFileName = "";
				}

				// Clean temp.html
				if (htmlFileName.equals("temp.html")) {
					String filePath = dir_name + "/temp.html";
					try {
						File file = new File(filePath);
						FileWriter writer = new FileWriter(file);
						writer.write(""); // Write an empty string to the file
						writer.close();
					} catch (IOException e) {
						System.err.println("Error while cleaning the file: " + e.getMessage());
					}
				}

				// Clean the graph from previous entries
				if (htmlFileName.equals("graph.html")) {
					String filePath = dir_name + "/graph.html";
					try {
						File file = new File(filePath);
						FileWriter writer = new FileWriter(file);
						writer.write(""); // Write an empty string to the file
						writer.close();
					} catch (IOException e) {
						System.err.println("Error while cleaning the file: " + e.getMessage());
					}
				}

				if (!htmlFileName.isEmpty()) {
					// Read the HTML page we want to send
					try (BufferedReader htmlReader = new BufferedReader(new FileReader(dir_name + "/" + htmlFileName))) {
						String line;
						while ((line = htmlReader.readLine()) != null) {
							htmlContent.append(line).append("\n");
						}
						success = true;
					}
				}
			}

			if (success) {
				// Send the HTTP response
				out.println("HTTP/1.1 200 OK");
				out.println("Content-Type: text/html");
				out.println("Content-Length: " + htmlContent.length());
				out.println();
				out.println(htmlContent.toString());
			} else {
				// Error
				out.println("HTTP/1.1 404 Not Found");
				out.println("Content-Type: text/plain");
				out.println();
				out.println("404 Not Found");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the servlet and releases any resources held by it.
	 */
	@Override
	public void close() {
		// Implement any necessary cleanup here
	}
}
