import { Loading } from 'element-ui'

let loadingCount=0;
let loading;
const startLoading=()=>{
  loading=Loading.service({
    loading:true,
    text:"登录中...",
    background:'rga(0,0,0,0.7)'
  })
};
const endLoading=()=>{
  loading.close();
}

export const showLoading=()=>{
  if(loadingCount===0){
    startLoading()
  }
  loadingCount=loadingCount+1;
};
export const hideLoading=()=>{

  if(loadingCount<=0)
    return;
  loadingCount-=1;

  if(loadingCount===0){
    endLoading();
  }

}
