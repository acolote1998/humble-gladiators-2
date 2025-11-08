import { test, expect } from "@playwright/test";
import dotenv from "dotenv";
dotenv.config();

const FRONTEND_URL = process.env.FRONTEND_URL!;

test("has correct title", async ({ page }) => {
  console.log("🚀 ////START////");
  console.log("     Starting test: has correct title");
  console.log("     Navigating to frontend");
  await page.goto(FRONTEND_URL);
  console.log("     Checking page title");
  await expect(page).toHaveTitle(/Humble Gladiators 2/);
  console.log("✅ Title verification complete - test complete");
});
