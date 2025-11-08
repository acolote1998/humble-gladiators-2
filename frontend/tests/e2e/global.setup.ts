import { clerkSetup, setupClerkTestingToken } from "@clerk/testing/playwright";
import { chromium } from "@playwright/test";
import dotenv from "dotenv";
import path from "path";
import { fileURLToPath } from "url";

dotenv.config();

const FRONTEND_URL = process.env.FRONTEND_URL!;
const BACKEND_URL = process.env.BACKEND_URL!;
const E2E_CLERK_USER_USERNAME = process.env.E2E_CLERK_USER_USERNAME!;
const E2E_CLERK_USER_PASSWORD = process.env.E2E_CLERK_USER_PASSWORD!;

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const storagePath = path.join(__dirname, "storageState.json");

export default async function globalSetup() {
  console.log("🚀 GLOBAL SETUP");
  // Health check: Verify backend is ready before starting tests
  console.log("     🩺 Performing health check on backend...");

  const healthCheckUrl = BACKEND_URL + "/public/health";

  const maxRetries = 5;
  const retryDelay = 5000; // 5 seconds

  let lastError: Error | null = null;

  for (let attempt = 1; attempt <= maxRetries; attempt++) {
    try {
      console.log(`🔁 Health check attempt ${attempt}/${maxRetries}...`);
      const response = await fetch(healthCheckUrl, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(`Health check failed with status ${response.status}`);
      }

      console.log("     ✅ Backend health check passed");
      lastError = null;
      break;
    } catch (error) {
      lastError = error instanceof Error ? error : new Error(String(error));
      console.error(
        `❌ Health check attempt ${attempt}/${maxRetries} failed:`,
        lastError.message
      );

      if (attempt < maxRetries) {
        console.log(`⏳ Retrying in ${retryDelay / 1000} seconds...`);
        await new Promise((resolve) => setTimeout(resolve, retryDelay));
      }
    }
  }

  if (lastError) {
    throw new Error(
      `Backend is not ready after ${maxRetries} attempts. Last error: ${lastError.message}`
    );
  }

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
  console.log("✅ Global Setup Finished");
}
