"use client";
import { Menu } from "@/components/ui/admin-panel/menu";
import { SidebarToggle } from "@/components/ui/admin-panel/sidebar-toggle";
import { Button } from "@/components/ui/button";
import { useSidebar } from "@/lib/hooks/ui/use-sidebar";
import { useStore } from "@/lib/hooks/ui/use-store";
import { cn } from "@/lib/utils";
import MainLogo from "@/components/Navigation/MainLogo";
import MobileLogo from "@/components/Navigation/MobileLogo";

/**
 * Renders the side bar for sepal logos
 *
 * @author Stephen Prizio
 * @version 0.2.6
 */
export function SepalSidebar() {
  const sidebar = useStore(useSidebar, (x) => x);
  if (!sidebar) return null;
  const { isOpen, toggleOpen, getOpenState, setIsHover, settings } = sidebar;

  return (
    <aside
      className={cn(
        "bg-white fixed top-0 left-0 z-20 h-screen -translate-x-full lg:translate-x-0 transition-[width] ease-in-out duration-300",
        !getOpenState() ? "w-[90px]" : "w-72",
        settings.disabled && "hidden",
      )}
    >
      <SidebarToggle isOpen={isOpen} setIsOpen={toggleOpen} />
      <div
        onMouseEnter={() => setIsHover(true)}
        onMouseLeave={() => setIsHover(false)}
        className="relative h-full flex flex-col px-3 py-4 overflow-y-auto shadow-md"
      >
        <Button
          className={cn(
            "transition-transform ease-in-out duration-300",
            !getOpenState() ? "translate-x-1" : "translate-x-0",
          )}
          variant="link"
          asChild
        >
          <div className="flex items-center gap-2 pt-8">
            {!isOpen ? (
              <MobileLogo variant={"primary"} />
            ) : (
              <MainLogo variant={"primary"} />
            )}
          </div>
        </Button>
        <Menu isOpen={getOpenState()} />
      </div>
    </aside>
  );
}
