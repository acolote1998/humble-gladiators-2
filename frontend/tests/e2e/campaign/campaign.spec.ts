import { test, expect, type Page } from "@playwright/test";
import dotenv from "dotenv";
dotenv.config();

const FRONTEND_URL = process.env.FRONTEND_URL!;
const E2E_CLERK_USER_USERNAME = process.env.E2E_CLERK_USER_USERNAME!;
const E2E_CLERK_USER_PASSWORD = process.env.E2E_CLERK_USER_PASSWORD!;
test.describe.configure({ mode: "serial" });

async function ensureAuthenticated(page: Page) {
  console.log("     🔐 Ensuring Clerk session");
  const signOutButton = page.getByRole("button", { name: /sign out/i });

  try {
    await signOutButton.waitFor({ state: "visible", timeout: 10000 });
    console.log("     ✅ Existing Clerk session detected");
    return;
  } catch {
    console.log(
      "          🔑 No active Clerk session detected, performing login"
    );
  }

  await page.getByText(/sign in/i).click();
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

test.describe("Campaign Flow", () => {
  test.beforeAll("creating hero in test campaign", async ({ browser }) => {
    console.log("🚀 ////START////");
    test.setTimeout(120000); // Increase timeout for beforeAll hook to 2 minutes
    const context = await browser.newContext();
    const page = await context.newPage();
    console.log("     Starting beforeAll: navigating to campaigns page");
    await page.goto(FRONTEND_URL);
    console.log("     Waiting for page to fully load");
    await page.waitForLoadState("networkidle");
    console.log("     Ensuring Clerk authentication");
    await ensureAuthenticated(page);
    await expect(page.getByRole("button", { name: /sign out/i })).toBeVisible();
    console.log("     Clerk authentication ready");
    console.log("     Navigating to campaigns page");
    await page.getByText(/campaigns/i).click();
    console.log("     Waiting for navigation to campaigns route");
    await page.waitForURL(
      new RegExp(
        `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign`
      ),
      {
        timeout: 30000,
      }
    );
    await page.waitForLoadState("networkidle");
    // Wait for the campaigns page to load by checking for "Your Campaigns" text
    // This ensures navigation completed and the component is rendering
    await expect(page.getByText(/your campaigns/i)).toBeVisible({
      timeout: 30000,
    });
    console.log("     Campaigns page loaded");
    // Wait for the campaign element to be visible (this ensures the API call has completed)
    await expect(page.getByTestId(/test-Medieval Adventure/i)).toBeVisible({
      timeout: 60000,
    });
    console.log("     Test campaign found, clicking on it");
    await page.getByTestId(/test-Medieval Adventure/i).click();

    // Wait for navigation to campaign page
    console.log("     Waiting for navigation to campaign page");
    await page.waitForURL(
      new RegExp(
        `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign/\\d+`
      ),
      {
        timeout: 30000,
      }
    );
    console.log("     Navigated to campaign page");

    // Wait for campaign data to load - check for campaign cover image or campaign name
    // The campaign page shows cover image when data is loaded
    console.log("     Waiting for campaign data to load");
    await page.waitForLoadState("networkidle");
    // Wait for either the campaign cover image or campaign name to appear
    await Promise.race([
      page
        .locator('img[src*="data:image/jpeg;base64"]')
        .waitFor({ state: "visible", timeout: 30000 })
        .catch(() => null),
      page
        .getByText(/Medieval Adventure/i)
        .waitFor({ state: "visible", timeout: 30000 })
        .catch(() => null),
    ]);
    console.log("     Campaign data loaded");

    // Wait for hero existence check to complete and navigate-to-create-hero button to be visible
    console.log("     Waiting for hero existence check and create hero button");
    await expect(page.getByTestId("navigate-to-create-hero")).toBeVisible({
      timeout: 30000,
    });
    console.log("     Create hero button is visible, clicking it");
    await page.getByTestId("navigate-to-create-hero").click();
    console.log("     On hero creation page, filling hero name");
    await page.getByTestId("hero-name-input").fill("Aki Test!");
    console.log("     Clicking hero creation button");
    await page.getByTestId("hero-creation-button").click();
    console.log(
      "     Waiting for hero creation to complete and campaign stats to appear"
    );
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
    console.log("✅ beforeAll: Hero created successfully, beforeAll complete");
    await context.close();
  });

  test("navigating to campaigns shows the test campaign", async ({ page }) => {
    console.log("🚀 ////START////");
    console.log(
      "Starting test: navigating to campaigns shows the test campaign"
    );
    await page.goto(FRONTEND_URL);
    console.log("     Waiting for page to fully load");
    await page.waitForLoadState("networkidle");
    console.log("     Ensuring Clerk authentication");
    await ensureAuthenticated(page);
    await expect(page.getByRole("button", { name: /sign out/i })).toBeVisible();
    console.log("     Clerk authentication ready");
    console.log("     Navigating to campaigns page");
    await page.getByText(/campaigns/i).click();
    console.log("     Waiting for navigation to campaigns route");
    await page.waitForURL(
      new RegExp(
        `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign`
      ),
      {
        timeout: 30000,
      }
    );
    await page.waitForLoadState("networkidle");
    console.log("     Waiting for campaigns page to load");
    await expect(page.getByText(/your campaigns/i)).toBeVisible({
      timeout: 30000,
    });
    console.log("     Campaigns page loaded");
    await expect(page.getByTestId(/test-Medieval Adventure/i)).toBeVisible({
      timeout: 60000,
    });
    console.log("✅ Test campaign found and visible - test complete");
  });

  test("navigating to campaign in the navbar shows the test campaign", async ({
    page,
  }) => {
    console.log("🚀 ////START////");
    console.log(
      "Starting test: navigating to campaign in the navbar shows the test campaign"
    );
    await page.goto(FRONTEND_URL);
    console.log("     Waiting for page to fully load");
    await page.waitForLoadState("networkidle");
    console.log("     Ensuring Clerk authentication");
    await ensureAuthenticated(page);
    await expect(page.getByRole("button", { name: /sign out/i })).toBeVisible();
    console.log("     Clerk authentication ready");
    console.log("     Navigating to campaigns page");
    await page.getByText(/campaigns/i).click();
    console.log("     Waiting for navigation to campaigns route");
    await page.waitForURL(
      new RegExp(
        `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign`
      ),
      {
        timeout: 30000,
      }
    );
    await page.waitForLoadState("networkidle");
    console.log("     Waiting for campaigns page to load");
    await expect(page.getByText(/your campaigns/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByTestId(/test-Medieval Adventure/i)).toBeVisible({
      timeout: 60000,
    });
    console.log("     Test campaign loaded");
    await page.getByTestId(/test-Medieval Adventure/i).click();
    console.log("     Accessed the campaign");

    await page.getByTestId("navbar-compendium").click();
    console.log(
      "     Navigating to compendium just to get away of campaign to be able to test the navbar"
    );

    await page.getByTestId("navbar-campaign").click();
    console.log("     Navigating to campaign page through navbar");
    await expect(page.getByText(/Campaign Stats/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByText(/Armors/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByText(/Boots/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByText(/Consumables/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByText(/Helmets/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByText(/Shields/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByText(/Weapons/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByText(/Spells/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByText(/Characters/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByText(/Win Rate/i)).toBeVisible({
      timeout: 30000,
    });
    console.log("     All stats rendered correctly");
    console.log("✅ Test campaign page correct - test complete");
  });

  test("navigating to compendium and verifying that all cards are created", async ({
    page,
  }) => {
    console.log("🚀 ////START////");
    console.log(
      "Starting test: navigating to compendium and verifying that all cards are created"
    );
    await page.goto(FRONTEND_URL);
    console.log("     Waiting for page to fully load");
    await page.waitForLoadState("networkidle");
    console.log("     Ensuring Clerk authentication");
    await ensureAuthenticated(page);
    await expect(page.getByRole("button", { name: /sign out/i })).toBeVisible();
    console.log("     Clerk authentication ready");
    console.log("     Navigating to campaigns page");
    await page.getByText(/campaigns/i).click();
    console.log("     Waiting for navigation to campaigns route");
    await page.waitForURL(
      new RegExp(
        `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign`
      ),
      {
        timeout: 30000,
      }
    );
    await page.waitForLoadState("networkidle");
    console.log("     Waiting for campaigns page to load");
    await expect(page.getByText(/your campaigns/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByTestId(/test-Medieval Adventure/i)).toBeVisible({
      timeout: 60000,
    });
    console.log("     Clicking on test campaign");
    await page.getByTestId(/test-Medieval Adventure/i).click();
    console.log("     Navigating to compendium");
    await page.getByTestId("navbar-compendium").click();
    console.log("     Checking NPCs category");
    await page.getByText(/npc's/i).click();
    await expect(page.getByTestId("character-card")).toHaveCount(2);
    console.log("     Found 2 character cards");
    console.log("     Checking armors category");
    await page.getByText(/armors/i).click();
    await expect(page.getByTestId("armor-card")).toHaveCount(2);
    console.log("     Found 2 armor cards");
    console.log("     Checking boots category");
    await page.getByText(/boots/i).click();
    await expect(page.getByTestId("boots-card")).toHaveCount(2);
    console.log("     Found 2 boots cards");
    console.log("     Checking consumables category");
    await page.getByText(/consumables/i).click();
    await expect(page.getByTestId("consumable-card")).toHaveCount(2);
    console.log("     Found 2 consumable cards");
    console.log("     Checking helmets category");
    await page.getByText(/helmets/i).click();
    await expect(page.getByTestId("helmet-card")).toHaveCount(2);
    console.log("     Found 2 helmet cards");
    console.log("     Checking shields category");
    await page.getByText(/shields/i).click();
    await expect(page.getByTestId("shield-card")).toHaveCount(2);
    console.log("     Found 2 shield cards");
    console.log("     Checking spells category");
    await page.getByText(/spells/i).click();
    await expect(page.getByTestId("spell-card")).toHaveCount(2);
    console.log("     Found 2 spell cards");
    console.log("     Checking weapons category");
    await page.getByText(/weapons/i).click();
    await expect(page.getByTestId("weapon-card")).toHaveCount(2);
    console.log("     Found 2 weapon cards");
    console.log("✅ All card categories verified - test complete");
  });

  test("navigates to the item booster route, opens an item booster and verifies that the correct amount of cards are in the inventory", async ({
    page,
  }) => {
    console.log("🚀 ////START////");
    console.log(
      "Starting test: navigates to the item booster route, opens an item booster and verifies that the correct amount of cards are in the inventory"
    );
    await page.goto(FRONTEND_URL);
    console.log("     Waiting for page to fully load");
    await page.waitForLoadState("networkidle");
    console.log("     Ensuring Clerk authentication");
    await ensureAuthenticated(page);
    await expect(page.getByRole("button", { name: /sign out/i })).toBeVisible();
    console.log("     Clerk authentication ready");
    console.log("     Navigating to campaigns page");
    await page.getByText(/campaigns/i).click();
    console.log("     Waiting for navigation to campaigns route");
    await page.waitForURL(
      new RegExp(
        `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign`
      ),
      {
        timeout: 30000,
      }
    );
    await page.waitForLoadState("networkidle");
    console.log("     Waiting for campaigns page to load");
    await expect(page.getByText(/your campaigns/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByTestId(/test-Medieval Adventure/i)).toBeVisible({
      timeout: 60000,
    });
    console.log("     Clicking on test campaign");
    await page.getByTestId(/test-Medieval Adventure/i).click();
    console.log("     Navigating to item boosters");
    await page.getByTestId("navbar-item-boosters").click();
    console.log("     Opening item booster");
    await page.getByTestId("open-booster-button").click();
    // gives max 5 minutes for the booster to open
    console.log("     Waiting for booster to open (max 5 minutes)");
    await expect(page.getByTestId("booster-data")).toBeVisible({
      timeout: 300_300,
    });
    console.log("     🎉 Booster opened successfully");
    console.log("     Navigating to inventory");
    await page.getByTestId("navbar-inventory").click();

    const categories = [
      { tab: "armors", card: "armor" },
      { tab: "boots", card: "boots" },
      { tab: "consumables", card: "consumable" },
      { tab: "helmets", card: "helmet" },
      { tab: "shields", card: "shield" },
      { tab: "spells", card: "spell" },
      { tab: "weapons", card: "weapon" },
    ];

    let totalCards = 0;

    for (const { tab, card } of categories) {
      const tabElement = page.getByText(new RegExp(tab, "i"));
      try {
        await tabElement.first().waitFor({ state: "visible", timeout: 1500 });
        await tabElement.first().click();
        const cardCount = await page.getByTestId(`${card}-card`).count();
        totalCards += cardCount;
        if (cardCount > 0) {
          console.log(
            `     Found ${cardCount} ${card} card(s) in ${tab} category`
          );
        }
      } catch {
        // Tab didn't appear within 1.5s, skip it
        console.log(`     Skipping ${tab} category (tab not visible)`);
      }
    }

    console.log(`     Total cards found in inventory: ${totalCards}`);
    await expect(totalCards).toBe(3);
    console.log("✅ Verified total cards count is 3 - test complete");
  });

  test("navigates to the inventory route and equips an item if available", async ({
    page,
  }) => {
    console.log("🚀 ////START////");
    console.log(
      "Starting test: navigates to the inventory route and equips an item if available"
    );
    await page.goto(FRONTEND_URL);
    console.log("     Waiting for page to fully load");
    await page.waitForLoadState("networkidle");
    console.log("     Ensuring Clerk authentication");
    await ensureAuthenticated(page);
    await expect(page.getByRole("button", { name: /sign out/i })).toBeVisible();
    console.log("     Clerk authentication ready");
    console.log("     Navigating to campaigns page");
    await page.getByText(/campaigns/i).click();
    console.log("     Waiting for navigation to campaigns route");
    await page.waitForURL(
      new RegExp(
        `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign`
      ),
      {
        timeout: 30000,
      }
    );
    await page.waitForLoadState("networkidle");
    console.log("     Waiting for campaigns page to load");
    await expect(page.getByText(/your campaigns/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByTestId(/test-Medieval Adventure/i)).toBeVisible({
      timeout: 60000,
    });
    console.log("     Clicking on test campaign");
    await page.getByTestId(/test-Medieval Adventure/i).click();
    console.log("     Waiting for navigation to campaign detail page");
    await page.waitForURL(
      new RegExp(
        `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign/\\d+`
      ),
      {
        timeout: 30000,
      }
    );
    await page.waitForLoadState("networkidle");
    console.log("     Navigating to inventory");
    await page.getByTestId("navbar-inventory").click();
    console.log("     Waiting for navigation to inventory route");
    await expect(page.getByText(/Aki Test!/i)).toBeVisible();
    await page.waitForLoadState("networkidle");
    console.log("     Waiting for hero data to load");
    await expect(page.getByText("Aki Test!")).toBeVisible({
      timeout: 30000,
    });
    console.log("     Hero data loaded, inventory page ready");
    const equippableCategories = [
      "armors",
      "boots",
      "helmets",
      "shields",
      "weapons",
    ];

    console.log("     Checking each category tab");
    for (const category of equippableCategories) {
      const tab = page.getByText(new RegExp(category, "i"));
      try {
        await tab.first().waitFor({ state: "visible", timeout: 1500 });
        await tab.first().click();
      } catch {
        // Tab didn't appear within 1.5s, skip it
        console.log(`     Skipping ${category} category (tab not visible)`);
      }
    }

    console.log("     Verifying item placeholders count (should be 5)");
    await expect(page.getByTestId("item-placeholder")).toHaveCount(5);
    const equippableCount = await page
      .getByTestId("equip-item-inventory")
      .count();
    if (equippableCount > 0) {
      console.log(
        `     Found ${equippableCount} equippable item(s), equipping first item`
      );
      const equippableItems = page.getByTestId("equip-item-inventory");
      await equippableItems.first().click();
      console.log(
        "     Item equipped, verifying updated placeholder count (should be 4)"
      );
      await expect(page.getByTestId("item-placeholder")).toHaveCount(4);
      console.log("✅ Item successfully equipped - test complete");
    } else {
      console.log("✅ No equippable items found - test complete");
    }
  });

  test("navigates to the inventory route and unequips an item if available", async ({
    page,
  }) => {
    console.log("🚀 ////START////");
    console.log(
      "Starting test: navigates to the inventory route and unequips an item if available"
    );
    await page.goto(FRONTEND_URL);
    console.log("     Waiting for page to fully load");
    await page.waitForLoadState("networkidle");
    console.log("     Ensuring Clerk authentication");
    await ensureAuthenticated(page);
    await expect(page.getByRole("button", { name: /sign out/i })).toBeVisible();
    console.log("     Clerk authentication ready");
    console.log("     Navigating to campaigns page");
    await page.getByText(/campaigns/i).click();
    console.log("     Waiting for navigation to campaigns route");
    await page.waitForURL(
      new RegExp(
        `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign`
      ),
      {
        timeout: 30000,
      }
    );
    await page.waitForLoadState("networkidle");
    console.log("     Waiting for campaigns page to load");
    await expect(page.getByText(/your campaigns/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByTestId(/test-Medieval Adventure/i)).toBeVisible({
      timeout: 60000,
    });
    console.log("     Clicking on test campaign");
    await page.getByTestId(/test-Medieval Adventure/i).click();
    console.log("     Waiting for navigation to campaign detail page");
    await page.waitForURL(
      new RegExp(
        `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign/\\d+`
      ),
      {
        timeout: 30000,
      }
    );
    await page.waitForLoadState("networkidle");
    console.log("     Navigating to inventory");
    await page.getByTestId("navbar-inventory").click();
    console.log("     Waiting for navigation to inventory route");
    await expect(page.getByText(/Aki Test!/i)).toBeVisible();
    await page.waitForLoadState("networkidle");
    console.log("     Waiting for hero data to load");
    await expect(page.getByText("Aki Test!")).toBeVisible({
      timeout: 30000,
    });
    console.log("     Hero data loaded, inventory page ready");

    const placeholderCount = await page.getByTestId("item-placeholder").count();
    console.log(`     Current item placeholder count: ${placeholderCount}`);
    if (placeholderCount === 4) {
      console.log(
        "     Item is equipped (placeholder count is 4), unequipping item"
      );
      await expect(page.getByTestId("item-placeholder")).toHaveCount(4);
      const equippedItem = page.getByTestId("unequip-item-inventory");
      await equippedItem.first().click();
      console.log(
        "     Item unequipped, verifying updated placeholder count (should be 5)"
      );
      await expect(page.getByTestId("item-placeholder")).toHaveCount(5);
      console.log("✅ Item successfully unequipped - test complete");
    } else {
      console.log(
        `✅ No item equipped (placeholder count is ${placeholderCount}, expected 4) - test complete`
      );
    }
  });

  test("navigates to the character booster route, opens an character booster and verifies that it opened", async ({
    page,
  }) => {
    console.log("🚀 ////START////");
    console.log(
      "Starting test: navigates to the character booster route, opens an character booster and verifies that it opened"
    );
    await page.goto(FRONTEND_URL);
    console.log("     Waiting for page to fully load");
    await page.waitForLoadState("networkidle");
    console.log("     Ensuring Clerk authentication");
    await ensureAuthenticated(page);
    await expect(page.getByRole("button", { name: /sign out/i })).toBeVisible();
    console.log("     Clerk authentication ready");
    console.log("     Navigating to campaigns page");
    await page.getByText(/campaigns/i).click();
    console.log("     Waiting for navigation to campaigns route");
    await page.waitForURL(
      new RegExp(
        `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign`
      ),
      {
        timeout: 30000,
      }
    );
    await page.waitForLoadState("networkidle");
    console.log("     Waiting for campaigns page to load");
    await expect(page.getByText(/your campaigns/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByTestId(/test-Medieval Adventure/i)).toBeVisible({
      timeout: 60000,
    });
    console.log("     Clicking on test campaign");
    await page.getByTestId(/test-Medieval Adventure/i).click();
    console.log("     Navigating to character boosters");
    await page.getByTestId("navbar-character-boosters").click();
    console.log("     Opening character booster");
    await page.getByTestId("open-booster-button").click();
    // gives max 5 minutes for booster to open
    console.log("     Waiting for booster to open (max 5 minutes)");
    await expect(page.getByTestId("booster-data")).toBeVisible({
      timeout: 300_00,
    });
    console.log("✅ Character booster opened successfully - test complete");
  });

  test("navigates to the battle route and fights and finishes a battle", async ({
    page,
  }) => {
    console.log("🚀 ////START////");
    test.setTimeout(240000); // this test has 4 minutes for completion
    console.log("     Starting battle test: navigating to campaigns");
    await page.goto(FRONTEND_URL);
    console.log("     Waiting for page to fully load");
    await page.waitForLoadState("networkidle");
    console.log("     Ensuring Clerk authentication");
    await ensureAuthenticated(page);
    await expect(page.getByRole("button", { name: /sign out/i })).toBeVisible();
    console.log("     Clerk authentication ready");
    console.log("     Navigating to campaigns page");
    await page.getByText(/campaigns/i).click();
    console.log("     Waiting for navigation to campaigns route");
    await page.waitForURL(
      new RegExp(
        `${FRONTEND_URL.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}campaign`
      ),
      {
        timeout: 30000,
      }
    );
    await page.waitForLoadState("networkidle");
    console.log("     Waiting for campaigns page to load");
    await expect(page.getByText(/your campaigns/i)).toBeVisible({
      timeout: 30000,
    });
    await expect(page.getByTestId(/test-Medieval Adventure/i)).toBeVisible({
      timeout: 60000,
    });
    console.log("     Clicking on test campaign");
    await page.getByTestId(/test-Medieval Adventure/i).click();
    console.log("     Navigating to battles");
    await page.getByTestId("navbar-battles").click();
    console.log("     Waiting for battle creation button to load");
    await page.waitForLoadState("networkidle");
    await expect(page.getByTestId("create-battle-button")).toBeVisible();
    console.log("     Creating battle");
    await page.getByTestId("create-battle-button").click();

    // Wait for battle page to load
    console.log("     Waiting for battle page to load");
    await page.waitForLoadState("networkidle");

    // Wait for "Character to play" text to confirm battle state loaded
    console.log("     Waiting for battle state to load");
    await expect(page.getByText(/Character to play:/i)).toBeVisible({
      timeout: 30000,
    });
    console.log("     Battle state loaded");

    // Check for and click "Start Battle" button if enemy starts first
    const startBattleButton = page.getByTestId("start-battle-trigger");
    const startBattleCount = await startBattleButton.count();
    if (startBattleCount > 0) {
      console.log("     Enemy starts first, clicking Start Battle button");
      await startBattleButton.click();
      // Wait for battle to actually start after clicking
      await page.waitForTimeout(2000);
      console.log("     Battle started");
    } else {
      console.log("     Hero starts first, battle already in progress");
    }

    const battleTimeout = 300000;
    const start = Date.now();
    let actionCount = 0;

    console.log("     Entering battle loop");
    while (Date.now() - start < battleTimeout) {
      // Check if battle is finished first
      try {
        const closeButtonCount = await page
          .getByTestId("close-battle-button")
          .count();
        if (closeButtonCount > 0) {
          console.log("     Battle finished! Clicking close button");
          await page.getByTestId("close-battle-button").click();
          // Wait a bit before finishing loop
          await page.waitForTimeout(1000);
          break;
        }
      } catch (error) {
        // Battle might have finished and navigated away
        console.log(
          "Error checking close button (battle may have finished):",
          error
        );
      }

      // Try to play a card if available
      try {
        const punchCardCount = await page.getByTestId("punch-card").count();
        if (punchCardCount > 0) {
          actionCount++;
          console.log(
            `     Playing action #${actionCount}: clicking punch card`
          );
          await page.getByTestId("punch-card").click();
          console.log(
            `     Action #${actionCount}: clicking battle-hero-stats (targeting self for efficiency)`
          );
          await page.getByTestId("battle-hero-stats").click();
          // Wait for state refresh after hero action (2.8s delay + network time)
          console.log(
            `     Action #${actionCount}: waiting for state refresh (3-4 seconds)`
          );
          await page.waitForTimeout(3500);
          continue;
        }
      } catch (error) {
        // Battle might have finished mid-action
        console.log(
          `Error during action #${actionCount} (battle may have finished):`,
          error
        );
        // Check if battle finished
        try {
          const closeButtonCount = await page
            .getByTestId("close-battle-button")
            .count();
          if (closeButtonCount > 0) {
            console.log(
              "     Battle finished during action, clicking close button"
            );
            await page.getByTestId("close-battle-button").click();
            await page.waitForTimeout(1000);
            break;
          }
        } catch {
          // Continue loop
        }
      }

      // Wait between loop iterations to allow for NPC turns
      // If it's enemy's turn, NPC turn takes 5.5s trigger + 2.5s refresh = 8s
      // We'll wait a bit longer to be safe
      console.log("     Waiting for potential NPC turn (9-10 seconds)");
      await page.waitForTimeout(9500);
    }

    const elapsed = Date.now() - start;
    console.log(
      `     Battle completed in ${elapsed}ms (${(elapsed / 1000).toFixed(1)}s)`
    );
    expect(elapsed).toBeLessThan(battleTimeout);

    // After battle is finished, we should get redirected to the campaign page
    console.log("     Waiting for redirect to campaign page");
    await expect(page).toHaveURL(`${FRONTEND_URL}campaign/1`, {
      timeout: 10000,
    });
    console.log("✅ Battle test complete");
  });
});
