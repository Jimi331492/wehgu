// pages/index/login/login.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {

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
                        wx.redirectTo({
                            url: '/pages/index/index',
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

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function () {
        if (wx.getStorageSync('isLogin') === true) {
            wx.redirectTo({
                url: '/pages/index/index',
            })
        }
    },





    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    }
})