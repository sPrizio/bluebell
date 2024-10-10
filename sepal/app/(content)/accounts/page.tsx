'use client'

import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import React, {useEffect} from "react";
import {Icons} from "@/lib/enums";
import {BaseCard} from "@/components/Card/BaseCard";
import {Button} from "@/components/ui/button";
import {IconCirclePlus, IconSquareRoundedCheckFilled} from "@tabler/icons-react";
import NewAccountForm from "@/components/Form/NewAccountForm";
import BaseModal from "@/components/Modal/BaseModal";
import {accounts} from "@/lib/sample-data";
import AccountsTable from "@/components/Table/account/AccountsTable";

/**
 * The page that shows all of a user's accounts
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function AccountsPage() {

  const {
    pageTitle,
    pageSubtitle,
    pageIconCode,
    setPageTitle,
    setPageSubtitle,
    setPageIconCode
  } = useSepalPageInfoContext()

  useEffect(() => {
    setPageTitle('Accounts')
    setPageSubtitle('A list of all your trading accounts.')
    setPageIconCode(Icons.Dashboard)
  }, [])


  //  RENDER

  return (
    <div className={'grid grid-cols-1 gap-8 w-full'}>
      <div className={'flex flex-row items-center justify-end'}>
        <div>
          <BaseModal title={'Add a new Trading Account'}
                     trigger={<Button className="w-full text-white"><IconCirclePlus/>&nbsp;Add a new account</Button>}
                     content={<NewAccountForm/>}
          />
        </div>
      </div>
      <p>TODO</p>
      <ul>
        <li>Clicking on an account brings us to the account details page</li>
      </ul>
      <div className={'flex items-center text-sm justify-end w-full'}>
        *&nbsp;The&nbsp;&nbsp;<span className={'inline-block'}><IconSquareRoundedCheckFilled className={'text-primary'} /></span>&nbsp;&nbsp;indicates a default account. A default account
        is the account that will be shown in the performance section on initial view. The default account can be changed at any time.
      </div>
      <div className={''}>
        <BaseCard
          title={'Active Accounts'}
          subtitle={'All actively traded accounts.'}
          cardContent={
            <AccountsTable
              accounts={accounts.filter(acc => acc.active)}
              allowAccountSelection={true}
              showCompactTable={false}
            />
          }
        />
      </div>
      <div className={''}>
        <BaseCard
          title={'Inactive Accounts'}
          subtitle={'Inactive or disabled accounts are ones that are not currently being traded.'}
          cardContent={
            <AccountsTable
              accounts={accounts.filter(acc => !acc.active)}
              allowAccountSelection={true}
              showCompactTable={false}
            />
          }
        />
      </div>
    </div>
  )
}