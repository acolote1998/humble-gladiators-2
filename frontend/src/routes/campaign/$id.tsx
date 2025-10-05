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
export const Route = createFileRoute("/campaign/$id")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id" });
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
      <div className="grid grid-cols-3">
        {characterInstancesData?.map((char) => (
          <CharacterInstanceCard {...char} />
        ))}
      </div>
      <div className="grid grid-cols-3">
        {armorTemplatesData?.map((armor) => (
          <ArmorTemplateCard {...armor} />
        ))}
      </div>
      <div className="grid grid-cols-3">
        {bootsTemplatesData?.map((armor) => (
          <BootsTemplateCard {...armor} />
        ))}
      </div>
      <div className="grid grid-cols-3">
        {consumableTemplatesData?.map((armor) => (
          <ConsumableTemplateCard {...armor} />
        ))}
      </div>
      <div className="grid grid-cols-3">
        {helmetTemplatesData?.map((armor) => (
          <HelmetTemplateCard {...armor} />
        ))}
      </div>
      <div className="grid grid-cols-3">
        {shieldTemplatesData?.map((armor) => (
          <ShieldTemplateCard {...armor} />
        ))}
      </div>
      <p
        onClick={() => {
          console.log(spellTemplatesData);
        }}
      >
        Log Spells
      </p>
      <p
        onClick={() => {
          console.log(weaponTemplatesData);
        }}
      >
        Log Weapons
      </p>
    </>
  );
}
