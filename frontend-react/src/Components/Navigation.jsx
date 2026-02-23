import { Menu } from "antd";
import { Link } from "react-router-dom";
import {
  DashboardOutlined,
  MailOutlined,
  FileTextOutlined,
  BarChartOutlined,
} from "@ant-design/icons";

function Navigation() {
  return (
    <Menu mode="inline" defaultSelectedKeys={["dashboard"]} style={{ borderRight: 0 }}>
      <Menu.Item key="dashboard" icon={<DashboardOutlined />}>
        <Link to="/dashboard">Dashboard</Link>
      </Menu.Item>

      <Menu.Item key="templates" icon={<FileTextOutlined />}>
        <Link to="/templates">Templates</Link>
      </Menu.Item>

      <Menu.Item key="emails" icon={<MailOutlined />}>
        <Link to="/emails">Emails</Link>
      </Menu.Item>

      <Menu.Item key="reports" icon={<BarChartOutlined />}>
        <Link to="/reports">Reports</Link>
      </Menu.Item>
    </Menu>
  );
}

export default Navigation;