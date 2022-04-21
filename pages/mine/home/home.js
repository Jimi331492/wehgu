const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');

const app = getApp()
Component({
    options: {
        addGlobalClass: true,
        multipleSlots: true
    },
    lifetimes: {
        attached() {
            if (true !== wx.getStorageSync('isLogin')) {
                this.setData({
                    ['userInfo.avatar']: '/images/custom-avatar.png',
                });
            } else {
                this.setData({
                    userInfo: wx.getStorageSync("userInfo"),
                    isLogin: wx.getStorageSync('isLogin')
                });
            }


        }
    },
    data: {
        userInfo: null,
        isLogin: false
    },
    methods: {
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

                    http.postRequest("/mp_get_unionId", form, ContentTypeEnum.Json_Sub,
                        res => {
                            console.log(res);
                            wx.setStorageSync('sessionId', res.data.sessionId);
                            wx.setStorageSync('unionId', res.data.unionId);
                            wx.setStorageSync('openid', res.data.openId);
                            http.fill_token_toheader(res.data.unionId);

                            http.postRequest("/getMPUserInfo", res.data.unionId, ContentTypeEnum.Default_Sub,
                                res => {
                                    wx.setStorageSync('userInfo', res.data);
                                    wx.setStorageSync('isLogin', true)
                                    this.setData({
                                        userInfo: wx.getStorageSync('userInfo'),
                                        isLogin: true
                                    })
                                },
                                err => {
                                    wx.showToast({
                                        icon: "none",
                                        title: err.message,
                                    })
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

        //刷新数据
        changeData() {
            this.setData({
                userInfo: wx.getStorageSync('userInfo')
            })
            console.log(this.data.userInfo.introduce);
            console.log('上一个页面一键刷新');
        },
        // 路由跳转
        navChange(e) {
            console.log(e.currentTarget.dataset.url);
            let url = e.currentTarget.dataset.url
            if (!this.data.isLogin) {
                this.noLogin()
            } else {
                wx.navigateTo({
                    url: url,
                })
            }
        },

        toLogin() {
            wx.navigateBack({
                delta: 1,
            })
        },

        noLogin() {
            wx.showModal({
                title: '提示',
                content: '用户未登录。',
                confirmText: '一键登陆',
                success: res => {
                    if (res.confirm) {
                        this.getUserProfile()
                    }
                }
            })
        },

        toEdit() {
            wx.navigateTo({
                url: '/pages/mine/edit/edit',
            })
        },

        navigateTo(e) {
            wx.navigateTo({
                url: e.currentTarget.dataset.path,
            })
        },

        // 退出登录
        confirmExit: function () {
            app.confirmExit("退出登录")
        },
    }
})