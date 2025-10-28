import type { CampaignDto } from "../../types/campaignTypes";
const CampaignInfo = ({
  name,
  theme,
  coverImgBase64,
  cardBackImgBase64,
}: CampaignDto) => {
  return (
    <>
      <div
        className="
        flex
        justify-center
        gap-5
        bg-gray-300
        border
        border-gray-400
        p-6
        rounded-lg
        hover:scale-102
        transition-all
        duration-500
      hover:bg-yellow-200
      hover:border-yellow-400
        cursor-pointer
        w-270
        h-72
        "
      >
        <div
          className="
          flex
          flex-col
          items-center
        bg-gray-200
          p-3
          rounded-lg
          border
          border-gray-400
          text-lg
          w-113
          "
        >
          <p
            className="
            text-center
            font-semibold
            text-2xl
            mb-2
            "
          >
            {name}
          </p>
          <div className="grid grid-cols-2 gap-6 text-center h-43 w-full">
            <div className="bg-gray-300 px-2 py-1 rounded-lg">
              <p className="text-xl">Wanted Themes</p>
              <div className="flex flex-col items-center gap-1 overflow-y-auto h-36">
                {theme.wantedThemes.map((theme, index) => {
                  return (
                    <p
                      className="bg-gray-200 text-md tracking-tighter p-1 rounded-lg"
                      key={index}
                    >
                      {theme}
                    </p>
                  );
                })}
              </div>
            </div>
            <div className="bg-gray-300 px-2 py-1 rounded-lg">
              <p className="text-xl">Unwanted Themes</p>
              <div className="flex flex-col items-center gap-1 overflow-y-auto h-36">
                {theme.unwantedThemes.map((theme, index) => {
                  return (
                    <p
                      className="bg-gray-200 text-md tracking-tighter p-1 rounded-lg"
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
        <div>
          {coverImgBase64 && (
            <img
              draggable={false}
              className="h-60 rounded-md"
              src={`data:image/jpeg;base64,${coverImgBase64}`}
            />
          )}
        </div>
        <div>
          {cardBackImgBase64 && (
            <img
              draggable={false}
              className="h-60 rounded-md"
              src={`data:image/jpeg;base64,${cardBackImgBase64}`}
            />
          )}
        </div>
      </div>
    </>
  );
};

export default CampaignInfo;
