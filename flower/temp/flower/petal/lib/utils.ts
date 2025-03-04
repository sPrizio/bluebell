import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"
import {z} from "zod";
import parsePhoneNumberFromString from "libphonenumber-js";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export const zodPhone = z.string().optional().transform((arg, ctx) => {

  //  optional means we allow empty strings
  if (!arg) {
    return ''
  }

  const phone = parsePhoneNumberFromString(arg, {
    // set this to use a default country when the phone number omits country code
    defaultCountry: 'US',

    // set to false to require that the whole string is exactly a phone number,
    // otherwise, it will search for a phone number anywhere within the string
    extract: false,
  });

  // when it's good
  if (phone?.isValid() ?? false) {
    return phone?.number ?? false;
  }

  // when it's not
  ctx.addIssue({
    code: z.ZodIssueCode.custom,
    message: 'Invalid phone number. If using a number outside USA/CA, please include your country code (+xx)',
  });

  return z.NEVER;
});