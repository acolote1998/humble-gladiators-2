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
import type { ArmorType } from "../../../types/armorTypes";
import type { BootsType } from "../../../types/bootsTypes";
import type { ConsumableType } from "../../../types/consumablesTypes";
import type { HelmetType } from "../../../types/helmetTypes";
import type { WeaponType } from "../../../types/weaponTypes";
import type { SpellType } from "../../../types/spellTypes";
import type { ShieldType } from "../../../types/shieldTypes";
import {
  useGetLostBattlesForHeroForCampaignIdAndUsery,
  useGetWonBattlesForHeroForCampaignIdAndUsery,
} from "../../../hooks/useBattles";
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
  const [winratePercent, setWinratePercent] = useState<number>(0);
  const { id: campaignId } = useParams({ from: "/campaign/$id/" });
  const { data: wonBattles } = useGetWonBattlesForHeroForCampaignIdAndUsery(
    Number(campaignId)
  );
  const { data: lostBattles } = useGetLostBattlesForHeroForCampaignIdAndUsery(
    Number(campaignId)
  );
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

  const calculatePercentOfAchievement = (total: number, discovered: number) => {
    return Math.round((discovered * 100) / total);
  };

  useEffect(() => {
    const calculateDiscoveredItemsPercent = (
      items:
        | ArmorType[]
        | BootsType[]
        | ConsumableType[]
        | HelmetType[]
        | ShieldType[]
        | SpellType[]
        | WeaponType[]
    ) => {
      const total = items.length;
      const discovered = items.filter((s) => {
        return s.discovered;
      }).length;
      return calculatePercentOfAchievement(total, discovered);
    };
    const calculateDiscoveredCharactersPercent = () => {
      if (characterInstancesData) {
        const total = characterInstancesData.filter((c) => {
          return c.characterType === "NPC";
        }).length;
        const discovered = characterInstancesData.filter((c) => {
          return c.discovered;
        }).length;
        return calculatePercentOfAchievement(total, discovered);
      }
      return 0;
    };
    const calculateWinrate = (wonBattles: number, lostBattles: number) => {
      return calculatePercentOfAchievement(
        wonBattles + lostBattles,
        wonBattles
      );
    };
    if (armorTemplatesData)
      setPercentDiscoveredArmors(
        calculateDiscoveredItemsPercent(armorTemplatesData)
      );
    if (bootsTemplatesData)
      setPercentDiscoveredBoots(
        calculateDiscoveredItemsPercent(bootsTemplatesData)
      );
    if (consumableTemplatesData)
      setPercentDiscoveredConsumables(
        calculateDiscoveredItemsPercent(consumableTemplatesData)
      );
    if (helmetTemplatesData)
      setPercentDiscoveredHelmets(
        calculateDiscoveredItemsPercent(helmetTemplatesData)
      );
    if (shieldTemplatesData)
      setPercentDiscoveredShields(
        calculateDiscoveredItemsPercent(shieldTemplatesData)
      );
    if (weaponTemplatesData)
      setPercentDiscoveredWeapons(
        calculateDiscoveredItemsPercent(weaponTemplatesData)
      );
    if (spellTemplatesData)
      setPercentDiscoveredSpells(
        calculateDiscoveredItemsPercent(spellTemplatesData)
      );
    if (characterInstancesData)
      setPercentDiscoveredCharacters(calculateDiscoveredCharactersPercent());
    if (wonBattles && lostBattles) {
      setWinratePercent(
        calculateWinrate(wonBattles.length, lostBattles.length)
      );
    }
  }, [
    armorTemplatesData,
    bootsTemplatesData,
    consumableTemplatesData,
    helmetTemplatesData,
    shieldTemplatesData,
    weaponTemplatesData,
    spellTemplatesData,
    characterInstancesData,
    wonBattles,
    lostBattles,
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
                        percentAchieved={percentDiscoveredArmors}
                      />
                      <DiscoveredItemInfo
                        itemName="Boots"
                        percentAchieved={percentDiscoveredBoots}
                      />
                      <DiscoveredItemInfo
                        itemName="Consumables"
                        percentAchieved={percentDiscoveredConsumables}
                      />
                      <DiscoveredItemInfo
                        itemName="Helmets"
                        percentAchieved={percentDiscoveredHelmets}
                      />
                      <DiscoveredItemInfo
                        itemName="Shields"
                        percentAchieved={percentDiscoveredShields}
                      />
                      <DiscoveredItemInfo
                        itemName="Weapons"
                        percentAchieved={percentDiscoveredWeapons}
                      />
                      <DiscoveredItemInfo
                        itemName="Spells"
                        percentAchieved={percentDiscoveredSpells}
                      />
                      <DiscoveredItemInfo
                        itemName="Characters"
                        percentAchieved={percentDiscoveredCharacters}
                      />
                      <DiscoveredItemInfo
                        itemName="Win Rate"
                        percentAchieved={winratePercent}
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
