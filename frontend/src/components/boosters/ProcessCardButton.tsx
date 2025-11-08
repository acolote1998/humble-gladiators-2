type ProcessCardButtonProps = {
  onClickCallback: () => void;
  buttonText: string;
  isBeingProcessed: boolean;
};

const ProcessCardButton = ({
  buttonText,
  onClickCallback,
  isBeingProcessed,
}: ProcessCardButtonProps) => {
  return (
    <p
      onClick={() => {
        onClickCallback();
      }}
      className={`${isBeingProcessed ? "opacity-30 cursor-progress" : "opacity-100 cursor-pointer"} bg-[var(--information-color)] text-[var(--light-text)] text-center p-2 m-2 rounded-xl select-none transition-all duration-300`}
    >
      {buttonText}
    </p>
  );
};

export default ProcessCardButton;
