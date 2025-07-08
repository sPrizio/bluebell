import Link from "next/link";
import { Button } from "@/components/ui/button";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";

/**
 * Renders the 404 page
 *
 * @author Stephen Prizio
 * @version 0.2.6
 */
export default function NotFoundPage() {
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
            {resolveIcon(Icons.Planet, "text-primary", 200)}
          </div>
          <div className={""}>
            <div className={"text-8xl font-semibold pb-6 text-black"}>404</div>
            <div className={"text-2xl font-semibold pb-3 text-black"}>
              Hello little wanderer!
            </div>
            <div className={"pb-3 text-black"}>
              The page you are looking for does not exist. How you got here is a
              mystery. But you can click the button below to go back to your
              dashboard.
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
