/**
 * App icons represented as icons
 */
export enum Icons {
  LayoutDashboard = "dashboard",
  PieChart = "account-overview",
  UserCircle = "user-profile",
  News = "market-news",
  ChartScatter = "performance",
  Logout = "logout",
  ArrowUp = "arrow-up",
  ArrowLeft = "arrow-left",
  ArrowRight = "arrow-right",
  ArrowDown = "arrow-down",
  ArrowLeftRight = "arrow-left-right",
  ChartDoughnutFilled = "chart-doughnut",
  ReplaceFilled = "replace",
  ArrowBarDown = "arrow-bar-down",
  ArrowBarUp = "arrow-bar-up",
  Mountain = "mountain",
  Search = "analysis",
  Briefcase = "portfolios",
  Brain = "jobs-overview",
  BrandReact = "job",
  CirclePlus = "circle-plus",
  Flag3Filled = "default-icon",
  Edit = "edit",
  Trash = "trash",
  ExternalLink = "external-link",
  InfoSquareRoundedFilled = "icon-info-square-filled",
  CircleCheckFilled = "icon-circle-check-filled",
  BrandGoogleFilled = "brand-google-filled",
  BrandAppleFilled = "brand-apple-filled",
  BrandFacebookFilled = "brand-facebook-filled",
  XboxX = "xbox-x",
  XboxXFilled = "xbox-x-filled",
  Menu2 = "menu-2",
  MailFilled = "mail-filled",
  PointFilled = "point-filled",
  AlertTriangleFilled = "alert-triangle-outlined",
  HelpSquareRounded = "help-square-rounded",
  CalendarMonth = "calendar-month",
  X = "x",
  CircleCheck = "circle-check",
  CircleMinus = "circle-minus",
  Database = "database",
  Logs = "logs",
  MeteorFilled = "meteor-filled",
  Planet = "planet",
  BarrierBlock = "barrier-block",
}

/**
 * Enum representing Trade record intervals for aggregating data
 */
export class AggregateInterval {
  static readonly DAILY = new AggregateInterval("DAILY", "Daily");
  static readonly MONTHLY = new AggregateInterval("MONTHLY", "Monthly");
  static readonly YEARLY = new AggregateInterval("YEARLY", "Yearly");

  // private to disallow creating other instances of this type
  private constructor(
    public readonly code: string,
    public readonly label: string,
  ) {}

  public static get(val: string): AggregateInterval {
    switch (val.toUpperCase()) {
      case "DAILY":
        return this.DAILY;
      case "MONTHLY":
        return this.MONTHLY;
      default:
        return this.YEARLY;
    }
  }
}
