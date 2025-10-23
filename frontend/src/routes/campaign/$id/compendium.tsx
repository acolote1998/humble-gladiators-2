import { createFileRoute } from "@tanstack/react-router";
import { useParams } from "@tanstack/react-router";
import { useGetAllArmorTemplatesForCampaignByUser } from "../../../hooks/useArmors";
import { useGetAllBootsTemplatesForCampaignByUser } from "../../../hooks/useBoots";
import { useGetAllConsumableTemplatesForCampaignByUser } from "../../../hooks/useConsumables";
import { useGetAllHelmetTemplatesForCampaignByUser } from "../../../hooks/useHelmets";
import { useGetAllShieldTemplatesForCampaignByUser } from "../../../hooks/useShields";
import { useGetAllSpellTemplatesForCampaignByUser } from "../../../hooks/useSpells";
import { useGetAllWeaponTemplatesForCampaignByUser } from "../../../hooks/useWeapons";
import { useGetCharactersByCampaignAndUser } from "../../../hooks/userCharacters";
import { CharacterCard } from "../../../components/cards/CharacterCard";
import { ArmorCard } from "../../../components/cards/ArmorCard";
import { BootsCard } from "../../../components/cards/BootsCard";
import { ConsumableCard } from "../../../components/cards/ConsumableCard";
import { HelmetCard } from "../../../components/cards/HelmetCard";
import { ShieldCard } from "../../../components/cards/ShieldCard";
import { SpellCard } from "../../../components/cards/SpellCard";
import { WeaponCard } from "../../../components/cards/WeaponCard";

export const Route = createFileRoute("/campaign/$id/compendium")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id/compendium" });
  const { data: characterInstancesData } = useGetCharactersByCampaignAndUser(
    Number(campaignId)
  );
  const { data: armorTemplatesData } = useGetAllArmorTemplatesForCampaignByUser(
    Number(campaignId)
  );
  const { data: bootsTemplatesData } = useGetAllBootsTemplatesForCampaignByUser(
    Number(campaignId)
  );
  const { data: consumableTemplatesData } =
    useGetAllConsumableTemplatesForCampaignByUser(Number(campaignId));
  const { data: helmetTemplatesData } =
    useGetAllHelmetTemplatesForCampaignByUser(Number(campaignId));
  const { data: shieldTemplatesData } =
    useGetAllShieldTemplatesForCampaignByUser(Number(campaignId));
  const { data: spellTemplatesData } = useGetAllSpellTemplatesForCampaignByUser(
    Number(campaignId)
  );
  const { data: weaponTemplatesData } =
    useGetAllWeaponTemplatesForCampaignByUser(Number(campaignId));
  return (
    <>
      <details>
        <summary>NPCs</summary>
        <div className="grid grid-cols-3">
          {characterInstancesData
            ?.sort((a, b) => {
              if (a.imgBase64 && !b.imgBase64) return -1; // a has image, b doesn't → a first
              if (!a.imgBase64 && b.imgBase64) return 1; // b has image, a doesn't → b first
              return 0; // both have or both don't → keep order
            })
            .map(
              (char) =>
                char.characterType === "NPC" && (
                  <CharacterCard
                    key={char.name + char.id}
                    {...char}
                    renderingFrom="COMPENDIUM"
                  />
                )
            )}
        </div>
      </details>
      <details>
        <summary>Armors</summary>
        <div className="grid grid-cols-3">
          {armorTemplatesData
            ?.sort((a, b) => {
              if (a.imgBase64 && !b.imgBase64) return -1; // a has image, b doesn't → a first
              if (!a.imgBase64 && b.imgBase64) return 1; // b has image, a doesn't → b first
              return 0; // both have or both don't → keep order
            })
            .map((armor) => (
              <ArmorCard
                key={armor.name + armor.id}
                {...armor}
                renderingFrom="COMPENDIUM"
              />
            ))}
        </div>
      </details>
      <details>
        <summary>Boots</summary>
        <div className="grid grid-cols-3">
          {bootsTemplatesData
            ?.sort((a, b) => {
              if (a.imgBase64 && !b.imgBase64) return -1; // a has image, b doesn't → a first
              if (!a.imgBase64 && b.imgBase64) return 1; // b has image, a doesn't → b first
              return 0; // both have or both don't → keep order
            })
            .map((boot) => (
              <BootsCard
                key={boot.name + boot.id}
                {...boot}
                renderingFrom="COMPENDIUM"
              />
            ))}
        </div>
      </details>
      <details>
        <summary>Consumables</summary>
        <div className="grid grid-cols-3">
          {consumableTemplatesData
            ?.sort((a, b) => {
              if (a.imgBase64 && !b.imgBase64) return -1; // a has image, b doesn't → a first
              if (!a.imgBase64 && b.imgBase64) return 1; // b has image, a doesn't → b first
              return 0; // both have or both don't → keep order
            })
            .map((consumable) => (
              <ConsumableCard
                key={consumable.name + consumable.id}
                {...consumable}
                renderingFrom="COMPENDIUM"
              />
            ))}
        </div>
      </details>
      <details>
        <summary>Helmets</summary>
        <div className="grid grid-cols-3">
          {helmetTemplatesData
            ?.sort((a, b) => {
              if (a.imgBase64 && !b.imgBase64) return -1; // a has image, b doesn't → a first
              if (!a.imgBase64 && b.imgBase64) return 1; // b has image, a doesn't → b first
              return 0; // both have or both don't → keep order
            })
            .map((helmet) => (
              <HelmetCard
                key={helmet.name + helmet.id}
                {...helmet}
                renderingFrom="COMPENDIUM"
              />
            ))}
        </div>
      </details>
      <details>
        <summary>Shields</summary>
        <div className="grid grid-cols-3">
          {shieldTemplatesData
            ?.sort((a, b) => {
              if (a.imgBase64 && !b.imgBase64) return -1; // a has image, b doesn't → a first
              if (!a.imgBase64 && b.imgBase64) return 1; // b has image, a doesn't → b first
              return 0; // both have or both don't → keep order
            })
            .map((shield) => (
              <ShieldCard
                key={shield.name + shield.id}
                {...shield}
                renderingFrom="COMPENDIUM"
              />
            ))}
        </div>
      </details>
      <details>
        <summary>Spells</summary>
        <div className="grid grid-cols-3">
          {spellTemplatesData
            ?.sort((a, b) => {
              if (a.imgBase64 && !b.imgBase64) return -1; // a has image, b doesn't → a first
              if (!a.imgBase64 && b.imgBase64) return 1; // b has image, a doesn't → b first
              return 0; // both have or both don't → keep order
            })
            .map((spell) => (
              <SpellCard
                key={spell.name + spell.id}
                {...spell}
                renderingFrom="COMPENDIUM"
              />
            ))}
        </div>
      </details>
      <details>
        <summary>Weapons</summary>
        <div className="grid grid-cols-3">
          {weaponTemplatesData
            ?.sort((a, b) => {
              if (a.imgBase64 && !b.imgBase64) return -1; // a has image, b doesn't → a first
              if (!a.imgBase64 && b.imgBase64) return 1; // b has image, a doesn't → b first
              return 0; // both have or both don't → keep order
            })
            .map((weapon) => (
              <WeaponCard
                key={weapon.name + weapon.id}
                {...weapon}
                renderingFrom="COMPENDIUM"
              />
            ))}
        </div>
      </details>
    </>
  );
}
