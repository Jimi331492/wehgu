//app.js
const http = require('./utils/httputils.js');
const ContentTypeEnum = require('./utils/ContentTypeEnum.js');
App({
    require: function ($uri) {
        return wx.require($uri)
    },
    onLaunch: function () {
        // console.log('进入小程序')

        // 获取当前时间
        var nowTime = Date.parse(new Date())
        var delineTime = Date.parse('2022-4-6')
        // console.log(nowTime > delineTime)
        if (nowTime > delineTime) {
            // 说明已经过了审核周期，正常显示
            this.globalData.isExamine = false
        }
        console.log(this.globalData.isExamine)

        // 获取系统状态栏信息
        wx.getSystemInfo({
            success: e => {
                this.globalData.StatusBar = e.statusBarHeight;
                let capsule = wx.getMenuButtonBoundingClientRect();
                if (capsule) {
                    this.globalData.Custom = capsule;
                    this.globalData.CustomBar = capsule.bottom + capsule.top - e.statusBarHeight;
                } else {
                    this.globalData.CustomBar = e.statusBarHeight + 50;
                }
            }
        })

        //重写分享方法
        wx.onAppRoute(function (res) {
            //获取加载的页面
            let pages = getCurrentPages(),
                //获取当前页面的对象
                view = pages[pages.length - 1],
                data;
            if (view) {
                data = view.data;
                console.log('是否重写分享方法', data.isOverShare);
                if (!data.isOverShare) {
                    data.isOverShare = true;
                    wx.showShareMenu({
                        withShareTicket: true,
                        menus: ['shareAppMessage', 'shareTimeline'],
                    });
                    view.onShareAppMessage = function () {
                        //分享配置
                        return {
                            title: 'WeHgu', // 子页面的title
                            path: 'pages/index/login/login',
                            imageUrl: '/images/custom-avatar.png'
                        };
                    };
                    view.onShareTimeline = function () {
                        //分享配置
                        return {
                            title: 'WeHgu', // 子页面的title
                            query: "?a=pyq",
                            imageUrl: '/images/custom-avatar.png'
                        };
                    };

                }
            }
        })
    },

    //登录
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

                        wx.setStorageSync('sessionId', res.data.sessionId);
                        wx.setStorageSync('unionId', res.data.unionId);
                        wx.setStorageSync('openid', res.data.openId);
                        http.fill_token_toheader(res.data.unionId);
                        wx.setStorageSync('isLogin', true)
                        http.postRequest("/getMPUserInfo", res.data.unionId, ContentTypeEnum.Default_Sub,
                            res => {
                                wx.setStorageSync('userInfo', res.data);
                                wx.redirectTo({
                                    url: '/pages/index/home/home',
                                })
                            },
                            err => {
                                wx.showToast({
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

    // 退出登录
    confirmExit: function (content) {
        this.globalData.starList = []
        wx.clearStorage({
            success: (res) => {
                console.log(res)
                wx.showToast({
                    icon: 'success',
                    title: content ? content : '清除状态',
                    duration: 2000,
                })

                setTimeout(() =>
                    // app.reloadThisPage(),
                    wx.redirectTo({
                        url: '/pages/index/login/login',
                    }), 1000
                )
            },
        })
    },

    getValueListBykey(key) {
        return new Promise((resolve, reject) => {
            const query = {
                limit: '',
                page: '',
                key: key
            }
            http.postRequest(`/dictionary/getDictionaryPage`, query, ContentTypeEnum.Default_Sub,
                res => {
                    resolve(res.data)
                }, err => {
                    wx.showToast({
                        icon: "none",
                        title: err.message,
                    })
                    reject(err)
                })
        })
    },






    // 全局可用
    reloadThisPage: function () {
        let currentPages = getCurrentPages()
        let lastRoute = currentPages[currentPages.length - 1].route
        let options = currentPages[currentPages.length - 1].options
        let optionsStr = ""
        for (let key in options) {
            optionsStr += '?' + key + '=' + options[key]
        }
        wx.redirectTo({
            url: '/' + lastRoute + optionsStr,
        })
    },
    globalData: {
        isExamine: true, //过审
        currentPost: null, //当前浏览的帖子
        starList: [], //保存当前用户的点赞对象
    }
})