import { Space } from "antd";
import RecentEmailCard from "../Cards/RecentEmailCard";

const mockEmails = [
  {
    recipient: "user1@email.com",
    subject: "Welcome Email",
    systemStatus: "QUEUED",
    providerStatus: "UNKNOWN",
  },
  {
    recipient: "user2@email.com",
    subject: "Verification Email",
    systemStatus: "SENT_TO_PROVIDER",
    providerStatus: "DELIVERED",
  },
  {
    recipient: "user3@email.com",
    subject: "Reset Password",
    systemStatus: "SENT_TO_PROVIDER",
    providerStatus: "OPENED",
  },
];

function RecentEmailsSection() {
  return (
    <div>
      <div style={{ fontSize: 16, fontWeight: 600, marginBottom: 12 }}>
        Recent Emails
      </div>

      <Space direction="vertical" size="middle" style={{ width: "100%" }}>
        {mockEmails.map((email, index) => (
          <RecentEmailCard key={index} {...email} />
        ))}
      </Space>
    </div>
  );
}

export default RecentEmailsSection;