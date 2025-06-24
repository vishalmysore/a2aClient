package vishalmysore.a2a.rest;

import io.github.vishalmysore.a2a.client.A2AAgent;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.common.Agent;
import io.github.vishalmysore.common.AgentIdentity;
import io.github.vishalmysore.mcp.client.MCPAgent;
import io.github.vishalmysore.mcp.domain.ListToolsResult;
import io.github.vishalmysore.mesh.AgentCatalog;
import jakarta.servlet.http.HttpSession;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;
import vishalmysore.a2a.A2AConnections;
import vishalmysore.a2a.sse.SseController;

import java.util.HashMap;
import java.util.Map;

@Log
@Service
public class AgenticMeshController {


    @Autowired
    private A2AConnections connections;

    @PostMapping("/agentCatalog")
    public String getAgentCatalog(String catalogid) {
        AgentCatalog catalog = connections.getAgentCatalog(catalogid);
        if (catalog == null) {
            log.info("No Agent added returning Mock catalog");
            return getAgentCatalogMock(); // Return an empty catalog if no agents are found
        }
        Map<AgentIdentity, Agent> agentMap = catalog.getAgents();
        if (agentMap == null || agentMap.isEmpty()) {
            log.info("No Agent added returning Mock catalog");
            return getAgentCatalogMock(); // Return an empty catalog if no agents are found
        }
        String id ;
        StringBuilder response = new StringBuilder("{");
        for (Map.Entry<AgentIdentity, Agent> entry : agentMap.entrySet()) {

            AgentIdentity identity = entry.getKey();
            id = identity.getAgentUniqueIDTobeUsedToIdentifyTheAgent();
            Agent agent = entry.getValue();
            String agentName =null,description=null,version=null,author=null;

            if(agent instanceof A2AAgent) {
                A2AAgent a2aAgent = (A2AAgent) agent;
                AgentCard card = (AgentCard) a2aAgent.getInfo();
                agentName = card.getName();
                description = card.getDescription();
                version = card.getVersion();
                author = card.getProvider().getOrganization();

            } else {
                MCPAgent mcpAgent = (MCPAgent) agent;
                ListToolsResult tools  = (ListToolsResult) mcpAgent.getInfo();
                agentName = tools.retrieveToolList();
                description = tools.retrieveToolList();

            }

            response.append("\"").append(id).append("\": {")
                    .append("\"agentId\": \"").append(id).append("\",")
                    .append("\"name\": \"").append(agentName).append("\",")
                    .append("\"description\": \"").append(description).append("\",")
                    .append("\"version\": \"").append(version).append("\",")
                    .append("\"author\": \"").append(author).append("\"")
                    .append("},");
        }

        // Remove trailing comma and close JSON object
        if (response.charAt(response.length() - 1) == ',') {
            response.deleteCharAt(response.length() - 1);
        }
        response.append("}");

        return response.toString();
    }

    @PostMapping(value = "/agentCatalogMock", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getAgentCatalogMock() {
        return "{"
                + "\"agent1\": {"
                +     "\"agentId\": \"agent1\","
                +     "\"name\": \"Travel Booking\","
                +     "\"description\": \"Helps you book flights, hotels, and rentals. This is Mock Agent Please add real agents using Plus Sign\","
                +     "\"version\": \"1.2.0\","
                +     "\"author\": \"TravelCorp\""
                + "},"
                + "\"agent2\": {"
                +     "\"agentId\": \"agent2\","
                +     "\"name\": \"Car Servicing\","
                +     "\"description\": \"Schedules car maintenance and repairs.  This is Mock Agent Please add real agents using Plus Sign\","
                +     "\"version\": \"1.0.5\","
                +     "\"author\": \"AutoCare Inc.\""
                + "},"
                + "\"agent3\": {"
                +     "\"agentId\": \"agent3\","
                +     "\"name\": \"Lawn Mower Repair\","
                +     "\"description\": \"Finds nearby lawn mower repair specialists.  This is Mock Agent Please add real agents using Plus Sign\","
                +     "\"version\": \"0.9.8\","
                +     "\"author\": \"GardenFix\""
                + "},"
                + "\"agent4\": {"
                +     "\"agentId\": \"agent4\","
                +     "\"name\": \"Home Cleaning\","
                +     "\"description\": \"Books home cleaning services on demand.  This is Mock Agent Please add real agents using Plus Sign\","
                +     "\"version\": \"2.1.0\","
                +     "\"author\": \"CleanCo\""
                + "},"
                + "\"agent5\": {"
                +     "\"agentId\": \"agent5\","
                +     "\"name\": \"Food Delivery\","
                +     "\"description\": \"Orders food from your favorite local restaurants. This is Mock Agent Please add real agents using Plus Sign\","
                +     "\"version\": \"3.0.2\","
                +     "\"author\": \"Foodies\""
                + "}"
                + "}";
    }




}
