import { createFileRoute } from "@tanstack/react-router";
import { useParams } from "@tanstack/react-router";
import { useGetHeroByCampaignAndUser } from "../../../hooks/useCharacters";
import { ArmorCard } from "../../../components/cards/ArmorCard";
import { BootsCard } from "../../../components/cards/BootsCard";
import { ConsumableCard } from "../../../components/cards/ConsumableCard";
import { HelmetCard } from "../../../components/cards/HelmetCard";
import { ShieldCard } from "../../../components/cards/ShieldCard";
import { SpellCard } from "../../../components/cards/SpellCard";
import { WeaponCard } from "../../../components/cards/WeaponCard";
import { useGetIsBattleOngoing } from "../../../hooks/useBattles";
import { Loader } from "../../../components/Loader";
import type { HelmetType } from "../../../types/helmetTypes";
import type { ArmorType } from "../../../types/armorTypes";
import type { ShieldType } from "../../../types/shieldTypes";
import type { WeaponType } from "../../../types/weaponTypes";
import type { BootsType } from "../../../types/bootsTypes";
import InventoryCardPlaceholder from "../../../components/cards/InventoryCardPlaceholder";
import { ArmorIcon } from "../../../components/icons/typeofcards/ArmorIcon";
import { BootIcon } from "../../../components/icons/typeofcards/BootIcon";
import { ConsumableIcon } from "../../../components/icons/typeofcards/ConsumableIcon";
import { HelmetIcon } from "../../../components/icons/typeofcards/HelmetIcon";
import { ShieldIcon } from "../../../components/icons/typeofcards/ShieldIcon";
import { SpellIcon } from "../../../components/icons/typeofcards/SpellIcon";
import { WeaponIcon } from "../../../components/icons/typeofcards/WeaponIcon";

export const Route = createFileRoute("/campaign/$id/inventory")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id/inventory" });
  const {
    data: heroData,
    isLoading: isLoadingHero,
    isError: isErrorHero,
  } = useGetHeroByCampaignAndUser(Number(campaignId));
  const { data: isBattleOngoing, isLoading: isBattleOngoingCheckLoading } =
    useGetIsBattleOngoing(Number(campaignId));
  const getEquippedItem = (
    item: HelmetType[] | ArmorType[] | ShieldType[] | WeaponType[] | BootsType[]
  ) => {
    let equippedItem = undefined;
    if (heroData) {
      equippedItem = item.filter((i) => {
        return i.equipped;
      })[0];
    }
    return equippedItem;
  };
  const getEquippedHelmet = () => {
    if (heroData) return getEquippedItem(heroData.inventory.helmets);
  };
  const equippedHelmet = getEquippedHelmet();
  const getEquippedArmor = () => {
    if (heroData) return getEquippedItem(heroData.inventory.armors);
  };
  const equippedArmor = getEquippedArmor();
  const getEquippedShield = () => {
    if (heroData) return getEquippedItem(heroData.inventory.shields);
  };
  const equippedShield = getEquippedShield();
  const getEquippedWeapon = () => {
    if (heroData) return getEquippedItem(heroData.inventory.weapons);
  };
  const equippedWeapon = getEquippedWeapon();
  const getEquippedBoots = () => {
    if (heroData) return getEquippedItem(heroData.inventory.boots);
  };
  const equippedBoots = getEquippedBoots();
  return (
    <>
      {isLoadingHero ? (
        <Loader />
      ) : isErrorHero ? (
        <p>Error loading hero</p>
      ) : isBattleOngoingCheckLoading ? (
        <Loader />
      ) : !isBattleOngoing && heroData ? (
        <div
          className="
          mx-5
          p-5
          rounded-b-2xl
          border-3
        border-gray-400 
        bg-gray-200
        "
          id="inventory-start"
        >
          <div>
            <h2 className="text-2xl my-2 font-semibold bg-gray-300 text-center p-2">
              {heroData.name}
            </h2>
          </div>
          <div className="flex justify-evenly">
            <div>
              {equippedHelmet ? (
                <HelmetCard
                  key={equippedHelmet.name + equippedHelmet.id + "equipped"}
                  {...(equippedHelmet as HelmetType)}
                  renderingFrom="INVENTORY"
                />
              ) : (
                <InventoryCardPlaceholder type="HELMET" />
              )}
            </div>
            <div>
              {equippedArmor ? (
                <ArmorCard
                  key={equippedArmor.name + equippedArmor.id + "equipped"}
                  {...(equippedArmor as ArmorType)}
                  renderingFrom="INVENTORY"
                />
              ) : (
                <InventoryCardPlaceholder type="ARMOR" />
              )}
            </div>
            <div>
              {equippedWeapon ? (
                <WeaponCard
                  key={equippedWeapon.name + equippedWeapon.id + "equipped"}
                  {...(equippedWeapon as WeaponType)}
                  renderingFrom="INVENTORY"
                />
              ) : (
                <InventoryCardPlaceholder type="WEAPON" />
              )}
            </div>
            <div>
              {equippedShield ? (
                <ShieldCard
                  key={equippedShield.name + equippedShield.id + "equipped"}
                  {...(equippedShield as ShieldType)}
                  renderingFrom="INVENTORY"
                />
              ) : (
                <InventoryCardPlaceholder type="SHIELD" />
              )}
            </div>
            <div>
              {equippedBoots ? (
                <BootsCard
                  key={equippedBoots.name + equippedBoots.id + "equipped"}
                  {...(equippedBoots as BootsType)}
                  renderingFrom="INVENTORY"
                />
              ) : (
                <InventoryCardPlaceholder type="BOOTS" />
              )}
            </div>
          </div>
          <div>
            <div>
              <h2 className="text-xl my-2 font-semibold bg-gray-300 text-center p-2">
                Stats
              </h2>
              <div className="grid grid-cols-7 text-center">
                <p>
                  HP - {heroData.stats.currentHp}/{heroData.stats.maxHp}
                </p>
                <p>
                  MP - {heroData.stats.currentMp}/{heroData.stats.maxMp}
                </p>
                <p>P. DMG - {heroData.stats.physicalDamage}</p>
                <p>M. DMG - {heroData.stats.magicalDamage}</p>
                <p>P. DEF - {heroData.stats.physicalDefense}</p>
                <p>M. DEF - {heroData.stats.magicalDefense}</p>
                <p>LVL - {heroData.stats.level}</p>
                <p>STR - {heroData.stats.strength}</p>
                <p>CON - {heroData.stats.constitution}</p>
                <p>INT - {heroData.stats.intelligence}</p>
                <p>SPD - {heroData.stats.speed}</p>
                <p>LCK - {heroData.stats.luck}</p>
                <p>WGT - {heroData.stats.height}</p>
                <p>HGT - {heroData.stats.weight}</p>
                {/* <p>
                  EXP - {heroData.stats.currentExp}/
                  {heroData.stats.expForNextLevel}
                </p> */}
              </div>
            </div>
          </div>
          <div>
            <h2 className="text-xl my-2 font-semibold bg-gray-300 text-center p-2">
              Inventory
            </h2>
            <div>
              {heroData.inventory.armors.length > 0 && (
                <details>
                  <summary>
                    <div className="flex justify-start gap-3">
                      <ArmorIcon width={32} />
                      <p className="text-2xl">Armors</p>
                    </div>
                  </summary>
                  <div className="grid grid-cols-5">
                    {heroData.inventory.armors.map((armor) => (
                      <ArmorCard
                        key={armor.name + armor.id}
                        {...armor}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
              )}
              {heroData.inventory.boots.length > 0 && (
                <details>
                  <summary>
                    <div className="flex justify-start gap-3">
                      <BootIcon width={32} />
                      <p className="text-2xl">Boots</p>
                    </div>
                  </summary>
                  <div className="grid grid-cols-5">
                    {heroData.inventory.boots.map((boot) => (
                      <BootsCard
                        key={boot.name + boot.id}
                        {...boot}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
              )}
              {heroData.inventory.consumables.length > 0 && (
                <details>
                  <summary>
                    <div className="flex justify-start gap-3">
                      <ConsumableIcon width={32} />
                      <p className="text-2xl">Consumables</p>
                    </div>
                  </summary>
                  <div className="grid grid-cols-5">
                    {heroData.inventory.consumables.map((consumable) => (
                      <ConsumableCard
                        key={consumable.name + consumable.id}
                        {...consumable}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
              )}
              {heroData.inventory.helmets.length > 0 && (
                <details>
                  <summary>
                    <div className="flex justify-start gap-3">
                      <HelmetIcon width={32} />
                      <p className="text-2xl">Helmets</p>
                    </div>
                  </summary>
                  <div className="grid grid-cols-5">
                    {heroData.inventory.helmets.map((helmet) => (
                      <HelmetCard
                        key={helmet.name + helmet.id}
                        {...helmet}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
              )}
              {heroData.inventory.shields.length > 0 && (
                <details>
                  <summary>
                    <div className="flex justify-start gap-3">
                      <ShieldIcon width={32} />
                      <p className="text-2xl">Shields</p>
                    </div>
                  </summary>
                  <div className="grid grid-cols-5">
                    {heroData.inventory.shields.map((shield) => (
                      <ShieldCard
                        key={shield.name + shield.id}
                        {...shield}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
              )}
              {heroData.inventory.spells.length > 0 && (
                <details>
                  <summary>
                    <div className="flex justify-start gap-3">
                      <SpellIcon width={32} />
                      <p className="text-2xl">Spells</p>
                    </div>
                  </summary>
                  <div className="grid grid-cols-5">
                    {heroData.inventory.spells.map((spell) => (
                      <SpellCard
                        key={spell.name + spell.id}
                        {...spell}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
              )}
              {heroData.inventory.weapons.length > 0 && (
                <details>
                  <summary>
                    <div className="flex justify-start gap-3">
                      <WeaponIcon width={32} />
                      <p className="text-2xl">Weapons</p>
                    </div>
                  </summary>
                  <div className="grid grid-cols-5">
                    {heroData.inventory.weapons.map((weapon) => (
                      <WeaponCard
                        key={weapon.name + weapon.id}
                        {...weapon}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
              )}
            </div>
          </div>
        </div>
      ) : (
        <div
          className="
          mx-5
          p-5
          rounded-b-2xl
          border-3
        border-gray-400 
        bg-gray-200
        "
        >
          <div className="text-lg font-semibold text-center flex items-center justify-center">
            It is not possible to use the inventory during an ongoing battle.
          </div>
        </div>
      )}
    </>
  );
}
