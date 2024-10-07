import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import {IconExternalLink} from "@tabler/icons-react";
import Link from "next/link";
import {formatNumberForDisplay} from "@/lib/services";

export default function AccountsTable(
  {
    accounts = [],
  }
    : Readonly<{
    accounts: Array<Account>,
  }>
) {


  //  RENDER

  return (
    <Table>
      <TableCaption>
        <div className={"flex items-center justify-center gap-1"}>
          <div className={""}>
            <Link href={'#'}>View All Accounts</Link>
          </div>
          <div className={""}>
            <Link href={'#'}><IconExternalLink size={18} /></Link>
          </div>
        </div>
      </TableCaption>
      <TableHeader>
        <TableRow>
          <TableHead>Name</TableHead>
          <TableHead className={'text-center'}>Type</TableHead>
          <TableHead className={'text-center'}>Broker</TableHead>
          <TableHead className="text-right">Balance</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {
          accounts && accounts.length && accounts.map((item, itx) => {
            return (
              <TableRow key={itx}>
                <TableCell>{item.name}</TableCell>
                <TableCell className={'text-center'}>{item.accountType}</TableCell>
                <TableCell className={'text-center'}>{item.broker}</TableCell>
                <TableCell className="text-right">${formatNumberForDisplay(item.balance)}</TableCell>
              </TableRow>
            )
          })
        }
      </TableBody>
    </Table>
  )
}