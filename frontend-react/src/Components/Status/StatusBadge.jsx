import { Tag } from "antd";

const STATUS_COLORS = {
  QUEUED: "default",
  SENT_TO_PROVIDER: "processing",

  PENDING: "warning",
  DELIVERED: "success",
  FAILED: "error",
};

function StatusBadge({ status }) {
  if (!status) return null;

  return <Tag color={STATUS_COLORS[status] || "default"}>{status}</Tag>;
}

export default StatusBadge;