import { useState } from "react";
import { useCreateCampaign } from "../../hooks/useCampaigns";
import { useGetCreationCampaignState } from "../../hooks/useCampaigns";
import CreationProgressBar from "./CreationProgressBar";
import { PageContainer } from "../ui/PageContainer";
import type { BannedMessageType } from "@/types/bannedMessageType";
import type { AxiosError } from "axios";

const CreateCampaign = () => {
  const {
    mutate: createCampaign,
    isPending: isCreatePending,
    isError: isCreateError,
    error: campaignCreationError,
  } = useCreateCampaign();
  const { data: campaignCreationState } = useGetCreationCampaignState();
  const [campaignName, setCampaignName] = useState<string>("");
  const [wantedThemesInput, setWantedThemesInput] = useState<string>("");
  const [unwantedThemesInput, setUnwantedThemesInput] = useState<string>("");

  // Derive isCreating from existing states instead of managing separate state
  const isCreating =
    isCreatePending ||
    (campaignCreationState &&
      campaignCreationState !== "CAMPAIGN_NOT_FOUND" &&
      campaignCreationState !== "GAME_CREATED");

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!campaignName.trim()) return;

    const formatThemes = (themes: string) =>
      themes
        .split(",")
        .map((theme) => theme.trim())
        .filter((theme) => theme.length > 0);

    createCampaign({
      campaignName: campaignName.trim(),
      theme: {
        wantedThemes: formatThemes(wantedThemesInput),
        unwantedThemes: formatThemes(unwantedThemesInput),
      },
    });
  };

  return (
    <PageContainer>
      <div className="flex flex-col items-center gap-8 justify-center">
        <form
          className="w-full max-w-3xl bg-(--page-container-bg-darker) border border-(--page-container-border) rounded-2xl shadow-md px-10 py-8 flex flex-col gap-6"
          onSubmit={handleSubmit}
        >
          <div className="text-center">
            <h2 className="text-3xl font-semibold text-(--dark-text)">
              Launch a New Campaign
            </h2>
            <p className="text-sm text-(--dark-text) opacity-70 mt-2">
              Give your world a name and curate its themes. Separate multiple
              themes with commas.
            </p>
          </div>

          <label className="flex flex-col gap-2 text-(--dark-text)">
            <span className="text-lg font-semibold">Campaign Name</span>
            <input
              value={campaignName}
              onChange={(e) => setCampaignName(e.target.value)}
              disabled={isCreating}
              placeholder="E.g. Tales of the Sapphire Isles"
              className="bg-(--page-container-bg) border border-(--page-container-border) rounded-lg px-4 py-2 text-(--dark-text) focus:outline-none focus:ring-2 focus:ring-emerald-300 transition-shadow disabled:opacity-70"
              type="text"
              required
            />
          </label>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <label className="flex flex-col gap-2 text-(--dark-text)">
              <span className="text-lg font-semibold">Wanted Themes</span>
              <input
                value={wantedThemesInput}
                onChange={(e) => setWantedThemesInput(e.target.value)}
                disabled={isCreating}
                placeholder="mystic forest, clockwork cities"
                className="bg-(--page-container-bg) border border-(--page-container-border) rounded-lg px-4 py-2 text-(--dark-text) focus:outline-none focus:ring-2 focus:ring-emerald-300 transition-shadow disabled:opacity-70"
                type="text"
              />
              <span className="text-xs opacity-70">
                These themes will be favored when generating content.
              </span>
            </label>

            <label className="flex flex-col gap-2 text-(--dark-text)">
              <span className="text-lg font-semibold">Unwanted Themes</span>
              <input
                value={unwantedThemesInput}
                onChange={(e) => setUnwantedThemesInput(e.target.value)}
                disabled={isCreating}
                placeholder="space opera, high technology"
                className="bg-(--page-container-bg) border border-(--page-container-border) rounded-lg px-4 py-2 text-(--dark-text) focus:outline-none focus:ring-2 focus:ring-emerald-300 transition-shadow disabled:opacity-70"
                type="text"
              />
              <span className="text-xs opacity-70">
                Avoid these tones to keep your campaign focused.
              </span>
            </label>
          </div>

          {isCreateError ? (
            <div
              data-testid="banned-message"
              className="px-6 py-3 text-lg font-semibold rounded-md border border-(--unavailable-color-border) bg-(--unavailable-color) text-(--light-text) cursor-not-allowed select-none hover:scale-105 transition-all duration-2000 hover:bg-black hover:text-white hover:border-black text-center"
            >
              {(() => {
                const axiosError =
                  campaignCreationError as AxiosError<BannedMessageType>;
                const errorData = axiosError?.response?.data;
                return errorData?.banned ? (
                  <span>
                    You have been temporarily banned until{" "}
                    {new Date(errorData.bannedUntil).toLocaleString("en-GB", {
                      year: "numeric",
                      month: "short",
                      day: "numeric",
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </span>
                ) : (
                  "Error"
                );
              })()}
            </div>
          ) : (
            <button
              type="submit"
              disabled={isCreating}
              className={`px-6 py-3 text-lg font-semibold rounded-md border border-(--page-container-bg-darkerer) bg-(--page-container-border) text-(--light-text) transition-all ease-in-out duration-300 ${
                isCreating
                  ? "opacity-70 cursor-progress"
                  : "hover:bg-[var(--action-positive-bg)] hover:text-[var(--action-positive-foreground)] hover:scale-105 cursor-pointer"
              }`}
            >
              {isCreating ? "Creating..." : "Create Campaign"}
            </button>
          )}
        </form>
        {campaignCreationState !== "CAMPAIGN_NOT_FOUND" &&
          campaignCreationState && (
            <div className="w-full max-w-3xl mb-13 xl:mb-0">
              <CreationProgressBar creationState={campaignCreationState} />
            </div>
          )}
      </div>
    </PageContainer>
  );
};

export default CreateCampaign;
