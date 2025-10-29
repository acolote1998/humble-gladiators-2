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
      <p className="col-span-2 text-lg text-center font-semibold">{itemName}</p>
      <div className="col-span-8 relative bg-yellow-100 border-yellow-600 border rounded-md h-10">
        <div
          className={`transition-all duration-500 rounded-md border-blue-500 border absolute bg-blue-300 top-0 left-0 h-full flex items-center justify-center`}
          style={{ width: `${percentDiscovered}%` }}
        >
          <p className="text-center font-black text-lg">{percentDiscovered}%</p>
        </div>
      </div>
    </>
  );
};
