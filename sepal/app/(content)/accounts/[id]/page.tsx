'use client'

import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import React, {useEffect, useState} from "react";
import {Icons} from "@/lib/enums";
import {useParams, useRouter} from "next/navigation";
import {isNumeric} from "@/lib/functions";
import {accounts} from "@/lib/sample-data";
import {Loader2} from "lucide-react";
import {BaseCard} from "@/components/Card/BaseCard";
import {Button} from "@/components/ui/button";
import {IconEdit, IconTrash} from "@tabler/icons-react";
import BaseModal from "@/components/Modal/BaseModal";
import AccountForm from "@/components/Form/account/AccountForm";

/**
 * Renders the account details page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function AccountDetailPage() {

  const [account, setAccount] = useState<Account>()
  const router = useRouter();
  const { id } : { id: string } = useParams();
  const [isLoading, setIsLoading] = useState<boolean>(false)

  const {
    pageTitle,
    pageSubtitle,
    pageIconCode,
    breadcrumbs,
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
  }, [account])

  useEffect(() => {
    getAccount()
  }, []);


  //  GENERAL FUNCTIONS

  /**
   * Fetches the associated account information
   */
  async function getAccount() {

    setIsLoading(true)
    //await delay(2000)

    if (isNumeric(id)) {
      const accountTestId = parseInt(id)
      for (let acc of accounts) {
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
   * Computes a dynamic account description
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
      <p>TODO</p>
      <ul>
        <li>Build out page layout</li>
        <li>Include controls for editing and deleting an account (editing will open equivalent of new form modal but with values, delete will open a confirmation modal)</li>
      </ul>
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
            {
              !account ?
                <div>
                  No Content
                </div>
                :
                <div>
                  <BaseCard
                    title={'Test'}
                    subtitle={'Hello'}
                    cardContent={<p>Hello World!</p>}
                    headerControls={[
                      <BaseModal
                        key={0}
                        title={'Update Trading Account Information'}
                        description={'Here you can edit/update any account information. Note that some aspects of your account cannot be changed after account creation.'}
                        trigger={<Button className="" variant={"outline"}><IconEdit/>&nbsp;Update</Button>}
                        content={<AccountForm mode={'edit'} account={account} />}
                      />,
                      <BaseModal
                        key={1}
                        title={'Delete Trading Account'}
                        trigger={<Button className="bg-primaryRed text-white hover:bg-red-500"><IconTrash/>&nbsp;Delete</Button>}
                        content={<AccountForm/>}
                      />
                    ]}
                  />
                </div>
            }
          </div>
      }
    </div>
  )
}