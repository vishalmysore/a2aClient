window.SSE_SOURCE = new EventSource("/sse/stream");
if (
  !window.socket    // in the process of closing
) {
  window.socket = new WebSocket(window.A2A_CONFIG.websocketUrl);
  console.log("Creating new:", window.socket);
  window.socket.onopen = () => {
    console.log("✅ WebSocket connected.");
  };

  window.socket.onerror = (err) => {
    console.error("❌ WebSocket error:", err);
  };

  window.socket.onclose = () => {
    console.warn("🔌 WebSocket closed.");
  };
}

console.log("WebSocket object:", window.socket);