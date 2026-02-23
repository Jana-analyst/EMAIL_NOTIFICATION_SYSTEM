import { test, expect } from '@playwright/test';

test('template + email complete flow', async ({ page }) => {

  // ============================
  // Open Templates Page
  // ============================

  await page.goto('http://localhost:5177/templates');
  await page.waitForTimeout(1000);

  // ============================
  // Template Creation Locators
  // ============================

  const newTemplateButton = page.getByRole('button', { name: 'New Template' });

  const templateTitleInput = page
      .getByText('Template Title')
      .locator('xpath=following::input[1]');

  const subjectInput = page
      .getByText('Subject')
      .locator('xpath=following::input[1]');

  const bodyEditor = page.locator('.ql-editor');

  const saveButton   = page.getByRole('button', { name: 'Save' });
  const cancelButton = page.getByRole('button', { name: 'Cancel' });

  // ============================
  // Create New Template
  // ============================

  await newTemplateButton.click();
  await page.waitForTimeout(1000);

  await templateTitleInput.fill('TEMPLATE TEST');
  await page.waitForTimeout(1000);

  await subjectInput.fill('NAME {{name}}');
  await page.waitForTimeout(1000);

  await bodyEditor.fill('Helloo {{name}}');
  await page.waitForTimeout(1000);

  await saveButton.click();
  await page.waitForTimeout(1500);

  // ✅ CRITICAL FIX — RETURN TO LIST
  await cancelButton.click();
  await page.waitForTimeout(1000);

  // ============================
  // Edit Existing Template
  // ============================

  const existingTemplate = page.getByText('Welcome TemplateSubject:');

  const editTitleInput = page
      .getByText('Template Title')
      .locator('xpath=following::input[1]');

  const editSubjectInput = page
      .getByText('Subject')
      .locator('xpath=following::input[1]');

  await existingTemplate.click();
  await page.waitForTimeout(1000);

  await editTitleInput.fill('ALLA JANARDHAN');
  await page.waitForTimeout(1000);

  await editSubjectInput.fill('WELCOME {{NAME}}');
  await page.waitForTimeout(1000);

  await bodyEditor.fill('HELLO {{NAME}}');
  await page.waitForTimeout(1000);

  await saveButton.click();
  await page.waitForTimeout(1500);

  // ============================
  // Navigate to Emails
  // ============================

  const emailsLink = page.getByRole('link', { name: 'Emails' });
  await emailsLink.click();
  await page.waitForTimeout(1000);

  // ============================
  // Email Flow Locators
  // ============================

  const composeButton  = page.getByRole('button', { name: 'Compose Email' });
  const templateSelect = page.getByText('ALLA JANARDHANWELCOME {{NAME}}');

  const recipientInput = page.locator('#recipient');
  const nameInput      = page.locator('#NAME');

  const sendButton = page.getByRole('button', { name: 'Send Email' });

  // ============================
  // Compose Email
  // ============================

  await composeButton.click();
  await page.waitForTimeout(1000);

  await templateSelect.click();
  await page.waitForTimeout(1000);

  await recipientInput.fill('janardhaniit474@gmail.com');
  await page.waitForTimeout(1000);

  await nameInput.fill('Alla Janardhan');
  await page.waitForTimeout(1000);

  await sendButton.click();
  await page.waitForTimeout(1500);

  // ============================
  // Strict-mode Safe Validation
  // ============================

  await expect(page.getByText('QUEUED').first()).toBeVisible();

});