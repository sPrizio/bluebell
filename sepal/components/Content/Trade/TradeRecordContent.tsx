import {AggregateInterval} from "@/lib/enums";

export default function TradeRecordContent(
  {
    tradeRecord,
    aggInterval
  }
    : Readonly<{
    tradeRecord: TradeRecord;
    aggInterval: AggregateInterval;
  }>
) {


  //  RENDER

  return (
    <p>HELLO WORLD!</p>
  )
}