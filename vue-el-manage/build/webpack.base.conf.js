'use strict'
const path = require('path')
const utils = require('./utils')
const config = require('../config')
const vueLoaderConfig = require('./vue-loader.conf')

function resolve(dir) {
    return path.join(__dirname, '..', dir)
}


function resolveSVGIcons() {
    const project = process.env.package || 'document'
    const resolves = []
    resolves.push(resolve(`../vue-el-manage/src/${project}/icons`))
    let thirds = process.env.third || '[]'
    thirds = thirds.replace(/\//g, '"')
    thirds = JSON.parse(thirds)
    thirds.forEach(temp => {
        resolves.push(resolve(`../vue-el-manage-${temp.toLowerCase()}/src/icons`))
    })
    return resolves
}

const resolves = resolveSVGIcons()


module.exports = {
    context: path.resolve(__dirname, '../'),
    entry: {
        app: ['babel-polyfill', './src/main.js']
    },
    output: {
        path: config.build.assetsRoot,
        filename: '[name].js',
        publicPath: process.env.NODE_ENV === 'production'
            ? config.build.assetsPublicPath
            : config.dev.assetsPublicPath
    },
    resolve: {
        extensions: ['.js', '.vue', '.json'],
        alias: {
            'vue$': 'vue/dist/vue.esm.js',
            '@': resolve('../vue-el-manage'),
            '@COM': resolve('../vue-el-manage/src/common'),
            '@DOC': resolve('../vue-el-manage/src/document'),
            '@MAN': resolve('../vue-el-manage/src/manage'),
            '@C': resolve('../vue-el-manage/src/manage/components')
            // '@SEAL': resolve('../vue-el-manage-seal/src'),
        }
    },
    externals: {
        BMap: 'BMap'
    },
    module: {
        rules: [
            {
                test: /\.vue$/,
                loader: 'vue-loader',
                options: vueLoaderConfig
            },
            {
                test: /\.js$/,
                loader: 'babel-loader',
                include: [resolve('src'), resolve('test'), resolve('node_modules/webpack-dev-server/client')]
            },
            {
                test: /\.svg$/,
                loader: 'svg-sprite-loader',
                include: resolves,
                // include: [resolve(`src/${process.env.package||'document'}/icons`),resolve(`../vue-manage-crm/src/icons`),resolve(`../vue-manage-office/src/icons`)],
                options: {
                    symbolId: 'icon-[name]'
                }
            },
            {
                test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
                loader: 'url-loader',
                exclude: resolves,
                // exclude: [resolve(`src/${process.env.package||'document'}/icons`),resolve(`../vue-manage-crm/src/icons`),resolve(`../vue-manage-office/src/icons`)],
                options: {
                    limit: 10000,
                    name: utils.assetsPath('img/[name].[hash:7].[ext]')
                }
            },
            {
                test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    name: utils.assetsPath('media/[name].[hash:7].[ext]')
                }
            },
            {
                test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    name: utils.assetsPath('fonts/[name].[hash:7].[ext]')
                }
            }
        ]
    },
    node: {
        // prevent webpack from injecting useless setImmediate polyfill because Vue
        // source contains it (although only uses it if it's native).
        setImmediate: false,
        // prevent webpack from injecting mocks to Node native modules
        // that does not make sense for the client
        dgram: 'empty',
        fs: 'empty',
        net: 'empty',
        tls: 'empty',
        child_process: 'empty'
    }
}
