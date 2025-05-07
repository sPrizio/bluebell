'use client'

import React from "react";
import {Icons} from "@/lib/enums";
import {useRouter, useSearchParams} from 'next/navigation'
import {logErrors, selectNewAccount} from "@/lib/functions/util-functions";
import {BaseCard} from "@/components/Card/BaseCard";
import AccountTransactionsTable from "@/components/Table/Account/AccountTransactionsTable";
import {Button} from "@/components/ui/button";
import {IconCirclePlus} from "@tabler/icons-react";
import BaseModal from "@/components/Modal/BaseModal";
import TransactionForm from "@/components/Form/Transaction/TransactionForm";
import {useActiveAccount} from "@/lib/hooks/api/useActiveAccount";
import ReusableSelect from "@/components/Input/ReusableSelect";
import {PageInfoProvider} from "@/lib/context/PageInfoProvider";
import LoadingPage from "@/app/loading";
import Error from "@/app/error";

/**
 * The page that shows all of a user's account's transactions. Accounts can be cycled
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function TransactionsPage() {

  const searchParams = useSearchParams();
  const router = useRouter();
  const {isLoading, isError, error, activePortfolio, activeAccount, hasMismatch} = useActiveAccount();

  if (isLoading) {
    return <LoadingPage/>;
  }

  if (hasMismatch || isError) {
    logErrors('User and portfolio mismatch!', error);
    return <Error/>;
  }

  const accNumber = activeAccount?.accountNumber ?? -1
  const pageInfo = {
    title: 'Transactions',
    subtitle: `A list of transactions for ${activeAccount?.name ?? ''}`,
    iconCode: Icons.Transactions,
    breadcrumbs: [
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: `${activeAccount?.name ?? ''}`, href: '/accounts/' + accNumber, active: false},
      {label: 'Transactions', href: '/transactions', active: true},
    ]
  }


  //  RENDER

  return (
    <PageInfoProvider value={pageInfo}>
      <div>
        {
          <div className={'grid grid-cols-1 gap-8'}>
            <div className={'flex items-center justify-end gap-4'}>
              <ReusableSelect
                title={'Account'}
                initialValue={accNumber.toString()}
                options={activePortfolio?.accounts?.filter(acc => acc.active)?.map(a => {
                  return {label: a.name, value: a.accountNumber.toString()}
                }) ?? []}
                handler={(val: string) => {
                  selectNewAccount(router, searchParams, parseInt(val))
                }}
              />
            </div>
            <div>
              {
                ((activeAccount?.transactions?.length ?? 0) === 0) &&
                  <div className="text-center text-slate-500">No account activity.</div>
              }
              {
                (activeAccount?.transactions?.length ?? 0) > 0 ?
                  <BaseCard
                    loading={isLoading}
                    title={'Transactions'}
                    subtitle={'A look at all of your transactions for this account.'}
                    cardContent={<AccountTransactionsTable account={activeAccount} transactions={activeAccount?.transactions ?? []}
                                                           showActions={true} showBottomLink={false}/>}
                    headerControls={[
                      <BaseModal
                        key={0}
                        title={'Add a new Transaction'}
                        description={'Keep track of your account\'s transactions by adding withdrawals & deposits.'}
                        trigger={<Button className="w-full text-white"><IconCirclePlus/>&nbsp;Add Transaction</Button>}
                        content={<TransactionForm account={activeAccount} mode={'create'}/>}
                      />
                    ]}
                  /> : null
              }
            </div>
          </div>
        }
      </div>
    </PageInfoProvider>
  )
}