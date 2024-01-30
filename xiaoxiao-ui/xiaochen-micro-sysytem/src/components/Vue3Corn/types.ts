export interface Vue3CronProps {
  cronValue: string;
  i18n: string;
  maxHeight: string;
}
export interface SpecificSpecific {
  [index: number]: number;
}

export interface Output {
  second: string;
  minute: string;
  hour: string;
  day: string;
  month: string;
  week: string;
  year: string;
}

export interface CronState {
  language: string;
  second: CronItem;
  minute: CronItem;
  hour: CronItem;
  day: DayCronItem;
  week: WeekCronItem;
  month: CronItem;
  year: CronItem;
  output: Output;
}

export interface CronItem {
  cronEvery: string;
  incrementStart: number;
  incrementIncrement: number;
  rangeStart: number;
  rangeEnd: number;
  specificSpecific: number[];
}

export interface DayCronItem extends CronItem {
  cronDaysBeforeEomMinus: number;
  cronDaysNearestWeekday: number;
}

export interface WeekCronItem extends CronItem {
  cronLastSpecificDomDay: number;
  cronNthDayDay: number;
  cronNthDayNth: number;
}
export interface Translation {
  name: string;
  every: string;
  interval: string[];
  specific: string;
  cycle: string[];
}

export interface DayTranslation {
  name: string;
  every: string;
  intervalWeek: string[];
  intervalDay: string[];
  specificWeek: string;
  specificDay: string;
  lastDay: string;
  lastWeekday: string;
  lastWeek: string[];
  beforeEndMonth: string[];
  nearestWeekday: string[];
  someWeekday: string[];
}

export interface MonthTranslation {
  name: string;
  every: string;
  interval: string[];
  specific: string;
  cycle: string[];
}

export interface YearTranslation {
  name: string;
  every: string;
  interval: string[];
  specific: string;
  cycle: string[];
}

export interface Translations {
  Seconds: Translation;
  Minutes: Translation;
  Hours: Translation;
  Day: DayTranslation;
  Week: string[];
  Month: MonthTranslation;
  Year: YearTranslation;
  Save: string;
  Close: string;
}
