package vishalmysore.a2a.sse;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class SseController {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping("/sse/stream")
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        broadcastUserCount();

        emitter.onCompletion(() -> {
            emitters.remove(emitter);
            broadcastUserCount();
        });

        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            broadcastUserCount();
        });

        return emitter;
    }

    private void broadcastUserCount() {
        int count = emitters.size();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("users").data(count));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }

    public void pushPopularServers(List<String> servers) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("popularServers").data(servers));
            } catch (IOException e) {
                emitter.complete();
            }
        }
    }

    public void pushPopularServers(String url) {
        for (SseEmitter emitter : emitters) {
            try {
                // Wrap url in a JSON array string
                String jsonData = "[" + "\"" + url + "\"" + "]";
                emitter.send(SseEmitter.event().name("popularServers").data(jsonData));
            } catch (IOException e) {
                emitter.complete();
            }
        }
    }
    @PostMapping("/sse/send")
    public void sendEvent(@RequestParam String message) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("message").data(message));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
}
