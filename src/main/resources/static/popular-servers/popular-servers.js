function openPopularServersPopup() {
  document.getElementById("popular-servers-popup").style.display = "flex";
}

function closePopularServersPopup() {
  document.getElementById("popular-servers-popup").style.display = "none";
}

document.addEventListener("DOMContentLoaded", function () {
  if (!window.SSE_SOURCE) return;

  window.SSE_SOURCE.addEventListener("popularServers", (event) => {
    const list = JSON.parse(event.data);
    const listEl = document.getElementById("popular-servers-list");
    if (!listEl) return;

    listEl.innerHTML = "";
    list.forEach(server => {
      const li = document.createElement("li");
      li.textContent = server;
      listEl.appendChild(li);
    });
  });
});
