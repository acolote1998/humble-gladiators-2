import { test, expect } from "@playwright/test";
import dotenv from "dotenv";
dotenv.config();

const FRONTEND_URL = process.env.FRONTEND_URL!;

test("has correct title", async ({ page }) => {
  await page.goto(FRONTEND_URL);

  await expect(page).toHaveTitle(/Humble Gladiators 2/);
});
