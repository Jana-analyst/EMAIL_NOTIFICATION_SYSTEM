import { Layout } from "antd";
import Sidebar from "../Components/SideBar";
import Header from "./Header";
import PageContainer from "./PageContainer";

const { Sider, Header: AntHeader, Content } = Layout;

function MainLayout() {
  return (
    <Layout style={{ minHeight: "100vh", background: "#f5f7fa" }}>
      <Sider
        width={250}
        style={{
          background: "#fff",
          borderRight: "1px solid #f0f0f0",
        }}
      >
        <Sidebar />
      </Sider>

      <Layout>
        <AntHeader
          style={{
            background: "#fff",
            borderBottom: "1px solid #f0f0f0",
            padding: 0,
          }}
        >
          <Header />
        </AntHeader>

        <Content style={{ padding: "20px" }}>
          <PageContainer />
        </Content>
      </Layout>
    </Layout>
  );
}

export default MainLayout;