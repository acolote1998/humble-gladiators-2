import type { SVGProps } from "react";
const SvgComponent = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 24 24"
    {...props}
  >
    <path
      stroke="#000"
      strokeLinecap="round"
      strokeLinejoin="round"
      strokeWidth={2}
      d="M7 3v18M7 3l4 4M7 3 3 7m11-4h1m-1 6h3m-3 6h5m-5 6h7"
    />
  </svg>
);
export { SvgComponent as LevelIcon };
