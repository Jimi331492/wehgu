//获取应用实例
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');
const app = getApp()

Page({
    data: {
        PageCur: 'Home',
        isLogin: false,
        isExamine: true
    },

    NavChange(e) {
        console.log(e.currentTarget.dataset.cur);
        this.setData({
            PageCur: e.currentTarget.dataset.cur
        })
    },

    navigateTo(e) {
        wx.navigateTo({
            url: e.currentTarget.dataset.path,
        })
    },

    getStarList() {
        const userDetailId = wx.getStorageInfoSync("userInfo").userDetailUuid
        const query = {
            limit: "",
            page: "",
            userDetailUuid: userDetailId,
            status: 1
        }
        http.postRequest('/star/getStarPage', query, ContentTypeEnum.Default_Sub,
            res => {
                app.globalData.starList = res.data.records
                console.log(res.data.records);
            }, err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            })
    },

    onLoad: function () {
        if (wx.getStorageSync('isLogin') === true) {
            this.setData({
                isLogin: true
            })
            this.getStarList()
        }

        this.setData({
            isExamine: app.globalData.isExamine
        })
        console.log(this.data.isLogin);
    },


})