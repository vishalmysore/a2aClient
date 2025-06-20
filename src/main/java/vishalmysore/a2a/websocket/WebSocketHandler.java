package vishalmysore.a2a.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.common.AgentInfo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import vishalmysore.a2a.A2AConnections;
import vishalmysore.a2a.sse.SseController;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
@Log
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Autowired
    private SseController sseController;
    @Autowired
    private A2AConnections connections;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        log.info("Received message: " + message.getPayload()+" from session: " + session.getId());
        String payload = message.getPayload();
        JsonNode root = objectMapper.readTree(payload);
        String action = root.path("action").asText();

        if ("addAgent".equals(action)) {
            String url = root.path("url").asText();
            AgentInfo agentInfo = connections.addAgent(url, session.getId());
            session.sendMessage(new TextMessage("AgentAdded: " + objectMapper.writeValueAsString(" Agent added with URL: " + url )));
            int number = connections.getAllAgents(session.getId()).length;
            session.sendMessage(new TextMessage("NumberOfAgents: " + number));
            sseController.pushPopularServers(url);
        } else if ("talkToAgent".equals(action)) {
            String query = root.path("query").asText();
            String response = connections.conversation(session.getId(), query);
            log.info("Response from agent: " + response);
            session.sendMessage(new TextMessage("Agent says: " + response));
        }else if ("listAgents".equals(action)) {
            String[] response = connections.getAllAgents(session.getId());
            for (String agentInfo : response) {
                session.sendMessage(new TextMessage("AgentList: " + agentInfo));
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
