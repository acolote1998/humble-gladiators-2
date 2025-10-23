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
        )
      )}
    </div>
  );
}
