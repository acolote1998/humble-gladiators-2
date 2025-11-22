import type { SVGProps } from "react";
const SvgComponent = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    xmlSpace="preserve"
    viewBox="0 0 512 512"
    {...props}
  >
    <path
      d="M0 0h512v512H0z"
      style={{
        fill: "#32bea6",
      }}
    />
    <path
      d="m203.728 392.144-99.216-86.752 21.072-24.096 74.56 65.2 183.632-220.368 24.592 20.512z"
      style={{
        fill: "#fff",
      }}
    />
  </svg>
);
export { SvgComponent as GreenTickIcon };
