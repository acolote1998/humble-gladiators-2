import { createFileRoute, useParams } from "@tanstack/react-router";
import { useGetHeroExistence } from "../../../hooks/useCharacters";
import { RedirectCreateHeroButton } from "../../../components/campaigns/RedirectCreateHeroButton";
import { Loader } from "../../../components/Loader";
import { useGetCampaignByIdForAUser } from "../../../hooks/useCampaigns";
import CampaignStats from "../../../components/campaigns/CampaignStats";
import { PageContainer } from "@/components/ui/PageContainer";
export const Route = createFileRoute("/campaign/$id/")({
  component: RouteComponent,
});

function RouteComponent() {
  const { id: campaignId } = useParams({ from: "/campaign/$id/" });
  const { data: doesHeroExist, isLoading: doesHeroExistLoading } =
    useGetHeroExistence(Number(campaignId));
  const { data: campaignData, isLoading: isCampaignLoading } =
    useGetCampaignByIdForAUser(Number(campaignId));

  return (
    <>
      <PageContainer>
        {doesHeroExistLoading || isCampaignLoading ? (
          <Loader />
        ) : (
          campaignData &&
          campaignData.coverImgBase64 && (
            <div
              className="lg:grid lg:grid-cols-5 xl:p-5 justify-items-center place-items-center"
              id="campaign"
            >
              <img
                draggable={false}
                className={`${!doesHeroExist ? ` md:block` : `block`} h-[22vh] sm:h-[45vh] md:h-[60vh] xl:h-[74vh] rounded-md border-2 col-span-3`}
                src={`data:image/jpeg;base64,${campaignData.coverImgBase64}`}
              />
              <div className="lg:col-span-2">
                {doesHeroExistLoading ? (
                  <Loader />
                ) : doesHeroExist ? (
                  <>
                    <h1 className="text-4xl text-center font-bold tracking-wide py-6">
                      {campaignData.name}
                    </h1>
                    <CampaignStats />
                  </>
                ) : (
                  <RedirectCreateHeroButton campaignId={Number(campaignId)} />
                )}
              </div>
            </div>
          )
        )}
      </PageContainer>
    </>
  );
}
