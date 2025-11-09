import type { SVGProps } from "react";
const SvgComponent = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    className="icon"
    viewBox="0 0 1024 1024"
    {...props}
  >
    <path
      fill="#E1BEE7"
      d="M896 576c0-211.2-172.8-384-384-384S128 364.8 128 576v170.667h768V576z"
    />
    <path
      fill="#CE93D8"
      d="M832 576c0-177.067-142.933-320-320-320S192 398.933 192 576v106.667h640V576z"
    />
    <path
      fill="#E1BEE7"
      d="M768 576c0-140.8-115.2-256-256-256S256 435.2 256 576v106.667h512V576z"
    />
    <path
      fill="#CE93D8"
      d="M704 576c0-106.667-85.333-192-192-192s-192 85.333-192 192v106.667h384V576z"
    />
    <path
      fill="#E1BEE7"
      d="M640 569.6c0-70.4-57.6-128-128-128s-128 57.6-128 128v113.067h256V569.6z"
    />
    <path
      fill="#CE93D8"
      d="M576 576c0-36.267-27.733-64-64-64s-64 27.733-64 64v100.267h128V576z"
    />
    <path fill="#BF360C" d="M85.333 704h853.334v42.667H85.333z" />
    <path fill="#FF5722" d="M938.667 704H85.333L128 661.333h768z" />
    <path fill="#BF360C" d="M42.667 789.333h938.666V832H42.667z" />
    <path fill="#FF5722" d="M981.333 789.333H42.667l42.666-42.666h853.334z" />
  </svg>
);
export { SvgComponent as MagicalDefenseIcon };
