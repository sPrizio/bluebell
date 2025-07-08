import { Portfolio } from "@/types/apiTypes";
import { TableCell, TableHead, TableRow } from "@/components/ui/table";
import moment from "moment/moment";
import { DateTime } from "@/lib/constants";
import { Button } from "@/components/ui/button";
import React from "react";
import BaseModal from "@/components/Modal/BaseModal";
import DeletePortfolioForm from "@/components/Form/Portfolio/DeletePortfolioForm";
import PortfolioForm from "@/components/Form/Portfolio/PortfolioForm";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";
import BaseTableContainer from "@/components/Table/BaseTableContainer";

/**
 * Renders a table of portfolios
 *
 * @param portfolios array of portfolios
 * @author Stephen Prizio
 * @version 1.0.0
 */
export default function PortfoliosTable({
  portfolios,
}: Readonly<{ portfolios: Array<Portfolio> }>) {
  //  RENDER

  return (
    <div className={"py-4"}>
      {portfolios && portfolios.length > 0 ? (
        <div>
          <BaseTableContainer
            headerContent={
              <TableRow className={"hover:bg-transparent"}>
                <TableHead className="w-[40px]" />
                <TableHead className="w-[100px] text-primary font-bold">
                  ID
                </TableHead>
                <TableHead className="w-[200px] text-primary font-bold">
                  Name
                </TableHead>
                <TableHead className="w-[200px] text-primary font-bold">
                  Date Created
                </TableHead>
                <TableHead className="w-[50px] text-primary font-bold" />
                <TableHead className="w-[50px] text-primary font-bold" />
              </TableRow>
            }
            bodyContent={
              portfolios?.map((portfolio, idx) => {
                return (
                  <TableRow
                    key={portfolio.uid + idx}
                    className={"hover:bg-transparent"}
                  >
                    <TableCell className="w-[40px]">
                      {portfolio.defaultPortfolio
                        ? resolveIcon(Icons.Flag3Filled, "text-primary")
                        : null}
                    </TableCell>
                    <TableCell className="w-[100px]">
                      {portfolio.portfolioNumber}
                    </TableCell>
                    <TableCell className="w-[200px]">
                      {portfolio.name}
                    </TableCell>
                    <TableCell className="w-[200px]">
                      {moment(portfolio.created).format(
                        DateTime.ISOShortMonthShortDayYearWithTimeFormat,
                      )}
                    </TableCell>
                    <TableCell className="w-[50px]">
                      <BaseModal
                        title={"Edit Portfolio"}
                        description={"Edit your portfolio information."}
                        content={
                          <PortfolioForm portfolio={portfolio} mode={"edit"} />
                        }
                        trigger={
                          <Button variant={"outline"}>
                            {resolveIcon(Icons.Edit)}
                          </Button>
                        }
                      />
                    </TableCell>
                    <TableCell className="w-[50px]">
                      <BaseModal
                        title={"Delete Portfolio"}
                        description={
                          "Delete your portfolio information and associated accounts."
                        }
                        content={<DeletePortfolioForm portfolio={portfolio} />}
                        trigger={
                          <Button variant={"outline"}>
                            {resolveIcon(Icons.Trash)}
                          </Button>
                        }
                      />
                    </TableCell>
                  </TableRow>
                );
              }) ?? null
            }
          />
        </div>
      ) : (
        <div className={"text-center pb-2 text-slate-600"}>
          No portfolios found.
        </div>
      )}
    </div>
  );
}
