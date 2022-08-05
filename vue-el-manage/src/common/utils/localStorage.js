/**
 * author tjs
 * date 2021-09-29
 * @type {{set(string, Object, Number): void, get(string): (string|undefined), clear(): void, remove(string): void}}
 */

const storage = {
    /**
     * 保存方法
     * @param {string} key key值
     * @param {Object} value 保存内容 localStorage不能直接保存 Object 对象 需要使用 stringify 进行转换
     * @param {Number} expired 有效时间 定义 单位秒 非必须
     */
    set(key, value, expired) {
        /* 判断值的类型 localStorage不能直接保存 Object 对象 需要使用 stringify 进行转换*/
        if (typeof value !== 'string') {
            value = JSON.stringify(value);
        }

        localStorage.setItem(key, value.toString());
        //是否设置有效期
        if (expired) {
            /* 存储有效期 (new Date()).valueOf() 获取当前时间 */
            window.localStorage.setItem(`${key}_expires_`, ((new Date()).valueOf() + Number(1000 * expired)).toString());
        }
    },

    /**
     * 获取方法
     * @param {string} key key键
     * @returns {string,undefined}
     */
    get(key) {
        /* 存在没有设置有效期的情况，默认设置时间为(new Date()).valueOf() + 1 */
        const expired = window.localStorage.getItem(`${key}_expires_`) || (new Date()).valueOf() + 1;
        const now = (new Date()).valueOf();
        /* 判断当前时间 是否超过 失效时间  */
        if (now >= expired) {
            /* 超过失效时间 删除 储存内容  */
            window.localStorage.removeItem(key);
            return;
        }

        let val = window.localStorage.getItem(key);
        if (typeof value !== 'string') {
            val = JSON.parse(val);
        }
        return val;
    },

    /**
     * 清空方法
     */
    clear() {
        window.localStorage.clear();
    },

    /**
     * 移除方法
     * @param {string} key key键
     */
    remove(key) {
        window.localStorage.removeItem(key);
    },
}

export default storage;
