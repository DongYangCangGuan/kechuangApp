const {SAVE_USER_INFO,SAVE_WEATHER, COUNTER} = require('./mutation-types');

module.exports = {
    [SAVE_USER_INFO](state, {userInfo}) {
        state.userInfo = userInfo;
    },
    // [SAVE_WEATHER](state, {weatherinfo}) {
    //     state.weatherinfo = weatherinfo;
    // }
    [COUNTER](state,{counter}) {
      state.counter = counter
    }
}