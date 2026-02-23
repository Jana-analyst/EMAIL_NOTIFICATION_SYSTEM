import { Space, Typography } from "antd";
import { AppstoreOutlined, UserOutlined, MailOutlined } from "@ant-design/icons";

const { Text } = Typography;

function Header() {

  const themeColor = "#001529"; 

  return (
    <div
      style={{
        height: "64px", 
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        padding: "0 24px",
        background: themeColor, 
        boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
        color: "#fff"
      }}
    >
      <Space size="small">
        <MailOutlined style={{ fontSize: '20px', color: '#1890ff' }} />
        <Text style={{ color: "#fff", fontWeight: 700, fontSize: '18px', letterSpacing: '0.5px' }}>
          EMAIL NOTIFICATION SYSTEM
        </Text>
      </Space>

      <Space size="large">
        <AppstoreOutlined style={{ fontSize: '18px', cursor: 'pointer', color: 'rgba(255,255,255,0.85)' }} />
        <div style={{ borderLeft: '1px solid rgba(255,255,255,0.2)', height: '20px' }} />
        <Space size="small" style={{ cursor: 'pointer' }}>
          <UserOutlined style={{ fontSize: '18px', color: 'rgba(255,255,255,0.85)' }} />
          <Text style={{ color: "#fff", fontSize: '14px' }}>Admin</Text>
        </Space>
      </Space>
    </div>
  );
}

export default Header;