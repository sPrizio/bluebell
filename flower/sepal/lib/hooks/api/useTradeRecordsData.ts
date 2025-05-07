import {DateTime} from "@/lib/constants";
import {UserTradeRecordControlSelection} from "@/types/uiTypes";
import moment from "moment";
import {useTradeRecordControlsQuery, useTradeRecordsQuery} from "../query/queries";

/**
 * Computes the start and dates for lookup based on the filters
 */
function computeDates(selection: UserTradeRecordControlSelection): { start: string, end: string } {
  const getMoment = (val: 'month' | 'year') => {
    return moment(selection.year + '-' + selection.month + '-01', DateTime.ISODateLongMonthFormat).startOf(val);
  }

  switch (selection.aggInterval.code) {
    case 'YEARLY':
      return {
        start: getMoment('year').subtract(50, 'years').format(DateTime.ISODateFormat),
        end: getMoment('year').add(1, 'years').format(DateTime.ISODateFormat)
      };
    case 'MONTHLY':
      return {
        start: getMoment('year').format(DateTime.ISODateFormat),
        end: getMoment('year').add(1, 'years').format(DateTime.ISODateFormat)
      };
    default:
      return {
        start: getMoment('month').format(DateTime.ISODateFormat),
        end: getMoment('month').add(1, 'months').format(DateTime.ISODateFormat)
      };
  }
}

export function useTradeData(accNumber: number, filters: UserTradeRecordControlSelection, enabled: boolean) {
  const { start, end } = computeDates(filters);

  const tradeRecordControlsQuery = useTradeRecordControlsQuery(
    accNumber,
    filters.aggInterval.code,
    {enabled}
  );

  const tradeRecordsQuery = useTradeRecordsQuery(
    accNumber,
    start,
    end,
    filters.aggInterval.code,
    50,
    {enabled}
  );

  return { tradeRecordControlsQuery, tradeRecordsQuery };
}
