import request from '@MAN/utils/request'
import {api} from '@MAN/api/constants'

export const getDictionaryInfo = (params) =>{
  const obj = {
    ...api,
    params: {
      className:'Dictionary',
      method:'getDictionaryInfo',
      param:params
    }
  }

  return request.json(obj)

}

export const getRoleBaseMsg = (params) =>{
  const obj = {
    ...api,
    params: {
      className:'RoleBaseMsg',
      method:'getRoleBaseMsg',
      param:params
    }
  }

  return request.json(obj)

}
