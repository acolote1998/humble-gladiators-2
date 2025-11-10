import { useNavigate } from "@tanstack/react-router";
type CreateHeroButtonType = { campaignId: number };
export const RedirectCreateHeroButton = ({
  campaignId,
}: CreateHeroButtonType) => {
  const navigate = useNavigate();
  return (
    <div className="flex justify-center" data-testid="navigate-to-create-hero">
      <button
        className="
        border-[var(--page-container-bg-darkerer)] 
        bg-[var(--page-container-border)] 
        text-[var(--light-text)]
        px-0.5
        xl:px-5
        py-5
        text-xl
        rounded-md
        font-semibold
        md:hover:text-[var(--dark-text)]
        md:hover:bg-[var(--action-positive-bg)] 
        md:hover:tracking-wider
        cursor-pointer
        md:hover:scale-110
        transition-all
        ease-in-out
        duration-800
            "
        onClick={() => {
          navigate({ to: `/campaign/${campaignId}/createHero` });
        }}
      >
        ⚔️ Forge Your Hero 🛡️
      </button>
    </div>
  );
};
