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
            app.getUserProfile()
        },

        //刷新数据
        changeData() {
            this.setData({
                userInfo: wx.getStorageSync('userInfo')
            })
          
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