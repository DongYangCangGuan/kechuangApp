import request from '@MAN/utils/request'
import {api} from '@MAN/api/constants'
export const getMonitorCount = (params) =>{
  const obj = {
    ...api,
    params: {
      className:'Loginfo',
      method:'getLineChartCount',
      param:params
    }
  }
  return request.json(obj)
}

export const getChartData = (params) =>{
  const obj = {
    ...api,
    params: {
      className:'Loginfo',
      method:'getLogCharts',
      param:params
    }
  }
  return request.json(obj)
}

export const getLogInfo = (params) =>{
  const obj = {
    ...api,
    params: {
      className:'Loginfo',
      method:'getLogInfo',
      param:params
    }
  }
  return request.json(obj)
}
