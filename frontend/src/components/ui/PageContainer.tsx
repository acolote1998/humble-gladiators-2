import type { ReactNode } from "react";
interface PageContainerProps {
  children: ReactNode;
  h?: string;
  minH?: string;
  id?: string;
  dataTestId?: string;
}
export const PageContainer = ({
  children,
  h,
  minH,
  id,
  dataTestId,
}: PageContainerProps) => {
  return (
    <div
      id={`${id ? id : ""}`}
      data-testid={`${dataTestId ? dataTestId : ""}`}
      className={`
            mx-10
          p-5
          rounded-2xl
          border-5
        border-[var(--page-container-border)] 
        bg-[var(--page-container-bg)]
        ${h ? `h-${h}` : ""}
        ${minH ? `min-h-${minH}` : ""}
        `}
    >
      {children}
    </div>
  );
};
