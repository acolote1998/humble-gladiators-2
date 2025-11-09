type StatBarType = {
  currentValue: number;
  maxValue: number;
  type: HpOrMana;
  barHeight: number;
  widthPercent: number;
};
type HpOrMana = "HP" | "MANA";
const StatBar = ({
  currentValue,
  maxValue,
  type,
  barHeight,
  widthPercent,
}: StatBarType) => {
  const currentHpPercent = () => {
    return Math.round((currentValue * 100) / maxValue);
  };
  return (
    <div
      title={`${type === "HP" ? "Health" : "Magic"} points`}
      style={{ height: `${barHeight}rem`, width: `${widthPercent}%` }}
      className="relative cursor-pointer select-none"
    >
      <div className="absolute z-3 h-full w-full flex items-center justify-center">
        <p className="text-[var(--light-text)] text-lg font-semibold tracking-widest">
          {currentValue}/{maxValue}
        </p>
      </div>
      <div
        style={{ width: `${currentHpPercent()}%` }}
        className={`absolute z-2 h-full rounded-xl border-2
            ${type === "HP" ? "bg-red-500 border-red-700" : "bg-blue-500 border-blue-700"}
            `}
      ></div>
      <div className="absolute z-1 bg-gray-700 h-full w-full rounded-xl"></div>
    </div>
  );
};

export default StatBar;
