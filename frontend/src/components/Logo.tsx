import { useNavigate } from "@tanstack/react-router";
const Logo = () => {
  const navigate = useNavigate();
  return (
    <div
      className="flex items-center justify-center cursor-pointer"
      onClick={() => {
        navigate({ to: "/" });
      }}
    >
      <h1
        className="
          text-6xl
          tracking-tighter
          font-black
          text-center
          p-6
          rounded-b-2xl
          border-l-5
          border-r-5
          border-b-5
        border-gray-400 
        bg-gray-200"
      >
        Humble Gladiators 2
      </h1>
    </div>
  );
};

export default Logo;
