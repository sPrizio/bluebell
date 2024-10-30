import {AggregateInterval} from "@/lib/enums";

interface PageInfoContext {
  pageTitle: string,
  pageSubtitle: string,
  pageIconCode: string,
  breadcrumbs: Array<AppLink>,
  user: User
  setPageTitle: React.Dispatch<React.SetStateAction<string>>,
  setPageSubtitle: React.Dispatch<React.SetStateAction<string>>,
  setPageIconCode: React.Dispatch<React.SetStateAction<string>>,
  setBreadcrumbs: React.Dispatch<React.SetStateAction<Array<AppLink>>>,
  setUser: React.Dispatch<React.SetStateAction<User>>,
}

interface SidebarNavigationLinkType {
  label: string,
  href: string,
  icon: React.ReactNode
}

interface ModalContext {
  open: boolean,
  setOpen: React.Dispatch<React.SetStateAction<boolean>>
}

interface AppLink {
  href: string,
  label: string,
  active: boolean
}

interface UserTradeRecordControlSelection {
  aggInterval: AggregateInterval,
  month: string,
  year: string,
}