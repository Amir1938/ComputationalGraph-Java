# Advanced-Programming-Project : Computational Graph Visualization
by: Amir Ilan and Maya Naor

The project employs a publisher-subscriber architecture to construct a computational graph. This graph enables parallel execution of complex calculations by breaking them into interconnected components. Each component's output is input for subsequent components, forming a dependency chain.

## Key Components

* Agent: Interface defining core functionality for entities that publish and subscribe to topics.
*IncAgent, PlusAgent: Implementations of Agent for specific operations.
*ParallelAgent: Wrapper enabling parallel processing of messages.
*Message: Represents data exchanged between agents, supporting multiple formats.
*Topic and TopicManagerSingleton: Manage topics and subscriptions.
*Graph and Node: Represent the graph structure of the system.

### HTTP Server and Servlets
*MyHTTPServer: Implements a simple HTTP server that handles GET, POST, and DELETE requests.
*RequestParser: Parses HTTP requests and extracts relevant information.
*Servlet: Interface for handling HTTP requests.
*ConfLoader: Servlet for loading and processing configuration files.
*HtmlLoader: Servlet for serving HTML files.
*TopicDisplayer: Servlet for displaying topics and their messages in HTML format.

### Configuration and Utilities
GenericConfig: Reads configuration files to create agents.
HtmlGraphWriter: Generates HTML representation of the graph structure.

### Web Interface
*index.html: Main page for the web interface
*form.html: Form for uploading configuration files and interacting with topics
*initialGraph.html: Container for graph visualization

## Features
-Asynchronous communication between components
-Support for multiple data formats
-Parallel processing of messages
-Thread-safe topic management
-Flexible publish-subscribe model
-Custom agent implementations
-Graph-based system representation and analysis
-Configuration file support for system setup
-Web-based interaction and visualization
-Dynamic topic updates and display

## Web Interface Components
### index.html
The main entry point for the web interface. It provides a container for the graph visualization and other components.
### form.html
Contains two main sections:
1. Upload Configuration: Allows users to select and upload a configuration file
2. Topic Interaction: Enables users to update topic values by specifying a topic and a message
### initialGraph.html
Provides a container (#canvasContainer) for rendering the graph visualization.

## Configuration File Format
The configuration file should follow this format for each agent:
```
[Agent Class Name]
[Comma-separated list of subscription topics]
[Comma-separated list of publication topics]
```
example: 
```
configs.IncAgent
input_topic
output_topic
configs.PlusAgent
input_topic1,input_topic2
sum_output_topic
```

## Usage
1. Start the HTTP server:
 ```java
MyHTTPServer server = new MyHTTPServer(8080, 10);
server.addServlet("GET", "/html", new HtmlLoader("html_files"));
server.addServlet("POST", "/conf", new ConfLoader());
server.addServlet("GET", "/topics", new TopicDisplayer());
server.start();
```
2. Access the web interface:
Open a web browser and navigate to `http://localhost:8080/html/index.html`

3. Upload configuration: Use the "Upload Configuration" form to select and upload a configuration file
4. View and interact with the system:
The graph visualization will appear after uploading a valid configuration
Use the "Topic" form to update topic values
The graph and topic table will update in real-time

## Project Demo link:
https://youtu.be/P1d6o3oAgMw
