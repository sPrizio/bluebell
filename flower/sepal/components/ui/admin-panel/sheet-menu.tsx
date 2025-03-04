import Link from "next/link";
import {MenuIcon, PanelsTopLeft} from "lucide-react";

import {Button} from "@/components/ui/button";
import {Menu} from "@/components/ui/admin-panel/menu";
import {
  Sheet,
  SheetHeader,
  SheetContent,
  SheetTrigger,
  SheetTitle
} from "@/components/ui/sheet";
import MainLogo from "@/components/Navigation/MainLogo";

export function SheetMenu() {
  return (
    <Sheet>
      <SheetTrigger className="lg:hidden" asChild>
        <Button className="h-8" variant="outline" size="icon">
          <MenuIcon size={20}/>
        </Button>
      </SheetTrigger>
      <SheetContent className="sm:w-72 px-3 h-full flex flex-col" side="left">
        <SheetHeader>
          <Button
            className="flex justify-center items-center pb-2 pt-1"
            variant="link"
            asChild
          >
            <SheetTitle className="font-bold text-lg">
              <MainLogo variant={'primary'}/>
            </SheetTitle>
          </Button>
        </SheetHeader>
        <Menu isOpen/>
      </SheetContent>
    </Sheet>
  );
}
