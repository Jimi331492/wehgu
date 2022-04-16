//获取应用实例
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');
const app = getApp()

Page({
    data: {
        PageCur: 'Home',
        isLogin: false,
        isExamine: true,
        starList: [],
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



    onLoad: function () {
        if (wx.getStorageSync('isLogin') === true) {
            this.setData({
                isLogin: true
            })

            //延时获取starList
            setTimeout(() => {
                this.setData({
                    starList: app.globalData.starList
                });
                console.log('sss' + JSON.stringify(this.data.starList));
            }, 800)

        }

        this.setData({
            isExamine: app.globalData.isExamine
        })
        console.log(this.data.isLogin);
    },


})