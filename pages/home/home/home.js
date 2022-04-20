// pages/home/home.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');

const app = getApp()
Component({
    options: {
        addGlobalClass: true,
    },
    properties: {
        starList: {
            type: Array,
            default: []
        },
    },
    lifetimes: {
        attached() {

            this.towerSwiper();
            // 初始化towerSwiper 传已有的数组名即可
            this.getPostList()
            this.setData({
                isLogin: wx.getStorageSync('isLogin'),
                isExamine: app.globalData.isExamine,
            });


        },
        ready() {
            console.log('#home' + JSON.stringify(this.data.starList));
        }

    },
    data: {
        bakPath: '/images/custom-avatar.png',

        isExamine: true, //审核字段
        isLogin: false,
        // 轮播图相关
        cardCur: 0,
        swiperList: [],
        //菜单导航相关
        menuList: [{
            icon: 'cardboardfill',
            color: 'red',
            badge: 0,
            name: '表白墙',
            url: "/pages/home/category/category"
        }, {
            icon: 'shopfill',
            color: 'yellow',
            badge: 0,
            name: '跳蚤市场',

        }, {
            icon: 'writefill',
            color: 'cyan',
            badge: 0,
            name: '学习专区',
            url: "/pages/home/category/category"
        }, {
            icon: 'sponsorfill',
            color: 'olive',
            badge: 0,
            name: '失物招领',
            url: "/pages/home/category/category"
        }, {
            icon: 'servicefill',
            color: 'yellow',
            badge: 0,
            name: '在线客服',
            url: "/pages/home/category/category"
        }, {
            icon: 'countdownfill',
            color: 'orange',
            badge: 0,
            name: '校园活动',
            url: "/pages/home/category/category"
        }, {
            icon: 'messagefill',
            color: 'olive',
            badge: 0,
            name: '吐槽专区',
            url: "/pages/home/category/category"
        }, {
            icon: 'questionfill',
            color: 'olive',
            badge: 0,
            name: '问题反馈',
            url: "/pages/home/category/category"
        }],
        gridCol: 4,
        skin: false,

        //导航栏相关
        TabCur: 0,
        scrollLeft: 0,
        tabList: [{
            label: "评论最多",
            sort: "comment_count",
            order: "desc",
        }, {
            label: "点赞最多",
            sort: "star",
            order: "desc",
        }, {
            label: "最新发布",
            sort: "add_time",
            order: "desc",
        }, {
            label: "我的点赞",
        }],
        query: {
            limit: 6,
            page: 1,
            auditStatus: '正常'
        },
        total: 0,
        isBottom: false,
        postList: [],

        // starList: [], //用户点赞的列表
        triggered: false, //下拉状态
    },
    methods: {
        // 路由跳转
        toItem(e) {
            app.globalData.currentPost = e.currentTarget.dataset.post
            wx.navigateTo({
                url: '/pages/home/item/item',
            })
        },

        //获取帖子
        getPostList() {
            http.postRequest('/post/getPostPage', this.data.query, ContentTypeEnum.Json_Sub,
                res => {
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

                }, err => {
                    console.log(err);
                })
        },

        loadPostList() {
            if (this.data.query.limit * this.data.query.page >= this.data.total) {
                if (this.data.isBottom) {
                    wx.showToast({
                        icon: "none",
                        title: '一滴都没有了',
                    })
                }
                this.setData({
                    isBottom: true
                })

            }
            if (!this.data.isBottom) {
                this.data.query.page += 1;
                this.getPostList()
            }

        },

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
                            app.getUserProfile()
                        }
                    }
                })
            } else {
                wx.navigateTo({
                    url: url,
                })
            }
        },

        getUserProfile: function () {
            wx.login({
                success: res => {
                    if (res.code) {
                        wx.setStorageSync('code', res.code);
                    }
                }
            })

            wx.getUserProfile({
                desc: '用于完善资料',
                success: res => {
                    const form = {
                        signature: res.signature,
                        rawData: res.rawData,
                        encryptedData: res.encryptedData,
                        iv: res.iv,
                        jscode: wx.getStorageSync('code'),
                        appid: "wxeb4f620b577ff31a"
                    }

                    http.postRequest("/mp_get_unionId", form, ContentTypeEnum.Json_Sub,
                        res => {
                            console.log(res);
                            wx.setStorageSync('sessionId', res.data.sessionId);
                            wx.setStorageSync('unionId', res.data.unionId);
                            wx.setStorageSync('openid', res.data.openId);
                            http.fill_token_toheader(res.data.unionId);
                            wx.setStorageSync('isLogin', true)
                            http.postRequest("/getMPUserInfo", res.data.unionId, ContentTypeEnum.Default_Sub,
                                res => {
                                    wx.setStorageSync('userInfo', res.data);
                                    this.setData({
                                        userInfo: res.data
                                    })
                                },
                                err => {
                                    wx.showToast({
                                        icon: "none",
                                        title: err.message,
                                    })
                                })

                        },
                        err => {
                            wx.showToast({
                                icon: 'none',
                                title: err.message,
                            })
                        }
                    )
                },
                fail: err => {
                    console.log(err);
                }
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
        towerSwiper() {
            const nowTime = new Date();
            const query = {
                limit: "",
                page: "",
                status: true,
                nowTime: nowTime
            }
            http.postRequest('/carousel/getCarouselPage', query, ContentTypeEnum.Json_Sub,
                res => {
                    const list = res.data.records
                    for (let i = 0; i < list.length; i++) {
                        list[i].zIndex = parseInt(list.length / 2) + 1 - Math.abs(i - parseInt(list.length / 2))
                        list[i].mLeft = i - parseInt(list.length / 2)
                        list[i].index = i
                        list[i].type = 'image'
                    }
                    this.setData({
                        swiperList: list
                    })

                }, err => {
                    console.log(err);
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
        },

        onPulling(e) {},

        onRefresh(e) {
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
            this.getPostList();
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

        updatePostList(item) {
            const postList = this.data.postList;
            const index = postList.findIndex(e => {
                return e.postUuid === item.postUuid
            })
            postList[index] = item
            this.setData({
                starList: app.globalData.starList,
                postList: postList
            });
            console.log('updateStarList success');
        },



        /**
         * 用户点击右上角分享
         */
        onShareAppMessage: function () {

        },

    }
})