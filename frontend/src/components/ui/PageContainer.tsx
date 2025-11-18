import type { ReactNode } from "react";
interface PageContainerProps {
  children: ReactNode;
  vh?: string;
  minH?: string;
  maxH?: string;
  id?: string;
  dataTestId?: string;
  extraClasses?: string;
}
export const PageContainer = ({
  children,
  vh,
  minH,
  id,
  dataTestId,
  extraClasses,
  maxH,
}: PageContainerProps) => {
  return (
    <div
      id={`${id ? id : ""}`}
      data-testid={`${dataTestId ? dataTestId : ""}`}
      style={{
        height: `${vh ? `${vh}vh` : "auto"}`,
        minHeight: `${minH ? `${minH}vh` : "auto"}`,
        maxHeight: `${maxH ? `${maxH}vh` : "none"}`,
      }}
      className={`
            mx-2 xl:mx-10
            p-2.5 xl:p-5
            rounded-lg
            border-5
            border-[var(--page-container-border)] 
            bg-[var(--page-container-bg)]
            relative
            ${extraClasses ? extraClasses : ""}
        `}
    >
      <>
        {/* For debugging UI responsiveness */}
        {/* <p
          className="absolute top-0 left-0 bg-gray-300 text-xs block
    sm:hidden md:hidden lg:hidden xl:hidden 2xl:hidden"
        >
          DEFAULT
        </p>

        <p
          className="absolute top-0 left-0 bg-red-300 text-xs hidden
    sm:block md:hidden lg:hidden xl:hidden 2xl:hidden"
        >
          SMALL
        </p>

        <p
          className="absolute top-0 left-0 bg-yellow-300 text-xs hidden
    sm:hidden md:block lg:hidden xl:hidden 2xl:hidden"
        >
          MEDIUM
        </p>

        <p
          className="absolute top-0 left-0 bg-green-300 text-xs hidden
    sm:hidden md:hidden lg:block xl:hidden 2xl:hidden"
        >
          LARGE
        </p>

        <p
          className="absolute top-0 left-0 bg-blue-300 text-xs hidden
    sm:hidden md:hidden lg:hidden xl:block 2xl:hidden"
        >
          XL
        </p>

        <p
          className="absolute top-0 left-0 bg-purple-300 text-xs hidden
    sm:hidden md:hidden lg:hidden xl:hidden 2xl:block"
        >
          2XL
        </p> */}
      </>

      {children}
    </div>
  );
};
