let start = "";


let init = () => {
  start = "";
}

let startTime = () => {
  start = new Date().getTime();
}


let stopTime = () => {
  let end = new Date().getTime();
  return (end - start) / 60000;
}

init();

module.exports = {
  startTime,
  stopTime
}