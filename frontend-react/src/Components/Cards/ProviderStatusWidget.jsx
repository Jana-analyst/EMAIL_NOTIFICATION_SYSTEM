import { Card } from "antd";
import {
  BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer,
} from "recharts";
import { useEffect, useState } from "react";
import { listEmails } from "../../api/emailApi";

function ProviderStatusWidget() {
  const [data, setData] = useState([]);

  useEffect(() => {
    load();
  }, []);

  async function load() {
    try {
      const emails = await listEmails();

      const grouped = {};

      emails.forEach((mail) => {
        const key = mail.providerStatus || "UNKNOWN";
        grouped[key] = (grouped[key] || 0) + 1;
      });

      const chartData = Object.keys(grouped).map((status) => ({
        provider: status,
        count: grouped[status],
      }));

      setData(chartData);

    } catch (err) {
      console.error("Provider widget failed", err);
    }
  }

  return (
    <Card title="Provider Status">
      <div style={{ width: "100%", height: 220 }}>
        <ResponsiveContainer>
          <BarChart data={data}>
            <XAxis dataKey="provider" />
            <YAxis />
            <Tooltip />
            <Bar dataKey="count" />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </Card>
  );
}

export default ProviderStatusWidget;