import { Button, Space } from "antd";

function EmailActions({ onCompose }) {
  return (
    <Space style={{ marginBottom: 16 }}>
      <Button type="primary" onClick={onCompose}>
        Compose Email
      </Button>
      <Button>Test</Button>
    </Space>
  );
}

export default EmailActions;