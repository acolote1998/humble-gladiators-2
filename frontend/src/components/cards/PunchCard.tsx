export const PunchCard = () => {
  return (
    <>
      <div
        className={`hover-zoom relative my-5 w-85 h-119 bg-cover bg-no-repeat p-2 select-none cursor-pointer`}
        style={{
          backgroundImage: `url('/templates/punchCardTemplate.png')`,
        }}
      >
        {/* Category & name */}
        <div className="flex flex-col items-center mt-9">
          <img
            draggable={false}
            src={`/categories/punch.png`}
            className="w-65.5 h-auto"
          />
          <p
            title="DESCRIPTION"
            className="text-sm opacity-80 text-center p-1 mt-17 px-7"
          >
            Punching the enemy with your bare hands
          </p>
        </div>
      </div>
    </>
  );
};
