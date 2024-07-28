# Advanced-Programming-Project : Computational Graph Visualization
by: Amir Ilan and Maya Naor

The project employs a publisher-subscriber architecture to construct a computational graph. This graph enables parallel execution of complex calculations by breaking them down into interconnected components. Each component's output serves as input for subsequent components, forming a dependency chain.

## Key Components

Agent: Interface defining core functionality for entities that can publish and subscribe to topics.

IncAgent, PlusAgent: Implementations of Agent for specific operations.
ParallelAgent: Wrapper enabling parallel processing of messages.
Message: Represents data exchanged between agents, supporting multiple formats.
Topic and TopicManagerSingleton: Manage topics and subscriptions.
Graph and Node: Represent the graph structure of the system.

### HTTP Server and Servlets
MyHTTPServer: Implements a simple HTTP server that handles GET, POST, and DELETE requests.
RequestParser: Parses HTTP requests and extracts relevant information.
Servlet: Interface for handling HTTP requests.
ConfLoader: Servlet for loading and processing configuration files.
HtmlLoader: Servlet for serving HTML files.
TopicDisplayer: Servlet for displaying topics and their messages in HTML format.

### Configuration and Utilities
GenericConfig: Reads configuration files to create agents.
HtmlGraphWriter: Generates HTML representation of the graph structure.

## Features
Asynchronous communication between components
Support for multiple data formats
Parallel processing of messages
Thread-safe topic management
Flexible publish-subscribe model
Custom agent implementations
Graph-based system representation and analysis
Configuration file support for system setup
Web-based interaction and visualization
Dynamic topic updates and display

## Usage
1. Start the HTTP server:

```java
MyHTTPServer server = new MyHTTPServer(8080, 10);
server.addServlet("GET", "/html", new HtmlLoader("html_files"));
server.addServlet("POST", "/conf", new ConfLoader());
server.addServlet("GET", "/topics", new TopicDisplayer());
server.start();


## Project Demo link:
https://youtu.be/P1d6o3oAgMw
