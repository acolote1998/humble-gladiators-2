import { test, expect } from "@playwright/test";
import dotenv from "dotenv";
dotenv.config();

const FRONTEND_URL = process.env.FRONTEND_URL!;
test("navigating to campaigns shows the test campaign", async ({ page }) => {
  await page.goto(FRONTEND_URL);
  await page.getByText(/campaigns/i).click();
  const testCampaign = page.getByTestId(/test-Medieval Adventure/i);
  await expect(testCampaign).toBeVisible();
});

test.only("creating hero in test campaign", async ({ page }) => {
  await page.goto(FRONTEND_URL);
  await page.getByText(/campaigns/i).click();
  const testCampaign = page.getByTestId(/test-Medieval Adventure/i);
  await expect(testCampaign).toBeVisible();
  await testCampaign.click();
  await page.getByText(/forge your hero/i).click();
  await page.getByTestId("hero-name-input").fill("Aki Test!");
  await page.getByText(/forge your hero/i).click();
  await expect(page.getByText(/campaign stats/i)).toBeVisible();
  await expect(page.getByText(/armors/i)).toBeVisible();
  await expect(page.getByText(/boots/i)).toBeVisible();
  await expect(page.getByText(/consumables/i)).toBeVisible();
  await expect(page.getByText(/helmets/i)).toBeVisible();
  await expect(page.getByText(/shields/i)).toBeVisible();
  await expect(page.getByText(/weapons/i)).toBeVisible();
  await expect(page.getByText(/spells/i)).toBeVisible();
  await expect(page.getByText(/characters/i)).toBeVisible();
  await expect(page.getByText(/win rate/i)).toBeVisible();
  await expect(page.getByText(/forge your hero/i)).toBeHidden();
});
