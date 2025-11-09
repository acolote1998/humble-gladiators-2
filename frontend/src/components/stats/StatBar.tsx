type StatBarType = {
  currentValue: number;
  maxValue: number;
  type: HpOrMana;
};
type HpOrMana = "HP" | "MANA";
const StatBar = ({ currentValue, maxValue, type }: StatBarType) => {
  const currentHpPercent = () => {
    return Math.round((currentValue * 100) / maxValue);
  };
  return (
    <div className="w-full relative h-10">
      <div className="absolute z-3 h-full w-full flex items-center justify-center">
        <p className="text-[var(--light-text)] text-lg font-semibold tracking-widest">
          {currentValue}/{maxValue}
        </p>
      </div>
      <div
        style={{ width: `${currentHpPercent()}%` }}
        className={`absolute z-2 h-full rounded-xl
            ${type === "HP" ? "bg-red-500" : "bg-blue-500"}
            `}
      ></div>
      <div className="absolute z-1 bg-gray-700 h-full w-full rounded-xl"></div>
    </div>
  );
};

export default StatBar;
