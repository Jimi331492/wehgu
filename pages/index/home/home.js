//获取应用实例
const app = getApp()

Page({
    data: {
        PageCur: 'Home',
        isLogin: wx.getStorageSync('isLogin'),
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

    onLoad: function () {
        if (wx.getStorageSync("isLogin")) {
            this.setData({
                isLogin: wx.getStorageSync("isLogin")
            })
        }
        this.setData({
            isExamine: app.globalData.isExamine
        })
        console.log(this.data.isLogin);
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    }
})