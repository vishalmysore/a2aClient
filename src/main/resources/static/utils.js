function renderJsonAsTable(data) {
  const table = document.createElement("table");
  table.style.borderCollapse = "collapse";
  table.style.marginTop = "8px";

  if (Array.isArray(data)) {
    if (data.length === 0) {
      const row = table.insertRow();
      row.insertCell().textContent = "(empty array)";
    } else {
      const headers = new Set();
      data.forEach(item => {
        if (typeof item === "object" && item !== null) {
          Object.keys(item).forEach(key => headers.add(key));
        }
      });

      const headerRow = table.insertRow();
      headers.forEach(header => {
        const th = document.createElement("th");
        th.textContent = header;
        th.style.border = "1px solid #ccc";
        th.style.padding = "4px";
        headerRow.appendChild(th);
      });

      data.forEach(item => {
        const row = table.insertRow();
        headers.forEach(header => {
          const cell = row.insertCell();
          cell.style.border = "1px solid #ccc";
          cell.style.padding = "4px";
          const value = item[header];
          cell.appendChild(formatValue(value));
        });
      });
    }
  } else if (typeof data === "object") {
    Object.entries(data).forEach(([key, value]) => {
      const row = table.insertRow();
      const keyCell = row.insertCell();
      const valueCell = row.insertCell();

      keyCell.textContent = key;
      keyCell.style.fontWeight = "bold";
      keyCell.style.border = valueCell.style.border = "1px solid #ccc";
      keyCell.style.padding = valueCell.style.padding = "4px";

      valueCell.appendChild(formatValue(value));
    });
  } else {
    const row = table.insertRow();
    row.insertCell().textContent = String(data);
  }

  return table;
}

function formatValue(value) {
  const span = document.createElement("span");

  if (Array.isArray(value)) {
    if (value.every(item => typeof item === "string" || typeof item === "number")) {
      // Render arrays of primitives as bullet list
      const ul = document.createElement("ul");
      value.forEach(val => {
        const li = document.createElement("li");
        li.textContent = val;
        ul.appendChild(li);
      });
      return ul;
    } else {
      // For array of objects, use table rendering recursively
      return renderJsonAsTable(value);
    }
  } else if (typeof value === "object" && value !== null) {
    return renderJsonAsTable(value);
  }

  span.textContent = String(value);
  return span;
}

