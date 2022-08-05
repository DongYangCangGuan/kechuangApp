import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import errorLog from './modules/errorLog'
import permission from './modules/permission'
import tagsView from './modules/tagsView'
import settings from './modules/settings'
import user from './modules/user'
import monitor from './modules/monitor'
import manage from './modules/manage'
import control from './modules/control'
import db from './modules/db'
import http from './modules/http'
import questionControl from "./modules/questionControl";
import approveCount from "./modules/approveCount";

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    errorLog,
    permission,
    tagsView,
    settings,
    user,
    monitor,
    manage,
    control,
    questionControl,
    db,
    http,
      approveCount
  }
})

export default store
