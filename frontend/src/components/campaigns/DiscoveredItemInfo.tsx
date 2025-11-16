export type DiscoveredItemInfoType = {
  itemName: string;
  percentAchieved: number;
  totalAchieved: number;
  totalPossible: number;
};
export const DiscoveredItemInfo = ({
  itemName,
  percentAchieved: percentDiscovered,
  totalAchieved,
  totalPossible,
}: DiscoveredItemInfoType) => {
  return (
    <>
      <p className="col-span-4 lg:col-span-2 text-md lg:text-lg text-center lg:font-semibold">
        {itemName}
      </p>
      <div className="col-span-6 lg:col-span-8 relative bg-[var(--highlight-color)] border-[var(--highlight-color-border)] border rounded-md h-5 lg:h-10 group">
        <div
          className={`transition-all duration-500 rounded-md border-[var(--information-color-border)] border bg-[var(--information-color)] top-0 left-0 h-full flex items-center justify-center`}
          style={{ width: `${percentDiscovered}%` }}
        ></div>

        <p className="group-hover:hidden absolute left-1/2 -translate-x-1/2 font-bold text-lg top-1/2 -translate-y-1/2">
          {percentDiscovered}%
        </p>
        <p className="hidden group-hover:inline-block absolute left-1/2 -translate-x-1/2 font-bold text-lg top-1/2 -translate-y-1/2">
          {totalAchieved}/{totalPossible}
        </p>
      </div>
    </>
  );
};
