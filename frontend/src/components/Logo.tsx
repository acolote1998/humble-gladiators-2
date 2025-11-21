import { useNavigate } from "@tanstack/react-router";
import { PageContainer } from "./ui/PageContainer";
const Logo = () => {
  const navigate = useNavigate();
  return (
    <PageContainer>
      <div
        data-testid="humble-gladiators-logo"
        onClick={() => {
          navigate({ to: "/" });
        }}
        className="flex justify-center cursor-pointer"
      >
        <img
          src={"/humble_gladiators_logo.png"}
          className="h-auto w-1/2 md:w-1/3 lg:w-1/4 xl:w-1/5 2xl:w-1/6"
        ></img>
      </div>
    </PageContainer>
  );
};

export default Logo;
