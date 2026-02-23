import { useState, useEffect } from "react";
import PageTitle from "../Components/PageInformation/PageTitle";
import TemplateActions from "../Components/Templates/TemplateActions";
import TemplateList from "../Components/Templates/TemplateList";
import TemplateEditorPage from "./TemplateEditorPage";

import {
    listTemplates,
    getTemplateDetails,
    createTemplate,
    updateTemplate,
} from "../api/templateApi";

function TemplatesPage() {
    const [templates, setTemplates] = useState([]);
    const [editingTemplate, setEditingTemplate] = useState(null);

    useEffect(() => {
        loadTemplates();
    }, []);

    async function loadTemplates() {
        try {
            const data = await listTemplates();
            setTemplates(data);
        } catch (err) {
            console.error("Template list failed", err);
        }
    }

    const handleNewTemplate = () => {
        setEditingTemplate({
            id: null,
            name: "",
            subject: "",
            body: "",
        });
    };

    const handleEditTemplate = async (templateListItem) => {
        try {
            const fullTemplate = await getTemplateDetails(templateListItem.id);
            setEditingTemplate(fullTemplate);
        } catch (err) {
            console.error("Template details failed", err);
        }
    };

    const handleSaveTemplate = async (template) => {
        try {
            if (template.id === null) {
                await createTemplate({
                    name: template.name,
                    subject: template.subject,
                    body: template.body,
                });
            } else {
                await updateTemplate(template.id, {
                    subject: template.subject,
                    body: template.body,
                });
            }

            setEditingTemplate(null);
            loadTemplates();
        } catch (err) {
            console.error("Template save failed", err);
        }
    };

    if (editingTemplate) {
        return (
            <TemplateEditorPage
                template={editingTemplate}
                onSave={handleSaveTemplate}
                onCancel={() => setEditingTemplate(null)}
            />
        );
    }
    console.log("TEMPLATES →", templates);
    return (
        <>
            <PageTitle title="Email Templates" />

            <TemplateActions onNewTemplate={handleNewTemplate} />

            <TemplateList
                templates={templates}
                onEdit={handleEditTemplate}
            />
        </>
    );
}

export default TemplatesPage;