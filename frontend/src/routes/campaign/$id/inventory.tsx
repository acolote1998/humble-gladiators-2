import { createFileRoute } from "@tanstack/react-router";
import { useParams } from "@tanstack/react-router";
import { useGetHeroByCampaignAndUser } from "../../../hooks/useCharacters";
import { ArmorCard } from "../../../components/cards/ArmorCard";
import { BootsCard } from "../../../components/cards/BootsCard";
import { HelmetCard } from "../../../components/cards/HelmetCard";
import { ShieldCard } from "../../../components/cards/ShieldCard";
import { WeaponCard } from "../../../components/cards/WeaponCard";
import { useGetIsBattleOngoing } from "../../../hooks/useBattles";
import { Loader } from "../../../components/Loader";
import type { HelmetType } from "../../../types/helmetTypes";
import type { ArmorType } from "../../../types/armorTypes";
import type { ShieldType } from "../../../types/shieldTypes";
import type { WeaponType } from "../../../types/weaponTypes";
import type { BootsType } from "../../../types/bootsTypes";
import InventoryCardPlaceholder from "../../../components/cards/InventoryCardPlaceholder";
import { PageContainer } from "@/components/ui/PageContainer";
import CardsDisplayer from "@/components/ui/CardsDisplayer";
import StatBar from "@/components/stats/StatBar";
import { PhysicalDamageIcon } from "@/components/icons/stats/PhysicalDamageIcon";
import { MagicalDamageIcon } from "@/components/icons/stats/MagicalDamageIcon";
import { PhysicalDefenseIcon } from "@/components/icons/stats/PhysicalDefenseIcon";
import { MagicalDefenseIcon } from "@/components/icons/stats/MagicalDefenseIcon";
import { LevelIcon } from "@/components/icons/stats/LevelIcon";
import { StrengthIcon } from "@/components/icons/stats/StrengthIcon";
import { ConstitutionIcon } from "@/components/icons/stats/ConstitutionIcon";
import { IntelligenceIcon } from "@/components/icons/stats/IntelligenceIcon";
import { SpeedIcon } from "@/components/icons/stats/SpeedIcon";
import { LuckIcon } from "@/components/icons/stats/LuckIcon";
import { WeightIcon } from "@/components/icons/stats/WeightIcon";
import { HeightIcon } from "@/components/icons/stats/HeightIcon";

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
    <PageContainer id="inventory-start">
      {isLoadingHero ? (
        <Loader />
      ) : isErrorHero ? (
        <p>Error loading hero</p>
      ) : isBattleOngoingCheckLoading ? (
        <Loader />
      ) : !isBattleOngoing && heroData ? (
        <>
          <div>
            <h2 className="text-2xl my-2 font-semibold bg-[var(--page-container-bg-darker)] text-center p-2">
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
              <h2 className="text-xl my-2 font-semibold bg-[var(--page-container-bg-darker)] text-center p-2">
                Stats
              </h2>
              <div className="grid grid-cols-7 text-center items-center justify-items-center">
                <StatBar
                  widthPercent={90}
                  barHeight={2}
                  currentValue={heroData.stats.currentHp}
                  maxValue={heroData.stats.maxHp}
                  type="HP"
                />
                <StatBar
                  widthPercent={90}
                  barHeight={2}
                  currentValue={heroData.stats.currentMp}
                  maxValue={heroData.stats.maxMp}
                  type="MANA"
                />

                <div
                  title="Physical Defense"
                  className="flex flex-col items-center cursor-pointer select-none gap-1"
                >
                  <PhysicalDefenseIcon width={24} />
                  {heroData.stats.physicalDefense}
                </div>
                <div
                  title="Magical Defense"
                  className="flex flex-col items-center cursor-pointer select-none gap-1"
                >
                  <MagicalDefenseIcon width={24} />
                  {heroData.stats.magicalDefense}
                </div>
                <div
                  title="Level"
                  className="flex flex-col items-center cursor-pointer select-none gap-1"
                >
                  <LevelIcon width={20} />
                  {heroData.stats.level}
                </div>

                <div
                  title="Strength"
                  className="flex flex-col items-center cursor-pointer select-none gap-1"
                >
                  <StrengthIcon width={24} />
                  {heroData.stats.strength}
                </div>
                <div
                  title="Constitution"
                  className="flex flex-col items-center cursor-pointer select-none gap-1"
                >
                  <ConstitutionIcon width={24} />
                  {heroData.stats.constitution}
                </div>
                <div
                  title="Physical Damage"
                  className="flex flex-col items-center cursor-pointer select-none gap-1"
                >
                  <PhysicalDamageIcon width={24} />
                  {heroData.stats.physicalDamage}
                </div>
                <div
                  title="Magical Damage"
                  className="flex flex-col items-center cursor-pointer select-none gap-1"
                >
                  <MagicalDamageIcon width={24} />
                  {heroData.stats.magicalDamage}
                </div>
                <div
                  title="Intelligence"
                  className="flex flex-col items-center cursor-pointer select-none gap-1"
                >
                  <IntelligenceIcon width={24} />
                  {heroData.stats.intelligence}
                </div>
                <div
                  title="Speed"
                  className="flex flex-col items-center cursor-pointer select-none gap-1"
                >
                  <SpeedIcon width={24} />
                  {heroData.stats.speed}
                </div>
                <div
                  title="Luck"
                  className="flex flex-col items-center cursor-pointer select-none gap-1"
                >
                  <LuckIcon width={24} />
                  {heroData.stats.luck}
                </div>
                <div
                  title="Weight"
                  className="flex flex-col items-center cursor-pointer select-none gap-1"
                >
                  <WeightIcon width={24} />
                  {heroData.stats.weight} kg
                </div>
                <div
                  title="Height"
                  className="flex flex-col items-center cursor-pointer select-none gap-1"
                >
                  <HeightIcon width={24} />
                  {heroData.stats.height} cm
                </div>
                {/* <p>
                  EXP - {heroData.stats.currentExp}/
                  {heroData.stats.expForNextLevel}
                </p> */}
              </div>
            </div>
          </div>
          {(heroData.inventory.armors.length > 0 ||
            heroData.inventory.boots.length > 0 ||
            heroData.inventory.consumables.length > 0 ||
            heroData.inventory.helmets.length > 0 ||
            heroData.inventory.shields.length > 0 ||
            heroData.inventory.spells.length > 0 ||
            heroData.inventory.weapons.length > 0) && (
            <div>
              <h2 className="text-xl my-2 font-semibold bg-[var(--page-container-bg-darker)] text-center p-2">
                Inventory
              </h2>
              <CardsDisplayer
                renderingThisDisplayFrom="INVENTORY"
                armors={heroData.inventory.armors}
                boots={heroData.inventory.boots}
                consumables={heroData.inventory.consumables}
                helmets={heroData.inventory.helmets}
                shields={heroData.inventory.shields}
                spells={heroData.inventory.spells}
                weapons={heroData.inventory.weapons}
              />
            </div>
          )}
        </>
      ) : (
        <div className="text-lg font-semibold text-center flex items-center justify-center">
          It is not possible to use the inventory during an ongoing battle.
        </div>
      )}
    </PageContainer>
  );
}
