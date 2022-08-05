import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const db = {
  namespaced: true,
  state: {
        keys: [],
        values: [],
        add:function(key,value){
          for(var i=0;i<this.keys.length;i++){
            if(this.keys[i]==key){
              this.values[i]=value;
                return;
            }
          }
          this.keys.push(key);this.values.push(value);
        },
        get:function (key) {
          for(var i=0;i<this.keys.length;i++) {
            if (this.keys[i] == key) {
              return this.values[i];
            }
          }
          return "";
        }
      },
      mutations: {
                SET_DB_NODE: (state, data) => {

                 if(!data){
                      if(!data.key){
                        console.log('key is null')
                      };
                      if(!data.value){
                        console.log('value is null')
                      };

                  }

                  state.add( data.key, data.value);
//                   for(var i=0;i<state.keys.length;i++){
//                     if(state.keys[i]==key){
//                       state.values[i]=value;
//                       return;
//                     }
//                   }
// state.keys.push(key);state.values.push(value);


                }
          },
    actions:  {
      addStore({commit}, key,value) {
        return new Promise(resolve => {
                commit('SET_DB_NODE', key,value   )    ;
              resolve();
            });
      }
    }
}

export default db
