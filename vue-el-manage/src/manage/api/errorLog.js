import request from '@MAN/utils/request'
import {api} from '@MAN/api/constants'

export const addErrorLog = (params)=>{
  const obj = {
    ...api,
    params: {
      className:'ErrorLog',
      method:'addErrorLog',
      param:params
    }
  }
  return request.json(obj)
}
