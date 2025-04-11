package com.bluebell.platform.constants;

import java.time.LocalDate;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

/**
 * Constants defined for the bluebell platform
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
public class CorePlatformConstants {

    /**
     * Generic message used when displaying an exception thrown from a class that should not have been instantiated
     */
    public static final String NO_INSTANTIATION = "%s classes should not be instantiated";

    private CorePlatformConstants() {
        throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
    }

    public static final String OS_PROFILE_ARGUMENT = "--os-profile";

    /**
     * Represents a value that when encountered will basically act as a non-factor when returning a limited collection of entries. This value is akin
     * to asking the collection to not have a size limit, i.e. show me all results
     */
    public static final int MAX_RESULT_SIZE = -1;

    /**
     * Represents the lowest supported date in the system
     */
    public static final LocalDate MIN_DATE = LocalDate.of(1970, 1, 1);

    /**
     * Represents the highest supported date in the system
     */
    public static final LocalDate MAX_DATE = LocalDate.of(2201, 1, 1);

    /**
     * Represents the maximum allowable calendar year value
     */
    public static final int MAX_CALENDAR_YEAR = Year.MAX_VALUE;

    /**
     * The default date format
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * The default short time format
     */
    public static final String SHORT_TIME_FORMAT = "HH:mm";

    /**
     * The default time format
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * The default date & time format
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * MT4 date format
     */
    public static final String MT4_DATE_FORMAT = "yyyy.MM.dd";

    /**
     * MT4 time format
     */
    public static final String MT4_TIME_FORMAT = "H:mm";

    /**
     * MT4 date & time format
     */
    public static final String MT4_DATE_TIME_FORMAT = "yyyy.MM.dd HH:mm:ss";

    /**
     * MT4 short date & time format
     */
    public static final String MT4_DATE_SHORT_TIME_FORMAT = "yyyy.MM.dd H:mm";

    /**
     * The default Eastern Timezone
     */
    public static final String EASTERN_TIMEZONE = "America/Toronto";

    /**
     * MetaTrader4 uses a specific timezone which is the Eastern European Timezone
     */
    public static final String METATRADER4_TIMEZONE = "EET";

    /**
     * Default size for trade records
     */
    public static final int DEFAULT_TRADE_RECORD_COLLECTION_SIZE = 10;

    /**
     * The current risk-free rate based on the 10-yr Government of Canada bonds
     */
    public static final double RISK_FREE_RATE_CANADA = 3.26;


    /**
     * A collection of random english words
     */
    public static final List<String> RANDOM_WORDS = Arrays.asList(
            "apple", "banana", "cherry", "dog", "elephant", "forest", "galaxy", "horizon", "island", "jungle", "keyboard", "lemon", "mountain", "notebook", "ocean", "pencil", "quasar", "rainbow", "sunflower", "tiger",
            "umbrella", "volcano", "whale", "xylophone", "yellow", "zebra", "adventure", "butterfly", "candle", "diamond", "engine", "fountain", "giraffe", "happiness", "igloo", "jigsaw", "kangaroo", "lighthouse", "mystery",
            "necklace", "orchestra", "penguin", "quicksand", "rocket", "snowflake", "treasure", "universe", "voyage", "waterfall", "xenon", "yogurt", "zeppelin", "ancient", "bridge", "crystal", "dragon", "emerald", "firework",
            "gondola", "harvest", "illusion", "square", "koala", "labyrinth", "meadow", "nectar", "octopus", "puzzle", "quiver", "riddle", "starlight", "twilight", "utopia", "vortex", "wanderlust", "x-ray", "yawn", "zenith",
            "asteroid", "blizzard", "comet", "dew", "echo", "fjord", "glacier", "monkey", "inferno", "journey", "kaleidoscope", "lagoon", "miracle", "nebula", "obsidian", "paradox", "quagmire", "resonance", "symphony",
            "tornado", "umbra", "vivid", "whisper", "xenophile", "yearn", "zephyr", "arcade", "breeze", "cavern", "dungeon", "enigma", "fantasy", "goblin", "hologram", "incantation", "jubilant", "kingdom", "lantern",
            "mystic", "nebular", "oracle", "potion", "quest", "realm", "sorcery", "trance", "unravel", "vista", "wizard", "xanadu", "yonder", "zodiac", "alchemy", "bonfire", "catacomb", "dagger", "eclipse", "firefly",
            "gargoyle", "honeycomb", "illusionist", "jade", "knight", "labyrinthine", "memento", "nightshade", "omen", "phantom", "quill", "runes", "scepter", "tapestry", "undertow", "vanguard", "warlock", "xylophonist",
            "yew", "zither", "adorn", "banquet", "cascade", "dahlia", "ember", "fable", "glisten", "harlequin", "iridescent", "jewel", "kismet", "lullaby", "murmur", "nostalgia", "opulent", "periwinkle", "quaint",
            "reverie", "serendipity", "talisman", "unison", "velvet", "whimsy", "xylotomy", "yearning", "zeal", "amber", "basilisk", "charm", "destiny", "epiphany", "flourish", "grace", "halo", "intrigue", "journeyman",
            "kindred", "luminous", "mirage", "nymph", "flower", "paragon", "quiescent", "rhapsody", "sapphire", "tranquil", "uplift", "valor", "wanderer", "xenial", "yesteryear", "zealot", "apex", "bravado", "cosmic",
            "daring", "elixir", "fervor", "genesis", "bottle", "inspire", "jubilee", "karma", "lotus", "momentum", "novella", "oblivion", "phenomenon", "quintessence", "renaissance", "solstice", "threshold", "utmost",
            "verity", "wistful", "xenogenesis", "yarn", "zen", "aurora", "bliss", "cipher", "delight", "essence", "felicity", "gossamer", "harmony", "infinity", "joyous", "kaleidoscopic", "lore", "melody", "nexus",
            "orison", "pulse", "tree", "radiance", "serene", "plane", "uplifting", "vividly", "wisp", "x-factor", "yoga", "zen-like"
    );

    /**
     * Validation messages used throughout the system, organized according to the entity packages
     */
    public static class Validation {

        private Validation() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static class Account {

            private Account() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String ACCOUNT_CANNOT_BE_NULL = "account cannot be null";

            public static final String ACCOUNT_NOT_FOUND = "Account not found";
        }

        public static class Action {

            private Action() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String ACTION_CANNOT_BE_NULL = "action cannot be null";
            public static final String PERFORMABLE_ACTION_CANNOT_BE_NULL = "performable action cannot be null";
        }

        public static class Email {

            private Email() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String TO_CANNOT_BE_NULL = "to cannot be null";

            public static final String SUBJECT_CANNOT_BE_NULL = "subject cannot be null";

            public static final String EMAIL_TEMPLATE_CANNOT_BE_NULL = "email template cannot be null";
        }

        public static class Job {

            private Job() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String JOB_CANNOT_BE_NULL = "job cannot be null";

            public static final String JOB_ID_CANNOT_BE_NULL = "job id cannot be null";

            public static final String JOB_STATUS_CANNOT_BE_NULL = "job status cannot be null";

            public static final String JOB_TYPE_CANNOT_BE_NULL = "job type cannot be null";
        }

        public static class Portfolio {

            private Portfolio() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String PORTFOLIO_CANNOT_BE_NULL = "portfolio cannot be null";
        }

        public static class Security {

            private Security() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String API_TOKEN_CANNOT_BE_NULL = "apiToken cannot be null";

            public static class User {

                private User() {
                    throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
                }

                public static final String USER_CANNOT_BE_NULL = "user cannot be null";

                public static final String USERNAME_CANNOT_BE_NULL = "username cannot be null";

                public static final String EMAIL_CANNOT_BE_NULL = "email cannot be null";

                public static final String USER_DATE_REGISTERED_CANNOT_BE_NULL = "user's registration date cannot be null";
            }
        }

        public static class System {

            private System() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String TIME_INTERVAL_CANNOT_BE_NULL = "time interval cannot be null";

            public static class PhoneNumber {

                private PhoneNumber() {
                    throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
                }

                public static final String PHONE_NUMBER_CANNOT_BE_NULL = "phone number cannot be null";

                public static final String PHONE_TYPE_CANNOT_BE_NULL = "phone type cannot be null";

                public static final String COUNTRY_CODE_CANNOT_BE_NEGATIVE = "country code cannot be a negative number";

                public static final String TELEPHONE_NUMBER_CANNOT_BE_NEGATIVE = "telephone number cannot be a negative number";

            }

            public static class IncomingPing {

                private IncomingPing() {
                    throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
                }

                public static final String INCOMING_PING_CANNOT_BE_NULL = "incoming ping cannot be null";

                public static final String SYSTEM_NAME_CANNOT_BE_NULL = "system name cannot be null";
            }
        }

        public static class Trade {

            private Trade() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String TRADE_CANNOT_BE_NULL = "trade cannot be null";

            public static final String TRADE_TYPE_CANNOT_BE_NULL = "tradeType cannot be null";

            public static final String TRADE_ID_CANNOT_BE_NULL = "tradeId cannot be null";

            public static final String IMPORT_STREAM_CANNOT_BE_NULL = "import stream cannot be null";
        }

        public static class DataIntegrity {

            private DataIntegrity() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String INTERVAL_CANNOT_BE_NULL = "interval cannot be null";

            public static final String UID_CANNOT_BE_NULL = "uid cannot be null";

            public static final String INVALID_INTERVAL = "%s was not a valid interval";

            public static final String INVALID_FILTER = "%s was not a valid filter";

            public static final String INVALID_WEEKDAY = "%s was not a valid weekday";

            public static final String BAD_LOCALE_ENUM = "One of the given locales was not a valid Currency or Country";
        }

        public static class DateTime {

            private DateTime() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String START_DATE_CANNOT_BE_NULL = "start date cannot be null";

            public static final String END_DATE_CANNOT_BE_NULL = "end date cannot be null";

            public static final String DATE_CANNOT_BE_NULL = "date cannot be null";

            public static final String START_DATE_INVALID_FORMAT = "The start date %s was not of the expected format %s";

            public static final String END_DATE_INVALID_FORMAT = "The end date %s was not of the expected format %s";

            public static final String MUTUALLY_EXCLUSIVE_DATES = "start date was after end date or vice versa";
        }

        public static class MarketPrice {

            private MarketPrice() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String AGGREGATED_PRICES_CANNOT_BE_NULL = "aggregated prices cannot be null";

            public static final String DATA_SOURCE_CANNOT_BE_NULL = "data source cannot be null";

            public static final String MARKET_PRICE_TIME_INTERVAL_CANNOT_BE_NULL = "market price time interval cannot be null";
        }
    }

    /**
     * Regex strings & expressions
     */
    public static class Regex {

        private Regex() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        /**
         * Global Phone number regex
         * (1) = country, (2) = area code, (3) = exchange, (4) = subscriber, (5) = extension where (x) indicates matcher group
         */
        public static final String PHONE_NUMBER_REGEX = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";

        public static final String MARKET_PRICE_VALID_SYMBOL_REGEX = "^[a-zA-Z][a-zA-Z0-9.!]*$";

        public static final String EMAIL_TEMPLATE_INTERPOLATION = "\\$\\{[a-zA-Z_]+\\}";

        public static class Import {

            private Import() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String MT4_HTML_TABLE_CELL = "<td.*?>(.*?)<\\/td>";

            public static final String MT4_HTML_TABLE_CELL_START = "<td.*?>";

            public static final String MT4_HTML_TABLE_CELL_END = "</td>";

            public static final String MT4_HTML_TABLE_ROW = "<tr.*?>(.*?)<\\/tr>";

            public static final String MT4_HTML_TABLE_ROW_START = "<tr.*?>";

            public static final String MT4_HTML_TABLE_ROW_END = "</tr>";
        }
    }

    /**
     * Email header & body templates
     */
    public static class EmailTemplates {

        private EmailTemplates() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String SIMPLE_TEMPLATE =
                """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Email Notification</title>
                            <style>
                                body {
                                    font-family: Arial, sans-serif;
                                    background-color: #f4f4f4;
                                    margin: 0;
                                    padding: 0;
                                }
                                .container {
                                    width: 100%;
                                    max-width: 600px;
                                    margin: 20px auto;
                                    background: #ffffff;
                                    padding: 20px;
                                    border-radius: 8px;
                                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                                }
                                .content {
                                    font-size: 16px;
                                    color: #333;
                                }
                                .footer {
                                    margin-top: 20px;
                                    font-size: 12px;
                                    color: #777;
                                    text-align: center;
                                    border-top: 1px solid #ddd;
                                    padding-top: 10px;
                                }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="content">
                                    ${body}
                                </div>
                                <div class="footer">
                                    <p>This is an autogenerated email. Please do not reply to this address.</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """;

        public static final String FAILED_JOB_TEMPLATE =
                """
                        <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Job Failure Notification</title>
                            <style>
                                body {
                                    font-family: Arial, sans-serif;
                                    background-color: #f4f4f4;
                                    margin: 0;
                                    padding: 0;
                                }
                                .container {
                                    width: 100%;
                                    max-width: 600px;
                                    margin: 20px auto;
                                    background: #ffffff;
                                    padding: 20px;
                                    border-radius: 8px;
                                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                                }
                                .header {
                                    font-size: 18px;
                                    font-weight: bold;
                                    color: #d9534f;
                                }
                                .content {
                                    font-size: 16px;
                                    color: #333;
                                    margin-top: 10px;
                                }
                                .details {
                                    font-size: 14px;
                                    background: #f8d7da;
                                    color: #721c24;
                                    padding: 10px;
                                    border-radius: 5px;
                                    margin-top: 10px;
                                }
                                .footer {
                                    margin-top: 20px;
                                    font-size: 12px;
                                    color: #777;
                                    text-align: center;
                                    border-top: 1px solid #ddd;
                                    padding-top: 10px;
                                }
                            </style>
                        </head>
                        <body>
                        <div class="container">
                            <div class="header">⚠ Job Failure Notification</div>
                            <div class="content">
                                <p>The following job has encountered an error:</p>
                                <div class="details">
                                    <p><strong>Job Name:</strong> ${jobName}</p>
                                    <p><strong>Failed At:</strong> ${timestamp}</p>
                                    <p><strong>Error Message:</strong> ${errorDetails}</p>
                                </div>
                                <p>Please investigate the issue and take necessary action.</p>
                            </div>
                            <div class="footer">
                                <p>This is an autogenerated email. Please do not reply to this address.</p>
                            </div>
                        </div>
                        </body>
                        </html>
                        """;
    }
}
