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
            rounded-2xl
            border-5
            border-[var(--page-container-border)] 
            bg-[var(--page-container-bg)]
            relative
            ${extraClasses ? extraClasses : ""}
        `}
    >
      {children}
    </div>
  );
};
