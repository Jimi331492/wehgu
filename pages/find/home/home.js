// pages/find/find.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');

const app = getApp()
Component({
    lifetimes: {
        attached() {
            this.setData({
                isLogin: wx.getStorageSync('isLogin')
            });
        }
    },
    data: {
        isLogin: false
    },
    methods: {
        getUserProfile() {
            app.getUserProfile()
        },
        
    }
})