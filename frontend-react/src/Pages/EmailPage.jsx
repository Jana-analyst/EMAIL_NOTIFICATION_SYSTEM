import { Button, Space, Select } from "antd";
import { useState, useMemo, useEffect } from "react";
import TemplateSelector from "../Components/Templates/TemplateSelector";
import EmailComposerModal from "../Components/Emails/ComposeEmailModal";
import EmailCardsGrid from "../Components/Emails/EmailCardsGrid";
import EmailDetailsPanel from "../Components/Emails/EmailDetailsPanel";
import { listEmails } from "../api/emailApi";

const SYSTEM_STATUS_OPTIONS = ["ALL", "QUEUED", "SENT_TO_PROVIDER"];
const PROVIDER_STATUS_OPTIONS = ["ALL", "PENDING", "DELIVERED", "FAILED"];

function EmailPage() {
    const [selectorOpen, setSelectorOpen] = useState(false);
    const [modalOpen, setModalOpen] = useState(false);
    const [selectedTemplate, setSelectedTemplate] = useState(null);
    const [testMode, setTestMode] = useState(false);
    const [emails, setEmails] = useState([]);
    const [activeEmail, setActiveEmail] = useState(null);

    const [systemFilter, setSystemFilter] = useState("ALL");
    const [providerFilter, setProviderFilter] = useState("ALL");

    const openSelector = (isTest) => {
        setTestMode(isTest);
        setSelectorOpen(true);
    };

    const handleTemplateSelect = (tpl) => {
        setSelectedTemplate(tpl);
        setSelectorOpen(false);
        setModalOpen(true);
    };

    // Updated with spread operator for better change detection
    async function loadEmails() {
        try {
            const data = await listEmails();
            setEmails([...data]); 
            console.log("Polling: Data refreshed from DB");
        } catch (err) {
            console.error("Email fetch failed", err);
        }
    }

    // Integrated Polling Logic
    useEffect(() => {
        loadEmails(); // Initial load

        const interval = setInterval(() => {
            loadEmails(); // Periodic check
        }, 5000); // 5 seconds

        return () => clearInterval(interval); // Cleanup
    }, []);

    const filteredEmails = useMemo(() => {
        return emails.filter((mail) => {
            const systemMatch =
                systemFilter === "ALL" || mail.systemStatus === systemFilter;

            const providerMatch =
                providerFilter === "ALL" || mail.providerStatus === providerFilter;

            return systemMatch && providerMatch;
        });
    }, [emails, systemFilter, providerFilter]); // React watches 'emails' here

    async function handleSendEmail(payload) {
        try {
            const res = await fetch("http://localhost:8080/api/emails", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    recipient: payload.recipient,
                    templateId: selectedTemplate.templateId,
                    variables: payload.variables,
                }),
            });

            if (!res.ok) throw new Error("Send failed");

            loadEmails();
            setModalOpen(false);
        } catch (err) {
            console.error("Email send failed", err);
        }
    }

    const handleEmailSelect = async (email) => {
        try {
            const res = await fetch(`http://localhost:8080/api/emails/${email.id || email.emailId}`);
            const details = await res.json();
            setActiveEmail(details);
        } catch (err) {
            console.error("Failed to load email details", err);
        }
    };

    return (
        <>
            <Space style={{ marginBottom: 16 }}>
                <Button type="primary" onClick={() => openSelector(false)}>
                    Compose Email
                </Button>
                <Button onClick={() => openSelector(true)}>Test</Button>
                <Select
                    value={systemFilter}
                    style={{ width: 160 }}
                    onChange={setSystemFilter}
                    options={SYSTEM_STATUS_OPTIONS.map((s) => ({
                        label: `System: ${s}`,
                        value: s,
                    }))}
                />
                <Select
                    value={providerFilter}
                    style={{ width: 180 }}
                    onChange={setProviderFilter}
                    options={PROVIDER_STATUS_OPTIONS.map((s) => ({
                        label: `Provider: ${s}`,
                        value: s,
                    }))}
                />
            </Space>

            <TemplateSelector
                open={selectorOpen}
                onClose={() => setSelectorOpen(false)}
                onSelect={handleTemplateSelect}
            />

            <EmailComposerModal
                open={modalOpen}
                onClose={() => setModalOpen(false)}
                template={selectedTemplate}
                isTest={testMode}
                onSend={handleSendEmail}
            />
            {activeEmail ? (
                <EmailDetailsPanel
                    email={activeEmail}
                    onClose={() => setActiveEmail(null)}
                />
            ) : (
                <EmailCardsGrid
                    emails={filteredEmails}
                    onSelect={handleEmailSelect}
                />
            )}
        </>
    );
}

export default EmailPage;