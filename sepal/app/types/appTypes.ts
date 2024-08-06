import moment from "moment";
import DurationConstructor = moment.unitOfTime.DurationConstructor;

export enum TradeType {
  BUY = 'BUY',
  SELL = 'SELL'
}

export enum Interval {
  MINUTE = 'MINUTE',
  DAILY = 'DAILY',
  WEEKLY = 'WEEKLY',
  MONTHLY = 'MONTHLY',
  YEARLY = 'YEARLY'
}

export type SimpleOption = {
  label: string,
  value: string,
  unit: DurationConstructor,
  count: number,
  interval: Interval
}

export type UserMenuOption = {
  label: string,
  link: string,
  icon: string,
}