const manage={

}

const control = {

  state: {
    showInfoStyle: [],
    addInfoStyle: [],
    editInfoStyle: [],
    ansInfoStyle: [],
    treeInfoStyle:[],
    importInfoStyle:[],
    detailInfoStyle: [],
    isShow: true,
    isAdd: false,
    isEdit: false,
    isTree:false,
    isImport:false,
    isDetail: false,
    common:'',
    isAns: false
  },
  mutations:{
   // SET_CONTROLLER_STATUS

    //changeBackStyle
    SET_BACK_STYLE : (state) => {

    let status=state.common;
    switch (status){
      case "tree":
        state.treeInfoStyle = ['animated', 'bounceOutRight', 'slow']
        setTimeout(() => { state.isTree = false }, 1000);

      break;


      case "import":
        state.importInfoStyle = ['animated', 'bounceOutRight', 'slow']
        setTimeout(() => { state.isImport = false }, 1000);
          state.showInfoStyle= ['animated', 'bounceInLeft', 'slow']
          setTimeout(() => { state.isShow = true }, 1000)
      break;

      case "add":
        state.addInfoStyle = ['animated', 'bounceOutRight', 'slow']
        setTimeout(() => { state.isAdd = false }, 1000);

        break;
        case "edit":
        // 从右边滑入修改页面后进行dom挂载
        state.editInfoStyle = ['animated', 'bounceInRight', 'slow']
        setTimeout(() => { state.isEdit = false }, 1000)
        break;
        case "detail":
        // 从右边滑入修改页面后进行dom挂载
        state.detailInfoStyle = ['animated', 'bounceInRight', 'slow']
        setTimeout(() => { state.isDetail = false }, 1000)

        break;

        case "ans":
        state.ansInfoStyle = ['animated', 'bounceInRight', 'slow']
        setTimeout(() => {
            state.isAns = false;
            state.isImport = true
            }, 1000)
        return;

        default:
            break;
    }
    // 从左边滑入主页面后进行dom挂载
    state.showInfoStyle= ['animated', 'bounceInLeft', 'slow']
    setTimeout(() => { state.isShow = true }, 1000)


  },
  SET_FORWARD_STYLE : (state, status) => {

      state.showInfoStyle = ['animated', 'bounceOutLeft', 'slow'];

  state.common=status;


     setTimeout(() => { state.isShow = false }, 1000);
      console.log('======'+status);
       switch (status){
         case "tree":
           state.treeInfoStyle = ['animated', 'bounceInRight', 'slow']
           setTimeout(() => { state.isTree = true }, 1000)

        break;


         case "import":
             state.importInfoStyle = ['animated', 'bounceInRight', 'slow']
             setTimeout(() => { state.isImport = true }, 1000)

          break;


         case "add":
           state.addInfoStyle = ['animated', 'bounceInRight', 'slow']
           setTimeout(() => { state.isAdd = true }, 1000)

           break;
         case "edit":
            // 从右边滑入修改页面后进行dom挂载
        state.editInfoStyle = ['animated', 'bounceInRight', 'slow']
                  setTimeout(() => { state.isEdit = true }, 1000)
           break;

        case "detail":
            state.detailInfoStyle = ['animated', 'bounceInRight', 'slow']
            setTimeout(() => { state.isDetail = true }, 1000)
           break;

        case "ans":
            state.ansInfoStyle = ['animated', 'bounceInRight', 'slow']
            setTimeout(() => {
                state.isImport = false;
                state.isAns = true}, 1000)
            break;

        default:
               break;
       }
      }

    },

  actions: {
    // setControllerStatus({commit}, state) {
    //   commit('SET_CONTROLLER_STATUS', state)
    // }
    changeForwardStyle({commit}, state) {
      commit('SET_FORWARD_STYLE', state)
    },
    changeBackStyle({commit}, state) {
      commit('SET_BACK_STYLE', state)
    },

  },
  computed:{


  }
}
export default control
