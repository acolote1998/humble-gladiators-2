import type { SVGProps } from "react";
const SvgComponent = (props: SVGProps<SVGSVGElement>) => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" {...props}>
    <path
      fill="#7f8c8d"
      d="M3 3v10c0 4.2 3.632 8 9 10 5.368-2 9-5.8 9-10V3H3z"
    />
    <path
      fill="#bdc3c7"
      d="M3 2v10c0 4.2 3.632 8 9 10 5.368-2 9-5.8 9-10V2H3z"
    />
    <path fill="#95a5a6" d="M3 2v10c0 4.2 3.632 8 9 10V2H3z" />
    <path
      fill="#ecf0f1"
      d="M5 4v8c0 3.4 2.825 6.4 7 8 4.175-1.6 7-4.6 7-8V4H5z"
    />
    <path fill="#2980b9" d="M5 12c0 3.4 2.825 6.4 7 8v-8H5z" />
    <path fill="#3498db" d="M12 4h7v8h-7z" />
    <path fill="#bdc3c7" d="M5 4h7v8H5z" />
  </svg>
);
export { SvgComponent as ShieldIcon };
