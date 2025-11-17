import { PageContainer } from "@/components/ui/PageContainer";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import Autoplay from "embla-carousel-autoplay";
import {
  Carousel,
  CarouselContent,
  CarouselItem,
} from "@/components/ui/carousel";
import { CampaignIcon } from "@/components/icons/navbar/CampaignIcon";
import { CardIcon } from "@/components/icons/typeofcards/CardIcon";
import { BattleIcon } from "@/components/icons/navbar/BattleIcon";
import { SignInButton, SignedIn, SignedOut } from "@clerk/clerk-react";

export const Route = createFileRoute("/")({
  component: RouteComponent,
});

function RouteComponent() {
  const navigate = useNavigate();
  return (
    <PageContainer>
      <div className="flex flex-col lg:grid lg:grid-cols-5 lg:justify-items-center h-full justify-evenly">
        <div className="lg:col-span-2 text-center flex flex-col gap-4 lg:gap-0 lg:justify-evenly p-4 lg:p-12 w-full">
          <h1 className="lg:text-4xl font-semibold tracking-wider lg:py-8">
            A modern collectible card RPG built for the browser
          </h1>
          <h2 className="lg:text-3xl italic lg:mb-8">
            Every playthrough is unique
          </h2>
          <div className="flex flex-col items-center lg:gap-3 lg:text-2xl font-thin italic">
            <div className="flex gap-3">
              <CampaignIcon width={18} />
              <h4>Build your campaign</h4>
            </div>
            <div className="flex gap-3">
              <CardIcon width={18} />
              <h4>Collect cards</h4>
            </div>
            <div className="flex gap-3">
              <BattleIcon width={24} />
              <h4>Fight turn-based battles</h4>
            </div>
          </div>
          <div
            className={`
                opacity-100 cursor-pointer
                border-[var(--page-container-bg-darkerer)] 
                bg-[var(--page-container-border)] 
                text-[var(--light-text)]
                lg:mx-10
                lg:my-5
                px-2 lg:px-5
                py-2 lg:py-5
                lg:text-xl
                rounded-md
                font-semibold
                hover:text-[var(--dark-text)]
                hover:bg-[var(--action-positive-bg)] 
                hover:tracking-wider
                hover:scale-110
                transition-all
                ease-in-out
                duration-800
                `}
          >
            <SignedIn>
              <button
                className="cursor-pointer"
                data-testid="hero-creation-button"
                onClick={() => {
                  navigate({ to: "/campaign" });
                }}
              >
                Start your story now ⚔️
              </button>
            </SignedIn>
            <SignedOut>
              <SignInButton>
                <button
                  className="cursor-pointer"
                  data-testid="hero-creation-button"
                  onClick={() => {
                    navigate({ to: "/campaign" });
                  }}
                >
                  Start your story now ⚔️
                </button>
              </SignInButton>
            </SignedOut>
          </div>
        </div>
        <div className="col-span-3 flex items-center justify-center">
          <Carousel
            plugins={[
              Autoplay({
                delay: 4000,
              }),
            ]}
          >
            <CarouselContent>
              <CarouselItem className="flex justify-center items-center">
                <img
                  className="rounded-xl"
                  src="/index/carrousel/campaign_1.png"
                />
              </CarouselItem>
              <CarouselItem className="flex justify-center items-center">
                <img
                  className="rounded-xl"
                  src="/index/carrousel/campaign_2.png"
                />
              </CarouselItem>
              <CarouselItem className="flex justify-center items-center">
                <img
                  className="rounded-xl"
                  src="/index/carrousel/campaign_3.png"
                />
              </CarouselItem>
            </CarouselContent>
          </Carousel>
        </div>
      </div>
    </PageContainer>
  );
}
