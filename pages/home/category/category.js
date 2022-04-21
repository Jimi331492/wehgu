// pages/home/category/category.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        title: null,
        query: {
            limit: 6,
            page: 1,
            auditStatus: '正常',
            tag: null,
        },
        total: 0,
        postList: [],

        triggered: false,
        isBottom: false,
    },

    onPulling(e) {},

    async onRefresh(e) {
        if (this._freshing === true) return
        this._freshing = true

        //重置
        this.data.query = {
            limit: 6,
            page: 1,
            auditStatus: '正常'
        };
        this.data.postList = [];
        this.data.total = 0;
        const res = await this.getPostList(this.data.query);
        this.setData({
            postList: res.data
        })
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
    async onLoad(options) {
        console.log(options);
        this.setData({
            ['query.tag']: options.tag,
            title: options.name
        })
        const res = await this.getPostList(this.data.query)
        this.data.total = res.data.total
        console.log(res);
        this.setData({
            postList: res.data.records,
        });
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