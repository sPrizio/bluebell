import {useSearchParams} from "next/navigation";
import {useEffect, useState} from "react";
import {Icons} from "@/lib/enums";
import {useSepalPageInfoContext} from "@/lib/context/SepalContext";

export default function PerformancePage() {

  const searchParams = useSearchParams()
  const [isLoading, setIsLoading] = useState(false)
  const [accNumber, setAccNumber] = useState(getAccountNumber())
  const [account, setAccount] = useState<Account>()

  const {
    pageTitle,
    pageSubtitle,
    pageIconCode,
    breadcrumbs,
    user,
    setPageTitle,
    setPageSubtitle,
    setPageIconCode,
    setBreadcrumbs,
    setUser
  } = useSepalPageInfoContext()

  useEffect(() => {
    setPageTitle('Transactions')
    setPageSubtitle('A list of transactions for trading account ' + accNumber)
    setPageIconCode(Icons.Transactions)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: accNumber.toString(), href: '/accounts/' + accNumber, active: false},
      {label: 'Transactions', href: '/transactions', active: true},
    ])

    getAccount()
  }, [accNumber])


  //  GENERAL FUNCTIONS

  /**
   * Fetches the account number requested by the page
   */
  function getAccountNumber() {
    let val = searchParams.get('account') ?? -1
    if (val !== -1 && isNumeric(val)) {
      return parseInt(val as string)
    }

    return -1
  }

  /**
   * Fetches the associated account information
   */
  async function getAccount() {

    setIsLoading(true)

    //TODO: temp
    await delay(2000)
    if (accNumber === -1) {
      await getDefaultAccount()
      if (searchParams.get('account') !== 'default') {
        toast({
          title: 'Invalid Account Number',
          description: `The account number ${accNumber} is not valid. Returning the default account.`,
          variant: 'danger'
        })
      }
    } else {
      for (let acc of accounts) {
        if (acc.accountNumber === accNumber) {
          setAccount(acc)
          setAccNumber(acc.accountNumber)
          setIsLoading(false)
          return
        }
      }

      // no account found
      await getDefaultAccount()
      toast({
        title: 'Account Not Found',
        description: `No account was found with the account number ${accNumber}. Returning the default account.`,
        variant: 'warning'
      })
    }

    setIsLoading(false)
  }

  /**
   * Returns the default account of the portfolio
   */
  async function getDefaultAccount() {
    setIsLoading(true)

    //TODO: temp
    await delay(2000)
    setAccount(accounts[0])
    setAccNumber(accounts[0].accountNumber)

    setIsLoading(false)
  }


  //  RENDER

  return (
    <div>
      HELLO WORLD!
    </div>
  )
}