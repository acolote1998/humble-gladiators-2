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
    <PageContainer vh="80">
      <div className="grid grid-cols-5 justify-items-center">
        <div className="col-span-2 w-full text-center flex flex-col justify-evenly p-12">
          <h1 className="text-4xl font-semibold tracking-wider py-8">
            A modern collectible card RPG built for the browser
          </h1>
          <h2 className="text-3xl italic mb-8">Every playthrough is unique</h2>
          <div className="flex flex-col items-center gap-3 text-2xl font-thin italic">
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
                mx-10
                my-5
                px-5
                py-5
                text-xl
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
        <div className="col-span-3 ">
          <Carousel
            plugins={[
              Autoplay({
                delay: 4000,
              }),
            ]}
          >
            <CarouselContent>
              <CarouselItem className="flex justify-center">
                <img
                  className="rounded-xl"
                  src="/index/carrousel/campaign_1.png"
                />
              </CarouselItem>
              <CarouselItem className="flex justify-center">
                <img
                  className="rounded-xl"
                  src="/index/carrousel/campaign_2.png"
                />
              </CarouselItem>
              <CarouselItem className="flex justify-center">
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
