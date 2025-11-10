type DiscoveredItemInfoType = {
  itemName: string;
  percentAchieved: number;
};
export const DiscoveredItemInfo = ({
  itemName,
  percentAchieved: percentDiscovered,
}: DiscoveredItemInfoType) => {
  return (
    <>
      <p className="col-span-4 xl:col-span-2 text-md xl:text-lg text-center xl:font-semibold">
        {itemName}
      </p>
      <div className="col-span-6 xl:col-span-8 relative bg-[var(--highlight-color)] border-[var(--highlight-color-border)] border rounded-md h-5 xl:h-10">
        <div
          className={`transition-all duration-500 rounded-md border-[var(--information-color-border)] border absolute bg-[var(--information-color)] top-0 left-0 h-full flex items-center justify-center`}
          style={{ width: `${percentDiscovered}%` }}
        >
          {percentDiscovered > 0 && (
            <p className="text-center font-black text-lg">
              {percentDiscovered}%
            </p>
          )}
        </div>
      </div>
    </>
  );
};
