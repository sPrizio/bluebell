import {ApiUrls} from "@/lib/constants";
import {getAuthHeader} from "@/lib/functions/security-functions";
import {
  Account,
  AccountCreationInfo,
  AccountDetails,
  AccountType,
  Broker,
  Currency,
  TradePlatform,
  Transaction,
  User
} from "@/types/apiTypes";

/**
 * Searches for an existing username that matches the given value
 *
 * @param username username
 * @param editMode if true, that means we're editing and should  disregard
 */
export function hasUsername(username: string, editMode: boolean): boolean {

  if (!editMode) {
    // we're creating a new profile, therefore we should search for existing usernames
    //  TODO: implement this on the backend
    //  TODO: temp
    /*if (username === 's.prizio') {
      return true;
    }*/

    return false;
  }

  return false;
}

/**
 * Searches for an existing email that matches the given value
 *
 * @param email email
 * @param editMode if true, we're editing therefore disregard
 */
export function hasEmail(email: string, editMode: boolean): boolean {

  if (!editMode) {
    // we're creating a new profile, therefor we should search for existing emails
    //  TODO: implement this on the backend
    //  TODO: temp
    /*if (email === 's.prizio@hotmail.com') {
      return true;
    }*/

    return false;
  }

  return false;
}

/**
 * Fetches the user for the given value
 *
 * @param val username
 */
export async function getUser(val: string): Promise<User> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  const res =
    await fetch(ApiUrls.User.GetUser.replace('{username}', val), {
      method: 'GET',
      headers: headers,
    })

  if (!res.ok) {
    throw new Error(`Failed to get recent transactions with status: ${res.status}`);
  }

  const data = await res.json();
  if (!data.success) {
    throw new Error(`API returned with error: ${data.message} || ${JSON.stringify(data)}`);
  }

  return data.data;
}

/**
 * Performs the user registration api call
 *
 * @param values form values
 */
export async function registerUser(values: any): Promise<User | null> {

  const ret = {
    user: {
      firstName: values?.firstName ?? '',
      lastName: values?.lastName ?? '',
      username: values?.username ?? '',
      email: values?.email ?? '',
      password: values?.password ?? '',
      phoneNumbers: [
        {
          phoneNumber: {
            phoneType: values?.phoneType ?? '',
            countryCode: 1,
            telephoneNumber: values?.telephoneNumber ?? '',
          }
        }
      ]
    }
  }

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.User.RegisterUser, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(ret)
      })

    if (res.ok) {
      const data = await res.json()
      if (data.success) {
        return data.data
      }
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Performs the user registration api call
 *
 * @param username username
 * @param values form values
 */
export async function updateUser(username: string, values: any): Promise<User | null> {

  const ret = {
    user: {
      firstName: values?.firstName ?? '',
      lastName: values?.lastName ?? '',
      username: values?.username ?? '',
      email: values?.email ?? '',
      phoneNumbers: [
        {
          phoneNumber: {
            phoneType: values?.phoneType ?? '',
            countryCode: 1,
            telephoneNumber: values?.telephoneNumber ?? '',
          }
        }
      ]
    }
  }

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.User.UpdateUser.replace('{username}', username), {
        method: 'PUT',
        headers: headers,
        body: JSON.stringify(ret)
      })

    if (res.ok) {
      const data = await res.json()
      if (data.success) {
        return data.data
      }
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Obtains the system's currencies
 */
export async function getCurrencies(): Promise<Array<Currency> | null> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.Account.GetCurrencies, {
        method: 'GET',
        headers: headers,
      })

    if (res.ok) {
      const data = await res.json()
      if (data.success) {
        return data.data
      }
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Obtains the systems brokers
 */
export async function getBrokers(): Promise<Array<Broker> | null> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.Account.GetBrokers, {
        method: 'GET',
        headers: headers,
      })

    if (res.ok) {
      const data = await res.json()
      if (data.success) {
        return data.data
      }
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Obtains the system's trade platforms
 */
export async function getTradePlatforms(): Promise<Array<TradePlatform> | null> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.Account.GetTradePlatforms, {
        method: 'GET',
        headers: headers,
      })

    if (res.ok) {
      const data = await res.json()
      if (data.success) {
        return data.data
      }
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Obtains the system's account types
 */
export async function getAccountTypes(): Promise<Array<AccountType> | null> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.Account.GetAccountTypes, {
        method: 'GET',
        headers: headers,
      })

    if (res.ok) {
      const data = await res.json()
      if (data.success) {
        return data.data
      }
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Obtains account creation info, which refers to the various system values needed when configuring a new account
 */
export async function getAccountCreationInfo(): Promise<AccountCreationInfo | null> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  const currencies = await getCurrencies()
  const brokers = await getBrokers()
  const tradePlatforms = await getTradePlatforms()
  const accountTypes = await getAccountTypes()

  return {
    currencies: currencies ?? [],
    brokers: brokers ?? [],
    platforms: tradePlatforms ?? [],
    accountTypes: accountTypes ?? [],
  };
}

/**
 * Creates a new account
 *
 * @param values account values
 */
export async function createAccount(values: any): Promise<Account | null> {

  const ret = {
    account: {
      active: values?.active ?? '',
      balance: values?.balance ?? '',
      name: values?.name ?? '',
      number: values?.accountNumber ?? '',
      currency: values?.currency ?? '',
      type: values?.accountType ?? '',
      broker: values?.broker ?? '',
      tradePlatform: values?.tradePlatform ?? '',
      isDefault: values?.defaultAccount ?? '',
    }
  }

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.Account.CreateAccount, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(ret)
      })

    if (res.ok) {
      const data = await res.json()
      if (data.success) {
        return data.data
      }
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Updates an existing account with the given payload
 *
 * @param accNumber account number
 * @param values updated values
 */
export async function updateAccount(accNumber: number, values: any): Promise<Account | null> {

  const ret = {
    account: {
      balance: values?.balance ?? '',
      name: values?.name ?? '',
      number: values?.accountNumber ?? '',
      currency: values?.currency ?? '',
      type: values?.accountType ?? '',
      broker: values?.broker ?? '',
      tradePlatform: values?.tradePlatform ?? '',
      isDefault: values?.defaultAccount ?? '',
      active: values?.active ?? '',
    }
  }

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.Account.UpdateAccount.replace('{accountNumber}', accNumber.toString()), {
        method: 'PUT',
        headers: headers,
        body: JSON.stringify(ret)
      })

    if (res.ok) {
      const data = await res.json()
      if (data.success) {
        return data.data
      }
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Deletes an account
 *
 * @param accNumber account number
 */
export async function deleteAccount(accNumber: number): Promise<boolean | null> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.Account.DeleteAccount.replace('{accountNumber}', accNumber.toString()), {
        method: 'DELETE',
        headers: headers,
      })

    if (res.ok) {
      const data = await res.json()
      return !!data.success;
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Obtains account information for the given account number
 *
 * @param accNumber account number
 */
export async function getAccountDetails(accNumber: number): Promise<AccountDetails | null> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.Account.GetDetails.replace('{accountNumber}', accNumber.toString()), {
        method: 'GET',
        headers: headers,
      })

    if (res.ok) {
      const data = await res.json()
      if (data.success) {
        return data.data
      }
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Obtains a list of the recent transactions, this will default to the default account
 */
export async function getRecentTransactions(): Promise<Array<Transaction>> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  const res =
    await fetch(ApiUrls.User.GetRecentTransactions, {
      method: 'GET',
      headers: headers,
    })

  if (!res.ok) {
    throw new Error(`Failed to get recent transactions with status: ${res.status}`);
  }

  const data = await res.json();
  if (!data.success) {
    throw new Error(`API returned with error: ${data.message} || ${JSON.stringify(data)}`);
  }

  return data.data;
}