const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        userLocationList: [],
        modalName: null,

        form: {
            userLocationUuid: null,
            consignee: null, //收货人
            consigneeTelephone: null, //收货人电话
            deliveryLocation: null, //所在地区
            isDefault: false,
        },
        bakForm: {
            userLocationUuid: null,
            consignee: null, //收货人
            consigneeTelephone: null, //收货人电话
            deliveryLocation: null, //所在地区
            isDefault: false,
        },
    },
    isDefaultInput(e) {
        console.log(e.detail.value);
        this.setData({
            ['form.isDefault']: e.detail.value
        })
    },
    phoneInput(e) {
        this.setData({
            ['form.consigneeTelephone']: e.detail.value
        })
    },
    consigneeInput(e) {
        this.setData({
            ['form.consignee']: e.detail.value
        })
    },

    deliveryLocationInput(e) {
        this.setData({
            ['form.deliveryLocation']: e.detail.value
        })
    },
    showModal(e) {
        console.log(e.currentTarget.dataset.item);
        this.setData({
            modalName: e.currentTarget.dataset.target,
            form: e.currentTarget.dataset.item
        })
    },
    hideModal(e) {
        this.setData({
            modalName: null,
            form: {
                ...this.data.bakForm
            }
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
                this.setData({
                    userLocationList: res.data.records
                })
            }, err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            })
    },

    saveLocation() {

        http.postRequest("/user_location/saveLocation", this.data.form, ContentTypeEnum.Default_Sub,
            res => {
                wx.showToast({
                    icon: "none",
                    title: res.message,
                })

                app.reloadThisPage()
            }, err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            }
        )
    },

    deleteLocation(e) {
        http.delRequest(`/user_location/deleteLocation?UID=${e.currentTarget.dataset.id}`, null,
            res => {
                wx.showToast({
                    icon: "none",
                    title: res.message,
                })

                app.reloadThisPage()
            }, err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            }
        )
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function () {
        this.getUserLocationList()
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    }
})