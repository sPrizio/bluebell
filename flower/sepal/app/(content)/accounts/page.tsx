'use client'

import React from "react";
import {BaseCard} from "@/components/Card/BaseCard";
import {Button} from "@/components/ui/button";
import {IconCirclePlus, IconSquareRoundedCheckFilled} from "@tabler/icons-react";
import BaseModal from "@/components/Modal/BaseModal";
import AccountsTable from "@/components/Table/Account/AccountsTable";
import AccountForm from "@/components/Form/Account/AccountForm";
import {Account} from "@/types/apiTypes";
import {useUserQuery} from "@/lib/hooks/queries";
import LoadingPage from "@/app/loading";
import {logErrors} from "@/lib/functions/util-functions";
import Error from "@/app/error";
import {Icons} from "@/lib/enums";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import PageHeaderSection from "@/components/Section/PageHeaderSection";

/**
 * The page that shows all of a user's accounts
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function AccountsPage() {

  const {data: user, isError: isUserError, error: userError, isLoading: isUserLoading} = useUserQuery();

  const pageInfo = {
    title: "Accounts",
    subtitle: "A list of all of your trading accounts.",
    iconCode: Icons.AccountOverview,
    breadcrumbs: [
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: true},
    ]
  }


  //  RENDER

  if (isUserLoading) {
    return <LoadingPage/>
  }

  if (isUserError) {
    logErrors(userError)
    return <Error/>
  }

  const activeAccounts : Array<Account> = user?.portfolios?.flatMap(p => p.accounts)?.filter((acc : Account) => acc.active) ?? []
  const inActiveAccounts : Array<Account> = user?.portfolios?.flatMap(p => p.accounts)?.filter((acc : Account) => !acc.active) ?? []

  return (
    <PageInfoProvider value={pageInfo}>
      <PageHeaderSection
        title={pageInfo.title}
        subtitle={pageInfo.subtitle}
        iconCode={pageInfo.iconCode}
        breadcrumbs={pageInfo.breadcrumbs}
      />
      <div className={'grid grid-cols-1 gap-8 w-full'}>
        <div className={'flex flex-row items-center justify-end'}>
          <div>
            <BaseModal title={'Add a new Trading Account'}
                       description={'Adding a new Account will include it as part of your portfolio. If you do not wish to track your Account in your portfolio, mark it as inactive. These settings can be changed at anytime from the Account page.'}
                       trigger={<Button className="w-full text-white"><IconCirclePlus/>&nbsp;Add a new account</Button>}
                       content={<AccountForm mode={'create'}/>}
            />
          </div>
        </div>
        <div className={'flex items-center text-sm justify-end w-full'}>
          <span className={'inline-block'}><IconSquareRoundedCheckFilled className={'text-primary'} /></span>&nbsp;&nbsp;indicates default account.
        </div>
        {
          activeAccounts.length === 0 && inActiveAccounts.length === 0 &&
            <div className={'text-center'}>
                No accounts found. Try adding one!
            </div>
        }
        {
          activeAccounts.length > 0 &&
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
        {
          inActiveAccounts.length > 0 &&
            <div className={''}>
                <BaseCard
                    title={'Inactive Accounts'}
                    subtitle={'Inactive or disabled accounts are ones that are not currently being traded.'}
                    cardContent={
                      <AccountsTable
                        accounts={inActiveAccounts}
                        allowAccountSelection={true}
                        showCompactTable={false}
                      />
                    }
                />
            </div>
        }
      </div>
    </PageInfoProvider>
  )
}