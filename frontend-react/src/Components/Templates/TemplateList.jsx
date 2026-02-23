import { Empty, Space, Card } from "antd";

function TemplateList({ templates, onEdit }) {
  if (!templates.length) return <Empty description="No Templates Yet" />;

  return (
    <Space direction="vertical" style={{ width: "100%" }}>
      {templates.map((tpl) => (
        <Card
          key={tpl.id}
          hoverable
          style={{ cursor: "pointer" }}
          onClick={() => onEdit(tpl)}
        >
          <div style={{ fontWeight: 600 }}>
            {tpl.name || "Untitled Template"}
          </div>

          <div style={{ fontSize: 13, color: "#888" }}>
            Subject: {tpl.subject || "—"}
          </div>

          <div style={{ fontSize: 12, color: "#aaa" }}>
            Last Modified: {tpl.lastModified || "—"}
          </div>
        </Card>
      ))}
    </Space>
  );
}

export default TemplateList;