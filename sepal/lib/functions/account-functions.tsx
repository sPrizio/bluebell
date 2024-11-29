import {ApiUrls} from "@/lib/constants";
import {getAuthHeader} from "@/lib/functions/security-functions";

/**
 * Searches for an existing username that matches the given value
 *
 * @param username username
 * @param editMode if true, we're editing therefore disregard
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
export async function getUser(val: string): Promise<User | null> {

  let headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.User.GetUser.replace('{username}', val), {
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
 * Performs the user registration api call
 *
 * @param values form values
 */
export async function registerUser(values: any): Promise<User | null> {

  let ret = {
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

  let headers = getAuthHeader()
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
 * @param values form values
 */
export async function updateUser(username: string, values: any): Promise<User | null> {

  let ret = {
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

  let headers = getAuthHeader()
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

export async function getCurrencies(): Promise<Array<Currency> | null> {

  let headers = getAuthHeader()
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

export async function getBrokers(): Promise<Array<Broker> | null> {

  let headers = getAuthHeader()
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

export async function getTradePlatforms(): Promise<Array<TradePlatform> | null> {

  let headers = getAuthHeader()
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

export async function getAccountTypes(): Promise<Array<AccountType> | null> {

  let headers = getAuthHeader()
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

export async function getAccountCreationInfo(): Promise<AccountCreationInfo | null> {

  let headers = getAuthHeader()
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

export async function createAccount(values: any): Promise<Account | null> {

  let ret = {
    account: {
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

  let headers = getAuthHeader()
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

export async function updateAccount(accNumber: val, values: any): Promise<Account | null> {

  let ret = {
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

  let headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.Account.UpdateAccount.replace('{accountNumber}', accNumber), {
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

export async function deleteAccount(accNumber: val): Promise<boolean | null> {

  let headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.Account.DeleteAccount.replace('{accountNumber}', accNumber), {
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

export async function getAccountDetails(accNumber: number): Promise<AccountDetails | null> {

  let headers = getAuthHeader()
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