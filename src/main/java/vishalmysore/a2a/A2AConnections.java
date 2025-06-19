package vishalmysore.a2a;

import io.github.vishalmysore.a2a.client.A2AAgent;
import io.github.vishalmysore.common.Agent;
import io.github.vishalmysore.common.AgentIdentity;
import io.github.vishalmysore.common.AgentInfo;
import io.github.vishalmysore.mesh.AgentCatalog;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

        // Convert agent information to a string array
        return agents.keySet().stream()
                .map(AgentIdentity::toString)
                .toArray(String[]::new);
    }

    public AgentInfo addAgent(String url , String sessionId){
        AgentCatalog catalog = sessionIDCatalogMap.get(sessionId);
        if (catalog == null) {
            catalog = new AgentCatalog();
            sessionIDCatalogMap.put(sessionId, catalog);
        }
        Agent a2aAgent = new A2AAgent();
        a2aAgent.connect(url);
        catalog.addAgent(url);
        return a2aAgent.getInfo();
    }

    public String conversation(String sessionId, String query){
       AgentCatalog catalog = sessionIDCatalogMap.get(sessionId);
         if (catalog == null) {
              log.warning("No catalog found for session ID: " + sessionId);
              return "Please add agent";
         }
       return catalog.processQuery(query).getTextResult() ;
    }

}
