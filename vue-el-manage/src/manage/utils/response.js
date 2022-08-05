import {Message} from "element-ui";

const doResponse = (res) => {
    if (JSON.stringify(res) == '{}') {
        return res
    }

    if (parseInt(res.code) !== 200) {
        Message({
            message: res.msg,
            type: 'error',
            duration: 5 * 1000,
            showClose: true
        })
        //token失效的操纵
        return Promise.reject('error')
    } else {
        return res
    }
}

export default doResponse
