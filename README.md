# Advanced-Programming-Project : Computational Graph Visualization
by: Amir Ilan and Maya Naor

The project employs a publisher-subscriber architecture to construct a computational graph. This graph enables parallel execution of complex calculations by breaking them down into interconnected components. Each component's output serves as input for subsequent components, forming a dependency chain.

## Features
- **Configuration Management: Enables users to upload and apply configuration files that define the graph’s structure and properties.
- **Real-time Interaction: Allows users to publish messages to topics, with the graph dynamically updating to reflect these changes in real-time.

## Getting Started

### Prerequisites
- Java JDK 11 or higher

### Running the Project

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/your-project-repo.git
   cd your-project-repo


## How to run it:
1. Compile the Java Code Navigate to the project directory and compile the Java code.
   <pre>javac -d bin src/**/*.java </pre>
2. Start the Server Run the MainTrain class to start the server.
   <pre>java -cp bin graph.MainTrain </pre>
3. Access the Application Open a web browser and navigate to http://localhost:8080/app/index.html to access the application interface.

## Commands
- Upload Configuration: Use the "Upload" button in the application interface to load a .conf file that defines the graph structure.
- Interact with the Graph: Click and drag nodes to reposition them. Use the interface to publish messages to topics and observe the graph's response.
- Deploy New Configuration: After modifying or uploading a new configuration file, use the "Deploy" button to apply changes to the graph.

## How It Works
- Backend: The Java backend handles HTTP requests, manages configurations, and processes topic messages. It dynamically updates the graph structure based on the configuration files and messages received.
- Frontend: The D3.js library renders the graph based on the data provided by the backend. It supports interactive manipulation of the graph elements.

## Project Demo link:
https://youtu.be/P1d6o3oAgMw
