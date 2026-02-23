import { Row, Col } from "antd";
import { useEffect } from "react";
import { useLocation } from "react-router-dom";

import VolumeTrendWidget from "../Components/Cards/VolumeTrendWidget";
import ProviderStatusWidget from "../Components/Cards/ProviderStatusWidget";
import RecentEmailsSection from "../Components/Containers/RecentEmailsSection";

function DashboardPage() {
  const location = useLocation();

  useEffect(() => {
    // Runs ONLY when route becomes /dashboard or /
    console.log("Dashboard activated → reload widgets");
  }, [location.pathname]);

  return (
    <div>
      <div style={{ fontSize: 20, fontWeight: 600, marginBottom: 16 }}>
        Dashboard
      </div>

      <Row gutter={16}>
        <Col span={12}>
          <VolumeTrendWidget />
        </Col>

        <Col span={12}>
          <ProviderStatusWidget />
        </Col>
      </Row>

      <div style={{ marginTop: 24 }}>
        <RecentEmailsSection key={location.pathname} />
      </div>
    </div>
  );
}

export default DashboardPage;