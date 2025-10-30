import { clerkSetup, setupClerkTestingToken } from "@clerk/testing/playwright";
import { chromium } from "@playwright/test";
import dotenv from "dotenv";
import path from "path";
import { fileURLToPath } from "url";

dotenv.config();

const FRONTEND_URL = process.env.FRONTEND_URL!;
const E2E_CLERK_USER_USERNAME = process.env.E2E_CLERK_USER_USERNAME!;
const E2E_CLERK_USER_PASSWORD = process.env.E2E_CLERK_USER_PASSWORD!;

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const storagePath = path.join(__dirname, "storageState.json");

export default async function globalSetup() {
  // Initialize Clerk Testing Token
  await clerkSetup();

  // Launch a browser to authenticate once
  const browser = await chromium.launch();
  const page = await browser.newPage();

  // Inject testing token
  await setupClerkTestingToken({ page });

  // Go to frontend and log in
  await page.goto(FRONTEND_URL);
  await page.getByText(/sign in/i).click();
  await page
    .getByPlaceholder("Enter your email address")
    .fill(E2E_CLERK_USER_USERNAME);
  await page.getByRole("button", { name: "Continue" }).click();
  await page
    .getByPlaceholder("Enter your password")
    .fill(E2E_CLERK_USER_PASSWORD);
  await page.getByRole("button", { name: "Continue" }).click();

  // Save authenticated storage state
  await page.context().storageState({ path: storagePath });
  await browser.close();
}
