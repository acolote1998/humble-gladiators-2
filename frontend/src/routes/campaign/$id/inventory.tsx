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
import {
  useGetIsBattleOngoing,
  useGetWonBattlesForHeroForCampaignIdAndUsery,
} from "../../../hooks/useBattles";
import { useGetLostBattlesForHeroForCampaignIdAndUsery } from "../../../hooks/useBattles";
import { Loader } from "../../../components/Loader";

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
  const { data: wonBattles, isLoading: isLoadingWonBattles } =
    useGetWonBattlesForHeroForCampaignIdAndUsery(Number(campaignId));
  const { data: lostBattles, isLoading: isLoadingLostBattles } =
    useGetLostBattlesForHeroForCampaignIdAndUsery(Number(campaignId));
  const { data: isBattleOngoing, isLoading: isBattleOngoingCheckLoading } =
    useGetIsBattleOngoing(Number(campaignId));
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
        >
          <div className="grid grid-cols-9">
            <div className="col-span-8">
              <div className="grid grid-cols-5">
                <div className="col-span-1">
                  {heroData.inventory.helmets.map((helmet) => {
                    if (helmet.equipped)
                      return (
                        <HelmetCard
                          key={helmet.name + helmet.id + "equipped"}
                          {...helmet}
                          renderingFrom="INVENTORY"
                        />
                      );
                  })}
                </div>
                <div className="col-span-1">
                  {heroData.inventory.armors.map((armor) => {
                    if (armor.equipped)
                      return (
                        <ArmorCard
                          key={armor.name + armor.id + "equipped"}
                          {...armor}
                          renderingFrom="INVENTORY"
                        />
                      );
                  })}
                </div>
                <div className="col-span-1">
                  {heroData.inventory.weapons.map((weapon) => {
                    if (weapon.equipped)
                      return (
                        <WeaponCard
                          key={weapon.name + weapon.id + "equipped"}
                          {...weapon}
                          renderingFrom="INVENTORY"
                        />
                      );
                  })}
                </div>
                <div className="col-span-1">
                  {heroData.inventory.shields.map((shield) => {
                    if (shield.equipped)
                      return (
                        <ShieldCard
                          key={shield.name + shield.id + "equipped"}
                          {...shield}
                          renderingFrom="INVENTORY"
                        />
                      );
                  })}
                </div>
                <div className="col-span-1">
                  {heroData.inventory.boots.map((boot) => {
                    if (boot.equipped)
                      return (
                        <BootsCard
                          key={boot.name + boot.id + "equipped"}
                          {...boot}
                          renderingFrom="INVENTORY"
                        />
                      );
                  })}
                </div>
              </div>
            </div>
            <div>
              <div>
                <div>
                  <h2 className="text-2xl my-2 font-semibold bg-gray-300 text-center p-2">
                    {heroData.name}
                  </h2>
                  <div className="flex items-center justify-center gap-20">
                    <p>
                      HP - {heroData.stats.currentHp}/{heroData.stats.maxHp}
                    </p>
                    <p>
                      MP - {heroData.stats.currentMp}/{heroData.stats.maxMp}
                    </p>
                  </div>
                </div>
                <div>
                  <h2 className="text-xl my-2 font-semibold bg-gray-300 text-center p-2">
                    Stats
                  </h2>
                  <div className="grid grid-cols-2 text-center">
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
                    {isLoadingWonBattles ? (
                      <p>...</p>
                    ) : wonBattles ? (
                      <p>Won battles - {wonBattles.length}</p>
                    ) : (
                      <p>Won battles - 0</p>
                    )}
                    {isLoadingLostBattles ? (
                      <p>...</p>
                    ) : lostBattles ? (
                      <p>Lost battles - {lostBattles.length}</p>
                    ) : (
                      <p>Lost battles - 0</p>
                    )}
                    {/* <p>
                  EXP - {heroData.stats.currentExp}/
                  {heroData.stats.expForNextLevel}
                </p> */}
                  </div>
                </div>
              </div>
            </div>
          </div>{" "}
          <div>
            <h2 className="text-xl my-2 font-semibold bg-gray-300 text-center p-2">
              Inventory
            </h2>
            <div>
              {heroData.inventory.armors.length > 0 && (
                <details>
                  <summary>Armors</summary>
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
                  <summary>Boots</summary>
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
                  <summary>Consumables</summary>
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
                  <summary>Helmets</summary>
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
                  <summary>Shields</summary>
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
                  <summary>Spells</summary>
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
                  <summary>Weapons</summary>
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
