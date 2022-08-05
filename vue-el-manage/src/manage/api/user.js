import request from '@MAN/utils/request'
import {api} from '@MAN/api/constants'

export function login(params) {
    const obj = {
        uri: 'auth/login/',//本地
         // uri: 'service-authorize/auth/login/',//uat
        action: '',
        params: params
    }
    return request.json(obj)
}

export function getInfo(params) {
    const obj = {
        ...api,
        params: {
            className: 'Menu',
            method: 'getSecurityInfoAll',
            param: {role: params}
        }
    }
    return request.json(obj)
}

export function logout() {
    return request({
        url: '/user/logout',
        method: 'post'
    })
}

