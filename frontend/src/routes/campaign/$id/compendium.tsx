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
import { PageContainer } from "@/components/ui/PageContainer";
import CardsDisplayer from "@/components/ui/CardsDisplayer";

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
      <PageContainer>
        <CardsDisplayer
          renderingThisDisplayFrom="COMPENDIUM"
          armors={armorTemplatesData}
          boots={bootsTemplatesData}
          characters={characterInstancesData}
          consumables={consumableTemplatesData}
          helmets={helmetTemplatesData}
          shields={shieldTemplatesData}
          spells={spellTemplatesData}
          weapons={weaponTemplatesData}
        />
      </PageContainer>
    </>
  );
}
