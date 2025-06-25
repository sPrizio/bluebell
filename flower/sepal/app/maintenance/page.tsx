import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";

/**
 * A small page wrapper for the maintenance page
 *
 * @author Stephen Prizio
 * @version 0.2.6
 */
export default function MaintenancePage() {
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
            {resolveIcon(Icons.BarrierBlock, "text-orange-500", 200)}
          </div>
          <div className={""}>
            <div className={"text-6xl font-semibold pb-6 text-black"}>
              Break Time
            </div>
            <div className={"text-2xl font-semibold pb-3 text-black"}>
              Hello friend!
            </div>
            <div className={"pb-3 text-black"}>
              Unfortunately we cannot serve you at this time, but rest assured!
              We are hard at work, and we&apos;ll be back soon!
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
