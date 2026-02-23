const BASE = "http://localhost:8080/api/templates";

/* Template List Response DTO → UI Model */
export async function listTemplates() {
  const res = await fetch("http://localhost:8080/api/templates");

  if (!res.ok) throw new Error("Failed to load templates");

  const data = await res.json();

  return data.map((tpl) => ({
    id: tpl.templateId,                 // CRITICAL FIX
    name: tpl.name,
    subject: tpl.subject,
    lastModified: tpl.updatedAt,         // CRITICAL FIX
  }));
}

/* Template Details Response DTO → UI Model */
export const getTemplateDetails = async (id) => {
  const res = await fetch(`${BASE}/${id}`);
  if (!res.ok) throw new Error("Failed to fetch template details");

  const dto = await res.json();

  return {
    id: dto.templateId,
    name: dto.name,
    subject: dto.subject,
    body: dto.body,
    createdAt: dto.createdAt,
    updatedAt: dto.updatedAt,
  };
};

/* Create Template DTO (Matches Backend Contract) */
export const createTemplate = async ({ name, subject, body }) => {
  const res = await fetch(BASE, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, subject, body }),
  });

  if (!res.ok) throw new Error("Failed to create template");

  const dto = await res.json();

  return {
    id: dto.templateId,
    name: dto.name,
    subject: dto.subject,
    body: dto.body,
    createdAt: dto.createdAt,
    updatedAt: dto.updatedAt,
  };
};

/* Update Template DTO (ONLY Subject + Body) */
export const updateTemplate = async (id, { subject, body }) => {
  const res = await fetch(`${BASE}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ subject, body }),
  });

  if (!res.ok) throw new Error("Failed to update template");

  const dto = await res.json();

  return {
    id: dto.templateId,
    name: dto.name,
    subject: dto.subject,
    body: dto.body,
    updatedAt: dto.updatedAt,
  };
};

/* Delete Template */
export const deleteTemplate = async (id) => {
  const res = await fetch(`${BASE}/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Failed to delete template");
};