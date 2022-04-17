// pages/home/item/item.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');
const util = require('../../../utils/util.js');
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        starList: [],
        item: null, //帖子
        imgURIList: [], //多张图片的地址
        avatar: null, //发帖子的人头像

        query: {
            limit: "",
            page: ""
        },

        // 表单
        imgList: [], //上传图片的人
        form: {
            content: null, //评论内容
            path: null, //图片路径，ps.如果他评论图片的话
            postUuid: null, //根评论
            fromUserDetailUuid: null, //主动方
            toUserDetailUuid: null, //被动方
            parentId: null, //父级评论id
        },

    },

    contentInput(e) {
        this.setData({
            ['form.content']: e.detail.value
        })
    },

    getURIByPath(path) {
        return new Promise((resolve, reject) => {
            http.postRequest('/file/getURIByPath', path, ContentTypeEnum.Default_Sub,
                res => {
                    resolve(res.data)
                }, err => {
                    wx.showToast({
                        icon: "none",
                        title: err.message,
                    })
                    reject(err.message)
                })
        })

    },

    //点赞
    likedIt(e) {
        const form = {}
        const type = e.currentTarget.dataset.type
        if (type === "post") {
            form.type = 0
        } else if (type === "comment") {
            form.type = 1
        }
        form.status = 1
        form.linkedUuid = e.currentTarget.dataset.id
        //点赞完成更新本地缓存数据和视图
        this.updateLocalCache(form)

    },

    //取消点赞
    cancelLikedIt(e) {
        const form = {}
        const type = e.currentTarget.dataset.type
        if (type === "post") {
            form.type = 0
        } else if (type === "comment") {
            form.type = 1
        }
        form.status = 0
        form.linkedUuid = e.currentTarget.dataset.id
        this.updateLocalCache(form)

    },

    saveStar: util.debounce(function (arg) {
        const form = arg[0]
        console.log(form);
        http.postRequest('/star/saveStarToRedis', form, ContentTypeEnum.Default_Sub,
            res => {
                console.log(res.message);

            }, err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            })
    }),

    //点赞完成更新本地缓存数据和视图
    updateLocalCache(form) {
        //拿到本地点赞列表
        const starList = app.globalData.starList

        //判断点赞类型是给帖子点赞还是给评论点赞
        if (form.type === 0) {
            const item = this.data.item
            //判断新增是点赞还是取消点赞
            console.log("before", starList);
            if (form.status === 1) {
                const star = {
                    type: form.type,
                    linkedUuid: form.linkedUuid,
                    status: form.status
                }
                starList.push(star)
                item.star++;
            } else {
                const index = starList.findIndex(e => {
                    return e.linkedUuid === form.linkedUuid
                })
                console.log(index);
                starList.splice(index, 1)
                item.star--;
            }
            app.globalData.starList = starList
            //更新详情页和home页
            this.updateStarList(starList, item)
            console.log("after", starList);

        }else{//给评论点赞

        }




        this.saveStar(form)
    },
    updateStarList(starList, item) {
        this.setData({
            starList: starList,
            item: item
        })
    },

    //评论
    async saveComment(e) {
        //如果是回复评论
        const id = e.currentTarget.dataset.id;
        if (id !== null) {
            this.data.form.parentId = id
        }
        //如果有照片
        if (this.data.imgList.length > 0) {
            const path = await this.upload('comment', this.data.imgList[0])
            console.log('先上传图片获得相对path', path);
            this.setData({
                imgList: []
            })
        }
        /*
         *  调用接口
         */
        console.log('再保存评论');
        http.postRequest('/comment/saveComment', this.data.form, ContentTypeEnum.Default_Sub,
            res => {
                wx.showToast({
                    title: '评论成功',
                })
                //重置表单
                const fromUserDetailUuid = wx.getStorageSync('userInfo').userDetailUuid
                const form = {
                    content: null, //评论内容d
                    path: null, //图片路径，ps.如果他评论图片的话
                    postUuid: this.data.item.postUuid, //根评论
                    fromUserDetailUuid: fromUserDetailUuid, //主动方
                    toUserDetailUuid: null, //被动方
                    parentId: null, //父级评论id
                }
                this.setData({
                    form: form
                })

                const postUuid = this.data.item.postUuid
                const query = {
                    limit: "",
                    page: "",
                    postUuid: postUuid
                }
                this.getCommentTree(query)
            }, err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            })
    },

    //选择图片
    ChooseImage() {
        wx.chooseImage({
            count: 1, //默认9
            sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
            sourceType: ['album'], //从相册选择
            success: res => {
                this.setData({
                    imgList: res.tempFilePaths,
                })
            }
        });
    },

    /**
     * 上传图片
     * @param {*} e 
     */
    upload: function (entityName, path) {
        return new Promise((resolve, reject) => {
            wx.showLoading({
                title: '上传中...',
                mask: true,
            })
            wx.uploadFile({
                url: this.data.baseURL + '/file/uploadPicture',
                filePath: path,
                fileType: 'image',
                name: 'picture',
                formData: {
                    'pictype': entityName
                },
                success: res => {
                    console.log('上传头像成功获得相对路径' + res);
                    let map = JSON.parse(res.data)
                    this.data.form.path = map.data
                    resolve(map.data)
                },
                fail: err => {
                    console.log("上传失败", err)
                    reject(err)
                }
            })
        })
    },

    getCommentTree(query) {
        /*
         *  调用接口
         */
        http.postRequest('/comment/getCommentPage', query, ContentTypeEnum.Default_Sub,
            res => {
                this.setData({
                    ['item.commentList']: res.data.records
                })
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
        //初始化界面
        const item = app.globalData.currentPost;
        console.log(item.commentList);
        const avatar = wx.getStorageSync('userInfo').avatar
        this.updateStarList(app.globalData.starList)
        this.setData({
            item: item,
            avatar: avatar,
        })
        if (item.pictureNum > 1 && item.imgPathList.length > 1) {
            wx.showLoading({
                title: '加载图片中...',
            })
            item.imgPathList.forEach(async (path, i) => {
                // 跳过第一张
                if (i !== 0) {
                    const URI = await this.getURIByPath(path)
                    console.log('url', URI);
                    this.data.imgURIList.push(URI)

                    if (i === item.imgPathList.length - 1) {
                        wx.hideLoading()
                        console.log(this.data.imgURIList);
                        this.setData({
                            imgURIList: this.data.imgURIList
                        })
                    }
                }
            })
        }
        //初始化评论
        // this.getCommentTree()
        //初始化表单
        const fromUserDetailUuid = wx.getStorageSync('userInfo').userDetailUuid
        const form = {
            content: null, //评论内容
            path: null, //图片路径，ps.如果他评论图片的话
            postUuid: item.postUuid, //根评论
            fromUserDetailUuid: fromUserDetailUuid, //主动方
            toUserDetailUuid: null, //被动方
            parentId: null, //父级评论id
        }
        this.data.form = form
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
        console.log(11);
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