import { cardCategoryImageClass, cardSizeClass } from "./util/CardSizes";

export const PunchCard = () => {
  return (
    <>
      <div
        data-testid="punch-card"
        className={`hover-zoom ${cardSizeClass}`}
        style={{
          backgroundImage: `url('/templates/punchCardTemplate.png')`,
        }}
      >
        {/* Category & name */}
        <div className="flex flex-col items-center mt-0.25 xl:mt-9">
          <img
            draggable={false}
            src={`/categories/punch.png`}
            className={`${cardCategoryImageClass}`}
          />
          <p
            title="DESCRIPTION"
            className="hidden xl:block text-sm opacity-80 text-center p-1 mt-17 px-7"
          >
            Punching the enemy with your bare hands
          </p>
        </div>
      </div>
    </>
  );
};
