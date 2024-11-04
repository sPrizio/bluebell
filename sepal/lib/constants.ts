//  each page has a header section that can contain an icon, this is the icon's size
import {z} from "zod";
import {safeConvertEnum} from "@/lib/functions";

export const DEFAULT_PAGE_HEADER_SECTION_ICON_SIZE = 36;

export const BASE_COLORS = ['red', 'green', 'blue', 'orange', 'pink', 'grey', 'black']

export const ApiCredentials = {
  AuthHeader: 'fp-api_token',
  TestAccountNumber: '28331289',
  TestUserToken: 'Zmxvd2VycG90X2FwaV90b2tlbiZzLnByaXppb0Bob3RtYWlsLmNvbSYyMDI0LTA1LTAxVDEyOjE2OjQ3',
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