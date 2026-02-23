import { Card } from "antd";
import StatusBadge from "../Status/StatusBadge";

function RecentEmailCard({ recipient, subject, systemStatus, providerStatus }) {
  return (
    <Card size="small">
      <div style={{ fontWeight: 500, marginBottom: 4 }}>
        {subject}
      </div>

      <div style={{ fontSize: 12, color: "#888", marginBottom: 8 }}>
        {recipient}
      </div>

      <div style={{ display: "flex", gap: 8 }}>
        <StatusBadge status={systemStatus} />
        <StatusBadge status={providerStatus} />
      </div>
    </Card>
  );
}

export default RecentEmailCard;