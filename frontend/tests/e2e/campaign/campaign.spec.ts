import { test, expect } from "@playwright/test";
import dotenv from "dotenv";
dotenv.config();

const FRONTEND_URL = process.env.FRONTEND_URL!;
test.describe.configure({ mode: "serial" });

test.describe("Campaign Flow", () => {
  test.beforeAll("creating hero in test campaign", async ({ browser }) => {
    console.log("CALLED!!!!");
    const context = await browser.newContext();
    const page = await context.newPage();
    await page.goto(FRONTEND_URL);
    await page.getByText(/campaigns/i).click();
    const testCampaign = page.getByTestId(/test-Medieval Adventure/i);
    await expect(testCampaign).toBeVisible();
    await testCampaign.click();
    await page.getByTestId("navigate-to-create-hero").click();
    await page.getByTestId("hero-name-input").fill("Aki Test!");
    await page.getByTestId("hero-creation-button").click();
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

  test("navigating to campaigns shows the test campaign", async ({ page }) => {
    await page.goto(FRONTEND_URL);
    await page.getByText(/campaigns/i).click();
    const testCampaign = page.getByTestId(/test-Medieval Adventure/i);
    await expect(testCampaign).toBeVisible();
  });

  test("navigating to compendium and verifying that all cards are created", async ({
    page,
  }) => {
    await page.goto(FRONTEND_URL);
    await page.getByText(/campaigns/i).click();
    const testCampaign = page.getByTestId(/test-Medieval Adventure/i);
    await expect(testCampaign).toBeVisible();
    await testCampaign.click();
    await page.getByTestId("navbar-compendium").click();
    await page.getByText(/npcs/i).click();
    await page.getByText(/armors/i).click();
    await page.getByText(/boots/i).click();
    await page.getByText(/consumables/i).click();
    await page.getByText(/helmets/i).click();
    await page.getByText(/shields/i).click();
    await page.getByText(/spells/i).click();
    await page.getByText(/weapons/i).click();
    await expect(page.getByTestId("character-card")).toHaveCount(2);
    await expect(page.getByTestId("armor-card")).toHaveCount(2);
    await expect(page.getByTestId("boots-card")).toHaveCount(2);
    await expect(page.getByTestId("consumable-card")).toHaveCount(2);
    await expect(page.getByTestId("helmet-card")).toHaveCount(2);
    await expect(page.getByTestId("shield-card")).toHaveCount(2);
    await expect(page.getByTestId("spell-card")).toHaveCount(2);
    await expect(page.getByTestId("weapon-card")).toHaveCount(2);
  });
});
