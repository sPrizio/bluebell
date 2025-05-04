'use client'

import React from "react";
import {Icons} from "@/lib/enums";
import {PageInfoProvider} from "@/lib/context/PageInfoProvider";
import PageHeaderSection from "@/components/Section/PageHeaderSection";
import {useAccountQuery, useUserQuery} from "@/lib/hooks/queries";
import LoadingPage from "@/app/loading";
import {logErrors} from "@/lib/functions/util-functions";
import Error from "@/app/error";
import AccountDetailsCmp from "@/components/Account/AccountDetailsCmp";
import {usePortfolioStore} from "@/lib/store/portfolioStore";
import {Portfolio} from "@/types/apiTypes";

/**
 * The base layout for the Account detail page
 *
 * @param children Content
 * @param params Account ID
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function AccountsLayout(
  {
    children,
    params
  }: Readonly<{
    children: React.ReactNode;
    params: { id: string }
  }>
) {

  const { data: user, isLoading: isUserLoading } = useUserQuery();
  const { data: account, isError: isAccountError, error: accountError, isLoading: isAccountLoading } = useAccountQuery(params.id)
  const {selectedPortfolioId} = usePortfolioStore()

  if (isUserLoading || !user || !selectedPortfolioId || selectedPortfolioId === -1) {
    return <LoadingPage/>
  }

  const activePortfolio: Portfolio | null = user?.portfolios?.find(p => p.portfolioNumber === selectedPortfolioId) ?? null

  if (isAccountLoading) {
    return <LoadingPage/>
  }

  if (isAccountError) {
    logErrors(accountError)
    return <Error/>
  }

  const pageInfo = {
    title: "Account Overview",
    subtitle: computeDescription(),
    iconCode: Icons.Mountain,
    breadcrumbs: [
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: `${activePortfolio?.name ?? ''} Accounts`, href: '/accounts', active: false},
      {label: `${account?.name ?? 'Account'}`, href: `/accounts/${params.id}`, active: true},
    ]
  }


  //  GENERAL FUNCTIONS

  /**
   * Computes a dynamic Account description
   */
  function computeDescription() {
    let string = ''
    if (account?.accountType?.code?.length ?? -1 > 0) {
      string += account?.accountType.label + ' Account ' + account?.accountNumber
    }

    if (account?.broker?.code?.length ?? - 1 > 0) {
      string += ' with ' + account?.broker.label
    }

    return string
  }


  //  RENDER

  return (
    <PageInfoProvider value={pageInfo}>
      <PageHeaderSection
        title={pageInfo.title}
        subtitle={pageInfo.subtitle}
        iconCode={pageInfo.iconCode}
        breadcrumbs={pageInfo.breadcrumbs}
      />
      <div className={''}>
        <div className={'grid grid-cols-1 gap-4'}>
          {!account ? <div>No Content</div> : <AccountDetailsCmp account={account}/>}
          {children}
        </div>
      </div>
    </PageInfoProvider>
  )
}