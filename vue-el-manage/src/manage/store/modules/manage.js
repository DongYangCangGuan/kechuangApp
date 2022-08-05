import {getDictionaryInfo,getRoleBaseMsg} from '@MAN/api/manage'

const manage = {
  namespaced: true,
  actions: {
    getDictionaryInfo: ({commit}, params) => {
      return new Promise(
        resolve => {
          getDictionaryInfo(params).then(
            response => {
              resolve(response.data)
            }
          )
        }
      )
    },
    getRoleBaseMsg: ({commit}, params) => {
      return new Promise(
        resolve => {
          getRoleBaseMsg(params).then(
            response => {
              const {resultData} = response
              resolve(resultData||null)
            }
          )
        }
      )
    }
  }
}


export default manage
