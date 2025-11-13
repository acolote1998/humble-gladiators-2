import { useNavigate } from "@tanstack/react-router";
import type { CampaignDto } from "../../types/campaignTypes";
const CampaignInfo = ({
  id: campaignId,
  name,
  theme,
  coverImgBase64,
  cardBackImgBase64,
  campaignCreationState,
}: CampaignDto) => {
  const navigate = useNavigate();
  return (
    <>
      <div
        className={`
        flex
        justify-center
        gap-5
        bg-[var(--page-container-bg-darker)]
        border
        border-[var(--page-container-border)]
        p-6
        rounded-lg
        hover:scale-102
        transition-all
        duration-500
        ${campaignCreationState == "GAME_CREATED" ? "hover:bg-[var(--highlight-color)] hover:border-[var(--highlight-color-border)] cursor-pointer" : "hover:bg-[var(--unavailable-color)] hover:border-[var(--unavailable-color-border)] cursor-wait"}
        w-full
        h-auto
        `}
        onClick={() => {
          if (campaignCreationState == "GAME_CREATED")
            navigate({ to: `/campaign/${campaignId}#campaign` });
        }}
      >
        {campaignCreationState == "GAME_CREATED" &&
        cardBackImgBase64 &&
        coverImgBase64 ? (
          <div className="flex flex-col lg:flex-row gap-5">
            <div
              className="
              flex
              flex-col
              items-center
              bg-[var(--page-container-bg)]
              p-3
              rounded-lg
              border
              border-[var(--page-container-border)]
              text-lg
              w-auto
              "
            >
              <div className="flex items-center space-x-2">
                <p className="font-semibold text-lg lg:text-2xl lg:mb-2">
                  {name}
                </p>
                {/* Mobile card back */}
                {cardBackImgBase64 && (
                  <img
                    draggable={false}
                    className="lg:hidden h-17 w-auto rounded-md"
                    src={`data:image/jpeg;base64,${cardBackImgBase64}`}
                  />
                )}
              </div>

              <div className="hidden lg:grid lg:grid-cols-2 gap-6 text-center w-full">
                <div className="bg-[var(--page-container-bg-darker)] px-2 py-1 rounded-lg">
                  <p className="text-sm xl:text-xl">Wanted Themes</p>
                  <div className="flex flex-col items-center gap-1 overflow-y-auto  lg:min-h-36">
                    {theme.wantedThemes.map((theme, index) => {
                      return (
                        <p
                          className="bg-[var(--page-container-bg)] lg:text-xs xl:text-md tracking-tighter p-1 rounded-lg"
                          key={index}
                        >
                          {theme}
                        </p>
                      );
                    })}
                  </div>
                </div>
                <div className="bg-[var(--page-container-bg-darker)] px-2 py-1 rounded-lg">
                  <p className="text-sm xl:text-xl">Unwanted Themes</p>
                  <div className="flex flex-col items-center gap-1 overflow-y-auto  lg:min-h-36">
                    {theme.unwantedThemes.map((theme, index) => {
                      return (
                        <p
                          className="bg-[var(--page-container-bg)] lg:text-xs xl:text-md tracking-tighter p-1 rounded-lg"
                          key={index}
                        >
                          {theme}
                        </p>
                      );
                    })}
                  </div>
                </div>
              </div>
            </div>
            <div className="grid grid-cols-1 justify-center place-items-center">
              {coverImgBase64 && (
                <img
                  draggable={false}
                  className="h-38 sm:h-45 md:h-55 lg:h-60 rounded-md"
                  src={`data:image/jpeg;base64,${coverImgBase64}`}
                />
              )}
            </div>
            {/* Desktop back card */}
            {cardBackImgBase64 && (
              <img
                draggable={false}
                className="hidden lg:block lg:h-60 rounded-md"
                src={`data:image/jpeg;base64,${cardBackImgBase64}`}
              />
            )}
          </div>
        ) : (
          <div className="flex items-center gap-30 tracking-tighter hover:tracking-widest transition-all duration-1500">
            <h1 className="text-3xl lg:text-5xl font-semibold  opacity-30">
              {name}
            </h1>
            <p className="loader scale-300 opacity-30" />
          </div>
        )}
      </div>
    </>
  );
};

export default CampaignInfo;
