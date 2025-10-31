import { test, expect } from "@playwright/test";
import dotenv from "dotenv";
dotenv.config();

const FRONTEND_URL = process.env.FRONTEND_URL!;
test("navigating to campaigns shows the seeded campaign", async ({ page }) => {
  await page.goto(FRONTEND_URL);
  await page.getByText(/campaigns/i).click();
  const seededCampaign = page.getByTestId(/test-Medieval Adventure/i);
  await expect(seededCampaign).toBeVisible({ timeout: 15000 });
});
