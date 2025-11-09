import type { SVGProps } from "react";
const SvgComponent = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    aria-hidden="true"
    className="iconify iconify--emojione"
    viewBox="0 0 64 64"
    {...props}
  >
    <path
      fill="#ffe54d"
      d="M22 0c0 16.9-9.1 32-22 32 12.9 0 22 15.1 22 32 0-16.9 9.1-32 22-32-12.9 0-22-15.1-22-32"
    />
    <path
      fill="#6adbc6"
      d="M53 0c0 8.4-4.6 16-11 16 6.4 0 11 7.6 11 16 0-8.4 4.6-16 11-16-6.4 0-11-7.6-11-16"
    />
    <path
      fill="#ff73c0"
      d="M48 32c0 8.4-4.6 16-11 16 6.4 0 11 7.6 11 16 0-8.4 4.6-16 11-16-6.4 0-11-7.6-11-16"
    />
  </svg>
);
export { SvgComponent as MagicalDamageIcon };
