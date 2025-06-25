import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";
import Link from "next/link";
import { Button } from "@/components/ui/button";

/**
 * Renders the unauthorized page
 *
 * @author Stephen Prizio
 * @version 0.2.6
 */
export default function UnauthorizedPage() {
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
            {resolveIcon(Icons.LockFilled, "text-primaryRed", 200)}
          </div>
          <div className={""}>
            <div className={"text-8xl font-semibold pb-6 text-black"}>401</div>
            <div className={"text-2xl font-semibold pb-3 text-black"}>
              Hello, Infiltrator
            </div>
            <div className={"pb-3 text-black"}>
              You do not possess sufficient privileges to view this page. Please
              return to the dashboard.
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
