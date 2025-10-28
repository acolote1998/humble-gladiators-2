import { useNavigate } from "@tanstack/react-router";
type CreateHeroButtonType = { campaignId: number };
export const CreateHeroButton = ({ campaignId }: CreateHeroButtonType) => {
  const navigate = useNavigate();
  return (
    <div className="flex justify-center">
      <button
        className="
          border-gray-400 
          bg-gray-500 
          text-white
            m-20
            px-5
            py-5
            text-xl
            rounded-md
            font-semibold
            hover:text-black
            hover:bg-emerald-200
            hover:tracking-wider
            cursor-pointer
            hover:scale-110
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
