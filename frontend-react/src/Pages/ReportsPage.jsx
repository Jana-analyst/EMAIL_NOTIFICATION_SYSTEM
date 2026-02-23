import { Card, Row, Col } from "antd";
import PageTitle from "../Components/PageInformation/PageTitle";

function ReportsPage() {
  return (
    <>
      <PageTitle title="Report Analysis" />

      <Row gutter={[16, 16]}>
        <Col span={12}>
          <Card title="Graph Placeholder 1">Coming Later</Card>
        </Col>

        <Col span={12}>
          <Card title="Graph Placeholder 2">Coming Later</Card>
        </Col>

        <Col span={12}>
          <Card title="Graph Placeholder 3">Coming Later</Card>
        </Col>

        <Col span={12}>
          <Card title="Graph Placeholder 4">Coming Later</Card>
        </Col>

        <Col span={12}>
          <Card title="Graph Placeholder 5">Coming Later</Card>
        </Col>

        <Col span={12}>
          <Card title="Graph Placeholder 6">Coming Later</Card>
        </Col>
      </Row>
    </>
  );
}

export default ReportsPage;