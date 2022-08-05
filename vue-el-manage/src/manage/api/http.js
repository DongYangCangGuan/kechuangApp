import request from '@MAN/utils/request'
import request1 from '@MAN/utils/request1'

export function post(params) {
    let url = params.url;
    let className = params.className;
    let method = params.method;
    ;
    let param = params.param;

    if (!url) {
        url = 'manage/api';  //本地
         // url = 'service-manage/manage/manage/api';  //uat
    }

    const obj = {
        uri: url,
        action: '',
        params: {
            className: className,
            method: method,
            param: param
        }
    };
    // console.log('postpostpost:',obj)
    return request.json(obj)
}

export function fileUpload(params) {
    let url = params.url;
    let param = params.param;

    const obj = {
        uri: url,
        action: '',
        params: param
    };
    return request1.json(obj)
}

export function fileDownload(params) {
    let url = params.url;
    let param = params.param;
    if (typeof params.param == 'string') {
        param = {filePath: params.param}
    }

    const obj = {
        uri: url,
        action: '',
        params: param
    };
    return request.json(obj)
}


