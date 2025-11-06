import { test, expect } from "@playwright/test";
import dotenv from "dotenv";
dotenv.config();

const FRONTEND_URL = process.env.FRONTEND_URL!;
test.describe.configure({ mode: "serial" });

test.describe("Campaign Flow", () => {
  test.beforeAll("creating hero in test campaign", async ({ browser }) => {
    const context = await browser.newContext();
    const page = await context.newPage();
    await page.goto(FRONTEND_URL);
    await page.getByText(/campaigns/i).click();
    await page.getByTestId(/test-Medieval Adventure/i).click();
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
  }, { timeout: 60000 }); // Increase beforeAll timeout to 60 seconds

  test("navigating to campaigns shows the test campaign", async ({ page }) => {
    await page.goto(FRONTEND_URL);
    await page.getByText(/campaigns/i).click();
    await expect(page.getByTestId(/test-Medieval Adventure/i)).toBeVisible();
  });

  test("navigating to compendium and verifying that all cards are created", async ({
    page,
  }) => {
    await page.goto(FRONTEND_URL);
    await page.getByText(/campaigns/i).click();
    await page.getByTestId(/test-Medieval Adventure/i).click();
    await page.getByTestId("navbar-compendium").click();
    await page.getByText(/npc's/i).click();
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

  test("navigates to the item booster route, opens an item booster and verifies that the correct amount of cards are in the inventory", async ({
    page,
  }) => {
    await page.goto(FRONTEND_URL);
    await page.getByText(/campaigns/i).click();
    await page.getByTestId(/test-Medieval Adventure/i).click();
    await page.getByTestId("navbar-item-boosters").click();
    await page.getByTestId("open-booster-button").click();
    // gives max 5 minutes for the booster to open
    await expect(page.getByTestId("booster-data")).toBeVisible({
      timeout: 300_300,
    });
    await page.getByTestId("navbar-inventory").click();
    const categories = [
      "armors",
      "boots",
      "consumables",
      "helmets",
      "shields",
      "spells",
      "weapons",
    ];

    for (const category of categories) {
      const tab = page.getByText(new RegExp(category, "i"));
      try {
        await tab.first().waitFor({ state: "visible", timeout: 1500 });
        await tab.first().click();
      } catch {
        // Tab didn't appear within 1.5s, skip it
      }
    }

    const cardType = [
      "armor",
      "boots",
      "consumable",
      "helmet",
      "shield",
      "spell",
      "weapon",
    ];

    let totalCards = 0;
    for (const card of cardType) {
      totalCards += await page.getByTestId(`${card}-card`).count();
    }
    await expect(totalCards).toBe(3);
  });

  test("navigates to the inventory route and equips an item if available", async ({
    page,
  }) => {
    await page.goto(FRONTEND_URL);
    await page.getByText(/campaigns/i).click();
    await page.getByTestId(/test-Medieval Adventure/i).click();
    await page.getByTestId("navbar-inventory").click();
    const categories = [
      "armors",
      "boots",
      "consumables",
      "helmets",
      "shields",
      "spells",
      "weapons",
    ];

    for (const category of categories) {
      const tab = page.getByText(new RegExp(category, "i"));
      try {
        await tab.first().waitFor({ state: "visible", timeout: 1500 });
        await tab.first().click();
      } catch {
        // Tab didn't appear within 1.5s, skip it
      }
    }

    await expect(page.getByTestId("item-placeholder")).toHaveCount(5);
    if ((await page.getByTestId("equip-item-inventory").count()) > 0) {
      const equippableItems = page.getByTestId("equip-item-inventory");
      await equippableItems.first().click();
      await expect(page.getByTestId("item-placeholder")).toHaveCount(4);
    }
  });

  test("navigates to the inventory route and unequips an item if available", async ({
    page,
  }) => {
    await page.goto(FRONTEND_URL);
    await page.getByText(/campaigns/i).click();
    await page.getByTestId(/test-Medieval Adventure/i).click();
    await page.getByTestId("navbar-inventory").click();

    if ((await page.getByTestId("item-placeholder").count()) === 4) {
      await expect(page.getByTestId("item-placeholder")).toHaveCount(4);
      const equippedItem = page.getByTestId("unequip-item-inventory");
      await equippedItem.first().click();
      await expect(page.getByTestId("item-placeholder")).toHaveCount(5);
    }
  });

  test("navigates to the character booster route, opens an character booster and verifies that it opened", async ({
    page,
  }) => {
    await page.goto(FRONTEND_URL);
    await page.getByText(/campaigns/i).click();
    await page.getByTestId(/test-Medieval Adventure/i).click();
    await page.getByTestId("navbar-character-boosters").click();
    await page.getByTestId("open-booster-button").click();
    // gives max 5 minutes for booster to open
    await expect(page.getByTestId("booster-data")).toBeVisible({
      timeout: 300_00,
    });
  });

  test("navigates to the battle route and fights and finishes a battle", async ({
    page,
  }) => {
    test.setTimeout(240000); // this test has 4 minutes for completion
    await page.goto(FRONTEND_URL);
    await page.getByText(/campaigns/i).click();
    await page.getByTestId(/test-Medieval Adventure/i).click();
    await page.getByTestId("navbar-battles").click();
    await expect(page.getByTestId("create-battle-button")).toBeVisible();
    await page.getByTestId("create-battle-button").click();
    try {
      await page
        .getByText(/Start Battle/i)
        .waitFor({ state: "visible", timeout: 2000 });
      await page.getByText(/Start Battle/i).click();

      // If the character would be faster, then the "Start Battle" button does not render
      // eslint-disable-next-line
    } catch {}
    const battleTimeout = 180000; // 3 min of fight max
    const start = Date.now();

    while (Date.now() - start < battleTimeout) {
      try {
        if ((await page.getByTestId("punch-card").count()) > 0) {
          await page.getByTestId("punch-card").click();
          await page.getByTestId("battle-hero-stats").click();
          // Wait a bit before checking again
          await page.waitForTimeout(500);
          continue;
        }
      } catch {
        //It's okay if we have a catch here, it means that most likely the battle finished in the middle of an attempt of using an attack card
        //and the punch card disappeared, causing an exception while trying to click an invisible object
      }

      try {
        if ((await page.getByTestId("close-battle-button").count()) > 0) {
          await page.getByTestId("close-battle-button").click();
          // Wait a bit before finishing loop
          await page.waitForTimeout(500);
          break;
        }
      } catch {
        //It's okay if we have a catch here, it means that most likely the battle finished and we navigated away from this logic, so there is an error
        //by trying to click on something that is not longer rendered
      }
    }

    expect(Date.now() - start).toBeLessThan(battleTimeout);

    // After battle is finished, we should get redirected to the campaign page
    await expect(page).toHaveURL(`${FRONTEND_URL}campaign/1`);
  });
});
