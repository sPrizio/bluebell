'use client'

import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import moment from "moment";
import {DateTime} from "@/lib/constants";
import {IconNews} from "@tabler/icons-react";
import {MarketNews} from "@/types/apiTypes";

/**
 * Renders the market news table
 *
 * @param news market news
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function NewsTable(
  {
    news = [],
  }
    : Readonly<{
    news?: Array<MarketNews>,
  }>
) {

  const background = ' bg-primary bg-opacity-5 hover:bg-primary hover:bg-opacity-5 '
  const pastStyle = ' opacity-25 '


  //  GENERAL FUNCTIONS

  /**
   * Computes the color for the severity level
   *
   * @param val severity level
   */
  function computeSeverity(val: number) {
    switch (val) {
      case 1:
        return ' text-threatLow '
      case 2:
        return ' text-threatModerate '
      default:
        return ' text-threatSevere '
    }
  }


  //  RENDER

  return (
    <div className={''}>
      <Table>
        <TableHeader>
          <TableRow className={'hover:bg-transparent'}>
            <TableHead>Date</TableHead>
            <TableHead>Time</TableHead>
            <TableHead>Country</TableHead>
            <TableHead>Impact</TableHead>
            <TableHead>News</TableHead>
            <TableHead>Forecast</TableHead>
            <TableHead>Previous</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {
            news?.map((news) => (
              <>
                <TableRow key={news.uid} className={'hover:bg-transparent ' + (news.active ? `${background}`: '') + (news.past ? `${pastStyle}` : '')}>
                  <TableCell className={'font-semibold text-primary'}
                             colSpan={7}>{moment(news.date).format(DateTime.ISOLongMonthDayYearFormat)}</TableCell>
                </TableRow>
                {
                  (news.slots?.length ?? 0) === 0 ?
                    <TableRow className={'hover:bg-transparent ' + (news.active ? `${background}`: '') + (news.past ? `${pastStyle}` : '')}>
                      <TableCell className={'text-slate-400 text-center'} colSpan={7}>No news today.</TableCell>
                    </TableRow> : null
                }
                {
                  news.slots?.map(slot => {
                    return (slot.entries?.length ?? 0) > 0 ?
                      slot.entries.map((entry, index) => {
                        return (
                          <TableRow key={slot.uid} className={'hover:bg-transparent border-0 ' + (news.active ? `${background}`: '') + (news.past ? `${pastStyle}` : '')}>
                            <TableCell/>
                            {index === 0 ? <TableCell className={''}>{slot.time}</TableCell> : <TableCell/>}
                            <TableCell className={''}>{entry.country}</TableCell>
                            <TableCell className={computeSeverity(entry.severityLevel)}>
                              <IconNews size={24} />
                            </TableCell>
                            <TableCell className={''}>{entry.content}</TableCell>
                            <TableCell className={''}>{entry.forecast}</TableCell>
                            <TableCell className={''}>{entry.previous}</TableCell>
                          </TableRow>
                        )
                      }) : null
                  })
                }
              </>
            )) ?? null
          }
        </TableBody>
      </Table>
    </div>
  )
}