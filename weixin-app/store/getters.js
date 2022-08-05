module.exports = {
  addTen: state => {
      return state.arr = state.arr.map((val) => val * 10);
  }
};