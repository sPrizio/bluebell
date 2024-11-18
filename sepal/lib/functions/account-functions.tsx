/**
 * Searches for an existing username that matches the given value
 *
 * @param username username
 * @param editMode if true, we're editing therefore disregard
 */
export function hasUsername(username: string, editMode: boolean) : boolean {

  if (!editMode) {
    // we're creating a new profile, therefore we should search for existing usernames
    //  TODO: implement this on the backend
    //  TODO: temp
    if (username === 's.prizio') {
      return true;
    }

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
export function hasEmail(email: string, editMode: boolean) : boolean {

  if (!editMode) {
    // we're creating a new profile, therefor we should search for existing emails
    //  TODO: implement this on the backend
    //  TODO: temp
    if (email === 's.prizio@hotmail.com') {
      return true;
    }

    return false;
  }

  return false;
}