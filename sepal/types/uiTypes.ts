interface PageInfoContext {
  pageTitle: string,
  pageSubtitle: string,
  pageIconCode: string,
  setPageTitle: React.Dispatch<React.SetStateAction<string>>,
  setPageSubtitle: React.Dispatch<React.SetStateAction<string>>,
  setPageIconCode: React.Dispatch<React.SetStateAction<string>>,
}

interface SidebarNavigationLinkType {
  label: string,
  href: string,
  icon: React.ReactNode
}