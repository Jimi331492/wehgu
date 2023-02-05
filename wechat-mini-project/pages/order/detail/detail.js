// pages/order/detail/detail.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');

const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        nowTime: null,
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
        userLocationList: [],
        form: {
            expected: null, //期望：0男生 1女生 NULL无所谓
            price: null, //价格
            deadlineTime: null, //最晚期限
            packageUIDList: [],
            userLocationUuid: null, //用户收货地址
            userLocation: null, //收货地址 展示用的
        },
        defaultLocation: null,
        choosedPackageList: [],
    },
    navigateTo(e) {
        wx.navigateTo({
            url: e.currentTarget.dataset.path,
        })
    },
    TimeChange(e) {
        console.log(e);


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
            ['form.expected']: e.currentTarget.dataset.expected
        })
    },

    bindUserLocation(e) {
        this.setData({
            defaultLocation: this.data.userLocationList[e.detail.value],
            ['form.userLocationUuid']: this.data.userLocationList[e.detail.value].userLocationUuid
        })
    },

    getUserLocationList() {
        const query = {
            limit: '',
            page: '',
            userDetailUuid: wx.getStorageSync('userInfo').userDetailUuid
        }
        http.postRequest('/user_location/getLocationPage', query, ContentTypeEnum.Default_Sub,
            res => {
                const defaultLocation = res.data.records.find(item => {
                    return item.isDefault === true
                })
                this.setData({
                    userLocationList: res.data.records,

                    ['form.userLocationUuid']: defaultLocation.userLocationUuid,
                    defaultLocation: defaultLocation,
                })
            }, err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            })
    },
    toPayTestUnit(uid) {
        wx.login({
            success: res => {
                console.log(res.code);
                if (res.code) {
                    var parms = {
                        "money": 1 * 100,
                        "code": res.code,
                        "wareName": "WeHgu微信支付测试"
                    }
                    http.postRequest("/wx/wxPay", parms, ContentTypeEnum.Default_Sub,
                        res => {
                            console.log(res)
                            console.log(uid + "2");
                            http.postRequest('/order/saveOrder', {
                                    orderUuid: uid,
                                    orderStatus: "待接单",
                                    ifPay: true,
                                }, ContentTypeEnum.Json_Sub,
                                res => {
                                    wx.showToast({
                                        title: '发布成功',
                                    })
                                }, err => {
                                    console.log(err);
                                })
                            // wx.requestPayment({
                            //     timeStamp: data.timeStamp,
                            //     nonceStr: data.nonceStr,
                            //     package: data.package,
                            //     signType: 'MD5',
                            //     paySign: data.paySign,
                            //     success: res => {
                            //         /**
                            //          * 修改订单状态
                            //          */


                            //     },
                            //     fail: res => {
                            //         console.log("error====>", res)
                            //     }
                            // })
                        },
                        res => {
                            console.log("postRequest err===>", res)
                        }
                    )
                }
            }
        })
    },
    saveOrder() {
        const time = new Date();
        const arr = this.data.form.deadlineTime.split(":")
        time.setHours(arr[1])
        time.setMinutes(arr[0])
        this.data.form.deadlineTime = time
        console.log(time);
        http.postRequest('/order/saveOrder', this.data.form, ContentTypeEnum.Default_Sub,
            res => {
                /**
                 * 调用支付接口
                 */
                wx.showLoading({
                    title: '调用支付接口...',
                })
                console.log(res.data);
                this.toPayTestUnit(res.data)
                wx.hideLoading()
            }, err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            })
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad() {
        const nowTime = new Date()
        const str = `${nowTime.getHours()+1}:${nowTime.getMinutes()}`
        this.getUserLocationList()
        const packageUIDList = app.globalData.choosedPackageList.map(item => {
            return item.packageUuid
        })
        this.setData({
            choosedPackageList: app.globalData.choosedPackageList,
            ['form.deadlineTime']: str,
            ['form.packageUIDList']: packageUIDList
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