const ContentTypeEnum = require('./ContentTypeEnum');
const app = getApp()
/**
 * 请求头
 */
//域名 正式版
const baseURL = 'https://serve.my3iao.com/wehgu'
//IP 测试版
// const baseURL = 'http://47.93.25.230:10077/wehgu'
//本地 开发版
// const baseURL = 'http://localhost:10077/wehgu'

function fill_token_toheader(token) {
  if (token == null || token == undefined || token == "") {
    token = wx.getStorageSync('unionId');
  }
  header["AuthorizationWxA"] = token;
  // console.log(token)
  return (token)
}
const header = {
  'os': 'android',
  'version': '1.0.0',
  'sessionId': wx.getStorageSync('sessionId'),
  'unionId': wx.getStorageSync('unionId'),
  'AuthorizationAppWx': 'wxeb4f620b577ff31a',
  'openId': wx.getStorageSync('openId')
}
/**
 * 供外部post请求调用  
 */
function post(url, params, contenttypeEnum, onSuccess, onFailed) {
  // console.log("请求方式：", "POST")
  if (contenttypeEnum == ContentTypeEnum.Form_Sub) {
    header["content-type"] = "application/x-www-form-urlencoded";
  } else if (contenttypeEnum == ContentTypeEnum.Json_Sub) {
    header["content-type"] = "application/json;charset=UTF-8";
  } else if (contenttypeEnum == ContentTypeEnum.Upload_Sub) {
    header["content-type"] = "multipart/form-data";
  } else {
    header["content-type"] = "application/json;charset=UTF-8";
  }
  request(url, params, "POST", onSuccess, onFailed);
}

/**
 * 供外部get请求调用
 */
function get(url, params, onSuccess, onFailed) {
  // console.log("请求方式：", "GET")
  request(url, params, "GET", onSuccess, onFailed);
}

/**
 * function: 封装网络请求
 * @url URL地址
 * @params 请求参数
 * @method 请求方式：GET/POST
 * @onSuccess 成功回调
 * @onFailed  失败回调
 */

function request(url, params, method, onSuccess, onFailed) {
  wx.showLoading({
    title: "正在加载中...",
  })

  let fullpath = getFullRequestPath(url);
  header["Cookie"] = wx.getStorageSync('cookie');
  header["wxaSessionId"] = wx.getStorageSync('wxaSessionId');
  header["openid"] = wx.getStorageSync('openid');
  wx.request({
    url: fullpath,
    data: dealParams(params),
    method: method,
    header: header,
    success: function (res) {
      wx.hideLoading()
      // console.log('响应：', res);
      if (res.header) {
        if ('Set-Cookie' in res.header) {
          if (res.header['Set-Cookie']) {
            wx.setStorageSync('cookie', res.header['Set-Cookie']);
          }
        } else if ('set-cookie' in res.header) {
          if (res.header['set-cookie']) {
            wx.setStorageSync('cookie', res.header['Set-Cookie']);
          }
        }
      }
      if (res.data.code === 200) {
        onSuccess(res.data); //request success
      } else {
        wx.showToast({
          title: res.data.message,
          icon: 'none',
          duration: 2000
        })
        onFailed(res.data)
      }
    },
    fail: function (error) {
      wx.hideLoading();
      wx.showToast({
        title: 'Network Error',
        icon: 'none',
        duration: 2000
      })
      onFailed(error); //failure for other reasons
    }
  })
}

/**
 * function: 根据需求处理请求参数：添加固定参数配置等
 * @params 请求参数
 */
function dealParams(params) {
  // console.log("请求参数:", params)
  return params;
}

function getFullRequestPath(url) {
  return baseURL + url;
}


// 1.通过module.exports方式提供给外部调用
module.exports = {
  baseURL: baseURL,
  postRequest: post,
  getRequest: get,
  fill_token_toheader: fill_token_toheader,
  getFullRequestPath: getFullRequestPath
}