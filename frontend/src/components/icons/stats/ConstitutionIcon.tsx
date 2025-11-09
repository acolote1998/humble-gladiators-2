import type { SVGProps } from "react";
const SvgComponent = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    className="icon flat-line"
    data-name="Flat Line"
    viewBox="0 0 24 24"
    {...props}
  >
    <path
      d="M18 15a6 6 0 0 1-12 0c0-4 2-5 6-12 4 7 6 8 6 12Z"
      style={{
        fill: "red",
        strokeWidth: 2,
      }}
    />
    <path
      d="M12 13v4m2-2h-4m2-12c-4 7-6 8-6 12a6 6 0 0 0 12 0c0-4-2-5-6-12Z"
      style={{
        fill: "none",
        stroke: "#c70000",
        strokeLinecap: "round",
        strokeLinejoin: "round",
        strokeWidth: 2,
      }}
    />
  </svg>
);
export { SvgComponent as ConstitutionIcon };
