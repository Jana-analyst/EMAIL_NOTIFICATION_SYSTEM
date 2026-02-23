import { Card } from "antd";
import {
  LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer,
} from "recharts";
import { useEffect, useState } from "react";
import { listEmails } from "../../api/emailApi";

function VolumeTrendWidget() {
  const [data, setData] = useState([]);

  useEffect(() => {
    load();
  }, []);

  async function load() {
    try {
      const emails = await listEmails();

      const grouped = {};

      emails.forEach((mail) => {
        const dateKey = new Date(mail.createdAt).toLocaleDateString();

        grouped[dateKey] = (grouped[dateKey] || 0) + 1;
      });

      const chartData = Object.keys(grouped).map((day) => ({
        day,
        count: grouped[day],
      }));

      setData(chartData);

    } catch (err) {
      console.error("Volume widget failed", err);
    }
  }

  return (
    <Card title="Email Volume">
      <div style={{ width: "100%", height: 220 }}>
        <ResponsiveContainer>
          <LineChart data={data}>
            <XAxis dataKey="day" />
            <YAxis />
            <Tooltip />
            <Line type="monotone" dataKey="count" strokeWidth={2} />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </Card>
  );
}

export default VolumeTrendWidget;