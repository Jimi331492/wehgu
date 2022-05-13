const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');

const app = getApp()
Component({

    lifetimes: {
        async attached() {
            this.setData({
                isLogin: wx.getStorageSync('isLogin')
            });


            const res = await this.getOrderList(this.data.query)
            this.setData({
                total: res.data.total,
                orderList: res.data.records
            })
        }
    },
    options: {
        addGlobalClass: true,
    },
    data: {
        isLogin: false,
        //导航栏相关
        TabCur: 0,
        scrollLeft: 0,
        tabList: [{
            label: "全部",
            value: null
        }, {
            label: "未发布",
            value: '待付款',
        }, {
            label: "待送货",
            value: '待送货',

        }, {
            label: "已完成",
            value: '已完成',
        }],

        //默认查询订单参数
        query: {
            limit: 8,
            page: 1,
            orderStatus: '待接单'
        },

        total: 0,
        orderList: [],

        //新增包裹相关
        pickUpSiteList: [],
        bakPackageForm: {
            pageageUuid: null,
            pickUpCode: null,
            pickUpSite: null,
            remark: null,
        },
        packageForm: {
            pageageUuid: null,
            pickUpCode: null,
            pickUpSite: null,
            remark: null,
        },
        packageQueryParams: {
            limit: '',
            page: '',
            orderStatus: null,
        },
        packageList: [], //包裹列表
        choosedPackageList: [], //被选中的包裹列表
        choosedPackageUIDList: [], //被选中的包裹UID列表
        //发布订单相关
        userLocationList: [],

    },
    methods: {

        pickUpCodeInput(e) {
            this.setData({
                ['packageForm.pickUpCode']: e.detail.value
            })
        },

        bindPickUpSite(e) {
            this.setData({
                ['packageForm.pickUpSite']: this.data.pickUpSiteList[e.detail.value].value
            })
        },

        remarkInput(e) {
            this.setData({
                ['packageForm.remark']: e.detail.value
            })
        },
        navigateTo(e) {
            wx.navigateTo({
                url: e.currentTarget.dataset.path,
            })
        },
        showModal(e) {
            this.setData({
                modalName: e.currentTarget.dataset.target
            })
        },

        toDetail(e) {
            const choosedPackageList = [];
            this.data.choosedPackageUIDList.forEach(item => {
                const bag = this.data.packageList.find(e => {
                    return e.packageUuid === item
                })
                choosedPackageList.push(bag)
            })
            console.log(choosedPackageList);
            app.globalData.choosedPackageList = choosedPackageList
            this.navigateTo(e)
        },
        async openMyCodeModal(e) {

            const res = await this.getPackageList(this.data.packageQueryParams);
            const data = {
                tabList: this.data.tabList,
                packageList: res.data.records
            }

            this.setData(data)
            this.showModal(e);
        },

        async openAddCodeModal(e) {
            this.showModal(e)
            //获取驿站
            const userInfo = wx.getStorageSync('userInfo')
            const key = `${userInfo.university}-${userInfo.campus}-驿站`
            const res = await app.getValueListBykey(key)
            const pickUpSiteList = res.records

            this.setData({
                pickUpSiteList: pickUpSiteList
            })

        },

        hideModal() {
            console.log(this.data.telephone);
            this.setData({
                modalName: null
            })
        },
        //tab导航
        async tabSelect(e) {
            this.setData({
                TabCur: e.currentTarget.dataset.id,
                scrollLeft: (e.currentTarget.dataset.id - 1) * 60,
                isBottom: false,
            })

            this.data.packageQueryParams.orderStatus = this.data.tabList[e.currentTarget.dataset.id].value
            const res = await this.getPackageList(this.data.packageQueryParams);

            this.setData({
                packageList: res.data.records
            })
        },

        //获取取件码
        getOrderList(query) {
            return new Promise((resolve, reject) => {
                http.postRequest('/order/getOrderPage', query, ContentTypeEnum.Default_Sub,
                    res => {
                        resolve(res);
                    }, err => {
                        reject(err)
                    })
            })
        },



        savePackage() {
            http.postRequest('/package/savePackage', this.data.packageForm, ContentTypeEnum.Default_Sub,
                res => {
                    //reset Form

                    wx.showToast({
                        icon: "none",
                        title: res.message,
                    })
                    this.updatePackList()
                    this.setData({
                        modalName: 'MyCode',
                        packageForm: this.data.bakPackageForm
                    })
                }, err => {
                    wx.showToast({
                        icon: "none",
                        title: err.message,
                    })
                })
        },
        async updatePackList() {
            const res = await this.getPackageList(this.data.packageQueryParams);

            this.setData({
                packageList: res.data.records
            })
        },
        confirmDeletePackage(e) {
            wx.showModal({
                title: '提示',
                content: '是否删除？',
                cancelText: '取消',
                confirmText: '确定',
                success: res => {
                    if (res.confirm) {
                        this.deletePackage(e)
                    }
                }
            })
        },
        deletePackage(e) {
            http.delRequest(`/package/batchDeletePackage`, [e.currentTarget.dataset.id],
                res => {
                    wx.showToast({
                        icon: "none",
                        title: res.message,
                    })
                    this.updatePackList();
                }, err => {
                    wx.showToast({
                        icon: "none",
                        title: err.message,
                    })
                })
        },


        getPackageList(query) {
            return new Promise((resolve, reject) => {
                http.postRequest('/package/getPackagePage', query, ContentTypeEnum.Default_Sub,
                    res => {
                        resolve(res);
                    }, err => {
                        reject(err)
                    })
            })
        },
        //被选中的包裹变化
        choosedPackageChange(e) {
            console.log(e);

            this.setData({

                choosedPackageUIDList: e.detail.value
            })
        },



    }
})