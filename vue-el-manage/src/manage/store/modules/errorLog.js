import {addErrorLog} from '@MAN/api/errorLog'
const actions = {
  addErrorLog({ commit }, params) {
    return new Promise(
      (resolve, reject) => {
        addErrorLog(params).then(
          () => {
            resolve()
          }
        ).catch(error => {
          reject(error)
        })
      }
    )
  }
}

export default {
  namespaced: true,
  actions
}
