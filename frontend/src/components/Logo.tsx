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
        <img
          src={"/humble_gladiators_logo.png"}
          className="h-14 w-auto sm:h-18 md:h-25 md:w-60 lg:w-90 lg:h-30"
        ></img>
        {/* <h1
          className="
          text-xl
          xl:text-6xl
          tracking-tight
          xl:tracking-tighter
          xl:font-black
          font-semibold
          text-center
"
        >
          Humble Gladiators 2
        </h1> */}
      </PageContainer>
    </div>
  );
};

export default Logo;
