<template>
  <div class="no-vue3-cron-div">
    <el-button class="language" @click="state.language = state.language === 'en' ? 'cn' : 'en'">
      {{ state.language === "en" ? "cn" : "en" }}
    </el-button>
    <el-tabs type="border-card">
      <el-tab-pane>
        <template #label>
          <span><i class="el-icon-date"></i> {{ translation.Seconds.name }}</span>
        </template>
        <div class="tabBody myScroller" :style="{ 'max-height': maxHeight }">
          <el-row>
            <el-radio v-model="state.second.cronEvery" label="1">{{ translation.Seconds.every }}</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.second.cronEvery" label="2">
              {{ translation.Seconds.interval[0] }}
              <el-input-number v-model="state.second.incrementIncrement" :min="1" :max="60"></el-input-number>
              {{ translation.Seconds.interval[1] || "" }}
              <el-input-number v-model="state.second.incrementStart" :min="0" :max="59"></el-input-number>
              {{ translation.Seconds.interval[2] || "" }}
            </el-radio>
          </el-row>
          <el-row>
            <el-radio class="long" v-model="state.second.cronEvery" label="3">
              {{ translation.Seconds.specific }}
              <el-select multiple v-model="state.second.specificSpecific">
                <el-option v-for="(val, index) in 60" :key="index" :value="val - 1">{{ val - 1 }}</el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.second.cronEvery" label="4">
              {{ translation.Seconds.cycle[0] }}
              <el-input-number v-model="state.second.rangeStart" :min="1" :max="60"></el-input-number>
              {{ translation.Seconds.cycle[1] || "" }}
              <el-input-number v-model="state.second.rangeEnd" :min="0" :max="59"></el-input-number>
              {{ translation.Seconds.cycle[2] || "" }}
            </el-radio>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane>
        <template #label>
          <span><i class="el-icon-date"></i> {{ translation.Minutes.name }}</span>
        </template>
        <div class="tabBody myScroller" :style="{ 'max-height': maxHeight }">
          <el-row>
            <el-radio v-model="state.minute.cronEvery" label="1">{{ translation.Minutes.every }}</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.minute.cronEvery" label="2">
              {{ translation.Minutes.interval[0] }}
              <el-input-number v-model="state.minute.incrementIncrement" :min="1" :max="60"></el-input-number>
              {{ translation.Minutes.interval[1] }}
              <el-input-number v-model="state.minute.incrementStart" :min="0" :max="59"></el-input-number>
              {{ translation.Minutes.interval[2] || "" }}
            </el-radio>
          </el-row>
          <el-row>
            <el-radio class="long" v-model="state.minute.cronEvery" label="3">
              {{ translation.Minutes.specific }}
              <el-select multiple v-model="state.minute.specificSpecific">
                <el-option v-for="(val, index) in 60" :key="index" :value="val - 1">{{ val - 1 }}</el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.minute.cronEvery" label="4">
              {{ translation.Minutes.cycle[0] }}
              <el-input-number v-model="state.minute.rangeStart" :min="1" :max="60"></el-input-number>
              {{ translation.Minutes.cycle[1] }}
              <el-input-number v-model="state.minute.rangeEnd" :min="0" :max="59"></el-input-number>
              {{ translation.Minutes.cycle[2] }}
            </el-radio>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane>
        <template #label>
          <span><i class="el-icon-date"></i> {{ translation.Hours.name }}</span>
        </template>
        <div class="tabBody myScroller" :style="{ 'max-height': maxHeight }">
          <el-row>
            <el-radio v-model="state.hour.cronEvery" label="1">{{ translation.Hours.every }}</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.hour.cronEvery" label="2">
              {{ translation.Hours.interval[0] }}
              <el-input-number v-model="state.hour.incrementIncrement" :min="0" :max="23"></el-input-number>
              {{ translation.Hours.interval[1] }}
              <el-input-number v-model="state.hour.incrementStart" :min="0" :max="23"></el-input-number>
              {{ translation.Hours.interval[2] }}
            </el-radio>
          </el-row>
          <el-row>
            <el-radio class="long" v-model="state.hour.cronEvery" label="3">
              {{ translation.Hours.specific }}
              <el-select multiple v-model="state.hour.specificSpecific">
                <el-option v-for="(val, index) in 24" :key="index" :value="val - 1">{{ val - 1 }}</el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.hour.cronEvery" label="4">
              {{ translation.Hours.cycle[0] }}
              <el-input-number v-model="state.hour.rangeStart" :min="0" :max="23"></el-input-number>
              {{ translation.Hours.cycle[1] }}
              <el-input-number v-model="state.hour.rangeEnd" :min="0" :max="23"></el-input-number>
              {{ translation.Hours.cycle[2] }}
            </el-radio>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane>
        <template #label>
          <span><i class="el-icon-date"></i> {{ translation.Day.name }}</span>
        </template>
        <div class="tabBody myScroller" :style="{ 'max-height': maxHeight }">
          <el-row>
            <el-radio v-model="state.day.cronEvery" label="1">{{ translation.Day.every }}</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.day.cronEvery" label="2">
              {{ translation.Day.intervalWeek[0] }}
              <el-input-number v-model="state.week.incrementIncrement" :min="1" :max="7"></el-input-number>
              {{ translation.Day.intervalWeek[1] }}
              <el-select v-model="state.week.incrementStart">
                <el-option v-for="(val, index) in 7" :key="index" :label="translation.Week[val - 1]" :value="val"></el-option>
              </el-select>
              {{ translation.Day.intervalWeek[2] }}
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.day.cronEvery" label="3">
              {{ translation.Day.intervalDay[0] }}
              <el-input-number v-model="state.day.incrementIncrement" :min="1" :max="31"></el-input-number>
              {{ translation.Day.intervalDay[1] }}
              <el-input-number v-model="state.day.incrementStart" :min="1" :max="31"></el-input-number>
              {{ translation.Day.intervalDay[2] }}
            </el-radio>
          </el-row>
          <el-row>
            <el-radio class="long" v-model="state.day.cronEvery" label="4">
              {{ translation.Day.specificWeek }}
              <el-select multiple v-model="state.week.specificSpecific">
                <el-option
                  v-for="(val, index) in 7"
                  :key="index"
                  :label="translation.Week[val - 1]"
                  :value="['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'][val - 1]"
                ></el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio class="long" v-model="state.day.cronEvery" label="5">
              {{ translation.Day.specificDay }}
              <el-select multiple v-model="state.day.specificSpecific">
                <el-option v-for="(val, index) in 31" :key="index" :value="val">{{ val }}</el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.day.cronEvery" label="6">{{ translation.Day.lastDay }}</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.day.cronEvery" label="7">{{ translation.Day.lastWeekday }}</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.day.cronEvery" label="8">
              {{ translation.Day.lastWeek[0] }}
              <el-select v-model="state.week.cronLastSpecificDomDay">
                <el-option v-for="(val, index) in 7" :key="index" :label="translation.Week[val - 1]" :value="val"></el-option>
              </el-select>
              {{ translation.Day.lastWeek[1] || "" }}
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.day.cronEvery" label="9">
              <el-input-number v-model="state.day.cronDaysBeforeEomMinus" :min="1" :max="31"></el-input-number>
              {{ translation.Day.beforeEndMonth[0] }}
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.day.cronEvery" label="10">
              {{ translation.Day.nearestWeekday[0] }}
              <el-input-number v-model="state.day.cronDaysNearestWeekday" :min="1" :max="31"></el-input-number>
              {{ translation.Day.nearestWeekday[1] }}
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.day.cronEvery" label="11">
              {{ translation.Day.someWeekday[0] }}
              <el-input-number v-model="state.week.cronNthDayNth" :min="1" :max="5"></el-input-number>
              <el-select v-model="state.week.cronNthDayDay">
                <el-option v-for="(val, index) in 7" :key="index" :label="translation.Week[val - 1]" :value="val"></el-option>
              </el-select>
              {{ translation.Day.someWeekday[1] }}
            </el-radio>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane>
        <template #label>
          <span><i class="el-icon-date"></i> {{ translation.Month.name }}</span>
        </template>
        <div class="tabBody myScroller" :style="{ 'max-height': maxHeight }">
          <el-row>
            <el-radio v-model="state.month.cronEvery" label="1">{{ translation.Month.every }}</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.month.cronEvery" label="2">
              {{ translation.Month.interval[0] }}
              <el-input-number v-model="state.month.incrementIncrement" :min="0" :max="12"></el-input-number>
              {{ translation.Month.interval[1] }}
              <el-input-number v-model="state.month.incrementStart" :min="0" :max="12"></el-input-number>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio class="long" v-model="state.month.cronEvery" label="3">
              {{ translation.Month.specific }}
              <el-select multiple v-model="state.month.specificSpecific">
                <el-option v-for="(val, index) in 12" :key="index" :label="val" :value="val"></el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.month.cronEvery" label="4">
              {{ translation.Month.cycle[0] }}
              <el-input-number v-model="state.month.rangeStart" :min="1" :max="12"></el-input-number>
              {{ translation.Month.cycle[1] }}
              <el-input-number v-model="state.month.rangeEnd" :min="1" :max="12"></el-input-number>
            </el-radio>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane>
        <template #label>
          <span><i class="el-icon-date"></i> {{ translation.Year.name }}</span>
        </template>
        <div class="tabBody myScroller" :style="{ 'max-height': maxHeight }">
          <el-row>
            <el-radio v-model="state.year.cronEvery" label="1">{{ translation.Year.every }}</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.year.cronEvery" label="2">
              {{ translation.Year.interval[0] }}
              <el-input-number v-model="state.year.incrementIncrement" :min="1" :max="99"></el-input-number>
              {{ translation.Year.interval[1] }}
              <el-input-number v-model="state.year.incrementStart" :min="2018" :max="2118"></el-input-number>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio class="long" v-model="state.year.cronEvery" label="3">
              {{ translation.Year.specific }}
              <el-select filterable multiple v-model="state.year.specificSpecific">
                <el-option v-for="(val, index) in 100" :key="index" :label="2017 + val" :value="2017 + val"></el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="state.year.cronEvery" label="4">
              {{ translation.Year.cycle[0] }}
              <el-input-number v-model="state.year.rangeStart" :min="2018" :max="2118"></el-input-number>
              {{ translation.Year.cycle[1] }}
              <el-input-number v-model="state.year.rangeEnd" :min="2018" :max="2118"></el-input-number>
            </el-radio>
          </el-row>
        </div>
      </el-tab-pane>
    </el-tabs>
    <div class="bottom">
      <div class="value">
        <span> cron预览: </span>
        <el-tag type="success">
          {{ cron }}
        </el-tag>
      </div>
      <div class="buttonDiv">
        <el-button type="primary" size="small" @click.stop="handleChange">{{ translation.Save }}</el-button>
        <el-button type="primary" size="small" @click="close">{{ translation.Close }}</el-button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts" name="Vue3Cron">
import Language from "@/components/Vue3Corn/language";
import { CronState, Vue3CronProps } from "@/components/Vue3Corn/types";
import { computed, reactive, watch } from "vue";

const props = withDefaults(defineProps<Vue3CronProps>(), {
  cronValue: "",
  i18n: "cn"
});

const state = reactive<CronState>({
  language: props.i18n,
  second: {
    cronEvery: "1",
    incrementStart: 3,
    incrementIncrement: 5,
    rangeStart: 0,
    rangeEnd: 0,
    specificSpecific: []
  },
  minute: {
    cronEvery: "1",
    incrementStart: 3,
    incrementIncrement: 5,
    rangeStart: 0,
    rangeEnd: 0,
    specificSpecific: []
  },
  hour: {
    cronEvery: "1",
    incrementStart: 3,
    incrementIncrement: 5,
    rangeStart: 0,
    rangeEnd: 0,
    specificSpecific: []
  },
  day: {
    cronEvery: "1",
    incrementStart: 1,
    incrementIncrement: 1,
    rangeStart: 0,
    rangeEnd: 0,
    specificSpecific: [],
    cronDaysBeforeEomMinus: 0,
    cronDaysNearestWeekday: 0
  },
  week: {
    cronEvery: "1",
    incrementStart: 1,
    incrementIncrement: 1,
    rangeStart: 0,
    rangeEnd: 0,
    specificSpecific: [],
    cronLastSpecificDomDay: 1,
    cronNthDayDay: 1,
    cronNthDayNth: 1
  },
  month: {
    cronEvery: "1",
    incrementStart: 3,
    incrementIncrement: 5,
    rangeStart: 0,
    rangeEnd: 0,
    specificSpecific: []
  },
  year: {
    cronEvery: "1",
    incrementStart: 2017,
    incrementIncrement: 1,
    rangeStart: 0,
    rangeEnd: 0,
    specificSpecific: []
  },
  output: {
    second: "",
    minute: "",
    hour: "",
    day: "",
    month: "",
    week: "",
    year: ""
  }
});

const translation = Language[state.language || "cn"];

const secondsText = computed(() => {
  let seconds = "";
  let cronEvery = state.second.cronEvery;
  switch (cronEvery.toString()) {
    case "1":
      seconds = "*";
      break;
    case "2":
      seconds = state.second.incrementStart + "/" + state.second.incrementIncrement;
      break;
    case "3":
      state.second.specificSpecific.map(val => {
        seconds += val + ",";
      });
      seconds = seconds.slice(0, -1);
      break;
    case "4":
      seconds = state.second.rangeStart + "-" + state.second.rangeEnd;
      break;
  }
  return seconds;
});
const minutesText = computed(() => {
  let minutes = "";
  let cronEvery = state.minute.cronEvery;
  switch (cronEvery.toString()) {
    case "1":
      minutes = "*";
      break;
    case "2":
      minutes = state.minute.incrementStart + "/" + state.minute.incrementIncrement;
      break;
    case "3":
      state.minute.specificSpecific.map(val => {
        minutes += val + ",";
      });
      minutes = minutes.slice(0, -1);
      break;
    case "4":
      minutes = state.minute.rangeStart + "-" + state.minute.rangeEnd;
      break;
  }
  return minutes;
});
const hoursText = computed(() => {
  let hours = "";
  let cronEvery = state.hour.cronEvery;
  switch (cronEvery.toString()) {
    case "1":
      hours = "*";
      break;
    case "2":
      hours = state.hour.incrementStart + "/" + state.hour.incrementIncrement;
      break;
    case "3":
      state.hour.specificSpecific.map(val => {
        hours += val + ",";
      });
      hours = hours.slice(0, -1);
      break;
    case "4":
      hours = state.hour.rangeStart + "-" + state.hour.rangeEnd;
      break;
  }
  return hours;
});
const daysText = computed(() => {
  let days = "";
  let cronEvery = state.day.cronEvery;
  switch (cronEvery.toString()) {
    case "1":
      break;
    case "2":
    case "4":
    case "11":
    case "8":
      days = "?";
      break;
    case "3":
      days = state.day.incrementStart + "/" + state.day.incrementIncrement;
      break;
    case "5":
      state.day.specificSpecific.map(val => {
        days += val + ",";
      });
      days = days.slice(0, -1);
      break;
    case "6":
      days = "L";
      break;
    case "7":
      days = "LW";
      break;
    case "9":
      days = "L-" + state.day.cronDaysBeforeEomMinus;
      break;
    case "10":
      days = state.day.cronDaysNearestWeekday + "W";
      break;
  }
  return days;
});
const weeksText = computed(() => {
  let weeks = "";
  let cronEvery = state.day.cronEvery;
  switch (cronEvery.toString()) {
    case "1":
    case "3":
    case "5":
      weeks = "?";
      break;
    case "2":
      weeks = state.week.incrementStart + "/" + state.week.incrementIncrement;
      break;
    case "4":
      state.week.specificSpecific.map(val => {
        weeks += val + ",";
      });
      weeks = weeks.slice(0, -1);
      break;
    case "6":
    case "7":
    case "9":
    case "10":
      weeks = "?";
      break;
    case "8":
      weeks = state.week.cronLastSpecificDomDay + "L";
      break;
    case "11":
      weeks = state.week.cronNthDayDay + "#" + state.week.cronNthDayNth;
      break;
  }
  return weeks;
});
const monthsText = computed(() => {
  let months = "";
  let cronEvery = state.month.cronEvery;
  switch (cronEvery.toString()) {
    case "1":
      months = "*";
      break;
    case "2":
      months = state.month.incrementStart + "/" + state.month.incrementIncrement;
      break;
    case "3":
      state.month.specificSpecific.map(val => {
        months += val + ",";
      });
      months = months.slice(0, -1);
      break;
    case "4":
      months = state.month.rangeStart + "-" + state.month.rangeEnd;
      break;
  }
  return months;
});
const yearsText = computed(() => {
  let years = "";
  let cronEvery = state.year.cronEvery;
  switch (cronEvery.toString()) {
    case "1":
      years = "*";
      break;
    case "2":
      years = state.year.incrementStart + "/" + state.year.incrementIncrement;
      break;
    case "3":
      state.year.specificSpecific.map(val => {
        years += val + ",";
      });
      years = years.slice(0, -1);
      break;
    case "4":
      years = state.year.rangeStart + "-" + state.year.rangeEnd;
      break;
  }
  return years;
});
const cron = computed(() => {
  return `${secondsText.value || "*"} ${minutesText.value || "*"} ${hoursText.value || "*"} ${daysText.value || "*"} ${
    monthsText.value || "*"
  } ${weeksText.value || "?"} ${yearsText.value || "*"}`;
});

watch(
  () => props.cronValue,
  newCron => {
    if (!newCron) return false;
    let crons = newCron.split(" ");
    // 解析seconds
    let secondsText = crons[0].trim();
    if (secondsText === "*") {
      state.second.cronEvery = "1";
    } else if (secondsText.includes("/")) {
      state.second.cronEvery = "2";
      let secondsTexts = secondsText.split("/");
      state.second.incrementStart = parseInt(secondsTexts[0]);
      state.second.incrementIncrement = parseInt(secondsTexts[1]);
    } else if (secondsText.includes(",") || isFinite(Number(secondsText))) {
      state.second.cronEvery = "3";
      state.second.specificSpecific = secondsText.split(",").map(item => parseInt(item));
    } else if (secondsText.includes("-")) {
      state.second.cronEvery = "4";
      let secondsTexts = secondsText.split("-");
      state.second.rangeStart = parseInt(secondsTexts[0]);
      state.second.rangeEnd = parseInt(secondsTexts[1]);
    }
    // 解析minutes
    let minutesText = crons[1].trim();
    if (minutesText === "*") {
      state.minute.cronEvery = "1";
    } else if (minutesText.includes("/")) {
      state.minute.cronEvery = "2";
      let minutesTexts = minutesText.split("/");
      state.minute.incrementStart = parseInt(minutesTexts[0]);
      state.minute.incrementIncrement = parseInt(minutesTexts[1]);
    } else if (minutesText.includes(",") || isFinite(Number(minutesText))) {
      state.minute.cronEvery = "3";
      state.minute.specificSpecific = minutesText.split(",").map(item => parseInt(item));
    } else if (minutesText.includes("-")) {
      state.minute.cronEvery = "4";
      let minutesTexts = minutesText.split("-");
      state.minute.rangeStart = parseInt(minutesTexts[0]);
      state.minute.rangeEnd = parseInt(minutesTexts[1]);
    }
    // 解析hours
    let hoursText = crons[2].trim();
    if (hoursText === "*") {
      state.hour.cronEvery = "1";
    } else if (hoursText.includes("/")) {
      state.hour.cronEvery = "2";
      let hoursTexts = hoursText.split("/");
      state.hour.incrementStart = parseInt(hoursTexts[0]);
      state.hour.incrementIncrement = parseInt(hoursTexts[1]);
    } else if (hoursText.includes(",") || isFinite(Number(hoursText))) {
      state.hour.cronEvery = "3";
      state.hour.specificSpecific = hoursText.split(",").map(item => parseInt(item));
    } else if (hoursText.includes("-")) {
      state.hour.cronEvery = "4";
      let hoursTexts = hoursText.split("-");
      state.hour.rangeStart = parseInt(hoursTexts[0]);
      state.hour.rangeEnd = parseInt(hoursTexts[1]);
    }
    // 解析days weeks
    let daysText = crons[3].trim();
    let weeksText = crons[5].trim();
    if (daysText.includes("/")) {
      state.day.cronEvery = "3";
      let daysTexts = daysText.split("/");
      state.day.incrementStart = parseInt(daysTexts[0]);
      state.day.incrementIncrement = parseInt(daysTexts[1]);
    } else if (daysText.includes(",") || isFinite(Number(daysText))) {
      state.day.cronEvery = "5";
      state.day.specificSpecific = daysText.split(",").map(item => parseInt(item));
    } else if (daysText === "L") {
      state.day.cronEvery = "6";
    } else if (daysText === "LW") {
      state.day.cronEvery = "7";
    } else if (daysText.startsWith("L-")) {
      state.day.cronEvery = "9";
      state.day.cronDaysBeforeEomMinus = parseInt(daysText.replaceAll("L-", ""));
    } else if (daysText.endsWith("W")) {
      state.day.cronEvery = "10";
      state.day.cronDaysNearestWeekday = parseInt(daysText.replaceAll("W", ""));
    } else if (daysText === "?") {
      if (weeksText.includes("/")) {
        state.day.cronEvery = "2";
        let weeksTexts = weeksText.split("/");
        state.week.incrementStart = parseInt(weeksTexts[0]);
        state.week.incrementIncrement = parseInt(weeksTexts[1]);
      } else if (weeksText.includes(",") || isFinite(Number(weeksText))) {
        state.day.cronEvery = "4";
        state.week.specificSpecific = weeksText.split(",").map(item => parseInt(item));
      } else if (weeksText.includes("#")) {
        state.day.cronEvery = "11";
        let weeksTexts = weeksText.split("#");
        state.week.cronNthDayDay = parseInt(weeksTexts[0]);
        state.week.cronNthDayNth = parseInt(weeksTexts[1]);
      } else if (weeksText.endsWith("L")) {
        state.day.cronEvery = "8";
        state.week.cronLastSpecificDomDay = parseInt(weeksText.replaceAll("L", ""));
      }
    } else {
      state.day.cronEvery = "1";
    }

    // 解析months
    let monthsText = crons[4].trim();
    if (monthsText === "*") {
      state.month.cronEvery = "1";
    } else if (monthsText.includes("/")) {
      state.month.cronEvery = "2";
      let monthsTexts = monthsText.split("/");
      state.month.incrementStart = parseInt(monthsTexts[0]);
      state.month.incrementIncrement = parseInt(monthsTexts[1]);
    } else if (monthsText.includes(",") || isFinite(Number(monthsText))) {
      state.month.cronEvery = "3";
      state.month.specificSpecific = monthsText.split(",").map(item => parseInt(item));
    } else if (monthsText.includes("-")) {
      state.month.cronEvery = "4";
      let monthsTexts = monthsText.split("-");
      state.month.rangeStart = parseInt(monthsTexts[0]);
      state.month.rangeEnd = parseInt(monthsTexts[1]);
    }
    // 解析years
    let yearsText = crons[6].trim();
    if (yearsText === "*") {
      state.year.cronEvery = "1";
    } else if (yearsText.includes("/")) {
      state.year.cronEvery = "2";
      let yearsTexts = yearsText.split("/");
      state.year.incrementStart = parseInt(yearsTexts[0]);
      state.year.incrementIncrement = parseInt(yearsTexts[1]);
    } else if (yearsText.includes(",") || isFinite(Number(yearsText))) {
      state.year.cronEvery = "3";
      state.year.specificSpecific = yearsText.split(",").map(item => parseInt(item));
    } else if (yearsText.includes("-")) {
      state.year.cronEvery = "4";
      let yearsTexts = yearsText.split("-");
      state.year.rangeStart = parseInt(yearsTexts[0]);
      state.year.rangeEnd = parseInt(yearsTexts[1]);
    }
  },
  {
    immediate: true
  }
);

const emit = defineEmits<{
  change: [value: string];
  close: [value: string];
}>();
const close = () => {
  emit("close", "");
};

const handleChange = () => {
  emit("change", cron.value);
  close();
};
</script>
<style lang="scss">
.no-vue3-cron-div {
  .language {
    position: absolute;
    right: 25px;
    z-index: 1;
  }

  .el-tabs {
    box-shadow: none;
  }

  .tabBody {
    overflow: auto;

    .el-row {
      margin: 20px 0;

      .long {
        .el-select {
          width: 200px;
        }
      }

      .el-input-number {
        width: 120px;
      }
    }
  }

  .myScroller {
    &::-webkit-scrollbar {
      /*滚动条整体样式*/
      width: 5px; /*高宽分别对应横竖滚动条的尺寸*/
      height: 1px;
    }

    &::-webkit-scrollbar-thumb {
      /*滚动条里面小方块*/
      border-radius: 10px;
      background-color: skyblue;
      background-image: -webkit-linear-gradient(
        45deg,
        rgba(255, 255, 255, 0.2) 25%,
        transparent 25%,
        transparent 50%,
        rgba(255, 255, 255, 0.2) 50%,
        rgba(255, 255, 255, 0.2) 75%,
        transparent 75%,
        transparent
      );
    }

    &::-webkit-scrollbar-track {
      /*滚动条里面轨道*/
      box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
      background: #ededed;
      border-radius: 10px;
    }
  }

  .bottom {
    width: 100%;
    margin-top: 5px;
    display: flex;
    align-items: center;
    justify-content: space-around;

    .value {
      float: left;
      font-size: 14px;
      vertical-align: middle;

      span:nth-child(1) {
        color: red;
      }
    }
  }
}
</style>
