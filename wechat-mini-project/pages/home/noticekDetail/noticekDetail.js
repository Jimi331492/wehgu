// pages/home/noticekDetail/noticekDetail.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');

const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        notice: {}
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad() {
        this.setData({
            notice: app.globalData.curNotice
        })
    },

    
})