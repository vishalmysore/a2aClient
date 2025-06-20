document.addEventListener("DOMContentLoaded", function () {
  fetch("/agentCatalog")
    .then(res => res.json())
    .then(data => {
      const container = document.getElementById("agent-list-container");

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
    });

  const canvas = document.getElementById("mesh-canvas");

  canvas.addEventListener("dragover", (e) => {
    e.preventDefault();
  });

  canvas.addEventListener("drop", (e) => {
    e.preventDefault();
    const data = e.dataTransfer.getData("text/plain");
    const div = document.createElement("div");
    div.className = "agent";
    div.textContent = data;
    canvas.appendChild(div);
  });
});
