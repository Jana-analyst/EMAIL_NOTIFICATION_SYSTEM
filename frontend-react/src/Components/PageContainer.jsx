import { Routes, Route, Navigate } from "react-router-dom";
import DashboardPage from "../Pages/DashboardPage";
import TemplatesPage from "../Pages/TemplatesPage";
import EmailPage from "../Pages/EmailPage";
import ReportsPage from "../Pages/ReportsPage";
import TemplateEditorPage from "../Pages/TemplateEditorPage";

function PageContainer() {
  return (
    <Routes>
      <Route path="/dashboard" element={<DashboardPage />} />
      <Route path="/templates" element={<TemplatesPage />} />
      <Route path="/emails" element={<EmailPage />} />
      <Route path="/reports" element={<ReportsPage />} />

      <Route path="*" element={<Navigate to="/dashboard" replace />} />
      <Route path="/templates/editor" element={<TemplateEditorPage />} />
    </Routes>
  );
}

export default PageContainer;