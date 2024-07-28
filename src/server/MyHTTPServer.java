package server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.RequestParser.RequestInfo;
import servlets.Servlet;

import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * The MyHTTPServer class implements a simple HTTP server that handles GET, POST, and DELETE requests using servlets.
 */
public class MyHTTPServer extends Thread implements HTTPServer {

    private final int port;
    private final ConcurrentHashMap<String, Servlet> get = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Servlet> post = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Servlet> delete = new ConcurrentHashMap<>();
    private final ExecutorService threadPool;
    private volatile boolean close = false;

    /**
     * Constructs a MyHTTPServer with the specified port and number of threads.
     *
     * @param port the port number on which the server will listen
     * @param nThreads the number of threads in the thread pool
     */
    public MyHTTPServer(int port, int nThreads) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(nThreads);
    }

    /**
     * Adds a servlet to handle requests for the specified HTTP command and URI.
     *
     * @param httpCommand the HTTP command (GET, POST, DELETE)
     * @param uri the URI for which the servlet will handle requests
     * @param s the servlet to handle requests
     */
    public void addServlet(String httpCommand, String uri, Servlet s) {
        ConcurrentHashMap<String, Servlet> currentServlet = getServletMap(httpCommand);
        currentServlet.put(uri, s);
    }

    /**
     * Removes a servlet for the specified HTTP command and URI.
     *
     * @param httpCommand the HTTP command (GET, POST, DELETE)
     * @param uri the URI for which the servlet will be removed
     */
    public void removeServlet(String httpCommand, String uri) {
        ConcurrentHashMap<String, Servlet> currentServlet = getServletMap(httpCommand);
        currentServlet.remove(uri);
    }

    /**
     * Returns the servlet map for the specified HTTP command.
     *
     * @param httpCommand the HTTP command (GET, POST, DELETE)
     * @return the servlet map for the specified HTTP command
     */
    private ConcurrentHashMap<String, Servlet> getServletMap(String httpCommand) {
        return switch (httpCommand.toLowerCase()) {
            case "get" -> get;
            case "post" -> post;
            case "delete" -> delete;
            default -> throw new IllegalArgumentException("Unsupported HTTP command: " + httpCommand);
        };
    }

    /**
     * Runs the server, accepting client connections and handling requests.
     */
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            //System.out.println("Server listening on port " + port);
            while (!close) {
                try {
                    serverSocket.setSoTimeout(1000);
                    Socket clientSocket = serverSocket.accept();
                    threadPool.submit(() -> handleClient(clientSocket));
                } catch (IOException ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the server and shuts down the thread pool.
     */
    public void close() {
        close = true;
        threadPool.shutdown();
        //System.out.println("Server closed");
    }

    /**
     * Handles a client connection, reading the request and dispatching it to the appropriate servlet.
     *
     * @param clientSocket the client socket
     */
    private void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            // Read client request
            RequestInfo request = RequestParser.parseRequest(reader);
            ConcurrentHashMap<String, Servlet> currentServlet = getServletMap(request.getHttpCommand());

            // Search for the longest URI match
            String longestMatch = "";
            for (String key : currentServlet.keySet()) {
                if (request.getUri().startsWith(key) && key.length() > longestMatch.length()) {
                    longestMatch = key;
                }
            }

            currentServlet.get(longestMatch).handle(request, clientSocket.getOutputStream());

            // Close stream and socket
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                Thread.currentThread().interrupt();
            } catch (IOException ignored) {}
        }
    }
}
