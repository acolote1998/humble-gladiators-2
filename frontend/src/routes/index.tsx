import { PageContainer } from "@/components/ui/PageContainer";
import { createFileRoute } from "@tanstack/react-router";
import Autoplay from "embla-carousel-autoplay";
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/components/ui/carousel";

export const Route = createFileRoute("/")({
  component: RouteComponent,
});

function RouteComponent() {
  return (
    <PageContainer vh="80">
      <div className="grid grid-cols-5 justify-items-center">
        <div className="col-span-2 w-full">asd</div>
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
                <img src="/index/carrousel/campaign_1.png" />
              </CarouselItem>
              <CarouselItem className="flex justify-center">
                <img src="/index/carrousel/campaign_2.png" />
              </CarouselItem>
              <CarouselItem className="flex justify-center">
                <img src="/index/carrousel/campaign_3.png" />
              </CarouselItem>
            </CarouselContent>
            <CarouselPrevious />
            <CarouselNext />
          </Carousel>
        </div>
      </div>
    </PageContainer>
  );
}
