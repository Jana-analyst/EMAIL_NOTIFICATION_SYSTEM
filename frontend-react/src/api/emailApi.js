const BASE_URL = "http://localhost:8080/api/emails";

export async function listEmails() {
  const res = await fetch(BASE_URL);

  if (!res.ok) {
    throw new Error("Failed to fetch emails");
  }

  const data = await res.json();

  return data.map((mail) => ({
    id: mail.emailId,
    recipient: mail.recipient,
    subject: mail.subject,
    systemStatus: mail.systemStatus,
    providerStatus: mail.providerStatus,
    createdAt: mail.createdAt,
  }));
}