import type { ArmorType } from "@/types/armorTypes";
import type { BootsType } from "@/types/bootsTypes";
import type { CharacterInstanceType } from "@/types/characterTypes";
import type { ConsumableType } from "@/types/consumablesTypes";
import type { HelmetType } from "@/types/helmetTypes";
import type { ShieldType } from "@/types/shieldTypes";
import type { SpellType } from "@/types/spellTypes";
import type { WeaponType } from "@/types/weaponTypes";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ArmorIcon } from "../icons/typeofcards/ArmorIcon";
import { BootIcon } from "../icons/typeofcards/BootIcon";
import { ConsumableIcon } from "../icons/typeofcards/ConsumableIcon";
import { HelmetIcon } from "../icons/typeofcards/HelmetIcon";
import { ShieldIcon } from "../icons/typeofcards/ShieldIcon";
import { SpellIcon } from "../icons/typeofcards/SpellIcon";
import { WeaponIcon } from "../icons/typeofcards/WeaponIcon";
import { ArmorCard } from "../cards/ArmorCard";
import { BootsCard } from "../cards/BootsCard";
import { ConsumableCard } from "../cards/ConsumableCard";
import { HelmetCard } from "../cards/HelmetCard";
import { ShieldCard } from "../cards/ShieldCard";
import { SpellCard } from "../cards/SpellCard";
import { WeaponCard } from "../cards/WeaponCard";
import { NpcIcon } from "../icons/typeofcards/NpcIcon";
import { CharacterCard } from "../cards/CharacterCard";

type CardsDisplayerType = {
  renderingThisDisplayFrom: RenderingFromEnum;
  armors?: ArmorType[];
  boots?: BootsType[];
  consumables?: ConsumableType[];
  helmets?: HelmetType[];
  weapons?: WeaponType[];
  spells?: SpellType[];
  shields?: ShieldType[];
  characters?: CharacterInstanceType[];
};
type RenderingFromEnum = "INVENTORY" | "COMPENDIUM";

const CardsDisplayer = ({
  renderingThisDisplayFrom,
  armors,
  boots,
  characters,
  consumables,
  helmets,
  shields,
  spells,
  weapons,
}: CardsDisplayerType) => {
  const getDefaultTab = () => {
    if (characters && characters.length > 0) {
      return "npcs";
    } else if (armors && armors.length > 0) {
      return "armors";
    } else if (boots && boots.length > 0) {
      return "boots";
    } else if (consumables && consumables.length > 0) {
      return "consumables";
    } else if (helmets && helmets.length > 0) {
      return "helmets";
    } else if (shields && shields.length > 0) {
      return "shields";
    } else if (spells && spells.length > 0) {
      return "spells";
    } else if (weapons && weapons.length > 0) {
      return "weapons";
    }
    return undefined;
  };

  return (
    <Tabs defaultValue={`${getDefaultTab()}`} id="card-displayer">
      <TabsList className="bg-[var(--page-container-bg-darker)] flex items-center w-full">
        {characters && characters.length > 0 && (
          <TabsTrigger value="npcs" className="flex justify-center gap-3">
            <NpcIcon width={32} />
            <p className="hidden xl:block text-2xl">NPC's</p>
          </TabsTrigger>
        )}
        {armors && armors.length > 0 && (
          <TabsTrigger value="armors" className="flex justify-center gap-3">
            <ArmorIcon width={32} />
            <p className="hidden xl:block text-2xl">Armors</p>
          </TabsTrigger>
        )}
        {boots && boots.length > 0 && (
          <TabsTrigger value="boots" className="flex justify-center gap-3">
            <BootIcon width={32} />
            <p className="hidden xl:block text-2xl">Boots</p>
          </TabsTrigger>
        )}
        {consumables && consumables.length > 0 && (
          <TabsTrigger
            value="consumables"
            className="flex justify-center gap-3"
          >
            <ConsumableIcon width={32} />
            <p className="hidden xl:block text-2xl">Consumables</p>
          </TabsTrigger>
        )}
        {helmets && helmets.length > 0 && (
          <TabsTrigger value="helmets" className="flex justify-center gap-3">
            <HelmetIcon width={32} />
            <p className="hidden xl:block text-2xl">Helmets</p>
          </TabsTrigger>
        )}
        {shields && shields.length > 0 && (
          <TabsTrigger value="shields" className="flex justify-center gap-3">
            <ShieldIcon width={32} />
            <p className="hidden xl:block text-2xl">Shields</p>
          </TabsTrigger>
        )}
        {spells && spells.length > 0 && (
          <TabsTrigger value="spells" className="flex justify-center gap-3">
            <SpellIcon width={32} />
            <p className="hidden xl:block text-2xl">Spells</p>
          </TabsTrigger>
        )}
        {weapons && weapons.length > 0 && (
          <TabsTrigger value="weapons" className="flex justify-center gap-3">
            <WeaponIcon width={32} />
            <p className="hidden xl:block text-2xl">Weapons</p>
          </TabsTrigger>
        )}
      </TabsList>
      {characters && characters.length > 0 && (
        <TabsContent value="npcs" className="grid grid-cols-4 2xl:grid-cols-5">
          {characters
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
                    renderingFrom={renderingThisDisplayFrom}
                  />
                )
            )}
        </TabsContent>
      )}
      {armors && armors.length > 0 && (
        <TabsContent
          value="armors"
          className="grid grid-cols-4 2xl:grid-cols-5"
        >
          {armors.map((armor) => (
            <ArmorCard
              key={armor.name + armor.id}
              {...armor}
              renderingFrom={renderingThisDisplayFrom}
            />
          ))}
        </TabsContent>
      )}
      {boots && boots.length > 0 && (
        <TabsContent value="boots" className="grid grid-cols-4 2xl:grid-cols-5">
          {boots.map((boot) => (
            <BootsCard
              key={boot.name + boot.id}
              {...boot}
              renderingFrom={renderingThisDisplayFrom}
            />
          ))}
        </TabsContent>
      )}
      {consumables && consumables.length > 0 && (
        <TabsContent
          value="consumables"
          className="grid grid-cols-4 2xl:grid-cols-5"
        >
          {consumables.map((consumable) => (
            <ConsumableCard
              key={consumable.name + consumable.id}
              {...consumable}
              renderingFrom={renderingThisDisplayFrom}
            />
          ))}
        </TabsContent>
      )}
      {helmets && helmets.length > 0 && (
        <TabsContent
          value="helmets"
          className="grid grid-cols-4 2xl:grid-cols-5"
        >
          {helmets.map((helmet) => (
            <HelmetCard
              key={helmet.name + helmet.id}
              {...helmet}
              renderingFrom={renderingThisDisplayFrom}
            />
          ))}
        </TabsContent>
      )}
      {shields && shields.length > 0 && (
        <TabsContent
          value="shields"
          className="grid grid-cols-4 2xl:grid-cols-5"
        >
          {shields.map((shield) => (
            <ShieldCard
              key={shield.name + shield.id}
              {...shield}
              renderingFrom={renderingThisDisplayFrom}
            />
          ))}
        </TabsContent>
      )}
      {spells && spells.length > 0 && (
        <TabsContent
          value="spells"
          className="grid grid-cols-4 2xl:grid-cols-5"
        >
          {spells.map((spell) => (
            <SpellCard
              key={spell.name + spell.id}
              {...spell}
              renderingFrom={renderingThisDisplayFrom}
            />
          ))}
        </TabsContent>
      )}
      {weapons && weapons.length > 0 && (
        <TabsContent
          value="weapons"
          className="grid grid-cols-4 2xl:grid-cols-5"
        >
          {weapons.map((weapon) => (
            <WeaponCard
              key={weapon.name + weapon.id}
              {...weapon}
              renderingFrom={renderingThisDisplayFrom}
            />
          ))}
        </TabsContent>
      )}
    </Tabs>
  );
};

export default CardsDisplayer;
