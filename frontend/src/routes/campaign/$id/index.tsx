import { createFileRoute, useParams } from "@tanstack/react-router";
import { useGetHeroExistence } from "../../../hooks/useCharacters";
import { RedirectCreateHeroButton } from "../../../components/campaigns/RedirectCreateHeroButton";
import { Loader } from "../../../components/Loader";
import { useGetCampaignByIdForAUser } from "../../../hooks/useCampaigns";
import { useGetAllArmorTemplatesForCampaignByUser } from "../../../hooks/useArmors";
import { useGetAllBootsTemplatesForCampaignByUser } from "../../../hooks/useBoots";
import { useGetAllConsumableTemplatesForCampaignByUser } from "../../../hooks/useConsumables";
import { useGetAllHelmetTemplatesForCampaignByUser } from "../../../hooks/useHelmets";
import { useGetAllShieldTemplatesForCampaignByUser } from "../../../hooks/useShields";
import { useGetAllSpellTemplatesForCampaignByUser } from "../../../hooks/useSpells";
import { useGetAllWeaponTemplatesForCampaignByUser } from "../../../hooks/useWeapons";
import { useGetCharactersByCampaignAndUser } from "../../../hooks/userCharacters";
import { useEffect, useState } from "react";
import { DiscoveredItemInfo } from "../../../components/campaigns/DiscoveredItemInfo";
export const Route = createFileRoute("/campaign/$id/")({
  component: RouteComponent,
});

function RouteComponent() {
  const [percentDiscoveredCharacters, setPercentDiscoveredCharacters] =
    useState<number>(0);
  const [percentDiscoveredArmors, setPercentDiscoveredArmors] =
    useState<number>(0);
  const [percentDiscoveredBoots, setPercentDiscoveredBoots] =
    useState<number>(0);
  const [percentDiscoveredConsumables, setPercentDiscoveredConsumables] =
    useState<number>(0);
  const [percentDiscoveredHelmets, setPercentDiscoveredHelmets] =
    useState<number>(0);
  const [percentDiscoveredShields, setPercentDiscoveredShields] =
    useState<number>(0);
  const [percentDiscoveredWeapons, setPercentDiscoveredWeapons] =
    useState<number>(0);
  const [percentDiscoveredSpells, setPercentDiscoveredSpells] =
    useState<number>(0);
  const { id: campaignId } = useParams({ from: "/campaign/$id/" });
  const {
    data: doesHeroExist,
    isLoading: doesHeroExistLoading,
    isError: doesHeroExistError,
  } = useGetHeroExistence(Number(campaignId));
  const { data: campaignData, isLoading: isCampaignLoading } =
    useGetCampaignByIdForAUser(Number(campaignId));

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

  const calculatePercentOfDiscovery = (total: number, discovered: number) => {
    return Math.round((discovered * 100) / total);
  };

  useEffect(() => {
    const calculateDiscoveredArmorsPercent = () => {
      if (armorTemplatesData) {
        const total = armorTemplatesData?.length;
        const discovered = armorTemplatesData?.filter((a) => {
          return a.discovered;
        }).length;
        return calculatePercentOfDiscovery(total, discovered);
      }
      return 0;
    };
    const calculateDiscoveredBootsPercent = () => {
      if (bootsTemplatesData) {
        const total = bootsTemplatesData?.length;
        const discovered = bootsTemplatesData?.filter((b) => {
          return b.discovered;
        }).length;
        return calculatePercentOfDiscovery(total, discovered);
      }
      return 0;
    };
    const calculateDiscoveredConsumablesPercent = () => {
      if (consumableTemplatesData) {
        const total = consumableTemplatesData.length;
        const discovered = consumableTemplatesData.filter((c) => {
          return c.discovered;
        }).length;
        return calculatePercentOfDiscovery(total, discovered);
      }
      return 0;
    };
    const calculateDiscoveredHelmetsPercent = () => {
      if (helmetTemplatesData) {
        const total = helmetTemplatesData.length;
        const discovered = helmetTemplatesData.filter((h) => {
          return h.discovered;
        }).length;
        return calculatePercentOfDiscovery(total, discovered);
      }
      return 0;
    };
    setPercentDiscoveredArmors(calculateDiscoveredArmorsPercent());
    setPercentDiscoveredBoots(calculateDiscoveredBootsPercent());
    setPercentDiscoveredConsumables(calculateDiscoveredConsumablesPercent());
    setPercentDiscoveredHelmets(calculateDiscoveredHelmetsPercent());
  }, [
    armorTemplatesData,
    bootsTemplatesData,
    consumableTemplatesData,
    helmetTemplatesData,
  ]);

  return (
    <>
      <div
        className="
          mx-5
          rounded-b-2xl
          border-3
        border-gray-400 
        bg-gray-200
        "
      >
        {doesHeroExistLoading || isCampaignLoading ? (
          <Loader />
        ) : (
          campaignData &&
          campaignData.coverImgBase64 && (
            <div className="grid grid-cols-5 p-5">
              <img
                draggable={false}
                className="h-[74vh] rounded-md border-2 col-span-3"
                src={`data:image/jpeg;base64,${campaignData.coverImgBase64}`}
              />
              {characterInstancesData &&
              armorTemplatesData &&
              bootsTemplatesData &&
              consumableTemplatesData &&
              helmetTemplatesData &&
              shieldTemplatesData &&
              weaponTemplatesData &&
              spellTemplatesData ? (
                <div className="col-span-2">
                  <h1 className="text-4xl text-center font-bold tracking-wide py-3">
                    {campaignData.name}
                  </h1>
                  <fieldset className="border p-4 rounded-lg border-gray-400">
                    <legend className="text-xl px-2">Campaign Stats</legend>
                    <div className="grid grid-cols-10 items-center gap-4">
                      <DiscoveredItemInfo
                        itemName="Armors"
                        percentDiscovered={percentDiscoveredArmors}
                      />
                      <DiscoveredItemInfo
                        itemName="Boots"
                        percentDiscovered={percentDiscoveredBoots}
                      />
                      <DiscoveredItemInfo
                        itemName="Consumables"
                        percentDiscovered={percentDiscoveredConsumables}
                      />
                      <DiscoveredItemInfo
                        itemName="Helmets"
                        percentDiscovered={percentDiscoveredHelmets}
                      />
                    </div>
                  </fieldset>
                </div>
              ) : (
                <Loader />
              )}
            </div>
          )
        )}
        {doesHeroExistError ? (
          <p>Error loading hero availability</p>
        ) : (
          !doesHeroExistLoading &&
          !doesHeroExist && (
            <RedirectCreateHeroButton campaignId={Number(campaignId)} />
          )
        )}
      </div>
    </>
  );
}
