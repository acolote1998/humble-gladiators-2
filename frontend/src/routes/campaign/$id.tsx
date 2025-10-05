import { createFileRoute, useParams } from "@tanstack/react-router";
import { useGetCampaignByIdForAUser } from "../../hooks/useCampaigns";
import CampaignItem from "../../components/campaigns/CampaignItem";
import { useGetCharactersByCampaignAndUser } from "../../hooks/userCharacters";
import { useGetAllArmorTemplatesForCampaignByUser } from "../../hooks/useArmors";
import { useGetAllBootsTemplatesForCampaignByUser } from "../../hooks/useBoots";
import { useGetAllConsumableTemplatesForCampaignByUser } from "../../hooks/useConsumables";
import { useGetAllHelmetTemplatesForCampaignByUser } from "../../hooks/useHelmets";
import { useGetAllShieldTemplatesForCampaignByUser } from "../../hooks/useShields";
import { useGetAllSpellTemplatesForCampaignByUser } from "../../hooks/useSpells";
import { useGetAllWeaponTemplatesForCampaignByUser } from "../../hooks/useWeapons";
import { CharacterInstanceCard } from "../../components/characters/CharacterInstanceCard";
import { ArmorTemplateCard } from "../../components/characters/ArmorTemplateCard";
import { BootsTemplateCard } from "../../components/characters/BootsTemplateCard";
import { ConsumableTemplateCard } from "../../components/characters/ConsumableTemplateCard";
import { HelmetTemplateCard } from "../../components/characters/HelmetTemplateCard";
import { ShieldTemplateCard } from "../../components/characters/ShieldTemplateCard";
import { SpellTemplateCard } from "../../components/characters/SpellTemplateCard";
import { WeaponTemplateCard } from "../../components/characters/WeaponTemplateCard";
import { useGetHeroByCampaignAndUser } from "../../hooks/userCharacters";
export const Route = createFileRoute("/campaign/$id")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id" });
  const { data: heroData } = useGetHeroByCampaignAndUser(Number(campaignId));
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
  const {
    data: campaignData,
    isError: isCampaignError,
    isLoading: isCampaignLoadingError,
  } = useGetCampaignByIdForAUser(Number(campaignId));
  return (
    <>
      {isCampaignError ? (
        <p>Loading</p>
      ) : isCampaignLoadingError ? (
        <p>Error loading</p>
      ) : (
        campaignData && <CampaignItem {...campaignData} />
      )}

      <p
        onClick={() => {
          console.log(heroData);
        }}
        className="bg-gray-400 p-3 rounded-lg"
      >
        Log Hero Data
      </p>
      <div className="grid grid-cols-3">
        {characterInstancesData?.map((char) => (
          <CharacterInstanceCard key={char.name} {...char} />
        ))}
      </div>
      <div className="grid grid-cols-3">
        {armorTemplatesData?.map((armor) => (
          <ArmorTemplateCard key={armor.name} {...armor} />
        ))}
      </div>
      <div className="grid grid-cols-3">
        {bootsTemplatesData?.map((boot) => (
          <BootsTemplateCard key={boot.name} {...boot} />
        ))}
      </div>
      <div className="grid grid-cols-3">
        {consumableTemplatesData?.map((consumable) => (
          <ConsumableTemplateCard key={consumable.name} {...consumable} />
        ))}
      </div>
      <div className="grid grid-cols-3">
        {helmetTemplatesData?.map((helmet) => (
          <HelmetTemplateCard key={helmet.name} {...helmet} />
        ))}
      </div>
      <div className="grid grid-cols-3">
        {shieldTemplatesData?.map((shield) => (
          <ShieldTemplateCard key={shield.name} {...shield} />
        ))}
      </div>
      <div className="grid grid-cols-3">
        {spellTemplatesData?.map((spell) => (
          <SpellTemplateCard key={spell.name} {...spell} />
        ))}
      </div>
      <div className="grid grid-cols-3">
        {weaponTemplatesData?.map((weapon) => (
          <WeaponTemplateCard key={weapon.name} {...weapon} />
        ))}
      </div>
    </>
  );
}
