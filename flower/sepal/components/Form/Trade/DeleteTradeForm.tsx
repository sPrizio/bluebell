"use client";

import { useDeleteTradeMutation } from "@/lib/hooks/query/mutations";
import { Trade } from "@/types/apiTypes";
import { useToast } from "@/lib/hooks/ui/use-toast";
import { useRouter } from "next/navigation";
import { useSepalModalContext } from "@/lib/context/SepalContext";
import { useEffect } from "react";
import { logErrors } from "@/lib/functions/util-functions";
import { Button } from "@/components/ui/button";
import { Loader2 } from "lucide-react";
import { useActiveAccount } from "@/lib/hooks/api/useActiveAccount";

/**
 * Renders a form for deleting trades
 *`
 * @param trade trade to delete
 * @param account account
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function DeleteTradeForm({
  trade,
}: Readonly<{ trade: Trade | null | undefined }>) {
  const { toast } = useToast();
  const { setOpen } = useSepalModalContext();
  const router = useRouter();

  const {
    isLoading: isActiveAccountLoading,
    isError: isActiveAccountError,
    error: activeAccountError,
    activeAccount,
    hasMismatch,
  } = useActiveAccount();

  const {
    mutate: deleteTrade,
    isPending: isDeleteTradeLoading,
    isSuccess: isDeleteTradeSuccess,
    isError: isDeleteTradeError,
    error: deleteTradeError,
  } = useDeleteTradeMutation(
    activeAccount?.accountNumber ?? -1,
    trade?.tradeId ?? "-1",
  );

  useEffect(() => {
    if (isDeleteTradeSuccess) {
      toast({
        title: "Deletion Successful!",
        description: "The trade was successfully deleted.",
        variant: "success",
      });

      setOpen(false);
      router.push(
        `/trades?account=${activeAccount?.accountNumber ?? "default"}`,
      );
    } else if (isDeleteTradeError) {
      toast({
        title: "Deletion Failed!",
        description:
          "An error occurred while deleting the trade. Please try again.",
        variant: "danger",
      });

      logErrors(deleteTradeError);
      setOpen(false);
    }
  }, [isDeleteTradeSuccess, isDeleteTradeError]);

  //  RENDER

  if (isActiveAccountError || hasMismatch) {
    logErrors(activeAccountError);
    setOpen(false);
    return null;
  }

  return (
    <div>
      <div className={"mb-3"}>
        Are you sure you want to delete this trade? This action cannot be
        undone.
      </div>
      <div className={"flex w-full justify-end items-center gap-4"}>
        <Button
          type="submit"
          className={"bg-primaryRed hover:bg-primaryRedLight text-white"}
          disabled={isDeleteTradeLoading || isActiveAccountLoading}
          onClick={deleteTrade}
        >
          {isDeleteTradeLoading ? (
            <Loader2 className="mr-2 h-4 w-4 animate-spin" />
          ) : null}
          Delete
        </Button>
        <Button
          type="button"
          className={"border border-gray-400"}
          variant={"outline"}
          onClick={() => setOpen(false)}
        >
          Cancel
        </Button>
      </div>
    </div>
  );
}
