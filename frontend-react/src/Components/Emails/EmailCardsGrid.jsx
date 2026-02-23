import { Card, Tag } from "antd";

function EmailCardsGrid({ emails, onSelect }) {
  return (
    <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
      {emails.map((mail) => (
        <Card
          key={mail.id || mail.emailId} // Better key detection
          hoverable
          onClick={() => onSelect(mail)}
          style={{ cursor: "pointer", borderRadius: 8 }}
          bodyStyle={{ padding: "14px 18px" }}
        >
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <div>
              <div style={{ fontSize: 15, fontWeight: 600, marginBottom: 4 }}>
                {mail.subject}
              </div>
              <div style={{ fontSize: 13, color: "#666" }}>
                {mail.recipient}
              </div>
            </div>
            <div style={{ display: "flex", gap: 6 }}>
              <Tag color="blue">{mail.systemStatus}</Tag>
              {/* Changed color to green if delivered for visual feedback */}
              <Tag color={mail.providerStatus === 'DELIVERED' ? 'green' : 'default'}>
                {mail.providerStatus}
              </Tag>
            </div>
          </div>
          <div style={{ fontSize: 12, color: "#999", marginTop: 8 }}>
            {mail.createdAt}
          </div>
        </Card>
      ))}
    </div>
  );
}

export default EmailCardsGrid;