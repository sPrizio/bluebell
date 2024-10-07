"use client";

import React, {useState} from "react";
import {Sidebar, SidebarBody} from "../ui/sidebar";
import {motion} from "framer-motion";
import {cn} from "@/lib/utils";
import MainLogo from "@/components/Navigation/MainLogo";
import MobileLogo from "@/components/Navigation/MobileLogo";
import SidebarNavigationLink from "@/components/Navigation/SidebarNavigationLink";
import {usePathname} from "next/navigation";

/**
 * Sidebar navigation component
 *
 * @param variant color variant
 * @param links navigation links
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function SidebarNavigation(
  {
    variant = 'primary',
    links = []
  }
    : Readonly<{
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white' | 'transparent',
    links: Array<SidebarNavigationLinkType>
  }>
) {

  const [open, setOpen] = useState(false);
  const pathName = usePathname();

  //  RENDER

  return (
    <div
      className={cn("rounded-md flex flex-col md:flex-row bg-white w-full flex-1 max-w-7xl mx-auto border border-neutral-200 overflow-hidden h-screen")}>
      <Sidebar open={open} setOpen={setOpen}>
        <SidebarBody className="justify-between gap-10">
          <div className="flex flex-col flex-1 overflow-y-auto overflow-x-hidden">
            {open ? <Logo variant={variant}/> : <LogoIcon/>}
            <div className="mt-8 flex flex-col gap-2 mb-auto">
              {links && links.length && links.map((link, idx) => {
                  return (
                    <SidebarNavigationLink key={idx} label={link.label} href={link.href} icon={link.icon} open={open}
                                           animate={true} active={link.href === pathName}/>
                  );
                }
              )}
            </div>
            <div>
              <small>
                <strong className="text-primary">bluebell&nbsp;&copy;&nbsp;</strong>{!open ? <br/> : null}V 0.0.1
              </small>
            </div>
          </div>
        </SidebarBody>
      </Sidebar>
    </div>
  );
}
export const Logo = (
  {
    variant = 'primary',
  }
    : Readonly<{
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white' | 'transparent',
  }>
) => {
  return (
    <div className="flex space-x-2 items-center justify-center text-sm text-black py-1 relative z-20">
      <motion.span initial={{opacity: 0}} animate={{opacity: 1}} className="w-full">
        <div className="h-16 flex items-center">
          <MainLogo variant={variant}/>
        </div>
      </motion.span>
    </div>
  );
};
export const LogoIcon = (
  {
    variant = 'primary',
  }
    : Readonly<{
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white' | 'transparent',
  }>
) => {
  return (
    <div className="font-normal flex space-x-2 items-center text-sm text-black py-1 relative z-20">
      <motion.span initial={{opacity: 0}} animate={{opacity: 1}} className="w-full">
        <div className="h-16 flex items-center">
          <MobileLogo variant={variant} hasBackground={false}/>
        </div>
      </motion.span>
    </div>
  );
};