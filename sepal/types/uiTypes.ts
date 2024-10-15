interface PageInfoContext {
  pageTitle: string,
  pageSubtitle: string,
  pageIconCode: string,
  breadcrumbs: Array<AppLink>,
  setPageTitle: React.Dispatch<React.SetStateAction<string>>,
  setPageSubtitle: React.Dispatch<React.SetStateAction<string>>,
  setPageIconCode: React.Dispatch<React.SetStateAction<string>>,
  setBreadcrumbs: React.Dispatch<React.SetStateAction<Array<AppLink>>>,
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