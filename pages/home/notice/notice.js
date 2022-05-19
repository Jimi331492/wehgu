// pages/home/notice/notice.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        noticeList: [],
    },
    toItem(e) {
        app.globalData.curNotice = e.currentTarget.dataset.item
        this.navigateTo(e)
    },
    navigateTo(e) {
        wx.navigateTo({
            url: e.currentTarget.dataset.path,
        })
    },

    getNoticeList() {
        const query = {
            limit: '',
            type: 0,
            page: '',
        }
        http.postRequest('/notice/getNoticePage', query, ContentTypeEnum.Json_Sub,
            res => {
                this.setData({
                    noticeList: res.data.records
                })
            }, err => {
                console.log(err);
            })
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad() {
        this.getNoticeList()
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload() {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})