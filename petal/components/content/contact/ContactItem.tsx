/**
 * Renders the contact item. Like an address or phone number
 *
 * @param icon icon
 * @param text text
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ContactItem(
  {
    icon = undefined,
    text = '',
  }
    : Readonly<{
    icon: React.ReactNode,
    text: string
  }>
) {


  //  RENDER

  return (
    <div className="flex w-full items-center [&:not(:last-child)]:mb-5">
      <div className="mr-6 text-primary p-3 border-2 border-primary rounded-full text-sm">{icon}</div>
      <div className="flex-grow">{text}</div>
    </div>
  )
}