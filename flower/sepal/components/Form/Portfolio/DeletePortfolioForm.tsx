"use client";

import { Portfolio } from "@/types/apiTypes";
import { useToast } from "@/lib/hooks/ui/use-toast";
import { useSepalModalContext } from "@/lib/context/SepalContext";
import { useRouter } from "next/navigation";
import { useDeletePortfolioMutation } from "@/lib/hooks/query/mutations";
import { useEffect } from "react";
import { logErrors } from "@/lib/functions/util-functions";
import { Button } from "@/components/ui/button";
import { Loader2 } from "lucide-react";

/**
 * Form for deleting portfolios
 *
 * @param portfolio portfolio
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function DeletePortfolioForm({
  portfolio,
}: Readonly<{ portfolio: Portfolio }>) {
  const { toast } = useToast();
  const { setOpen } = useSepalModalContext();
  const router = useRouter();
  const {
    mutate: deletePortfolio,
    isPending: isDeletePortfolioLoading,
    isSuccess: isDeletePortfolioSuccess,
    isError: isDeletePortfolioError,
    error: deletePortfolioError,
  } = useDeletePortfolioMutation(portfolio?.portfolioNumber ?? -1);

  useEffect(() => {
    if (isDeletePortfolioSuccess) {
      toast({
        title: "Deletion Successful!",
        description: "The portfolio was successfully deleted.",
        variant: "success",
      });

      setOpen(false);
      router.push("/portfolios");
    } else if (isDeletePortfolioError) {
      toast({
        title: "Deletion Failed!",
        description:
          "An error occurred while deleting portfolio. Please try again.",
        variant: "danger",
      });

      logErrors(deletePortfolioError);
      setOpen(false);
    }
  }, [isDeletePortfolioSuccess, isDeletePortfolioError]);

  //  RENDER

  return (
    <div>
      <div className={"mb-3"}>
        Are you sure you want to delete this portfolio? This action cannot be
        undone.
      </div>
      <div className={"flex w-full justify-end items-center gap-4"}>
        <Button
          type="submit"
          className={"bg-primaryRed hover:bg-primaryRedLight text-white"}
          disabled={isDeletePortfolioLoading}
          onClick={deletePortfolio}
        >
          {isDeletePortfolioLoading ? (
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
