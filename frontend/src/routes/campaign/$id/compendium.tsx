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
import { NpcIcon } from "../../../components/icons/typeofcards/NpcIcon";
import { ArmorIcon } from "../../../components/icons/typeofcards/ArmorIcon";
import { BootIcon } from "../../../components/icons/typeofcards/BootIcon";
import { ConsumableIcon } from "../../../components/icons/typeofcards/ConsumableIcon";
import { HelmetIcon } from "../../../components/icons/typeofcards/HelmetIcon";
import { ShieldIcon } from "../../../components/icons/typeofcards/ShieldIcon";
import { SpellIcon } from "../../../components/icons/typeofcards/SpellIcon";
import { WeaponIcon } from "../../../components/icons/typeofcards/WeaponIcon";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

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
      <div
        className="
          mx-5
          p-5
          rounded-b-2xl
          border-3
        border-gray-400 
        bg-gray-200
        "
      >
        <Tabs>
          <TabsList className="bg-gray-300">
            <TabsTrigger value="npcs" className="flex justify-start gap-3">
              <NpcIcon width={32} />
              <p className="text-2xl">NPC's</p>
            </TabsTrigger>
            <TabsTrigger value="armors" className="flex justify-start gap-3">
              <ArmorIcon width={32} />
              <p className="text-2xl">Armors</p>
            </TabsTrigger>
            <TabsTrigger value="boots" className="flex justify-start gap-3">
              <BootIcon width={32} />
              <p className="text-2xl">Boots</p>
            </TabsTrigger>
            <TabsTrigger
              value="consumables"
              className="flex justify-start gap-3"
            >
              <ConsumableIcon width={32} />
              <p className="text-2xl">Consumables</p>
            </TabsTrigger>
            <TabsTrigger value="helmets" className="flex justify-start gap-3">
              <HelmetIcon width={32} />
              <p className="text-2xl">Helmets</p>
            </TabsTrigger>
            <TabsTrigger value="shields" className="flex justify-start gap-3">
              <ShieldIcon width={32} />
              <p className="text-2xl">Shields</p>
            </TabsTrigger>
            <TabsTrigger value="spells" className="flex justify-start gap-3">
              <SpellIcon width={32} />
              <p className="text-2xl">Spells</p>
            </TabsTrigger>
            <TabsTrigger value="weapons" className="flex justify-start gap-3">
              <WeaponIcon width={32} />
              <p className="text-2xl">Weapons</p>
            </TabsTrigger>
          </TabsList>
          <TabsContent value="npcs" className="grid grid-cols-5">
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
          </TabsContent>
          <TabsContent value="armors" className="grid grid-cols-5">
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
          </TabsContent>
          <TabsContent value="boots" className="grid grid-cols-5">
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
          </TabsContent>
          <TabsContent value="consumables" className="grid grid-cols-5">
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
          </TabsContent>
          <TabsContent value="helmets" className="grid grid-cols-5">
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
          </TabsContent>
          <TabsContent value="shields" className="grid grid-cols-5">
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
          </TabsContent>
          <TabsContent value="spells" className="grid grid-cols-5">
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
          </TabsContent>
          <TabsContent value="weapons" className="grid grid-cols-5">
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
          </TabsContent>
        </Tabs>
      </div>
    </>
  );
}
