// pages/index/login/login.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        modalName: "",
        telephone: null,
        flag: null, //手机号预校验状态
        codeList: [], //验证码数组
        codeFocus: false,
        curItemIndex: 0, //当前输入第几位Code
        isSend: false,
        countdown: 30,
        toggleDelay: false,
    },

    showModal(e) {
        console.log(e);
        this.setData({
            modalName: e.currentTarget.dataset.target
        })
    },
    hideModal(e) {
        this.setData({
            modalName: null
        })
    },

    telephoneInput(e) {
        console.log(e.detail.value)
        const reg = /^(0|86|17951)?(13[0-9]|15[0-9]|17[3678]|18[0-9]|14[57])\d{8}$/ig;
        let flag = reg.test(e.detail.value);
        console.log(flag);
        this.setData({
            telephone: e.detail.value,
            flag: flag
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
                console.log(res.rawData);
                const form = {
                    signature: res.signature,
                    rawData: res.rawData,
                    encryptedData: res.encryptedData,
                    iv: res.iv,
                    jscode: wx.getStorageSync('code'),
                    appid: "wxeb4f620b577ff31a"
                }

                http.postRequest("/mp_get_unionId", form, ContentTypeEnum.Json_Sub,
                    res => {
                        console.log(res);
                        wx.setStorageSync('sessionId', res.data.sessionId);
                        wx.setStorageSync('unionId', res.data.unionId);
                        wx.setStorageSync('openid', res.data.openId);
                        http.fill_token_toheader(res.data.unionId);
                        wx.setStorageSync('userInfo', res.data.userInfo);
                        wx.setStorageSync('isLogin', true)
                        this.setData({
                            isLogin: wx.getStorageSync('isLogin'),
                            userInfo: wx.getStorageSync('userInfo')
                        })
                        wx.redirectTo({
                            url: '/pages/index/home/home',
                        })
                    },
                    err => {
                        wx.showToast({
                            icon: 'none',
                            title: err.message,
                        })
                    }
                )
            },
            fail: err => {
                console.log(err);
            }
        })
    },

    itemFocus() {
        console.log(1);
        this.setData({
            codeFocus: true
        })
    },

    codeInputChange(e) {
        console.log(e.detail.value);
        let codeList = this.data.codeList;

        let list = e.detail.value.split('');
        this.setData({
            curItemIndex: list.length //控制当前item显示位置
        });

        if (codeList.length < list.length) { //代表输入
            if (list) {
                for (let i = 0; i < list.length; i++) { //将input内容分割显示
                    this.setData({
                        ['codeList[' + i + ']']: list[i],
                        ['item_' + i]: list[i]
                    })
                }
            }
        } else if (codeList.length > list.length) { //代表删除
            codeList.splice(codeList.length - 1, 1);
            this.setData({
                codeList: codeList
            })
        }
        if (codeList.length >= 4) { //执行发送请求
            this.codeLogin(codeList.join(""));
        }

    },

    // 发送验证码
    sendCode() {
        /**
         * 发送验证码接口
         */
        http.getRequest(`/sendSMS?telephone=${this.data.telephone}`, null,
            res => {
                wx.showToast({
                    icon: "none",
                    title: res.message,
                })
                console.log(res);
                this.setData({
                    isSend: true,
                    toggleDelay: true
                })
                setTimeout(() => {
                    this.setData({
                        toggleDelay: false
                    })
                }, 1000)
                this.countdownTimer()
            }, err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            })
    },

    countdownTimer() {
        const timer = setInterval(() => {
            console.log(this.data.countdown);
            this.setData({
                countdown: this.data.countdown - 1
            })
            if (this.data.countdown === 0) {
                this.setData({
                    isSend: false,
                    countdown: 30
                })
                clearInterval(timer)
            }

        }, 1000)
    },

    toIndexHome() {
        wx.navigateTo({
            url: '/pages/index/home/home',
        })
    },

    codeLogin(code) {
        wx.login({
            success: res => {
                if (res.code) {
                    wx.setStorageSync('code', res.code);
                }

                wx.getUserInfo({
                    lang: 'zh_CN',
                    success: res => {
                        console.log(res.rawData);
                        const form = {
                            signature: res.signature,
                            rawData: res.rawData,
                            encryptedData: res.encryptedData,
                            iv: res.iv,
                            jscode: wx.getStorageSync('code'),
                            appid: "wxeb4f620b577ff31a",
                            code: code,
                            telephone: this.data.telephone,
                        }

                        console.log(form);

                        /**
                         * 调用校验验证码参数
                         */
                        http.postRequest(`/SMSLogin`, form, ContentTypeEnum.Default_Sub,
                            res => {
                                console.log(res);
                                wx.setStorageSync('sessionId', res.data.sessionId);
                                wx.setStorageSync('unionId', res.data.unionId);
                                wx.setStorageSync('openid', res.data.openId);
                                http.fill_token_toheader(res.data.unionId);
                                wx.setStorageSync('userInfo', res.data.userInfo);
                                wx.setStorageSync('isLogin', true)
                                this.setData({
                                    isLogin: wx.getStorageSync('isLogin'),
                                    userInfo: wx.getStorageSync('userInfo')
                                })
                                wx.redirectTo({
                                    url: '/pages/index/home/home',
                                })
                            }, err => {
                                wx.showToast({
                                    icon: "none",
                                    title: err.message,
                                })
                            })
                    },
                    fail: err => {
                        console.log(err);
                    }
                })
            }
        })



    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad() {
        if (wx.getStorageSync('isLogin') === true) {
            wx.redirectTo({
                url: '/pages/index/home/home',
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