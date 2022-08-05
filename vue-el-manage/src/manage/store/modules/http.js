import {post, fileUpload, fileDownload} from '@MAN/api/http'

const http = {
    namespaced: true,
    actions: {
        post: ({commit}, params) => {
            // console.log('wozijideqingqiu:',commit,params)
            return new Promise(
                resolve => {
                    post(params).then(
                        response => {
                            resolve(response.data)
                        }
                    )
                }
            )
        },
        fileUpload: ({commit}, params) => {
            return new Promise(
                resolve => {
                    fileUpload(params).then(
                        response => {
                            resolve(response.data)
                        }
                    )
                }
            )
        },
        fileUpload2: ({commit}, params) => {
                return new Promise(
                        resolve => {
                            fileUpload(params).then(
                                response => {
                                resolve(response)
                        }
                    )
                }
            )
        },
        fileDownload: ({commit}, params) => {
            return new Promise(
                resolve => {
                    fileDownload(params).then(
                        response => {
                            resolve(response.data)
                        }
                    )
                }
            )
        },
    }
};

export default http
