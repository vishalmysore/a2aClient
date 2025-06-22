## Online A2A Client built with WebSocket and Java

### Description
a2aClient is a google A2A (agent to agent) client implemented using WebSocket and Java. It facilitates real-time communication between agents by leveraging WebSocket.
Also provides mechanism for agentic mesh and agentic rag and create complete worklow using a2a or mcp servers  

[A2AJava](https://github.com/vishalmysore/a2ajava) is a pure Java implementation designed to support both the Google A2A (Agent-to-Agent) protocol and the MCP (Model Context Protocol), making it a versatile framework for building agentic applications. The A2A protocol standardizes how AI agents communicate and collaborate across different systems, while the MCP protocol focuses on enabling agents to access and trigger a wide range of tools and resources.

Key Features and Benefits of A2AJava (A2A & MCP Support):

Cross-Protocol Support: A2AJava natively supports both A2A and MCP in the same codebase, allowing developers to build applications where agents can communicate with each other (A2A) and also access external tools or services (MCP).

Cross-Platform and Cross-Language: Runs on any operating system with JVM support and is compatible with clients built in JavaScript, Python, and other languages, as well as LLM agents like Claude and Google A2A clients.

Flexible Integration: Can be used with Spring Boot, Quarkus, or pure Java/Kotlin, making it easy to integrate into a variety of enterprise and AI applications.

Automatic Tool and Resource Registration: Uses Java annotations (such as @Agent and @Action) to automatically expose methods as A2A tasks or MCP tools, simplifying agent and tool development.

Lightweight and Extensible: Designed for minimal overhead and maximum flexibility, supporting both local and remote tool execution, JSON-RPC, schema validation, and callback mapping
### Features
- Real-time communication using WebSocket.
- Lightweight and easy-to-use client.
- Built with Java for cross-platform compatibility.

## Binary 

https://github.com/vishalmysore/a2aClient/releases/tag/local 
run the jar with java -jar a2aClient-0.2.3.jar
Connect to http://localhost:7860


### Setup Instructions
1. Clone the repository:

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   java -jar target/a2aClient-0.2.3.jar
   ```

### Usage
Connect to http://localhost:7860
