import { useState, useEffect } from "react";
import { Row, Col, Card, Input, Button, Space } from "antd";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";

function TemplateEditorPage({ template, onSave, onCancel }) {
    const [name, setName] = useState("");
    const [subject, setSubject] = useState("");
    const [body, setBody] = useState("");

    useEffect(() => {
        setName(template.name || "");
        setSubject(template.subject || "");
        setBody(template.body || "");
    }, [template]);

    const handleSave = () => {
        onSave({
            ...template,
            name,
            subject,
            body,
        });
    };

    return (
        <>
            <div style={{ marginBottom: 16, display: "flex", justifyContent: "flex-end" }}>
                <Space>
                    <Button onClick={onCancel}>Cancel</Button>
                    <Button type="primary" onClick={handleSave}>
                        Save
                    </Button>
                </Space>
            </div>

            <Row gutter={16}>
                <Col span={12}>
                    <Card title="Template Details">
                        <div style={{ marginBottom: 12 }}>
                            <div>Template Title</div>
                            <Input value={name} onChange={(e) => setName(e.target.value)} />
                        </div>

                        <div style={{ marginBottom: 12 }}>
                            <div>Subject</div>
                            <Input value={subject} onChange={(e) => setSubject(e.target.value)} />
                        </div>

                        <ReactQuill value={body} onChange={setBody} />
                    </Card>
                </Col>

                <Col span={12}>
                    <Card title="Preview">
                        <div
                            style={{
                                border: "1px solid #e8e8e8",
                                borderRadius: 10,
                                background: "#fff",
                                padding: 20,
                            }}
                        >
                            {/* Subject */}
                            <div
                                style={{
                                    marginBottom: 16,
                                    paddingBottom: 10,
                                    borderBottom: "1px solid #f0f0f0",
                                }}
                            >
                                <div
                                    style={{
                                        fontSize: 12,
                                        color: "#888",
                                        marginBottom: 4,
                                    }}
                                >
                                    Subject
                                </div>

                                <div
                                    style={{
                                        fontSize: 16,
                                        fontWeight: 600,
                                    }}
                                >
                                    {subject || "Preview subject"}
                                </div>
                            </div>

                            {/* Body */}
                            <div>
                                <div
                                    style={{
                                        fontSize: 12,
                                        color: "#888",
                                        marginBottom: 6,
                                    }}
                                >
                                    Body
                                </div>

                                <div
                                    style={{
                                        background: "#fafafa",
                                        borderRadius: 8,
                                        padding: 14,
                                        minHeight: 180,
                                        lineHeight: 1.6,
                                        fontSize: 14,
                                    }}
                                    dangerouslySetInnerHTML={{
                                        __html:
                                            body ||
                                            "<span style='color:#999'>Preview body</span>",
                                    }}
                                />
                            </div>
                        </div>
                    </Card>
                </Col>
            </Row>
        </>
    );
}

export default TemplateEditorPage;