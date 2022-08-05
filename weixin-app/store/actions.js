const {SAVE_USER_INFO, SAVE_WEATHER} = require('./mutation-types');

module.exports = {
    saveUserInfo({commit, state}, {userInfo}) {
        console.log(userInfo);
        commit(SAVE_USER_INFO, {userInfo})
    },
    // saveWeather({commit, state}) {
    //     wx.request({
    //         url: 'http://www.weather.com.cn/data/sk/101010100.html',
    //         success(res) {
    //             console.log(res.data);
    //             let {weatherinfo} = res.data;
    //             commit(SAVE_WEATHER, {weatherinfo})
    //         }
    //     })
    // },
    saveCounter({commit,state}, {counter}) {
      console.log(counter);
      commit(SAVE_COUNTER, {counter})
    }
};
