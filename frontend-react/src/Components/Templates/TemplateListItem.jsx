import { Card } from "antd";
import { useNavigate } from "react-router-dom";

function TemplateListItem({ name, updated }) {
  const navigate = useNavigate();

  return (
    <Card
      size="small"
      hoverable
      onClick={() => navigate("/templates/editor")}
    >
      <div style={{ fontWeight: 500 }}>{name}</div>
      <div style={{ fontSize: 12, color: "#888" }}>
        Last modified: {updated}
      </div>
    </Card>
  );
}

export default TemplateListItem;