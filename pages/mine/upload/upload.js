// pages/mine/upload/upload.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');

const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        frontImage: [],
        backImage: [],

        //图片上传
        PicturePathSaveForm: {
            "pathSequenceList": [],
            "entityName": "auth",
            "entityUID": ""
        },
        baseURL: "",
        showUploadBtn: true
    },

    ChooseFrontImage() {
        wx.chooseImage({
            count: 1, //默认9
            sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
            sourceType: ['album'], //从相册选择
            success: (res) => {
                if (this.data.frontImage.length != 0) {
                    this.setData({
                        frontImage: this.data.frontImage.concat(res.tempFilePaths)
                    })
                } else {
                    this.setData({
                        frontImage: res.tempFilePaths
                    })
                }
            }
        });
    },

    ChooseBackImage() {
        wx.chooseImage({
            count: 1, //默认9
            sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
            sourceType: ['album'], //从相册选择
            success: (res) => {
                if (this.data.backImage.length != 0) {
                    this.setData({
                        backImage: this.data.backImage.concat(res.tempFilePaths)
                    })
                } else {
                    this.setData({
                        backImage: res.tempFilePaths
                    })
                }
            }
        });
    },
    ViewImage(e) {
        const imgList = [...this.data.frontImage, ...this.data.backImage]
        wx.previewImage({
            urls: imgList,
            current: e.currentTarget.dataset.url
        });
    },
    DelBackImage(e) {
        wx.showModal({
            title: '提示',
            content: '是否删除？',
            cancelText: '再看看',
            confirmText: '确定',
            success: res => {
                if (res.confirm) {
                    this.data.backImage.splice(e.currentTarget.dataset.index, 1);
                    this.setData({
                        backImage: this.data.backImage,
                    })

                    this.ifShowUplodaBtn()
                }
            }
        })
    },
    DelFrontImage(e) {
        wx.showModal({
            title: '提示',
            content: '是否删除？',
            cancelText: '再看看',
            confirmText: '确定',
            success: res => {
                if (res.confirm) {
                    this.data.frontImage.splice(e.currentTarget.dataset.index, 1);
                    this.setData({
                        frontImage: this.data.frontImage
                    })
                    this.ifShowUplodaBtn()
                }
            }
        })
    },
    //判断是否显示上传的Btn
    ifShowUplodaBtn() {
        const imgList = [...this.data.frontImage, ...this.data.backImage]
        if (imgList.length !== 0) return false
        //清空正反面 允许上传
        this.setData({
            showUploadBtn: true,
        })
    },

    uploadImage() {
        const entityUID = wx.getStorageSync('userInfo').userDetailUuid
        const entityName = 'auth'
        const imgList = [...this.data.frontImage, ...this.data.backImage]

        if (imgList.length < 2) {
            wx.showToast({
                icon: "none",
                title: '请选择图片进行上传',
            })
            return false
        }
        console.log(imgList);
        this.upload(entityUID, entityName, imgList)
    },
    /**
     * 上传图片
     * @param {*} e 
     */
    upload: function (entityUID, entityName, path) {
        this.data.PicturePathSaveForm.entityUID = entityUID;
        this.data.PicturePathSaveForm.entityName = entityName;
        wx.showLoading({
            title: '上传中...',
            mask: true,
        })
        for (let index = 0; index < path.length; index++) {

            wx.uploadFile({
                url: this.data.baseURL + '/file/uploadPicture',
                filePath: path[index],
                fileType: 'image',
                name: 'picture',
                formData: {
                    'pictype': this.data.PicturePathSaveForm.entityName
                },
                success: res => {
                    console.log('上传学生证成功获得相对路径' + res);
                    let obj = new Object();
                    let map = JSON.parse(res.data)
                    obj.path = map.data
                    obj.sequence = index;
                    this.data.PicturePathSaveForm.pathSequenceList.push(obj)
                    console.log('保存图片相对路径的参数' + this.data.PicturePathSaveForm);

                    //最后一个的时候保存路径
                    if (index === path.length - 1) {
                        http.postRequest("/file/savePicturePath", this.data.PicturePathSaveForm, ContentTypeEnum.Default_Sub,
                            res => {
                                wx.hideLoading()
                                wx.showModal({
                                    title: '上传成功',
                                    content: "等待审核",
                                    confirmText: "确定",
                                    showCancel: false,
                                    success: res => {
                                        if (res.confirm) {
                                            wx.navigateBack({
                                                delta: 2,
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

    async getUserStundentCard() {
        const entityUID = wx.getStorageSync('userInfo').userDetailUuid
        const query = {
            storeTypeTable: "auth",
            relationUuid: entityUID,
            limit: 2,
            page: 1,
        }
        const res = await app.getPicturePath(query)
        if (res.data.records.length < 2) return false
        this.setData({
            frontImage: [res.data.records[0].uri],
            backImage: [res.data.records[1].uri],
            showUploadBtn: false
        })

    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad() {


        this.getUserStundentCard()

        this.setData({
            baseURL: http.baseURL,
        })
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