//  each page has a header section that can contain an icon, this is the icon's size
import { z } from "zod";
import { safeConvertEnum } from "@/lib/functions/util-functions";
import { hasEmail, hasUsername } from "@/lib/functions/account-functions";
import {
  getAccountDomain,
  getAnalysisDomain,
  getChartDomain,
  getJobDomain,
  getMarketPriceDomain,
  getNewsDomain,
  getPortfolioDomain,
  getPortfolioRecordDomain,
  getSymbolDomain,
  getSystemDomain,
  getTradeDomain,
  getTradeRecordDomain,
  getUserDomain,
  isValidPassword,
} from "@/lib/functions/security-functions";
import { AccountCreationInfo } from "@/types/apiTypes";

export const DEFAULT_PAGE_HEADER_SECTION_ICON_SIZE = 36;

export const ISO_TIME_REGEX = /^(?:[01]?\d|2[0-3]):[0-5]\d(?::[0-5]\d)?$/;

export const BASE_COLORS = [
  "red",
  "green",
  "blue",
  "orange",
  "pink",
  "grey",
  "black",
];

export const ApiCredentials = {
  AuthHeader: "fp-api_token",
  TestAccountNumber: "28331289",
  TestUserToken:
    "Zmxvd2VycG90X2FwaV90b2tlbiZzLnByaXppb0Bob3RtYWlsLmNvbSYyMDI0LTExLTIwVDEzOjU2OjE1",
};

export const ApiUrls = {
  Account: {
    CreateAccount:
      getAccountDomain() + "/create-account?portfolioNumber={portfolioNumber}",
    UpdateAccount:
      getAccountDomain() +
      "/update-account?portfolioNumber={portfolioNumber}&accountNumber={accountNumber}",
    DeleteAccount:
      getAccountDomain() +
      "/delete-account?portfolioNumber={portfolioNumber}&accountNumber={accountNumber}",
    GetCurrencies: getAccountDomain() + "/currencies",
    GetAccountTypes: getAccountDomain() + "/account-types",
    GetBrokers: getAccountDomain() + "/brokers",
    GetTradePlatforms: getAccountDomain() + "/trade-platforms",
    GetDetails:
      getAccountDomain() + "/get-details?accountNumber={accountNumber}",
  },
  Analysis: {
    TimeBuckets:
      getAnalysisDomain() +
      "/time-buckets?accountNumber={accountNumber}&filter={filter}&isOpened={isOpened}",
    TradeDuration:
      getAnalysisDomain() +
      "/trade-durations?accountNumber={accountNumber}&filter={filter}&tradeDurationFilter={tradeDurationFilter}",
    Weekdays:
      getAnalysisDomain() +
      "/weekdays?accountNumber={accountNumber}&filter={filter}",
    WeekdaysTimeBuckets:
      getAnalysisDomain() +
      "/weekdays-time-buckets?accountNumber={accountNumber}&weekday={weekday}&filter={filter}",
  },
  Charting: {
    Get:
      getChartDomain() +
      "/apex-data?tradeId={tradeId}&accountNumber={accountNumber}&interval={interval}",
  },
  Job: {
    GetJobById: getJobDomain() + "/get-by-id?jobId={jobId}",
    GetJobsByStatusAndTypePaged:
      getJobDomain() +
      "/get-status-type-paged?start={start}&end={end}&jobStatus={jobStatus}&jobType={jobType}&page={page}&pageSize={pageSize}&sort={sort}",
    GetJobTypes: getJobDomain() + "/get-job-types",
  },
  MarketPrice: {
    GetTimeIntervals: getMarketPriceDomain() + "/time-intervals",
  },
  News: {
    GetNews: getNewsDomain() + "/get-for-interval?start={start}&end={end}",
    FetchNews: getNewsDomain() + "/fetch-news",
  },
  Portfolio: {
    GetPortfolio:
      getPortfolioDomain() + "/get?portfolioNumber={portfolioNumber}",
    CreatePortfolio: getPortfolioDomain() + "/create-portfolio",
    DeletePortfolio:
      getPortfolioDomain() +
      "/delete-portfolio?portfolioNumber={portfolioNumber}",
    UpdatePortfolio:
      getPortfolioDomain() +
      "/update-portfolio?portfolioNumber={portfolioNumber}",
  },
  PortfolioRecord: {
    GetPortfolioRecord:
      getPortfolioRecordDomain() + "/get?portfolioNumber={portfolioNumber}",
  },
  Symbol: {
    GetTradedSymbols:
      getSymbolDomain() + "/get-traded-symbols?accountNumber={accountNumber}",
  },
  System: {
    HealthCheck: getSystemDomain() + "/healthcheck",
  },
  Trade: {
    CreateTrade:
      getTradeDomain() + "/create-trade?accountNumber={accountNumber}",
    UpdateTrade:
      getTradeDomain() +
      "/update-trade?accountNumber={accountNumber}&tradeId={tradeId}",
    DeleteTrade:
      getTradeDomain() +
      "/delete-trade?accountNumber={accountNumber}&tradeId={tradeId}",
    GetTradeForTradeId:
      getTradeDomain() +
      "/get-for-trade-id?accountNumber={accountNumber}&tradeId={tradeId}",
    GetPagedTrades:
      getTradeDomain() +
      "/get-for-interval-paged?accountNumber={accountNumber}&start={start}&end={end}&page={page}&pageSize={pageSize}&tradeType={tradeType}&symbol={symbol}&sort={sort}",
    GetInsights:
      getTradeDomain() +
      "/get-trade-insights?accountNumber={accountNumber}&tradeId={tradeId}",
    ImportTrades:
      getTradeDomain() +
      "/import-trades?accountNumber={accountNumber}&isStrategy={isStrategy}",
  },
  TradeRecord: {
    GetTradeRecords:
      getTradeRecordDomain() +
      "/get-for-interval?accountNumber={accountNumber}&start={start}&end={end}&interval={interval}&count={count}",
    GetRecentTradeRecords:
      getTradeRecordDomain() +
      "/get-recent?accountNumber={accountNumber}&interval={interval}&count={count}",
    GetTradeLog:
      getTradeRecordDomain() +
      "/trade-log?start={start}&end={end}&interval={interval}&count={count}",
    GetTradeRecordControls:
      getTradeRecordDomain() +
      "/get-trade-record-controls?accountNumber={accountNumber}&interval={interval}",
  },
  User: {
    GetRecentTransactions: getUserDomain() + "/recent-transactions",
    GetUser: getUserDomain() + "/get?username={username}",
    UpdateUser: getUserDomain() + "/update?username={username}",
    RegisterUser: getUserDomain() + "/create",
  },
};

export const DateTime = {
  DatePickerDateFormat: "dd/MM/yyyy",
  ISODateFormat: "YYYY-MM-DD",
  ISODateLongMonthFormat: "YYYY-MMMM-DD",
  ISODateTimeFormat: "YYYY-MM-DDTHH:mm:ss",
  ISODayFormat: "Do",
  ISOEasyDateTimeFormat: "YYYY-MM-DD HH:mm:ss",
  ISOLongMonthDayYearFormat: "MMMM Do[,] YYYY",
  ISOLongMonthDayYearWithTimeFormat: "MMMM Do[,] YYYY [at] H:mm",
  ISOMonthDayFormat: "MMMM Do",
  ISOMonthFormat: "MMMM",
  ISOMonthWeekDayFormat: "dddd[,] MMMM Do",
  ISOShortMonthWeekDayFormat: "dddd[,] MMM D",
  ISOYearFormat: "YYYY",
  ISOMonthYearFormat: "MMMM YYYY",
  ISOShortMonthFormat: "MMM",
  ISOShortMonthDayFormat: "MMM D",
  ISOShortMonthFullDayFormat: "MMM Do",
  ISOShortMonthFullDayFormatTimeFormat: "MMM Do HH:mm",
  ISOShortestMonthFormat: "MM",
  ISOShortMonthDayYearFormat: "MMM Do[,] YYYY",
  ISOShortMonthDayYearWithTimeFormat: "MMM Do[,] YYYY [at] H:mm",
  ISOShortMonthShortDayYearWithTimeFormat: "MMM D[,] YYYY [at] H:mm",
  ISOShortHourFormat: "H:mm",
  ISOShortTimeFormat: "HH:mm",
  ISOTimeFormat: "HH:mm:ss",
  ISOWeekdayFormat: "dddd",
};

export const Css = {
  ColorPrimary: "rgb(0, 119, 228)",
  ColorGreen: "rgb(22, 163, 74)",
  ColorRed: "rgb(220, 38, 38)",
  ColorGraphAccPrimary: "#8884d8",
  ColorGraphAccSecondary: "#82ca9d",
  ColorGraphAccTertiary: "#bda74e",
  ColorWhite: "#FFFFFF",
  ColorFontPrimary: "rgb(100, 116, 139)",
  FontFamily: "Inter, sans-serif",
  SelectItemStyles: "hover:bg-primary/5 hover:cursor-pointer",
};

export function CRUDPortfolioSchema() {
  return z.object({
    isDefault: z.boolean(),
    active: z.boolean(),
    name: z
      .string()
      .min(3, {
        message:
          "Please enter a portfolio name with a minimum of 3 characters.",
      })
      .max(75, {
        message: "Please enter a portfolio name with at most 75 characters.",
      }),
  });
}

export function CRUDAccountSchema(accInfo: AccountCreationInfo | undefined) {
  return z
    .object({
      isDefault: z.boolean(),
      balance: z.coerce
        .number()
        .min(1, { message: "Please enter a number between 1 and 999999999." })
        .max(999999999, {
          message: "Please enter a number between 1 and 999999999.",
        }),
      active: z.boolean(),
      name: z
        .string()
        .min(3, {
          message:
            "Please enter an account name with a minimum of 3 characters.",
        })
        .max(75, {
          message: "Please enter an account name with at most 75 characters.",
        }),
      number: z.coerce
        .number()
        .min(1, { message: "Please enter a number between 1 and 99999999999." })
        .max(99999999999, {
          message: "Please enter a number between 1 and 99999999999.",
        }),
      currency: z.enum(
        safeConvertEnum(accInfo?.currencies?.map((item) => item.code) ?? []),
        { message: "Please select one of the given currencies." },
      ),
      broker: z.enum(
        safeConvertEnum(accInfo?.brokers?.map((item) => item.code) ?? []),
        { message: "Please select one of the given brokers." },
      ),
      type: z.enum(
        safeConvertEnum(accInfo?.accountTypes?.map((item) => item.code) ?? []),
        { message: "Please select one of the given Account types." },
      ),
      tradePlatform: z.enum(
        safeConvertEnum(accInfo?.platforms?.map((item) => item.code) ?? []),
        { message: "Please select one of the given trading platforms." },
      ),
      isLegacy: z.boolean(),
      accountOpenTime: z
        .date({
          required_error: "A transaction date is required.",
        })
        .nullable()
        .optional(),
      accountCloseTime: z
        .date({
          required_error: "A transaction date is required.",
        })
        .nullable()
        .optional(),
    })
    .superRefine((data, ctx) => {
      if (data.isLegacy) {
        if (!data.accountOpenTime) {
          ctx.addIssue({
            path: ["accountOpenTime"],
            code: z.ZodIssueCode.custom,
            message: "Open time is required for legacy accounts.",
          });
        }

        if (data.accountOpenTime && data.accountCloseTime) {
          if (data.accountCloseTime <= data.accountOpenTime) {
            ctx.addIssue({
              path: ["accountCloseTime"],
              code: z.ZodIssueCode.custom,
              message: "Close time must be after open time.",
            });
          }
        }
      }
    });
}

export function CRUDTransactionSchema() {
  return z.object({
    date: z.date({
      required_error: "A transaction date is required.",
    }),
    type: z.enum(safeConvertEnum(["DEPOSIT", "WITHDRAWAL"]), {
      message: "Please select a transaction type.",
    }),
    amount: z.coerce
      .number()
      .min(1, { message: "Please enter a number between 1 and 999999999." })
      .max(999999999, {
        message: "Please enter a number between 1 and 999999999.",
      }),
    account: z.coerce.number(),
  });
}

export function CRUDUserSchema(editMode: boolean) {
  return z
    .object({
      firstName: z
        .string()
        .min(3, {
          message: "Please enter a first name with a minimum of 3 characters.",
        })
        .max(75, {
          message: "Please enter a first name with at most 75 characters.",
        }),
      lastName: z
        .string()
        .min(3, {
          message: "Please enter a last name with a minimum of 3 characters.",
        })
        .max(75, {
          message: "Please enter a last name with at most 75 characters.",
        }),
      username: z
        .string()
        .min(3, {
          message: "Please enter a username with a minimum of 3 characters.",
        })
        .max(75, {
          message: "Please enter a username with at most 75 characters.",
        })
        .refine((val) => !hasUsername(val, editMode), {
          message: "Username already in use. Please try another one.",
        }),
      email: z
        .string()
        .email()
        .refine((val) => !hasEmail(val, editMode), {
          message: "Email already in use. Please try another one.",
        }),
      password: z.string().min(8, {
        message: "Please enter a password with a minimum of 8 characters.",
      }),
      confirmPassword: z.string().min(8, {
        message: "Please enter a password with a minimum of 8 characters.",
      }),
    })
    .superRefine(({ confirmPassword, password, username, email }, ctx) => {
      if (!isValidPassword(password)) {
        ctx.addIssue({
          code: "custom",
          message:
            "Password did not satisfy acceptance criteria. Please ensure your password contains a mix of letters, digits & symbols.",
          path: ["password"],
        });
      }

      if (confirmPassword !== password) {
        ctx.addIssue({
          code: "custom",
          message: "The passwords do not match. Please re-check your inputs",
          path: ["confirmPassword"],
        });
      }

      if (hasUsername(username, editMode)) {
        ctx.addIssue({
          code: "custom",
          message: "That username is already taken, please try another.",
          path: ["username"],
        });
      }

      if (hasEmail(email, editMode)) {
        ctx.addIssue({
          code: "custom",
          message: "That email is already in use, please try another.",
          path: ["email"],
        });
      }
    });
}

export function LoginSchema() {
  return z.object({
    username: z
      .string()
      .min(3, {
        message: "Please enter a username with a minimum of 3 characters.",
      })
      .max(75, {
        message: "Please enter a username with at most 75 characters.",
      }),
    password: z.string().min(8, {
      message: "Please enter a password with a minimum of 8 characters.",
    }),
  });
}

export function ForgotPasswordSchema() {
  return z.object({
    email: z.string().email({ message: "Please enter a valid email address." }),
  });
}

export function TradeImportSchema() {
  return z.object({
    isStrategy: z.boolean().default(false),
    filename: z
      .instanceof(FileList)
      .refine((file) => file?.length == 1, "File is required."),
  });
}

export function CRUDTradeSchema() {
  return z
    .object({
      tradeId: z.string().optional(),
      product: z
        .string()
        .min(3, {
          message: "Please enter a symbol with a minimum of 3 characters.",
        })
        .max(25, {
          message: "Please enter a symbol with at most 25 characters.",
        }),
      tradePlatform: z.string(),
      tradeType: z.enum(safeConvertEnum(["BUY", "SELL"]), {
        message: "Please select a trade type.",
      }),
      tradeOpenTime: z.date({
        required_error: "A date & time is required when opening a trade.",
      }),
      tradeCloseTime: z.date().nullable().optional(),
      openTime: z
        .string()
        .regex(ISO_TIME_REGEX, "Time must be in the format of hh:mm:ss"),
      closeTime: z
        .string()
        .regex(ISO_TIME_REGEX, "Time must be in the format of hh:mm:ss"),
      lotSize: z.coerce
        .number()
        .min(0.01, {
          message: "Please enter a number between 0.01 and 999999.",
        })
        .max(999999, {
          message: "Please enter a number between 0.01 and 999999.",
        }),
      openPrice: z.coerce
        .number()
        .min(0.01, {
          message: "Please enter a number between 0.01 and 999999.",
        })
        .max(999999, {
          message: "Please enter a number between 0.01 and 999999.",
        }),
      closePrice: z.coerce
        .number()
        .max(999999999, {
          message: "Please enter a number between 0 and 999999999.",
        })
        .optional(),
      netProfit: z.coerce
        .number()
        .min(-999999999, {
          message: "Please enter a number between -999999999 and 999999999.",
        })
        .max(999999999, {
          message: "Please enter a number between -999999999 and 999999999.",
        })
        .optional(),
      stopLoss: z.coerce
        .number()
        .max(999999999, {
          message: "Please enter a number between 0 and 999999999.",
        })
        .optional(),
      takeProfit: z.coerce
        .number()
        .max(999999999, {
          message: "Please enter a number between 0 and 999999999.",
        })
        .optional(),
    })
    .superRefine(
      ({ tradeOpenTime, tradeCloseTime, closePrice, netProfit }, ctx) => {
        const isClosePriceSet = closePrice !== 0;
        const isCloseTimeSet =
          tradeCloseTime !== null && tradeCloseTime !== undefined;
        const isNetProfitSet = netProfit !== 0;

        if (
          (isClosePriceSet && !isCloseTimeSet) ||
          (!isClosePriceSet && isCloseTimeSet)
        ) {
          ctx.addIssue({
            code: "custom",
            message:
              "You must provide both a close price and close time, or neither.",
            path: ["closePrice"],
          });
          ctx.addIssue({
            code: "custom",
            message:
              "You must provide both a close price and close time, or neither.",
            path: ["tradeCloseTime"],
          });
        }

        if (isCloseTimeSet && isClosePriceSet && !isNetProfitSet) {
          ctx.addIssue({
            code: "custom",
            message:
              "You must provide a net profit when setting a closed trade",
            path: ["netProfit"],
          });
        }

        if (isCloseTimeSet && tradeCloseTime < tradeOpenTime) {
          ctx.addIssue({
            code: "custom",
            message: "Close time cannot come before the open time",
            path: ["tradeOpenTime"],
          });
          ctx.addIssue({
            code: "custom",
            message: "Close time cannot come before the open time",
            path: ["tradeCloseTime"],
          });
        }
      },
    );
}
