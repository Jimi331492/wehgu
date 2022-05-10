// pages/order/detail/detail.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');

const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        basicsList: [{
            icon: 'usefullfill',
            name: '选择包裹'
        }, {
            icon: 'radioboxfill',
            name: '填写信息'
        }, {
            icon: 'roundclosefill',
            name: '支付订单'
        }, {
            icon: 'roundcheckfill',
            name: '等待接单'
        }, ],
        form: {
            expected: null, //期望：0男生 1女生 NULL无所谓
            price: null, //价格
            deadlineTime: null, //最晚期限
            packageUIDList: [],
            userLocationUuid: null, //用户收货地址
            userLocation: null, //收货地址 展示用的
        }
    },

    TimeChange(e) {
        this.setData({
            ['form.deadlineTime']: e.detail.value
        })
    },
    priceInput(e) {
        this.setData({
            ['form.price']: e.detail.value
        })
    },
    expectedChange(e) {
        this.setData({
            ['form.expected']: e.detail.value
        })
    },

    saveOrder() {},
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad() {
        this.setData({
            form: app.globalData.curOrderForm
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