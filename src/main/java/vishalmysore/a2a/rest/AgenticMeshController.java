package vishalmysore.a2a.rest;

import io.github.vishalmysore.common.Agent;
import io.github.vishalmysore.mesh.AgentCatalog;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import vishalmysore.a2a.A2AConnections;
import vishalmysore.a2a.sse.SseController;

@RestController
public class AgenticMeshController {

    @Autowired
    private SseController sseController;
    @Autowired
    private A2AConnections connections;

    @PostMapping("/agentCatalog")
    public AgentCatalog getAgentCatalog(HttpSession session) {
        return connections.getAgentCatalog(session.getId());
    }
}
