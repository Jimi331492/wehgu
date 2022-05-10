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

        //发布订单相关
        userLocationList: [],
        orderForm: {
            expected: null, //期望：0男生 1女生 NULL无所谓
            price: null, //价格
            deadlineTime: null, //最晚期限
            packageUIDList: [],
            userLocationUuid: null, //用户收货地址
            userLocation: null, //收货地址 展示用的
        }
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

        bindUserLocation(e) {
            this.setData({
                ['orderForm.userLocation']: this.data.userLocationList[e.detail.value].deliveryLocation,
                ['orderForm.userLocationUuid']: this.data.userLocationList[e.detail.value].userLocationUuid
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
        //更新组件视图
        // updateModalView(data) {
        //     return new Promise((resolve) => {
        //         //获取Modal组件给tabList赋值
        //         const pages = getCurrentPages();
        //         const currPage = pages[pages.length - 1]; //当前页面
        //         const order = currPage.selectComponent("#order")
        //         const modal = order.selectComponent("#modal")
        //         modal.setData(data)
        //         resolve(true)
        //     })

        // },

        async openMyCodeModal(e) {


            this.getUserLocationList()


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
                choosedPackageList: e.detail.value
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
                        ['orderForm.userLocation']: defaultLocation.deliveryLocation,
                        ['orderForm.userLocationUuid']: defaultLocation.userLocationUuid,
                    })
                }, err => {
                    wx.showToast({
                        icon: "none",
                        title: err.message,
                    })
                })
        }

    }
})