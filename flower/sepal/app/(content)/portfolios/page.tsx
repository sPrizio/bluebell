"use client";

import { Icons } from "@/lib/enums";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";

/**
 * The page that shows all of a user's portfolios
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function PortfoliosPage() {
  const pageInfo = {
    title: "Portfolios",
    subtitle: "A list of all of your portfolios.",
    iconCode: Icons.Portfolios,
    breadcrumbs: [
      { label: "Dashboard", href: "/dashboard", active: false },
      {
        label: `Portfolios`,
        href: "/portfolios",
        active: true,
      },
    ],
  };

  //  RENDER

  return (
    <PageInfoProvider value={pageInfo}>
      <div className={"text-center"}>
        This will be the portfolio listing page. This work will be completed in
        item BB-54.
      </div>
      <div>
        <br />
        Items to note:
        <br />
        <ul>
          <li>
            This page will be redirected to if a user registers and does not
            have any active portfolios
          </li>
          <li>
            A special state will need to be shown to force adding a new
            portfolio. When in this state, the breadcrumbs should not allow
            going back to dashboard
          </li>
        </ul>
      </div>
    </PageInfoProvider>
  );
}
