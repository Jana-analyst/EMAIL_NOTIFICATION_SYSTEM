import { Space } from "antd";
import {
  QuestionCircleOutlined,
  AppstoreOutlined,
  UserOutlined,
} from "@ant-design/icons";

function Header() {
  return (
    <div
      style={{
        height: "100%",
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        padding: "0 16px",
        background: "#fff",
      }}
    >
      <div style={{ fontWeight: 600 }}>
        Email Notification System
      </div>

      <Space size="middle">
        <QuestionCircleOutlined />
        <AppstoreOutlined />
        <UserOutlined />
      </Space>
    </div>
  );
}

export default Header;