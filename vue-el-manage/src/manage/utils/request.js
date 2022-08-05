import axios from 'axios'
import store from '@MAN/store'
import doResponse from './response'
import {Message, MessageBox} from "element-ui";
import {Encrypt, Decrypt} from '@COM/utils/encrypt';
import storage from "@COM/utils/localStorage";

const service = axios.create({
    baseURL: process.env.MINIMEETING_BASE_API, // api 的 base_url
    withCredentials: true, // 跨域请求时发送 cookies
    timeout: 50000,// request timeout
    headers: {
        "Content-Type": "application/x-www-json-urlencoded;charset=utf-8"
    }
})

// 是否正在刷新的标记
let isRefreshing = false
//重试队列
let requests = []

service.interceptors.request.use(
    config => {
        // Do something before request is sent
        if (store.state.user.token) {
            // 让每个请求携带token-- ['token']为自定义key 请根据实际情况自行修改
            config.headers['token'] = store.state.user.token
        }
        return config
    },
    error => {
        // Do something with request error
        Promise.reject(error)
    }
)

service.interceptors.response.use(
    /**
     * If you want to get information such as headers or status
     * Please return  response => response
     */
    /**
     * 下面的注释为通过在response里，自定义code来标示请求状态
     * 当code返回如下情况则说明权限有问题，登出并返回到登录页
     * 如想通过 XMLHttpRequest 来状态码标识 逻辑可写在下面error中
     * 以下代码均为样例，请结合自生需求加以修改，若不需要，则可删除
     */
    response => {
        console.log(response, 'response----------->')
        let decryptParam = Decrypt(response.data);
        if (decryptParam != null && decryptParam != '' && typeof decryptParam == 'string') {
            return doResponse(JSON.parse(decryptParam))
        }
        return doResponse(response.data)
    },
    error => {
        let err;
        if (error.request) {
            err = error.request;
        } else if (error.response) {
            err = error.response;
        }

        if (error && err) {
            error.code = err.status;
            switch (err.status) {
                case 400:
                    error.message = '请求错误(400)';
                    break;
                case 401:
                    error.message = '未授权，请重新登录(401)';
                    break;
                case 403:
                    error.message = '拒绝访问(403)';
                    break;
                case 404:
                    error.message = '请求出错(404)';
                    break;
                case 408:
                    error.message = '请求超时(408)';
                    break;
                case 500:
                    error.message = '服务器错误(500)';
                    break;
                case 501:
                    error.message = '服务未实现(501)';
                    break;
                case 502:
                    error.message = '网络错误(502)';
                    break;
                case 503:
                    error.message = '服务不可用(503)';
                    break;
                case 504:
                    error.message = '网络超时(504)';
                    break;
                case 505:
                    error.message = 'HTTP版本不受支持(505)';
                    break;
                default:
                    error.message = `连接出错 (${err.status})!`;
                    break;
            }
        } else {
            error.message = '连接服务器失败!'
        }

        let isFrame = storage.get("isFrame");
        if (!isFrame) {//防止多次弹框
            Message({
                message: error.message,
                type: 'error',
                duration: 5 * 1000
            });
        }
        storage.set("isFrame", true, 2);

        setTimeout(() => {
            store.dispatch('user/resetToken').then(() => {
                location.reload() // 为了重新实例化vue-router对象 避免bug
            });
        }, 3000);
        return Promise.reject(error);
    }
)

service.json = ({uri, action, params}) => {
    const url = `${uri}${action}`;
    if (!uri.endsWith("login/") && params != null && params != '' && typeof params != 'string') {
        return service.post(url, Encrypt(params));
    } else if (uri.endsWith("/downloadfile") && params != null && params != '') {
        console.log(params, 'params--------------------->')
        return service.post(url, Encrypt(params))
    }
    return service.post(url, params);
}

export default service
