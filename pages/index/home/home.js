//index.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');
//获取应用实例
const app = getApp()

Page({
    data: {
        PageCur: 'Mine',
        isLogin: wx.getStorageSync('isLogin')
    },

    NavChange(e) {
        console.log(e.currentTarget.dataset.cur);
        this.setData({
            PageCur: e.currentTarget.dataset.cur
        })
    },

    getUserProfile: function () {
        wx.login({
            success: res => {
                if (res.code) {
                    wx.setStorageSync('code', res.code);
                }
            }
        })

        wx.getUserProfile({
            desc: '用于完善资料',
            success: res => {
                const form = {
                    signature: res.signature,
                    rawData: res.rawData,
                    encryptedData: res.encryptedData,
                    iv: res.iv,
                    jscode: wx.getStorageSync('code'),
                    appid: "wxeb4f620b577ff31a"
                }

                http.postRequest("/mp_login", form, ContentTypeEnum.Json_Sub,
                    res => {
                        console.log(res);
                        wx.setStorageSync('sessionId', res.data.sessionId);
                        wx.setStorageSync('unionId', res.data.unionId);
                        wx.setStorageSync('openid', res.data.openId);
                        wx.setStorageSync('userInfo', res.data.userInfo);
                        wx.setStorageSync('isLogin', true)
                        http.fill_token_toheader(res.data.unionId);
                        this.setData({
                            isLogin: wx.getStorageSync('isLogin'),
                            userInfo: wx.getStorageSync('userInfo')
                        })
                    },
                    err => {
                        console.log(err);
                    }
                )
            },
            fail: err => {
                console.log(err);
            }
        })
    },


    onLoad: function () {
        console.log(this.data.isLogin);
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    }
})