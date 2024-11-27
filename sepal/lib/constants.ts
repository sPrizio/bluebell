//  each page has a header section that can contain an icon, this is the icon's size
import {z} from "zod";
import {safeConvertEnum} from "@/lib/functions/util-functions";
import {hasEmail, hasUsername} from "@/lib/functions/account-functions";
import {getAccountDomain, getUserDomain, isValidPassword} from "@/lib/functions/security-functions";
import parsePhoneNumberFromString from "libphonenumber-js";

export const DEFAULT_PAGE_HEADER_SECTION_ICON_SIZE = 36;

export const BASE_COLORS = ['red', 'green', 'blue', 'orange', 'pink', 'grey', 'black']

export const PHONE_REGEX = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";

export const ApiCredentials = {
  AuthHeader: 'fp-api_token',
  TestAccountNumber: '28331289',
  TestUserToken: 'Zmxvd2VycG90X2FwaV90b2tlbiZzLnByaXppb0Bob3RtYWlsLmNvbSYyMDI0LTExLTIwVDEzOjU2OjE1',
}

export const ApiUrls = {
  Account: {
    CreateAccount: getAccountDomain() + '/create-account',
    GetCurrencies: getAccountDomain() + '/currencies',
    GetAccountTypes: getAccountDomain() + '/account-types',
    GetBrokers: getAccountDomain() + '/brokers',
    GetTradePlatforms: getAccountDomain() + '/trade-platforms',
  },
  User: {
    GetUser: getUserDomain() + '/get?username={username}',
    RegisterUser: getUserDomain() + '/create',
  }
}

export const DateTime = {
  DatePickerDateFormat: 'dd/MM/yyyy',
  ISODateFormat: 'YYYY-MM-DD',
  ISODateLongMonthFormat: 'YYYY-MMMM-DD',
  ISODateTimeFormat: 'YYYY-MM-DDTHH:mm:ss',
  ISODayFormat: 'Do',
  ISOEasyDateTimeFormat: 'YYYY-MM-DD HH:mm:ss',
  ISOLongMonthDayYearFormat: 'MMMM Do[,] YYYY',
  ISOLongMonthDayYearWithTimeFormat: 'MMMM Do[,] YYYY [at] H:mm',
  ISOMonthDayFormat: 'MMMM Do',
  ISOMonthFormat: 'MMMM',
  ISOMonthWeekDayFormat: 'dddd[,] MMMM Do',
  ISOShortMonthWeekDayFormat: 'dddd[,] MMM D',
  ISOYearFormat: 'YYYY',
  ISOMonthYearFormat: 'MMMM YYYY',
  ISOShortMonthFormat: 'MMM',
  ISOShortMonthDayFormat: 'MMM D',
  ISOShortMonthFullDayFormat: 'MMM Do',
  ISOShortMonthFullDayFormatTimeFormat: 'MMM Do HH:mm',
  ISOShortestMonthFormat: 'MM',
  ISOShortMonthDayYearFormat: 'MMM Do[,] YYYY',
  ISOShortMonthDayYearWithTimeFormat: 'MMM Do[,] YYYY [at] H:mm',
  ISOShortHourFormat: 'H:mm',
  ISOShortTimeFormat: 'HH:mm',
  ISOTimeFormat: 'HH:mm:ss',
  ISOWeekdayFormat: 'dddd'
}

export const Css = {
  ColorPrimary: 'rgb(0, 119, 228)',
  ColorGreen: 'rgb(22, 163, 74)',
  ColorRed: 'rgb(220, 38, 38)',
  ColorGraphAccPrimary: '#8884d8',
  ColorGraphAccSecondary: '#82ca9d',
  ColorGraphAccTertiary: '#bda74e',
  FontFamily: 'Inter, sans-serif',
}

export function CRUDAccountSchema(accInfo: AccountCreationInfo | undefined) {
  return z.object({
    defaultAccount: z.boolean(),
    balance: z.coerce.number().min(1, { message: 'Please enter a number between 1 and 999999999.' }).max(999999999, { message: 'Please enter a number between 1 and 999999999.' }),
    active: z.boolean(),
    name: z.string().min(3, { message: 'Please enter an Account name with a minimum of 3 characters.' }).max(75, { message: 'Please enter an Account name with at most 75 characters.' }),
    accountNumber: z.coerce.number().min(1, { message: 'Please enter a number between 1 and 99999999999.' }).max(99999999999, { message: 'Please enter a number between 1 and 99999999999.' }),
    currency: z.enum(safeConvertEnum(accInfo?.currencies?.map(item => item.code) ?? []), { message: 'Please select one of the given currencies.' }),
    broker: z.enum(safeConvertEnum(accInfo?.brokers?.map(item => item.code) ?? []), { message: 'Please select one of the given brokers.' }),
    accountType: z.enum(safeConvertEnum(accInfo?.accountTypes?.map(item => item.code) ?? []), { message: 'Please select one of the given Account types.' }),
    tradePlatform: z.enum(safeConvertEnum(accInfo?.platforms?.map(item => item.code) ?? []), { message: 'Please select one of the given trading platforms.' })
  })
}

export function CRUDTransactionSchema() {
  return z.object({
    date: z.date({
      required_error: "A transaction date is required.",
    }),
    type: z.enum(safeConvertEnum(['Deposit', 'Withdrawal']), { message: 'Please select a transaction type.' }),
    amount: z.coerce.number().min(1, { message: 'Please enter a number between 1 and 999999999.' }).max(999999999, { message: 'Please enter a number between 1 and 999999999.' }),
    account: z.coerce.number()
  })
}

export function CRUDUserSchema(editMode: boolean) {
  return z.object({
    firstName: z.string().min(3, { message: 'Please enter a first name with a minimum of 3 characters.' }).max(75, { message: 'Please enter a first name with at most 75 characters.' }),
    lastName: z.string().min(3, { message: 'Please enter a last name with a minimum of 3 characters.' }).max(75, { message: 'Please enter a last name with at most 75 characters.' }),
    username: z.string().min(3, { message: 'Please enter a username with a minimum of 3 characters.' }).max(75, { message: 'Please enter a username with at most 75 characters.' }).refine((val) => !hasUsername(val, editMode), { message: 'Username already in use. Please try another one.' }),
    email: z.string().email().refine((val) => !hasEmail(val, editMode), { message: 'Email already in use. Please try another one.' }),
    phoneType: z.enum(safeConvertEnum(['MOBILE', 'HOME', 'OTHER']), {message: 'Please select one of the given phone types.'}),
    telephoneNumber: z.string().min(10, { message: 'A phone number was be exactly 10 digits.' }).max(10, { message: 'A phone number must be exactly 10 digits.' }).transform((arg, ctx) => {
      const phone = parsePhoneNumberFromString(arg, {
        // set this to use a default country when the phone number omits country code
        defaultCountry: 'US',

        // set to false to require that the whole string is exactly a phone number,
        // otherwise, it will search for a phone number anywhere within the string
        extract: false,
      });

      // when it's good
      if (phone && phone.isValid()) {
        return phone.formatNational();
      }

      // when it's not
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        message: 'The phone number was invalid.',
      });

      return z.NEVER;
    }),
    password: z.string().min(8, { message: 'Please enter a password with a minimum of 8 characters.' }),
    confirmPassword: z.string().min(8, { message: 'Please enter a password with a minimum of 8 characters.' })
  }).superRefine(({ confirmPassword, password, username, email }, ctx) => {
    if (!isValidPassword(password)) {
      ctx.addIssue({
        code: 'custom',
        message: 'Password did not satisfy acceptance criteria. Please ensure your password contains a mix of letters, digits & symbols.',
        path: ['password'],
      })
    }

    if (confirmPassword !== password) {
      ctx.addIssue({
        code: "custom",
        message: "The passwords do not match. Please re-check your inputs",
        path: ['confirmPassword']
      });
    }

    if (hasUsername(username, editMode)) {
      ctx.addIssue({
        code: "custom",
        message: "That username is already taken, please try another.",
        path: ['username']
      });
    }

    if (hasEmail(email, editMode)) {
      ctx.addIssue({
        code: "custom",
        message: "That email is already in use, please try another.",
        path: ['email']
      });
    }
  })
}

export function LoginSchema() {
  return z.object({
    username: z.string().min(3, { message: 'Please enter a username with a minimum of 3 characters.' }).max(75, { message: 'Please enter a username with at most 75 characters.' }),
    password: z.string().min(8, { message: 'Please enter a password with a minimum of 8 characters.' })
  })
}

export function ForgotPasswordSchema() {
  return z.object({
    email: z.string().email({ message: 'Please enter a valid email address.' }),
  })
}

export function TradeImportSchema() {
  return z.object({
    filename: z
      .instanceof(FileList)
      .refine((file) => file?.length == 1, 'File is required.'),
  })
}