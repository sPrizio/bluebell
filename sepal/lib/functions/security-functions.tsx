/**
 * Checks for a valid password as per the guidelines defined by bluebell
 *
 * @param val test input
 */
export function isValidPassword(val: string): boolean {

  const allowedSymbols = ['!', '@', '#', '$', '%', '&', '*', '-', '_', '+']

  let hasAlphanumeric = (/^[a-z0-9]+$/i.exec(val)?.length ?? -1) > 0;
  let hasSymbols = false;

  for (let str of allowedSymbols) {
    if (val.includes(str)) {
      hasSymbols = true;
      break
    }
  }

  return !hasAlphanumeric && hasSymbols;
}