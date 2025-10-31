import type { SVGProps } from "react";
const SvgComponent = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 32 32"
    {...props}
  >
    <path fill="#668077" d="M13 9h14V5H13v4Zm14-8v4H7v4S3 9 3 5s4-4 4-4h20Z" />
    <path fill="#FFE6EA" d="M13 9v8l-3-2-3 2V5h6v4Z" />
    <path
      fill="#FFC44D"
      fillRule="evenodd"
      d="M29 10v20s0 1-1 1H4s-1 0-1-1V5c0 4 4 4 4 4v8l3-2 3 2V9h15s1 0 1 1Z"
      clipRule="evenodd"
    />
    <path
      stroke="#000"
      strokeLinecap="round"
      strokeLinejoin="round"
      strokeWidth={2}
      d="M13 9h15s1 0 1 1v20s0 1-1 1H4s-1 0-1-1V5m0 0c0-4 4-4 4-4h22M3 5c0 4 4 4 4 4m0-4v12l3-2 3 2V5h14"
    />
  </svg>
);
export { SvgComponent as CompendiumIcon };
