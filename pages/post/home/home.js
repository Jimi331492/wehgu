// pages/post/home/home.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        //上传图片
        baseURL: "",
        imgList: [],
        PicturePathSaveForm: {
            "pathSequenceList": [],
            "entityName": "avatar",
            "entityUID": ""
        },

        //提交的表单
        form: {
            content: "",
            tag: null,
            pictureNum: 0,
        },
        newTag: null,

        //视图
        tagList: [],
        bakTagList: [],
        modalName: "",

        //获取视图
        queryTagParams: {
            limit: "",
            page: ""
        },




    },


    showModal(e) {
        console.log(e);
        this.setData({
            modalName: e.currentTarget.dataset.target
        })
    },
    hideModal(e) {
        this.setData({
            modalName: null
        })
    },

    newTagInput(e) {
        console.log(e);
        const tag = e.detail.value;
        this.setData({
            ['form.tag']: null,
            newTag: tag
        })
    },

    contentInput(e) {
        this.setData({
            ['form.content']: e.detail.value,
        })
    },

    resetTag() {
        this.setData({
            ['form.tag']: null,
            newTag: null
        })
    },

    getTagList() {
        http.postRequest("/tag/getTagPage", this.data.queryTagParams, ContentTypeEnum.Default_Sub,
            res => {
                this.setData({
                    tagList: res.data.records,
                    bakTagList: res.data.records
                })
            },
            err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            })
    },

    ChooseTag(e) {
        const tag = e.currentTarget.dataset.tag;
        this.setData({
            ['form.tag']: tag,
            newTag: null
        })
        this.hideModal()

    },

    ChooseImage() {
        wx.chooseImage({
            count: 9, //默认9
            sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
            sourceType: ['album'], //从相册选择
            success: res => {
                if (this.data.imgList.length != 0) {
                    this.setData({
                        imgList: this.data.imgList.concat(res.tempFilePaths),
                    })
                } else {
                    this.setData({
                        imgList: res.tempFilePaths,
                    })
                }
                this.setData({
                    ['form.pictureNum']: this.data.imgList.length
                })
            }
        });
    },
    ViewImage(e) {
        wx.previewImage({
            urls: this.data.imgList,
            current: e.currentTarget.dataset.url
        });
    },
    DelImg(e) {
        wx.showModal({
            title: '提示',
            content: '确定删除吗？',
            cancelText: '取消',
            confirmText: '确定',
            success: res => {
                if (res.confirm) {
                    this.data.imgList.splice(e.currentTarget.dataset.index, 1);
                    this.setData({
                        imgList: this.data.imgList
                    })
                }
            }
        })
    },

    savePost() {
        if (this.data.form.tag == null && this.data.newTag !== null) {
            this.data.form.tag = this.data.newTag
        }
        http.postRequest("/post/savePost", this.data.form, ContentTypeEnum.Default_Sub,
            res => {
                if (this.data.imgList.length > 0) {
                    this.upload(res.data, 'post')
                } else {
                    wx.showModal({
                        title: '提示',
                        content: '发布成功',
                        showCancel: false,
                        success: res => {
                            if (res.confirm) {
                                wx.navigateBack({
                                    delta: 1,
                                })
                            }
                        }
                    })
                }
                console.log("上传完毕！！！");
            },
            err => {
                wx.showToast({
                    icon: 'none',
                    title: err.message,
                })
            }
        )
    },

    /**
     * 上传图片
     * @param {*} e 
     */
    upload: function (entityUID, entityName) {
        this.data.PicturePathSaveForm.entityUID = entityUID;
        this.data.PicturePathSaveForm.entityName = entityName;
        wx.showLoading({
            title: '上传中...',
            mask: true,
        })
        for (let i = 0; i < this.data.imgList.length; i++) {
            wx.uploadFile({
                url: this.data.baseURL + '/file/uploadPicture',
                filePath: this.data.imgList[i],
                fileType: 'image',
                name: 'picture',
                formData: {
                    'pictype': this.data.PicturePathSaveForm.entityName
                },
                success: res => {
                    console.log('上传头像成功获得相对路径' + res);
                    let obj = new Object();
                    let map = JSON.parse(res.data)
                    obj.path = map.data
                    obj.sequence = i + 1;
                    this.data.PicturePathSaveForm.pathSequenceList.push(obj)
                    console.log('保存图片相对路径的参数' + this.data.PicturePathSaveForm);
                    if (i == this.data.imgList.length - 1) {
                        http.postRequest("/file/savePicturePath", this.data.PicturePathSaveForm, ContentTypeEnum.Default_Sub,
                            res => {
                                wx.hideLoading()
                                wx.showModal({
                                    title: '提示',
                                    content: '发布成功',
                                    showCancel: false,
                                    success: res => {
                                        if (res.confirm) {
                                            wx.navigateBack({
                                                delta: 1,
                                            })
                                        }
                                    }
                                })
                                console.log("上传完毕！！！");
                            }, err => {
                                wx.showToast({
                                    icon: "none",
                                    title: err.message,
                                })
                            }
                        )
                    }
                },
                fail(e) {
                    console.log("上传失败", e)
                }
            })
        }
    },




    /**
     * 生命周期函数--监听页面加载
     */
    onLoad() {
        this.getTagList();
        this.setData({
            baseURL: http.baseURL
        })

    },



    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})