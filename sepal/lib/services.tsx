import {IconChartBar, IconChartLine, IconChartPie, IconLogout2, IconNews, IconUserCircle} from "@tabler/icons-react";
import {Icons} from "@/lib/enums";

/**
 * Returns the correct icon based on the given enum value
 *
 * @param iconCode icon code
 * @param className optional css classes
 * @param iconSize optional icon size
 */
export function resolveIcon(iconCode: string, className = '', iconSize = 24) {
  switch (iconCode) {
    case Icons.TradingAccounts:
      return <IconChartBar className={className} size={iconSize} />;
    case Icons.AccountOverview:
      return <IconChartPie className={className} size={iconSize} />;
    case Icons.UserProfile:
      return <IconUserCircle className={className} size={iconSize} />;
    case Icons.MarketNews:
      return <IconNews className={className} size={iconSize} />;
    case Icons.Performance:
      return <IconChartLine className={className} size={iconSize} />;
    case Icons.Logout:
      return <IconLogout2 className={className} size={iconSize} />;
    default:
      return null;
  }
}