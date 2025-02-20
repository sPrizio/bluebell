import Link from "next/link";

/**
 * Renders a social icon within the contact us form
 *
 * @param icon icon
 * @param url link url
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ContactSocial(
  {
    icon = undefined,
    url = ''
  }
    : Readonly<{
    icon: React.ReactNode,
    url: string
  }>
) {


  //  RENDER

  return (
    <div className="[&:not(:last-child)]:mr-4">
      <Link href={url}>
        {icon}
      </Link>
    </div>
  )
}