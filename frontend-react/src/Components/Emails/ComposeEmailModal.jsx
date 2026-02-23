import { Modal, Form, Input, Button } from "antd";

function EmailComposerModal({ open, onClose, template, onSend }) {
  const [form] = Form.useForm();

  if (!template) return null;

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();

      const payload = {
        recipient: values.recipient,
        variables: {},
      };

      (template.placeholders || []).forEach((key) => {
        payload.variables[key] = values[key];
      });

      console.log("MODAL PAYLOAD →", payload);

      onSend(payload);

      form.resetFields();

    } catch (err) {
      console.error("Validation failed", err);
    }
  };

  return (
    <Modal
      title={template.name}
      open={open}
      onCancel={onClose}
      footer={null}
    >
      <Form layout="vertical" form={form}>
        <Form.Item
          label="Recipient"
          name="recipient"
          rules={[{ required: true }]}
        >
          <Input />
        </Form.Item>

        <Form.Item label="Subject">
          <Input value={template.subject} disabled />
        </Form.Item>

        <Form.Item label="Body">
          <Input.TextArea value={template.body} rows={6} disabled />
        </Form.Item>

        {(template.placeholders || []).map((ph) => (
          <Form.Item
            key={ph}
            label={ph.toUpperCase()}
            name={ph}
            rules={[{ required: true }]}
          >
            <Input />
          </Form.Item>
        ))}

        <Button type="primary" block onClick={handleSubmit}>
          Send Email
        </Button>
      </Form>
    </Modal>
  );
}

export default EmailComposerModal;