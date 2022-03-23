// pages/home/home.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');

const app = getApp()
Component({
    options: {
        addGlobalClass: true,
    },
    lifetimes: {
        attached() {

            this.towerSwiper('swiperList');
            // 初始化towerSwiper 传已有的数组名即可

            this.setData({
                isLogin: wx.getStorageSync('isLogin')
            });
        }
    },
    data: {
        isLogin: false,
        // 轮播图相关
        cardCur: 0,
        swiperList: [{
            id: 0,
            type: 'image',
            url: ''
        }, {
            id: 1,
            type: 'image',
            url: ''
        }, {
            id: 2,
            type: 'image',
            url: ''
        }, {
            id: 3,
            type: 'image',
            url: ''
        }, {
            id: 4,
            type: 'image',
            url: ''
        }, {
            id: 5,
            type: 'image',
            url: ''
        }, {
            id: 6,
            type: 'image',
            url: ''
        }],
        //菜单导航相关
        menuList: [{
            icon: 'cardboardfill',
            color: 'red',
            badge: 0,
            name: '表白墙',
            url: "/pages/getCowList/getCowList"
        }, {
            icon: 'shopfill',
            color: 'yellow',
            badge: 0,
            name: '跳蚤市场',
            url: "/pages/farmUnit/farmUnit"
        }, {
            icon: 'writefill',
            color: 'cyan',
            badge: 0,
            name: '学习专区',
            url: "/pages/yield/yield"
        }, {
            icon: 'sponsorfill',
            color: 'olive',
            badge: 0,
            name: '失物招领',
            url: "/pages/cost/cost"
        }, {
            icon: 'servicefill',
            color: 'yellow',
            badge: 0,
            name: '在线客服',
            url: "/pages/expert/expert"
        }, {
            icon: 'countdownfill',
            color: 'orange',
            badge: 0,
            name: '校园活动',
            url: "/pages/dietary/dietary"
        }, {
            icon: 'messagefill',
            color: 'olive',
            badge: 0,
            name: '吐槽专区',
            url: "/pages/breedConsult/breedConsult"
        }, {
            icon: 'questionfill',
            color: 'olive',
            badge: 0,
            name: '问题反馈',
            url: "/pages/illnessList/illnessList"
        }],
        gridCol: 4,
        skin: false,

        //导航栏相关
        TabCur: 0,
        scrollLeft: 0,
        tabList: ["最新发布", "最多人看", "距离最近", "历史记录"]
    },
    methods: {

        // 路由跳转
        navChange(e) {
            console.log(e.currentTarget.dataset.url);
            let url = e.currentTarget.dataset.url
            if (!this.data.isLogin) {
                wx.showModal({
                    title: '提示',
                    content: '用户未登录。',
                    confirmText: '去登陆',
                    success: res => {
                        if (res.confirm) {
                            this.getUserProfile()
                        }
                    }
                })
            } else {
                wx.navigateTo({
                    url: url,
                })
            }
        },

        /**
         * 用户点击右上角分享
         */
        onShareAppMessage: function () {

        },
        DotStyle(e) {
            this.setData({
                DotStyle: e.detail.value
            })
        },
        // cardSwiper
        cardSwiper(e) {
            this.setData({
                cardCur: e.detail.current
            })
        },
        // towerSwiper
        // 初始化towerSwiper
        towerSwiper(name) {
            let list = this.data[name];
            for (let i = 0; i < list.length; i++) {
                list[i].zIndex = parseInt(list.length / 2) + 1 - Math.abs(i - parseInt(list.length / 2))
                list[i].mLeft = i - parseInt(list.length / 2)
            }
            this.setData({
                swiperList: list
            })
        },
        // towerSwiper触摸开始
        towerStart(e) {
            this.setData({
                towerStart: e.touches[0].pageX
            })
        },
        // towerSwiper计算方向
        towerMove(e) {
            this.setData({
                direction: e.touches[0].pageX - this.data.towerStart > 0 ? 'right' : 'left'
            })
        },
        // towerSwiper计算滚动
        towerEnd(e) {
            let direction = this.data.direction;
            let list = this.data.swiperList;
            if (direction == 'right') {
                let mLeft = list[0].mLeft;
                let zIndex = list[0].zIndex;
                for (let i = 1; i < list.length; i++) {
                    list[i - 1].mLeft = list[i].mLeft
                    list[i - 1].zIndex = list[i].zIndex
                }
                list[list.length - 1].mLeft = mLeft;
                list[list.length - 1].zIndex = zIndex;
                this.setData({
                    swiperList: list
                })
            } else {
                let mLeft = list[list.length - 1].mLeft;
                let zIndex = list[list.length - 1].zIndex;
                for (let i = list.length - 1; i > 0; i--) {
                    list[i].mLeft = list[i - 1].mLeft
                    list[i].zIndex = list[i - 1].zIndex
                }
                list[0].mLeft = mLeft;
                list[0].zIndex = zIndex;
                this.setData({
                    swiperList: list
                })
            }
        },
        //tab导航
        tabSelect(e) {
            this.setData({
                TabCur: e.currentTarget.dataset.id,
                scrollLeft: (e.currentTarget.dataset.id - 1) * 60
            })
        }


    }
})