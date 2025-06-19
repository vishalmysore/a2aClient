package vishalmysore.a2a.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.common.AgentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import vishalmysore.a2a.A2AConnections;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Autowired
    private A2AConnections connections;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        JsonNode root = objectMapper.readTree(payload);
        String action = root.path("action").asText();

        if ("addAgent".equals(action)) {
            String url = root.path("url").asText();
            AgentInfo agentInfo = connections.addAgent(url, session.getId());
            session.sendMessage(new TextMessage("Agent added: " + objectMapper.writeValueAsString(agentInfo)));
        } else if ("talkToAgent".equals(action)) {
            String query = root.path("query").asText();
            String response = connections.conversation(session.getId(), query);
            session.sendMessage(new TextMessage("Agent says: " + response));
        }else if ("listAgents".equals(action)) {
            String[] response = connections.getAllAgents(session.getId());
            for (String agentInfo : response) {
                session.sendMessage(new TextMessage("Agent: " + agentInfo));
            }
        }

        else {
            session.sendMessage(new TextMessage("Unknown action: " + action));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session.getId());
    }
}
