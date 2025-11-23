import { test, expect, type Page } from "@playwright/test";
import dotenv from "dotenv";
dotenv.config();

const FRONTEND_URL = process.env.FRONTEND_URL!;
const E2E_CLERK_USER_USERNAME = process.env.E2E_CLERK_USER_USERNAME!;
const E2E_CLERK_USER_PASSWORD = process.env.E2E_CLERK_USER_PASSWORD!;
const amountOfCampaignsToCreate = 1;
test.describe.configure({ mode: "serial" });

async function ensureAuthenticated(page: Page) {
  console.log("     🔐 Ensuring Clerk session");
  const signOutButton = page.getByTestId("sign-out-button");

  try {
    await signOutButton.waitFor({ state: "visible", timeout: 10000 });
    console.log("     ✅ Existing Clerk session detected");
    return;
  } catch {
    console.log(
      "          🔑 No active Clerk session detected, performing login"
    );
  }

  await page.getByTestId("sign-in-button").click();
  await page
    .getByPlaceholder("Enter your email address")
    .fill(E2E_CLERK_USER_USERNAME);
  await page.getByRole("button", { name: "Continue" }).click();
  await page
    .getByPlaceholder("Enter your password")
    .fill(E2E_CLERK_USER_PASSWORD);
  await page.getByRole("button", { name: "Continue" }).click();

  await signOutButton.waitFor({ state: "visible", timeout: 30000 });
  console.log("     ✅ Clerk authentication ready (fallback login)");
}

test.describe("Campaign Creation Time Check", () => {
  test.skip(
    process.env.RUN_CAMPAIGN_CREATION_TIME_TEST !== "true",
    "Skipping Expensive Creation Time Check Test - set RUN_CAMPAIGN_CREATION_TIME_TEST=true to run"
  );
  test("Testing Campaign Creation Times", async ({ page }) => {
    test.setTimeout(900000000); // 15 hours timeout for 50 iterations

    for (
      let iteration = 1;
      iteration <= amountOfCampaignsToCreate;
      iteration++
    ) {
      const startTimestamp = Date.now();

      await page.goto(FRONTEND_URL);
      await page.waitForLoadState("networkidle");
      await ensureAuthenticated(page);
      await expect(page.getByTestId("sign-out-button")).toBeVisible();
      await page.getByText(/Start your story now/i).click();
      await page.waitForURL(
        new RegExp(
          `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign`
        ),
        {
          timeout: 30000,
        }
      );
      await page.waitForLoadState("networkidle");
      await expect(page.getByText(/your campaigns/i)).toBeVisible({
        timeout: 30000,
      });
      await page.getByTestId(/new-campaign-button/i).click();
      await expect(page.getByText(/Launch a New Campaign/i)).toBeVisible();
      await page.getByTestId(/campaign-name-input/i).fill("Tales of Pirates");
      await page.getByTestId("wanted-themes-input").fill("Pirates, Monsters");
      await page.getByTestId("unwanted-themes-input").fill("Sci-Fi");
      await page.getByTestId(/create-campaign-button-trigger/i).click();
      await page.waitForURL(
        new RegExp(
          `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign/\\d+`
        ),
        {
          timeout: 1200000,
        }
      );
      await page.waitForLoadState("networkidle");
      await expect(page.getByTestId("navbar-item-boosters")).toBeVisible({
        timeout: 1200000,
      });

      // Simulate average time for campaign image generation (20 seconds)
      // If we're generating real images, then comment out the wait
      console.log("     🖼️  Simulating image generation time (20s)...");
      await page.waitForTimeout(20000);

      const stopTimestamp = Date.now();
      const creationTime = stopTimestamp - startTimestamp;
      const creationTimeMinutes = (creationTime / 60000).toFixed(2);
      console.log(
        `     ⏱️  Campaign creation time: ${creationTimeMinutes} min`
      );
    }
  });
});
