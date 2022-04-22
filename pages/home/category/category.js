// pages/home/category/category.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        bakPath: '/images/custom-avatar.png',
        title: null,
        tag: null,
        query: {
            limit: 6,
            page: 1,
            auditStatus: '正常',
            tag: null,
            content: null,
        },
        content: null,
        total: 0,
        postList: [],
        starList: [],

        triggered: false,
        isBottom: false,
    },
    contentInput(e) {
        this.setData({
            content: e.detail.value
        })
    },
    async reset() {
        const query = {
            limit: 6,
            page: 1,
            auditStatus: '正常',
            tag: this.data.tag,
            content: null,
        };
        this.setData({
            content: null,
            isBottom: false,
            total: 0,
            query: query
        })
        const res = await this.getPostList(this.data.query)
        this.data.total = res.data.total
        this.setData({
            postList: res.data.records
        })
    },
    async searchPost() {
        if (this.data.content === null || this.data.content === '') {
            wx.showToast({
                icon: 'none',
                title: '请输入关键字查询',
            })
            return false
        }
        //重置
        this.data.query = {
            limit: 6,
            page: 1,
            auditStatus: '正常',
            tag: this.data.tag,
            content: this.data.content,
        };
        const res = await this.getPostList(this.data.query)
        this.data.total = res.data.total

        this.setData({
            postList: res.data.records
        })
    },

    onPulling(e) {},

    async onRefresh(e) {
        if (this._freshing === true) return
        this._freshing = true

        //重置
        this.data.query = {
            limit: 6,
            page: 1,
            auditStatus: '正常',
            tag: this.data.tag
        };
        this.data.postList = [];
        this.data.total = 0;
        const res = await this.getPostList(this.data.query);
        this.setData({
            postList: res.data.records
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
            title: options.name,
            tag: options.tag,
            starList: app.globalData.starList,
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
                    if (res.data.total <= 6) {
                        this.setData({
                            isBottom: true,
                        })
                    }
                    wx.hideLoading();
                }, err => {
                    console.log(err);
                    reject(err)
                    wx.hideLoading();
                })
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