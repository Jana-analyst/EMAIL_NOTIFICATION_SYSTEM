import { Button, Space } from "antd";

function TemplateActions({ onNewTemplate, onViewPlaceholders }) {
  return (
    <Space style={{ marginBottom: 16 }}>
      <Button type="primary" onClick={onNewTemplate}>
        New Template
      </Button>

      <Button onClick={onViewPlaceholders}>
        View Placeholders
      </Button>
    </Space>
  );
}

export default TemplateActions;