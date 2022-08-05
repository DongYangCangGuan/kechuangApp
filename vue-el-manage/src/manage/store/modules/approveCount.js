import store from "../index";
// import http from './http'

const state = {
    unApprovedCount: ''
}

const mutations = {
    GET_COUNT: (state,data) => {
        state.unApprovedCount = data;
    },
}

const actions = {
    getUnApprovedCount(context){
        setTimeout(async()=>{
            let params = {
                className:'Member',
                method:'getUnApprovedCount',
                param:{
                    approvalstatus: 0  //未审批的状态标志
                }
            };
            let res = await store.dispatch('http/post', params);
            context.commit('GET_COUNT', res)
            console.log('state.unApprovedCount==',state.unApprovedCount);
        },500)
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
