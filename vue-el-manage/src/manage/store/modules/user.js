import {login, logout, getInfo} from '@MAN/api/user'
import {getToken, setToken, removeToken, removeSecret} from '@COM/utils/auth'
import router, {resetRouter} from '@MAN/router'
import store from '@MAN/store'
import control from "./control";

const state = {
    token: getToken(),
    avatar: sessionStorage.getItem('avatar'),
    introduction: '',
    depart: sessionStorage.getItem('depart'),
    name: sessionStorage.getItem('name'),
    roles: '',
    roleslist: JSON.parse(sessionStorage.getItem('roleslist')),
    entity: JSON.parse(sessionStorage.getItem('entity'))
}

const mutations = {
    SET_TOKEN: (state, token) => {
        state.token = token
    },
    SET_INTRODUCTION: (state, introduction) => {
        state.introduction = introduction
    },
    SET_NAME: (state, name) => {
        state.name = name;
        sessionStorage.setItem('name', name);
    },
    SET_AVATAR: (state, avatar) => {
        state.avatar = avatar;
        sessionStorage.setItem('avatar', avatar);
    },
    SET_ROLES: (state, roles) => {
        state.roles = roles
        //sessionStorage.setItem('roles',JSON.stringify(roles));
    },
    SET_ROLESLIST: (state, roleslist) => {
        state.roleslist = roleslist;
        sessionStorage.setItem('roleslist', JSON.stringify(roleslist));
    },
    SET_ENTITY: (state, entity) => {
        state.entity = entity;
        sessionStorage.setItem('entity', JSON.stringify(entity));
    },
    SET_DEPART: (state, depart) => {
        state.depart = depart;
        sessionStorage.setItem('depart', depart);
    }

}

const actions = {
    // user login
    login: ({commit}, params) => {
        return new Promise(
            (resolve, reject) => {
                login(params).then(
                    response => {
                        const {data} = response;
                        data.deptName = data.deptName;
                        data.listrole = data.listrole;
                        commit('SET_TOKEN', data.token);
                        commit('SET_ROLESLIST', data.listrole);
                        setToken(data.token)
                        commit('SET_ENTITY', data)
                        commit('SET_AVATAR', data.headImg);
                        commit('SET_NAME', data.userName);
                        commit('SET_DEPART', data.deptName);
                        resolve()
                    }
                ).catch(error => {
                    reject(error)
                })
            }
        )
    },

    // get user info
    getInfo: ({commit, dispatch}, role) => {
        return new Promise(
            (resolve, reject) => {
                getInfo(role).then(
                    response => {
                        const {roles} = response
                        commit('SET_ROLES', roles)
                        resolve(response)
                    }
                ).catch(error => {
                    reject(error)
                })
            }
        )
    },

    // user logout
    logout({commit, state}) {
        return new Promise((resolve, reject) => {
            // logout(state.token).then(() => {
            //     commit('SET_TOKEN', '');
            //     commit('SET_ROLES', []);
            //     commit('SET_ENTITY', null);
            //     removeToken();
            //     removeSecret();
            //     resetRouter();
            //     resolve();
            // }).catch(error => {
            //     reject(error);
            // });
            commit('SET_TOKEN', '');
            commit('SET_ROLES', []);
            commit('SET_ENTITY', null);
            removeToken();
            removeSecret();
            resetRouter();
            resolve();
        })
    },

    // remove token
    resetToken: ({commit}) => {
        return new Promise(resolve => {
            commit('SET_TOKEN', '')
            removeToken()
            removeSecret()
            resolve()
        })
    },

    // Dynamically modify permissions
    changeRoles({commit, dispatch}, role) {
        return new Promise(async resolve => {

            console.log("切换角色");
            const token = role + '-token'

            console.log(role);

            commit('SET_TOKEN', token)
            setToken(token)

            const {data, roles} = await dispatch('getInfo', role)

            console.log(data);
            resetRouter()
            console.log("重置router")

            store.commit('permission/SET_ASYNC_ROUTES', data)
            // generate accessible routes map based on roles
            const accessRoutes = await dispatch('permission/generateRoutes', roles, {root: true})

            console.log(accessRoutes);
            // dynamically add accessible routes
            router.addRoutes(accessRoutes)


            resolve()
        })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
