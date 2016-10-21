/**
 * Created by jackie0 on 2016/8/3.
 */
var webpack = require('webpack');
var commonsPlugin = new webpack.optimize.CommonsChunkPlugin({name: 'common', minChunks: 2});
var path = require("path");
module.exports = {
    //插件项
    plugins: [commonsPlugin],
    //页面入口文件配置
    entry: {
        index: '../webapp/react/index.js',
        login: '../webapp/react/login.js'
    },
    //入口文件输出配置
    output: {
        path: '../webapp/react/build',
        filename: '[name].bundle.js'
    },
    module: {
        //加载器配置
        loaders: [
            {test: /\.css$/, loader: 'style-loader!css-loader'},
            {
                test: /\.js$/, loader: 'babel-loader',
                exclude: /(node_modules|bower_components)/,
                query: {
                    presets: ['es2015', 'react'],
                    "plugins": [["antd", {
                        style: 'css'
                    }]]
                }
            },
            {test: /\.scss$/, loader: 'style!css!sass?sourceMap'},
            {test: /\.(png|jpg)$/, loader: 'url-loader?limit=8192'}
        ]
    },
    //其它解决方案配置
    resolve: {
        root: ['G:/work-space/git/jackie0/showcase/src/main/webapp/resources/js/src'], //绝对路径
        extensions: ['', '.js', '.json', '.scss'],
        alias: {
            /*AppStore: 'js/stores/AppStores.js',
             ActionType: 'js/actions/ActionType.js',
             AppAction: 'js/actions/AppAction.js'*/
        }
    }
};