"use client";

import Link from "next/link";
import { Button } from "@/components/ui/button";
import { IconMeteorFilled } from "@tabler/icons-react";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";

/**
 * The generic error page
 *
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function Error() {
  //  RENDER

  return (
    <div className={"min-h-[90vh] flex items-center"}>
      <div
        className={
          "h-full w-1/2 flex items-center justify-center align-middle m-auto"
        }
      >
        <div className={"grid grid-cols-2 gap-6 items-center justify-center"}>
          <div className={"flex justify-center"}>
            {resolveIcon(Icons.MeteorFilled, "text-orange-500", 200)}
          </div>
          <div className={""}>
            <div className={"text-8xl font-semibold pb-6 text-black"}>500</div>
            <div className={"text-2xl font-semibold pb-3 text-black"}>
              Hello troublemaker!
            </div>
            <div className={"pb-3 text-black"}>
              An error occurred on our end. Please return to safety!
            </div>
            <Link href={"/dashboard"}>
              <Button className={"bg-primary text-white"}>Dashboard</Button>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
