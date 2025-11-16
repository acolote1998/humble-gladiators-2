import { useParams } from "@tanstack/react-router";
import { useEffect, useState } from "react";
import {
  useGetLostBattlesForHeroForCampaignIdAndUsery,
  useGetWonBattlesForHeroForCampaignIdAndUsery,
} from "../../hooks/useBattles";
import { useGetCharactersByCampaignAndUser } from "../../hooks/useCharacters";
import { useGetAllArmorTemplatesForCampaignByUser } from "../../hooks/useArmors";
import { useGetAllBootsTemplatesForCampaignByUser } from "../../hooks/useBoots";
import { useGetAllConsumableTemplatesForCampaignByUser } from "../../hooks/useConsumables";
import { useGetAllHelmetTemplatesForCampaignByUser } from "../../hooks/useHelmets";
import { useGetAllShieldTemplatesForCampaignByUser } from "../../hooks/useShields";
import { useGetAllSpellTemplatesForCampaignByUser } from "../../hooks/useSpells";
import { useGetAllWeaponTemplatesForCampaignByUser } from "../../hooks/useWeapons";
import type { ArmorType } from "../../types/armorTypes";
import type { BootsType } from "../../types/bootsTypes";
import type { ConsumableType } from "../../types/consumablesTypes";
import type { HelmetType } from "../../types/helmetTypes";
import type { ShieldType } from "../../types/shieldTypes";
import type { SpellType } from "../../types/spellTypes";
import type { WeaponType } from "../../types/weaponTypes";
import { DiscoveredItemInfo } from "./DiscoveredItemInfo";
import { Loader } from "../Loader";
import type { DiscoveredItemInfoType } from "./DiscoveredItemInfo";
import type { CharacterInstanceType } from "@/types/characterTypes";

const CampaignStats = () => {
  const [discoveredStatCharacters, setdiscoveredStatCharacters] =
    useState<DiscoveredItemInfoType>({
      itemName: "",
      percentAchieved: 0,
      totalAchieved: 0,
      totalPossible: 0,
    });
  const [discoveredStatArmors, setdiscoveredStatArmors] =
    useState<DiscoveredItemInfoType>({
      itemName: "",
      percentAchieved: 0,
      totalAchieved: 0,
      totalPossible: 0,
    });
  const [discoveredStatBoots, setdiscoveredStatBoots] =
    useState<DiscoveredItemInfoType>({
      itemName: "",
      percentAchieved: 0,
      totalAchieved: 0,
      totalPossible: 0,
    });
  const [discoveredStatConsumables, setdiscoveredStatConsumables] =
    useState<DiscoveredItemInfoType>({
      itemName: "",
      percentAchieved: 0,
      totalAchieved: 0,
      totalPossible: 0,
    });
  const [discoveredStatHelmets, setdiscoveredStatHelmets] =
    useState<DiscoveredItemInfoType>({
      itemName: "",
      percentAchieved: 0,
      totalAchieved: 0,
      totalPossible: 0,
    });
  const [discoveredStatShields, setdiscoveredStatShields] =
    useState<DiscoveredItemInfoType>({
      itemName: "",
      percentAchieved: 0,
      totalAchieved: 0,
      totalPossible: 0,
    });
  const [discoveredStatWeapons, setdiscoveredStatWeapons] =
    useState<DiscoveredItemInfoType>({
      itemName: "",
      percentAchieved: 0,
      totalAchieved: 0,
      totalPossible: 0,
    });
  const [discoveredStatSpells, setdiscoveredStatSpells] =
    useState<DiscoveredItemInfoType>({
      itemName: "",
      percentAchieved: 0,
      totalAchieved: 0,
      totalPossible: 0,
    });
  const [winrateStat, setWinrateStat] = useState<DiscoveredItemInfoType>({
    itemName: "",
    percentAchieved: 0,
    totalAchieved: 0,
    totalPossible: 0,
  });
  const { id: campaignId } = useParams({ from: "/campaign/$id/" });
  const { data: wonBattles } = useGetWonBattlesForHeroForCampaignIdAndUsery(
    Number(campaignId)
  );
  const { data: lostBattles } = useGetLostBattlesForHeroForCampaignIdAndUsery(
    Number(campaignId)
  );

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
    const getDiscoveredItemsAmount = (
      items:
        | ArmorType[]
        | BootsType[]
        | ConsumableType[]
        | HelmetType[]
        | ShieldType[]
        | SpellType[]
        | WeaponType[]
        | CharacterInstanceType[]
    ) => {
      return items.filter((s) => {
        return s.discovered;
      }).length;
    };
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
      const discovered = getDiscoveredItemsAmount(items);
      return calculatePercentOfAchievement(total, discovered);
    };

    const calculateDiscoveredCharactersPercent = () => {
      if (characterInstancesData) {
        const totalNpcs = characterInstancesData.filter((c) => {
          return c.characterType === "NPC";
        });
        const discovered = getDiscoveredItemsAmount(totalNpcs);
        return calculatePercentOfAchievement(totalNpcs.length, discovered);
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
      setdiscoveredStatArmors({
        itemName: "Armors",
        percentAchieved: calculateDiscoveredItemsPercent(armorTemplatesData),
        totalAchieved: getDiscoveredItemsAmount(armorTemplatesData),
        totalPossible: armorTemplatesData.length,
      });
    if (bootsTemplatesData)
      setdiscoveredStatBoots({
        itemName: "Boots",
        percentAchieved: calculateDiscoveredItemsPercent(bootsTemplatesData),
        totalAchieved: getDiscoveredItemsAmount(bootsTemplatesData),
        totalPossible: bootsTemplatesData.length,
      });

    if (consumableTemplatesData)
      setdiscoveredStatConsumables({
        itemName: "Consumables",
        percentAchieved: calculateDiscoveredItemsPercent(
          consumableTemplatesData
        ),
        totalAchieved: getDiscoveredItemsAmount(consumableTemplatesData),
        totalPossible: consumableTemplatesData.length,
      });

    if (helmetTemplatesData)
      setdiscoveredStatHelmets({
        itemName: "Helmets",
        percentAchieved: calculateDiscoveredItemsPercent(helmetTemplatesData),
        totalAchieved: getDiscoveredItemsAmount(helmetTemplatesData),
        totalPossible: helmetTemplatesData.length,
      });

    if (shieldTemplatesData)
      setdiscoveredStatShields({
        itemName: "Shields",
        percentAchieved: calculateDiscoveredItemsPercent(shieldTemplatesData),
        totalAchieved: getDiscoveredItemsAmount(shieldTemplatesData),
        totalPossible: shieldTemplatesData.length,
      });

    if (weaponTemplatesData)
      setdiscoveredStatWeapons({
        itemName: "Weapons",
        percentAchieved: calculateDiscoveredItemsPercent(weaponTemplatesData),
        totalAchieved: getDiscoveredItemsAmount(weaponTemplatesData),
        totalPossible: weaponTemplatesData.length,
      });

    if (spellTemplatesData)
      setdiscoveredStatSpells({
        itemName: "Spells",
        percentAchieved: calculateDiscoveredItemsPercent(spellTemplatesData),
        totalAchieved: getDiscoveredItemsAmount(spellTemplatesData),
        totalPossible: spellTemplatesData.length,
      });
    if (characterInstancesData)
      setdiscoveredStatCharacters({
        itemName: "Characters",
        percentAchieved: calculateDiscoveredCharactersPercent(),
        totalPossible: characterInstancesData.filter((c) => {
          return c.characterType === "NPC";
        }).length,
        totalAchieved: getDiscoveredItemsAmount(
          characterInstancesData.filter((c) => {
            return c.characterType === "NPC";
          })
        ),
      });
    if (wonBattles && lostBattles) {
      if (wonBattles.length !== 0 || lostBattles.length !== 0) {
        setWinrateStat({
          itemName: "Win rate",
          percentAchieved: calculateWinrate(
            wonBattles.length,
            lostBattles.length
          ),
          totalAchieved: wonBattles.length,
          totalPossible: wonBattles.length + lostBattles.length,
        });
      } else {
        setWinrateStat({
          itemName: "Win rate",
          percentAchieved: 0,
          totalAchieved: 0,
          totalPossible: 0,
        });
      }
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

  return armorTemplatesData &&
    bootsTemplatesData &&
    consumableTemplatesData &&
    helmetTemplatesData &&
    shieldTemplatesData &&
    weaponTemplatesData &&
    spellTemplatesData &&
    characterInstancesData &&
    wonBattles &&
    lostBattles ? (
    <fieldset className="cursor-pointer border p-4 rounded-lg border-[var(--page-container-border)]">
      <legend className="text-xl font-semibold xl:font-black tracking-wide px-2">
        Campaign Stats
      </legend>
      <div className="grid grid-cols-10 items-center gap-2.5 lg:gap-2 xl:gap-3">
        <DiscoveredItemInfo {...discoveredStatArmors} />
        <DiscoveredItemInfo {...discoveredStatBoots} />
        <DiscoveredItemInfo {...discoveredStatConsumables} />
        <DiscoveredItemInfo {...discoveredStatHelmets} />
        <DiscoveredItemInfo {...discoveredStatShields} />
        <DiscoveredItemInfo {...discoveredStatWeapons} />
        <DiscoveredItemInfo {...discoveredStatSpells} />
        <DiscoveredItemInfo {...discoveredStatCharacters} />
        <DiscoveredItemInfo {...winrateStat} />
      </div>
    </fieldset>
  ) : (
    <Loader />
  );
};

export default CampaignStats;
