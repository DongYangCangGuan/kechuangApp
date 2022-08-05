import {getMonitorCount,getChartData,getLogInfo} from '@MAN/api/monitor'
const monitor = {
  namespaced:true,
  actions:{
    getMonitorCount:({commit},params)=>{
      return new Promise(
        resolve=>{
          getMonitorCount(params).then(
            response=>{
              console.log("getMonitorCount");
              resolve(response)
              console.log(response);
            }
          )
        }
      )
    },
    getChartData:({commit},params)=>{
      return new Promise(
        resolve=>{
          getChartData(params).then(
            response=>{
              console.log("getChartData")
              resolve(response.resultData)
              console.log(response.resultData);
            }
          )
        }
      )
    },
    getLogInfo:({commit},params)=>{
      return new Promise(
        resolve=>{
          getLogInfo(params).then(
            response=>{
              console.log("getLogInfo");
              const {resultData,count} = response;
              console.log(response)
              console.log(resultData);

              console.log(count);
              resolve({resultData,count})
            }
          )
        }
      )
    },
  }
}

export default monitor
