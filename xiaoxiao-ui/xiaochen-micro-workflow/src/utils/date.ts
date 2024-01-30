export const getFormattedDateTime = (date: Date) => {
  let year = date.getFullYear();
  let month = padZero(date.getMonth() + 1); // 月份是从 0 开始的，所以要加 1
  let day = padZero(date.getDate());
  let hours = padZero(date.getHours());
  let minutes = padZero(date.getMinutes());
  let seconds = padZero(date.getSeconds());

  return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
};

function padZero(number: number) {
  // 在数字小于 10 的情况下，在前面补零
  return (number < 10 ? "0" : "") + number;
}

export const getDateTimeAfterMinutes = (minutes: number) => {
  let currentDate = new Date();
  currentDate.setMinutes(currentDate.getMinutes() + minutes);
  return currentDate;
};

export const getDateTimeBeforeMinutes = (minutes: number) => {
  let currentDate = new Date();
  currentDate.setMinutes(currentDate.getMinutes() - minutes);
  return currentDate;
};
