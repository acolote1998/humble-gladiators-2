import { createFileRoute } from "@tanstack/react-router";
import { useParams } from "@tanstack/react-router";
import { useGetHeroByCampaignAndUser } from "../../../hooks/useCharacters";
import { ArmorCard } from "../../../components/cards/ArmorCard";
import { BootsCard } from "../../../components/cards/BootsCard";
import { ConsumableCard } from "../../../components/cards/ConsumableCard";
import { HelmetCard } from "../../../components/cards/HelmetCard";
import { ShieldTemplateCard } from "../../../components/cards/ShieldCard";
import { SpellCard } from "../../../components/cards/SpellCard";
import { WeaponCard } from "../../../components/cards/WeaponCard";

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
  return (
    <div>
      {isLoadingHero ? (
        <p>Loading hero...</p>
      ) : isErrorHero ? (
        <p>Error loading hero</p>
      ) : (
        heroData && (
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
              <div className="grid grid-cols-4 text-center">
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
            <div>
              <h2 className="text-xl my-2 font-semibold bg-gray-300 text-center p-2">
                Inventory
              </h2>
              <div>
                <details>
                  <summary>Armors</summary>
                  <div className="grid grid-cols-3">
                    {heroData.inventory.armors.map((armor) => (
                      <ArmorCard
                        key={armor.name}
                        {...armor}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
                <details>
                  <summary>Boots</summary>
                  <div className="grid grid-cols-3">
                    {heroData.inventory.boots.map((boot) => (
                      <BootsCard
                        key={boot.name}
                        {...boot}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
                <details>
                  <summary>Consumables</summary>
                  <div className="grid grid-cols-3">
                    {heroData.inventory.consumables.map((consumable) => (
                      <ConsumableCard
                        key={consumable.name}
                        {...consumable}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
                <details>
                  <summary>Helmets</summary>
                  <div className="grid grid-cols-3">
                    {heroData.inventory.helmets.map((helmet) => (
                      <HelmetCard
                        key={helmet.name}
                        {...helmet}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
                <details>
                  <summary>Shields</summary>
                  <div className="grid grid-cols-3">
                    {heroData.inventory.shields.map((shield) => (
                      <ShieldTemplateCard
                        key={shield.name}
                        {...shield}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
                <details>
                  <summary>Spells</summary>
                  <div className="grid grid-cols-3">
                    {heroData.inventory.spells.map((spell) => (
                      <SpellCard
                        key={spell.name}
                        {...spell}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
                <details>
                  <summary>Weapons</summary>
                  <div className="grid grid-cols-3">
                    {heroData.inventory.weapons.map((weapon) => (
                      <WeaponCard
                        key={weapon.name}
                        {...weapon}
                        renderingFrom="INVENTORY"
                      />
                    ))}
                  </div>
                </details>
              </div>
            </div>
          </div>
        )
      )}
    </div>
  );
}
