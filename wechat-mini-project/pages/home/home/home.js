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
        async attached() {
            this.towerSwiper();
            // 初始化towerSwiper 传已有的数组名即可
            this.setData({
                isLogin: wx.getStorageSync('isLogin'),
                isExamine: app.globalData.isExamine,
            });

            const res = await this.getPostList(this.data.query)
            this.data.total = res.data.total
            console.log(res);
            this.setData({
                postList: res.data.records,
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
            url: `/pages/home/category/category?tag=${'表白'}&name=${'表白墙'}`
        }, {
            icon: 'shopfill',
            color: 'yellow',
            badge: 0,
            name: '跳蚤市场',
            url: `/pages/home/category/category?tag=${'二手交易'}&name=${'跳蚤市场'}`
        }, {
            icon: 'writefill',
            color: 'cyan',
            badge: 0,
            name: '学习专区',
            url: `/pages/home/category/category?tag=${'学习'}&name=${'学习专区'}`
        }, {
            icon: 'sponsorfill',
            color: 'olive',
            badge: 0,
            name: '失物招领',
            url: `/pages/home/category/category?tag=${'失物招领'}&name=${'失物招领'}`
        }, {
            icon: 'servicefill',
            color: 'yellow',
            badge: 0,
            name: '在线客服',
            url: `/pages/home/home/home`
        }, {
            icon: 'countdownfill',
            color: 'orange',
            badge: 0,
            name: '校园活动',
            url: `/pages/home/category/category?tag=${'活动'}&name=${'校园活动'}`,
        }, {
            icon: 'messagefill',
            color: 'olive',
            badge: 0,
            name: '吐槽专区',
            url: `/pages/home/category/category?tag=${'吐槽'}&name=${'吐槽专区'}`,
        }, {
            icon: 'questionfill',
            color: 'olive',
            badge: 0,
            name: '问题反馈',
            url: '/pages/home/report/report'
        }],
        gridCol: 4,
        skin: false,

        //导航栏相关
        TabCur: 0,
        scrollLeft: 0,
        tabList: [{
            label: "点赞最多",
        }, {
            label: "评论最多",
            sort: "comment_num",
            order: "desc",
        }, {
            label: "最新发布",
            sort: "add_time",
            order: "desc",
        }, {
            label: "最近点赞",
        }],
        query: {
            limit: 6,
            page: 1,
            auditStatus: '正常',
            sort: "comment_num",
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

        navigateTo(e) {
            wx.navigateTo({
                url: e.currentTarget.dataset.path,
            })
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



        getUserProfile() {
            app.getUserProfile()
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
        async tabSelect(e) {
            this.setData({
                TabCur: e.currentTarget.dataset.id,
                scrollLeft: (e.currentTarget.dataset.id - 1) * 60,
                isBottom: false,
            })
            const option = e.currentTarget.dataset.item;
            if (option.label === "最近点赞") {
                const starList = this.data.starList;
                const postStarList = starList.filter(e => {
                    return e.type === 0
                })

                let likedPostList = [];
                //帖子点赞类型为0直接返回空
                if (postStarList.length !== 0) {
                    postStarList.reverse()
                    if (postStarList.length > 6) {
                        postStarList = postStarList.slice(0, 6)
                    }
                    const res = await this.getLikedPost(postStarList);
                    likedPostList = res.data
                }
                this.setData({
                    postList: likedPostList,
                    isBottom: true
                })

            } else {
                const defQuery = {
                    limit: 6,
                    page: 1,
                    auditStatus: '正常',
                    sort: option.sort,
                    order: option.order
                };

                const res = await this.getPostList(defQuery);
                this.data.total = res.data.total
                this.setData({
                    postList: res.data.records,
                    query: defQuery,

                })

            }

        },

        getLikedPost(starList) {
            return new Promise((resolve, reject) => {
                http.postRequest('/post/getLikedPostPage', starList, ContentTypeEnum.Default_Sub,
                    res => {
                        resolve(res);
                    }, err => {
                        reject(err)
                    })
            })
        },

        onPulling(e) {},

        async onRefresh(e) {
            if (this._freshing === true) return
            this._freshing = true

            if (this.data.TabCur !== 3) {
                const query = {
                    limit: 6,
                    page: 1,
                    auditStatus: '正常',
                }

                this.data.query = {
                    ...this.data.tabList[this.data.TabCur],
                    ...query
                }
                console.log(this.data.query);
                //重置
                this.data.postList = [];
                this.data.total = 0;
                const res = await this.getPostList(this.data.query);
                this.setData({
                    postList: res.data.records,
                    isBottom: false,
                })
            } else {
                const starList = this.data.starList;
                const postStarList = starList.filter(e => {
                    return e.type === 0
                })

                let likedPostList = [];
                //帖子点赞类型为0直接返回空
                if (postStarList.length !== 0) {
                    postStarList.reverse()
                    if (postStarList.length > 6) {
                        postStarList = postStarList.slice(0, 6)
                    }
                    const res = await this.getLikedPost(postStarList);
                    likedPostList = res.data
                }
                this.setData({
                    postList: likedPostList,
                    isBottom: true
                })

            }


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
        //客服
        handleContact(e) {
            console.log(e);
        },

        /**
         * 用户点击右上角分享
         */
        onShareAppMessage: function () {

        },

    }
})