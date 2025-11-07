import { useNavigate } from "@tanstack/react-router";
import { PageContainer } from "./ui/PageContainer";
const Logo = () => {
  const navigate = useNavigate();
  return (
    <div
      className="flex items-center justify-center cursor-pointer"
      onClick={() => {
        navigate({ to: "/" });
      }}
    >
      <PageContainer>
        <h1
          className="
          text-6xl
          tracking-tighter
          font-black
          text-center
"
        >
          Humble Gladiators 2
        </h1>
      </PageContainer>
    </div>
  );
};

export default Logo;
