import {resolveIcon} from "@/lib/services";
import {DEFAULT_PAGE_HEADER_SECTION_ICON_SIZE} from "@/lib/constants";
import Breadcrumbs from "@/components/Navigation/Breadcrumb/Breadcrumbs";

/**
 * A generic page header section
 *
 * @param iconCode icon
 * @param title header title
 * @param subtitle head subtitle
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function PageHeaderSection(
  {
    iconCode = '',
    title = '',
    subtitle = '',
  }: Readonly<{
    iconCode?: string,
    title: string,
    subtitle?: string,
  }>
) {


  //  RENDER

  return (
    <div className={'flex items-center w-full pb-12'}>
      <div className={'mr-6 bg-primary text-white p-4 rounded-lg'}>
        {resolveIcon(iconCode, undefined, DEFAULT_PAGE_HEADER_SECTION_ICON_SIZE)}
      </div>
      <div className={'mr-6'}>
        <div className={'text-slate-700 text-2xl font-bold'}>{title}</div>
        <div className={'text-slate-700 text-md'}>{subtitle}</div>
      </div>
      <div className={'flex flex-1 justify-end'}>
        <Breadcrumbs />
      </div>
    </div>
  )
}