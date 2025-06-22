document.addEventListener("DOMContentLoaded", function () {
  // Load agent catalog
  fetch("/agentCatalog", { method: "POST" })
    .then(res => res.text())
    .then(text => {
      const container = document.getElementById("agent-list-container");

      if (!container) {
        console.error("agent-list-container not found");
        return;
      }

      container.innerHTML = ""; // clear "Loading agents..."

      let data;
      try {
        data = JSON.parse(text);
      } catch (err) {
        container.textContent = "⚠️ Failed to load agents (invalid JSON)";
        console.error("Invalid JSON from /agentCatalog:", text);
        return;
      }

      Object.entries(data).forEach(([id, agent]) => {
        const div = document.createElement("div");
        div.className = "agent";
        div.draggable = true;
        div.textContent = `${id}: ${agent.name || 'Agent'}`;
        div.addEventListener("dragstart", (e) => {
          e.dataTransfer.setData("text/plain", div.textContent);
        });
        container.appendChild(div);
      });
    })
    .catch(err => {
      const container = document.getElementById("agent-list-container");
      if (container) {
        container.textContent = "⚠️ Failed to load agent list.";
      }
      console.error("Fetch failed:", err);
    });

  // Setup drag-and-drop canvas
  const canvas = document.getElementById("mesh-canvas");

  if (!canvas) {
    console.warn("mesh-canvas not found");
    return;
  }

  canvas.addEventListener("dragover", (e) => {
    e.preventDefault();
  });

  canvas.addEventListener("drop", (e) => {
    e.preventDefault();
    const data = e.dataTransfer.getData("text/plain");

    const div = document.createElement("div");
    div.className = "agent";
    div.textContent = data;

    // Create remove button
    const removeBtn = document.createElement("button");
    removeBtn.textContent = "×";
    removeBtn.style.marginLeft = "10px";
    removeBtn.style.cursor = "pointer";
    removeBtn.style.background = "transparent";
    removeBtn.style.border = "none";
    removeBtn.style.color = "red";
    removeBtn.style.fontWeight = "bold";
    removeBtn.style.fontSize = "16px";
    removeBtn.style.lineHeight = "1";

    // Remove agent div when clicked
    removeBtn.addEventListener("click", () => {
      div.remove();
    });

    div.appendChild(removeBtn);
    canvas.appendChild(div);
  });
});
