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
      <p
        onClick={() => {
          console.log(characterInstancesData);
        }}
      >
        Log Characters
      </p>
      <p
        onClick={() => {
          console.log(armorTemplatesData);
        }}
      >
        Log Armors
      </p>
      <p
        onClick={() => {
          console.log(bootsTemplatesData);
        }}
      >
        Log Boots
      </p>
      <p
        onClick={() => {
          console.log(consumableTemplatesData);
        }}
      >
        Log Consumables
      </p>
      <p
        onClick={() => {
          console.log(helmetTemplatesData);
        }}
      >
        Log Helmets
      </p>
      <p
        onClick={() => {
          console.log(shieldTemplatesData);
        }}
      >
        Log Shields
      </p>
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
