import type { SVGProps } from "react";
const SvgComponent = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    className="si-glyph si-glyph-poker-2"
    viewBox="-0.5 0 17 17"
    {...props}
  >
    <title>{"620"}</title>
    <path
      fill="#434343"
      fillRule="evenodd"
      d="M10.494 1.01H4.412C3.082 1.01 2 1.107 2 2.454v13.101C2 16.903 3.082 17 4.412 17h6.082c1.33 0 2.412-.098 2.412-1.445V2.454c0-1.348-1.082-1.444-2.412-1.444ZM7.489 13.135 4.748 9.041l2.824-4.115 2.742 4.093-2.825 4.116Z"
      className="si-glyph-fill"
    />
  </svg>
);
export { SvgComponent as CardIcon };
