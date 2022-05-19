// pages/find/find.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');

const app = getApp()
Component({
    options: {
        addGlobalClass: true,
        multipleSlots: true
    },
    properties: {
        starList: {
            type: Array,
            default: []
        },
    },
    lifetimes: {
        async attached() {
            const query = {
                limit: 8,
                page: 1,
            }
            const defaultTabOptions = [{
                title: "点赞最多",
            }, {
                title: "评论最多",
                sort: "comment_num",
                order: "desc",
            }, {
                title: "最新发布",
                sort: "add_time",
                order: "desc",
            }, {
                title: "最近点赞",
            }]
            const _res = await this.getTagList(query);
            const tabList = [...defaultTabOptions, ..._res.data.records];
            const res = await this.getPostList(this.data.query)
            this.data.total = res.data.total

            this.setData({
                isLogin: wx.getStorageSync('isLogin'),
                postList: res.data.records,
                tabList: tabList,
                tagList: _res.data.records,
            });
        },
        ready() {
            console.log('#find' + JSON.stringify(this.data.starList));
        }
    },
    data: {
        bakPath: '/images/custom-avatar.png',
        isLogin: false,
        //获取视图
        queryTagParams: {
            limit: 8,
            page: 1
        },

        //视图
        tagList: [],
        bakTagList: [],

        //导航栏相关
        TabCur: 0,
        scrollLeft: 0,
        tabList: [],

        query: {
            limit: 6,
            page: 1,
            auditStatus: '正常'
        },
        total: 0,
        isBottom: false,
        postList: [],
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
        getUserProfile() {
            app.getUserProfile()
        },
        getTagList(query) {
            return new Promise((resolve, reject) => {
                http.postRequest("/tag/getTagPage", query, ContentTypeEnum.Default_Sub,
                    res => {
                        resolve(res)

                    },
                    err => {

                        reject(err)
                    })
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


    }
})