import { Drawer, Card, Space } from "antd";
import { useEffect, useState } from "react";
import { listTemplates, getTemplateDetails } from "../../api/templateApi";

function TemplateSelector({ open, onClose, onSelect }) {
  const [templates, setTemplates] = useState([]);

  useEffect(() => {
    if (open) loadTemplates();
  }, [open]);

  async function loadTemplates() {
    try {
      const data = await listTemplates();   // Already normalized by API
      setTemplates(data);
    } catch (err) {
      console.error("Failed to load templates", err);
    }
  }

  console.log("Templates →", templates);

  return (
    <Drawer
      title="Select Template"
      placement="right"
      width="50%"
      onClose={onClose}
      open={open}
    >
      <Space orientation="vertical" style={{ width: "100%" }}>
  {templates.map((tpl) => (
    <Card
      key={tpl.id}
      hoverable
      style={{ cursor: "pointer" }}
      onClick={async () => {
        try {
          const res = await fetch(`http://localhost:8080/api/templates/${tpl.id}`);
          const fullTemplate = await res.json();
          onSelect(fullTemplate);
        } catch (err) {
          console.error("Template details failed", err);
        }
      }}
    >
      <div style={{ fontWeight: 600 }}>{tpl.name}</div>
      <div style={{ fontSize: 12, color: "#888" }}>
        {tpl.subject}
      </div>
    </Card>
  ))}
</Space>
    </Drawer>
  );
}

export default TemplateSelector;