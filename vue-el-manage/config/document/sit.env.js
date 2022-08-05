const merge = require('webpack-merge')
const prodEnv = require('./prod.env')
module.exports = merge(prodEnv,{
  ENV_CONFIG: '"sit"',
  BASE_API: '"https://api-sit"',
  MOCK:true,
})
