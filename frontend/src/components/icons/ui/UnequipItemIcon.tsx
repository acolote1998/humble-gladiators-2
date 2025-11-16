import type { SVGProps } from "react";
const SvgComponent = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 16 16"
    {...props}
  >
    <g fill="#000">
      <path d="M10 0 9 1l2.293 2.293-5 5 1.414 1.414 5-5L15 7l1-1V0h-6Z" />
      <path d="M1 2h5v2H3v9h9v-3h2v5H1V2Z" />
    </g>
  </svg>
);
export { SvgComponent as UnequipItemIcon };
