package vishalmysore.a2a.rest;

import io.github.vishalmysore.common.Agent;
import io.github.vishalmysore.mesh.AgentCatalog;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vishalmysore.a2a.A2AConnections;
import vishalmysore.a2a.sse.SseController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AgenticMeshController {

    @Autowired
    private SseController sseController;
    @Autowired
    private A2AConnections connections;

    @PostMapping("/agentCatalogActual")
    public AgentCatalog getAgentCatalog(HttpSession session) {
        return connections.getAgentCatalog(session.getId());
    }

    @PostMapping("/agentCatalog")
    @ResponseBody
    public String getAgentCatalogMock() {
        return "{"
                + "\"agent1\": {"
                +     "\"name\": \"Travel Booking\","
                +     "\"description\": \"Helps you book flights, hotels, and rentals.\","
                +     "\"version\": \"1.2.0\","
                +     "\"author\": \"TravelCorp\""
                + "},"
                + "\"agent2\": {"
                +     "\"name\": \"Car Servicing\","
                +     "\"description\": \"Schedules car maintenance and repairs.\","
                +     "\"version\": \"1.0.5\","
                +     "\"author\": \"AutoCare Inc.\""
                + "},"
                + "\"agent3\": {"
                +     "\"name\": \"Lawn Mower Repair\","
                +     "\"description\": \"Finds nearby lawn mower repair specialists.\","
                +     "\"version\": \"0.9.8\","
                +     "\"author\": \"GardenFix\""
                + "},"
                + "\"agent4\": {"
                +     "\"name\": \"Home Cleaning\","
                +     "\"description\": \"Books home cleaning services on demand.\","
                +     "\"version\": \"2.1.0\","
                +     "\"author\": \"CleanCo\""
                + "},"
                + "\"agent5\": {"
                +     "\"name\": \"Food Delivery\","
                +     "\"description\": \"Orders food from your favorite local restaurants.\","
                +     "\"version\": \"3.0.2\","
                +     "\"author\": \"Foodies\""
                + "}"
                + "}";
    }



}
