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
export const Route = createFileRoute("/campaign/$id/")({
  component: RouteComponent,
});

function RouteComponent() {
  const [percentDiscoveredCharacters, setPercentDiscoveredCharacters] =
    useState<number>();
  const [percentDiscoveredArmors, setPercentDiscoveredArmors] =
    useState<number>();
  const [percentDiscoveredBoots, setPercentDiscoveredBoots] =
    useState<number>();
  const [percentDiscoveredConsumables, setPercentDiscoveredConsumables] =
    useState<number>();
  const [percentDiscoveredHelmets, setPercentDiscoveredHelmets] =
    useState<number>();
  const [percentDiscoveredShields, setPercentDiscoveredShields] =
    useState<number>();
  const [percentDiscoveredWeapons, setPercentDiscoveredWeapons] =
    useState<number>();
  const [percentDiscoveredSpells, setPercentDiscoveredSpells] =
    useState<number>();
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
    };
    setPercentDiscoveredArmors(calculateDiscoveredArmorsPercent());
  }, [armorTemplatesData]);

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
                    <div className="grid grid-cols-10 items-center">
                      <p className="col-span-1 text-lg font-semibold">Armors</p>
                      <div className="col-span-9 relative bg-yellow-100 border-yellow-600 border rounded-md h-10">
                        <div
                          className={`transition-all duration-500 rounded-md border-blue-500 border absolute bg-blue-300 top-0 left-0 h-full flex items-center justify-center`}
                          style={{ width: `${percentDiscoveredArmors}%` }}
                        >
                          <p className="text-center font-black text-lg">
                            {percentDiscoveredArmors}%
                          </p>
                        </div>
                      </div>
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
