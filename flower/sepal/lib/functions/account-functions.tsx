import {ApiUrls} from "@/lib/constants";
import {getAuthHeader} from "@/lib/functions/security-functions";
import {AccountDetails, User} from "@/types/apiTypes";

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