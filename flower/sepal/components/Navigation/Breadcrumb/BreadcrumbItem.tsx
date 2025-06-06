import Link from "next/link";

/**
 * An individual breadcrumb item
 *
 * @param label text
 * @param isLast if last, don't render a /
 * @param active if active, highlight
 * @param href link address
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function BreadcrumbItem({
  label = "",
  isLast = false,
  active = false,
  href = "#",
}: Readonly<{
  label: string;
  isLast?: boolean;
  active?: boolean;
  href?: string;
}>) {
  const size = " text-xs lg:text-sm ";

  //  RENDER

  const html = (
    <Link
      href={href?.toLowerCase() === "/home" ? "/dashboard" : href}
      className={"inline-block [&:not(:first-child)]:ml-4 font-medium"}
    >
      <span
        className={
          size +
          (!isLast ? "mr-4" : "") +
          (active ? " text-primary font-bold " : "")
        }
      >
        {label}
      </span>
      {!isLast ? <span className={size}>/</span> : null}
    </Link>
  );

  if (active) {
    return (
      <div className={"inline-block [&:not(:first-child)]:ml-4 font-medium"}>
        <span
          className={
            size +
            (!isLast ? "mr-4" : "") +
            (active ? " text-primary font-bold " : "")
          }
        >
          {label}
        </span>
        {!isLast ? <span className={size}>/</span> : null}
      </div>
    );
  } else {
    return html;
  }
}
