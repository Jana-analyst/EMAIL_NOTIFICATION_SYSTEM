import { Card, Tag, Button } from "antd";

function EmailDetailsPanel({ email, onClose }) {
    console.log("DETAILS PANEL DATA →", email);
  if (!email) return null;

  return (
    <Card
      style={{
        borderRadius: 12,
        boxShadow: "0 2px 6px rgba(0,0,0,0.05)",
      }}
      bodyStyle={{
        padding: "22px 26px",
      }}
    >
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <div style={{ fontSize: 18, fontWeight: 600 }}>
          Email Details
        </div>

        <Button onClick={onClose}>Close</Button>
      </div>

      <div style={{ marginTop: 18 }}>
        <div style={{ marginBottom: 12 }}>
          <div style={{ fontSize: 12, color: "#888" }}>Recipient</div>
          <div style={{ fontSize: 14, fontWeight: 500 }}>
            {email.recipient}
          </div>
        </div>

        <div style={{ marginBottom: 12 }}>
          <div style={{ fontSize: 12, color: "#888" }}>Subject</div>
          <div style={{ fontSize: 14, fontWeight: 500 }}>
            {email.subject}
          </div>
        </div>

        <div style={{ marginBottom: 12 }}>
          <div style={{ fontSize: 12, color: "#888" }}>System Status</div>
          <Tag color="blue">{email.systemStatus}</Tag>
        </div>

        <div style={{ marginBottom: 12 }}>
          <div style={{ fontSize: 12, color: "#888" }}>Provider Status</div>
          <Tag>{email.providerStatus}</Tag>
        </div>

        <div style={{ marginBottom: 12 }}>
          <div style={{ fontSize: 12, color: "#888" }}>Created At</div>
          <div style={{ fontSize: 13 }}>{email.createdAt}</div>
        </div>
      </div>

      <div style={{ marginTop: 14 }}>
        <div style={{ fontSize: 12, color: "#888", marginBottom: 6 }}>
          Body
        </div>

        <div
          style={{
            background: "#f7f8fa",
            borderRadius: 8,
            padding: 14,
            fontSize: 13,
            minHeight: 120,
          }}
        >
          {email.body ? (
            <div dangerouslySetInnerHTML={{ __html: email.body }} />
          ) : (
            "No content"
          )}
        </div>
      </div>
    </Card>
  );
}

export default EmailDetailsPanel;