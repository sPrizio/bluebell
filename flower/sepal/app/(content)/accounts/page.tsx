'use client'

import React from "react";
import {BaseCard} from "@/components/Card/BaseCard";
import {Button} from "@/components/ui/button";
import {IconCirclePlus, IconSquareRoundedCheckFilled} from "@tabler/icons-react";
import BaseModal from "@/components/Modal/BaseModal";
import AccountsTable from "@/components/Table/Account/AccountsTable";
import AccountForm from "@/components/Form/Account/AccountForm";
import {Account} from "@/types/apiTypes";
import LoadingPage from "@/app/loading";
import {logErrors} from "@/lib/functions/util-functions";
import Error from "@/app/error";
import {Icons} from "@/lib/enums";
import {PageInfoProvider} from "@/lib/context/PageInfoProvider";
import PageHeaderSection from "@/components/Section/PageHeaderSection";
import ReusableSelect from "@/components/Input/ReusableSelect";
import {usePortfolioStore} from "@/lib/store/portfolioStore";
import {useActivePortfolio} from "@/lib/hooks/api/useActivePortoflio";
import {useUserQuery} from "@/lib/hooks/query/queries";

/**
 * The page that shows all of a user's accounts
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function AccountsPage() {

  const {data: user} = useUserQuery();
  const { isLoading, isError, error, activePortfolio, hasMismatch } = useActivePortfolio();
  const {selectedPortfolioId, setSelectedPortfolioId} = usePortfolioStore()

  if (isLoading) {
    return <LoadingPage />;
  }

  if (hasMismatch || isError) {
    logErrors('User and portfolio mismatch!', error);
    return <Error />;
  }

  const pageInfo = {
    title: "Accounts",
    subtitle: "A list of all of your trading accounts.",
    iconCode: Icons.AccountOverview,
    breadcrumbs: [
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: `${activePortfolio?.name ?? ''} Accounts`, href: '/accounts', active: true},
    ]
  }

  const activeAccounts = activePortfolio?.accounts?.filter((acc: Account) => acc.active) ?? []
  const inactiveAccounts = activePortfolio?.accounts?.filter((acc: Account) => !acc.active) ?? []


  //  RENDER

  if (selectedPortfolioId === null) {
    setSelectedPortfolioId(activePortfolio?.portfolioNumber ?? -1)
  }

  let inactiveData = null
  let activeData =
    <div className={'text-center'}>
      No active accounts for this portfolio.
    </div>

  if ((activeAccounts?.length ?? 0) > 0) {
    activeData =
      <div className={''}>
        <BaseCard
          title={'Active Accounts'}
          subtitle={'All actively traded accounts.'}
          cardContent={
            <AccountsTable
              accounts={activeAccounts}
              allowAccountSelection={true}
              showCompactTable={false}
            />
          }
        />
      </div>
  }

  if ((inactiveAccounts?.length ?? 0) > 0) {
    inactiveData =
      <div className={''}>
        <BaseCard
          title={'Inactive Accounts'}
          subtitle={'Inactive or disabled accounts are ones that are not currently being traded.'}
          cardContent={
            <AccountsTable
              accounts={inactiveAccounts}
              allowAccountSelection={true}
              showCompactTable={false}
            />
          }
        />
      </div>
  }

  return (
    <PageInfoProvider value={pageInfo}>
      <PageHeaderSection
        title={pageInfo.title}
        subtitle={pageInfo.subtitle}
        iconCode={pageInfo.iconCode}
        breadcrumbs={pageInfo.breadcrumbs}
      />
      <div className={'grid grid-cols-1 gap-8 w-full'}>
        <div className={'flex gap-8 w-full items-end justify-end'}>
          <div className={'w-1/2 flex items-end justify-end gap-8'}>
            <div className={''}>
              <BaseModal title={'Add a new Trading Account'}
                         description={'Adding a new Account will include it as part of your portfolio. If you do not wish to track your Account in your portfolio, mark it as inactive. These settings can be changed at anytime from the Account page.'}
                         trigger={<Button className="w-full text-white"><IconCirclePlus/>&nbsp;Add a new
                           account</Button>}
                         content={<AccountForm portfolioNumber={selectedPortfolioId ?? -1} mode={'create'}/>}
              />
            </div>
            <div className={''}>
              <ReusableSelect
                title={'Portfolio'}
                initialValue={selectedPortfolioId?.toString()}
                options={user?.portfolios?.map(p => {
                  return {label: p.name, value: p.portfolioNumber}
                }) ?? []}
                handler={(val: string) => {
                  setSelectedPortfolioId(parseInt(val))
                }}
              />
            </div>
          </div>
        </div>

        <div className={'flex items-center text-sm justify-end w-full'}>
          <span className={'inline-block'}><IconSquareRoundedCheckFilled
            className={'text-primary'}/></span>&nbsp;&nbsp;indicates default account.
        </div>

        {activeData}
        {inactiveData}
      </div>
    </PageInfoProvider>
  )
}