import { Portfolio } from "@/types/apiTypes";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  IconEdit,
  IconSquareRoundedCheckFilled,
  IconTrash,
} from "@tabler/icons-react";
import moment from "moment/moment";
import { DateTime } from "@/lib/constants";
import { Button } from "@/components/ui/button";
import React, { useState } from "react";
import BaseModal from "@/components/Modal/BaseModal";
import DeletePortfolioForm from "@/components/Form/Portfolio/DeletePortfolioForm";
import PortfolioForm from "@/components/Form/Portfolio/PortfolioForm";

/**
 * Renders a table of portfolios
 *
 * @param portfolios array of portfolios
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function PortfoliosTable({
  portfolios,
}: Readonly<{ portfolios: Array<Portfolio> }>) {
  const [showModal, setShowModal] = useState<"edit" | "delete" | "none">(
    "none",
  );
  const [portfolio, setPortfolio] = useState<Portfolio>();

  //  RENDER

  return (
    <div>
      {portfolios && portfolios.length > 0 ? (
        <>
          <Table>
            <TableHeader>
              <TableRow className={"hover:bg-transparent"}>
                <TableHead />
                <TableHead className={"text-primary font-bold"}>ID</TableHead>
                <TableHead className={"text-primary font-bold"}>Name</TableHead>
                <TableHead className={"text-primary font-bold"}>
                  Date Created
                </TableHead>
                <TableHead className={"text-primary font-bold w-[50px]"} />
                <TableHead className={"text-primary font-bold w-[50px]"} />
              </TableRow>
            </TableHeader>
            <TableBody>
              {portfolios?.map((portfolio, idx) => {
                return (
                  <TableRow
                    key={portfolio.uid + idx}
                    className={"hover:cursor-pointer"}
                  >
                    <TableCell>
                      {portfolio.defaultPortfolio ? (
                        <IconSquareRoundedCheckFilled
                          className={"text-primary"}
                        />
                      ) : null}
                    </TableCell>
                    <TableCell>{portfolio.portfolioNumber}</TableCell>
                    <TableCell>{portfolio.name}</TableCell>
                    <TableCell>
                      {moment(portfolio.created).format(
                        DateTime.ISOShortMonthShortDayYearWithTimeFormat,
                      )}
                    </TableCell>
                    <TableCell>
                      <BaseModal
                        title={"Edit Portfolio"}
                        description={"Edit your portfolio information."}
                        content={
                          <PortfolioForm portfolio={portfolio} mode={"edit"} />
                        }
                        trigger={
                          <Button variant={"outline"}>
                            <IconEdit />
                          </Button>
                        }
                      />
                    </TableCell>
                    <TableCell>
                      <BaseModal
                        title={"Delete Portfolio"}
                        description={
                          "Delete your portfolio information and associated accounts."
                        }
                        content={<DeletePortfolioForm portfolio={portfolio} />}
                        trigger={
                          <Button variant={"outline"}>
                            <IconTrash />
                          </Button>
                        }
                      />
                    </TableCell>
                  </TableRow>
                );
              }) ?? null}
            </TableBody>
          </Table>
        </>
      ) : (
        <div className={"text-center pb-2 text-slate-600"}>
          No portfolios found.
        </div>
      )}
    </div>
  );
}
