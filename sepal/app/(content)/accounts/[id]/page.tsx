'use client'

import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import React, {useEffect, useState} from "react";
import {Icons} from "@/lib/enums";
import {useParams} from "next/navigation";
import {isNumeric} from "@/lib/functions/util-functions";
import {Loader2} from "lucide-react";
import AccountDetailsCmp from "@/components/Account/AccountDetailsCmp";
import {Account} from "@/types/apiTypes";

/**
 * Renders the Account details page
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function AccountDetailPage() {

  const [account, setAccount] = useState<Account>()
  const { id } : { id: string } = useParams();
  const [isLoading, setIsLoading] = useState<boolean>(false)

  const {
    user,
    pageTitle,
    pageSubtitle,
    pageIconCode,
    breadcrumbs,
    setUser,
    setPageTitle,
    setPageSubtitle,
    setPageIconCode,
    setBreadcrumbs
  } = useSepalPageInfoContext()

  useEffect(() => {
    setPageTitle(account?.name ?? 'Account Overview')
    setPageSubtitle(computeDescription())
    setPageIconCode(Icons.Mountain)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: `${id}`, href: `/accounts/${id}`, active: true}
    ])

    getAccount()
  }, [account, user])


  //  GENERAL FUNCTIONS

  /**
   * Fetches the associated Account information
   */
  async function getAccount() {

    setIsLoading(true)

    if (isNumeric(id)) {
      const accountTestId = parseInt(id)
      for (let acc of user?.accounts ?? []) {
        if (acc.accountNumber === accountTestId) {
          setAccount(acc)
          setIsLoading(false)
          return
        }
      }
    }

    setIsLoading(false)
  }

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
    <div className={''}>
      {
        isLoading ?
          <div className={'h-[72vh] flex items-center justify-center'}>
            <div className={'grid grid-cols-1 justify-items-center gap-4'}>
              <div>
                <Loader2 className="animate-spin text-primary" size={50} />
              </div>
              <div className={'text-lg'}>Loading account information</div>
            </div>
          </div>
          :
          <div className={'grid grid-cols-1 gap-4'}>
            {!account ? <div>No Content</div> : <AccountDetailsCmp account={account} />}
          </div>
      }
    </div>
  )
}