// pages/post/mine/mine.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');

const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        TabCur: 0,
        scrollLeft: 0,
        tabList: [{
            title: "全部",
            value: null,
        }, {
            title: "待审核",
            value: "待审核",
        }, {
            title: "未通过",
            value: "沉帖",
        }, {
            title: "已通过",
            value: "正常",
        }],
        query: {
            userDetailUuid: null,
            limit: 6,
            page: 1,
            auditStatus: null,
        },
        postList: [],
        total: 0,
        isBottom: false,
    },
    //获取帖子
    getPostList(query) {
        return new Promise((resolve, reject) => {
            wx.showLoading({
                title: '加载中...',
            })
            http.postRequest('/post/getPostPage', query, ContentTypeEnum.Json_Sub,
                res => {
                    resolve(res)
                    wx.hideLoading();
                }, err => {
                    console.log(err);
                    reject(err)
                    wx.hideLoading();
                })
        })

    },

    //tab导航
    async tabSelect(e) {
        this.setData({
            TabCur: e.currentTarget.dataset.id,
            scrollLeft: (e.currentTarget.dataset.id - 1) * 60,
            isBottom: false,
        })
        this.data.query.auditStatus = this.data.tabList[e.currentTarget.dataset.id].value
        const res = await this.getPostList(this.data.query)
        this.setData({
            postList: res.data.records,
            total: res.data.total,
        })

    },

    async loadPostList() {
        if (this.data.query.limit * this.data.query.page >= this.data.total) {
            if (this.data.isBottom) {
                wx.showToast({
                    icon: "none",
                    title: '到底了',
                })
            }
            this.setData({
                isBottom: true
            })
        }
        if (!this.data.isBottom) {
            this.data.query.page += 1;
            const res = await this.getPostList(this.data.query)
            let postList = [];
            this.data.total = res.data.total
            if (this.data.postList.length < 1) {
                postList = res.data.records
            } else {
                postList = this.data.postList.concat(res.data.records)
            }
            this.setData({
                postList: postList
            })
        }

    },

    onPulling(e) {},

    async onRefresh(e) {
        if (this._freshing === true) return
        this._freshing = true

        


        setTimeout(() => {
            this.setData({
                triggered: false,
                isBottom: false,
            })
            this._freshing = false
        }, 1000)
    },

    onRestore(e) {},

    onAbort(e) {},
    /**
     * 生命周期函数--监听页面加载
     */
    async onLoad() {
        const query = this.data.query
        query.userDetailUuid = wx.getStorageSync('userInfo').userDetailUuid
        const res = await this.getPostList(query)
        this.setData({
            postList: res.data.records,
            total: res.data.total,
        })
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