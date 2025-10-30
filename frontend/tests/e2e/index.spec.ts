import { setupClerkTestingToken } from "@clerk/testing/playwright";

import { clerk } from "@clerk/testing/playwright";
import { test, expect } from "@playwright/test";
import dotenv from "dotenv";
dotenv.config();

const FRONTEND_URL = process.env.FRONTEND_URL!;

const E2E_CLERK_USER_USERNAME = process.env.E2E_CLERK_USER_USERNAME!;
const E2E_CLERK_USER_PASSWORD = process.env.E2E_CLERK_USER_PASSWORD!;

test("has correct title", async ({ page }) => {
  await page.goto(FRONTEND_URL);

  await expect(page).toHaveTitle(/Humble Gladiators 2/);
});

test.beforeEach("sign up with Clerk Testing Token", async ({ page }) => {
  await setupClerkTestingToken({ page });
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
  await expect(page.getByText(/sign out/i)).toBeVisible();
  await expect(page.getByText(/sign in/i)).toBeHidden();
});

test.afterEach("log out from clerk after each test", async ({ page }) => {
  await page.getByText(/sign out/i).click();
});
