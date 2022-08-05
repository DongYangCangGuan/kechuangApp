const merge = require('webpack-merge')
const prodEnv = require('./prod.env')
module.exports = merge(prodEnv, {
    NODE_ENV: '"development"',
    ENV_CONFIG: '"dev"',
    BASE_API: '""',
    MINIMEETING_BASE_API: '"/seal/"',
    ERROR_HANDLER: '"json"',//错误回调时 使用json、form格式提交信
})
