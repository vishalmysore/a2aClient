package vishalmysore.a2a;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.vishalmysore.a2a.client.A2AAgent;
import io.github.vishalmysore.a2a.domain.AgentCard;
import io.github.vishalmysore.a2a.domain.Skill;
import io.github.vishalmysore.common.Agent;
import io.github.vishalmysore.common.AgentIdentity;
import io.github.vishalmysore.common.AgentInfo;
import io.github.vishalmysore.mcp.client.MCPAgent;
import io.github.vishalmysore.mcp.domain.ListToolsResult;
import io.github.vishalmysore.mcp.domain.Tool;
import io.github.vishalmysore.mesh.AgentCatalog;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Log
@Service
public class A2AConnections {
    Map<String, AgentCatalog> sessionIDCatalogMap;

    public A2AConnections() {

    }
    @PostConstruct
    public void init() {
        // Initialization logic if needed
        sessionIDCatalogMap = new java.util.concurrent.ConcurrentHashMap<>();

    }

    private String getAgentInfo(A2AAgent agent) {
        AgentCard card = (AgentCard) agent.getInfo();
        return getAgentInfo(card);
    }

    private String getAgentInfo(AgentCard card) {
        if (card == null) {
            return "{\"error\": \"No agent information available.\"}";
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode agentInfo = mapper.createObjectNode();

        agentInfo.put("name", card.getName());
        agentInfo.put("description", card.getDescription());
        agentInfo.put("version", card.getVersion());

        List<Skill> skills = card.getSkills();
        if (skills != null && !skills.isEmpty()) {
            ArrayNode skillsArray = mapper.createArrayNode();
            for (Skill skill : skills) {
                ObjectNode skillNode = mapper.createObjectNode();
                skillNode.put("name", skill.getName());
                skillNode.put("description", skill.getDescription());

                String[] examples = skill.getExamples();
                if (examples != null && examples.length > 0) {
                    ArrayNode examplesArray = mapper.createArrayNode();
                    for (String example : examples) {
                        examplesArray.add(example);
                    }
                    skillNode.set("examples", examplesArray);
                } else {
                    skillNode.put("examples", "No examples available");
                }
                skillsArray.add(skillNode);
            }
            agentInfo.set("skills", skillsArray);
        } else {
            agentInfo.put("skills", "No skills available");
        }

        return agentInfo.toString();
    }
    private String getAgentInfo(ListToolsResult toolsResult) {
        List<Tool> tools = toolsResult.getTools();
        if (tools == null || tools.isEmpty()) {
            return "No tools available for this agent.";
        }

        StringBuilder result = new StringBuilder("Tools:\n");
        for (Tool tool : tools) {
            result.append("Name: ").append(tool.getName()).append(", ")
                    .append("Description: ").append(tool.getDescription()).append("\n");

        }
        return result.toString();
    }

    private String getAgentInfo(MCPAgent agent) {
        ListToolsResult toolsResult = (ListToolsResult) agent.getInfo();
        return getAgentInfo(toolsResult);
    }

    public String[] getAllAgents(String sessionId) {
        AgentCatalog catalog = sessionIDCatalogMap.get(sessionId);
        if (catalog == null) {
            log.warning("No catalog found for session ID: " + sessionId);
            return new String[0];
        }
        Map<AgentIdentity, Agent> agents = catalog.getAgents();
        if (agents == null || agents.isEmpty()) {
            log.warning("No agents found in the catalog for session ID: " + sessionId);
            return new String[0];
        }

        return agents.entrySet().stream()
                .map(entry -> {
                    Agent agent = entry.getValue();
                    if (agent instanceof A2AAgent) {
                        return getAgentInfo((A2AAgent) agent);
                    } else if (agent instanceof MCPAgent) {
                        return getAgentInfo((MCPAgent) agent);
                    } else {
                        log.warning("Unknown agent type for AgentIdentity: " + entry.getKey());
                        return "Unknown agent type";
                    }
                })
                .toArray(String[]::new);
    }

    public AgentCatalog getAgentCatalog(String sessionId) {
        AgentCatalog catalog = sessionIDCatalogMap.get(sessionId);
        return catalog;
    }

    public AgentInfo addAgent(String url , String sessionId){
        AgentCatalog catalog = sessionIDCatalogMap.get(sessionId);
        if (catalog == null) {
            catalog = new AgentCatalog();
            sessionIDCatalogMap.put(sessionId, catalog);
        }
        Agent agent = catalog.addAgent(url);
        return agent.getInfo();
    }

    public String conversation(String sessionId, String query){
       AgentCatalog catalog = sessionIDCatalogMap.get(sessionId);
         if (catalog == null) {
              log.warning("No catalog found for session ID: " + sessionId);
              return "Please add agent using the Plus Icon on left side of the screen.";
         }
       return catalog.processQuery(query).getTextResult() ;
    }

}
