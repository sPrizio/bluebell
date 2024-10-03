import PageHeaderSection from "@/components/Section/PageHeaderSection";
import React from "react";
import {Icons} from "@/lib/enums";

/**
 * The page that shows all of a user's accounts
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function AccountsPage() {
  return (
    <>
      <PageHeaderSection
        title={'Trading Accounts'}
        subtitle={'An overview of your trading accounts.'}
        iconCode={Icons.TradingAccounts}
      />
      <div className={"border border-red-400 w-full bg-red-200 min-h-[5000px]"}>
        This is the really long content
      </div>
    </>
  );
}