// pages/mine/edit/edit.js
const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');

const app = getApp()
Page({


    /**
     * 页面的初始数据
     */
    data: {
        modalName: null,
        telephone: "",
        //表单输入
        userInfo: {},
        form: {
            gender: null,
            telephone: '',
        },


        //验证码
        flag: null, //手机号预校验状态
        codeList: [], //验证码数组
        codeFocus: false,
        curItemIndex: 0, //当前输入第几位Code
        isSend: false,
        countdown: 30,
        toggleDelay: false,

        //头像上传
        imgList: [],
        introduce: "",
        PicturePathSaveForm: {
            "pathSequenceList": [],
            "entityName": "avatar",
            "entityUID": ""
        },
        baseURL: "",
    },


    //选择头像
    ChooseImage() {
        wx.chooseImage({
            count: 1, //默认9
            sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
            sourceType: ['album', 'camera'], //从相册选择
            success: res => {
                const src = res.tempFilePaths[0]
                wx.navigateTo({
                    url: `../cropper/cropper?img=${src}`, //注意是这里跳转裁剪页面
                });
            }
        });

    },

    uploadImage(path) {
        const entityUID = this.data.userInfo.userDetailUuid
        const entityName = 'avatar'
        this.upload(entityUID, entityName, path)
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
        wx.uploadFile({
            url: this.data.baseURL + '/file/uploadPicture',
            filePath: path,
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
                obj.sequence = 1;
                this.data.PicturePathSaveForm.pathSequenceList.push(obj)
                console.log('保存图片相对路径的参数' + this.data.PicturePathSaveForm);

                const form = {
                    userDetailUuid: this.data.userInfo.userDetailUuid,
                    avatar: obj.path
                }
                //保存路径到用户信息
                this.saveEdit(form)
                http.postRequest("/file/savePicturePath", this.data.PicturePathSaveForm, ContentTypeEnum.Default_Sub,
                    res => {
                        wx.hideLoading()
                        wx.showToast({
                            icon: "none",
                            title: res.message,
                        })
                        const unionId = wx.getStorageSync("unionId")
                        this.getMPUserInfo(unionId)

                        console.log("上传完毕！！！");
                    }
                )

            },
            fail(e) {
                console.log("上传失败", e)
            }
        })

    },

    getMPUserInfo(unionId) {
        http.postRequest("/getMPUserInfo", unionId, ContentTypeEnum.Default_Sub,
            res => {
                wx.setStorageSync('userInfo', res.data);
                this.setData({
                    userInfo: res.data
                })
                this.changeLastPageData()
            },
            err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            })
    },

    changeLastPageData() {
        let pages = getCurrentPages()
        let prevPage = pages[0]; //首页
        wx.nextTick(() => {
            let mine = prevPage.selectComponent('#mine');
            console.log(mine);
            mine.changeData()
        })
    },

    //介绍输入
    introduceInput(e) {
        this.setData({
            ['userInfo.introduce']: e.detail.value
        })

    },
    saveIntroduceInput() {
        const form = {
            userDetailUuid: this.data.userInfo.userDetailUuid,
            introduce: this.data.userInfo.introduce
        }

        this.saveEdit(form)
    },
    //昵称输入
    nicknameInput(e) {
        this.setData({
            ['userInfo.nickname']: e.detail.value
        })
    },
    saveNicknameInput() {
        const form = {
            userDetailUuid: this.data.userInfo.userDetailUuid,
            nickname: this.data.userInfo.nickname
        }
        this.saveEdit(form)
    },

    //点击修改性别
    changeGender(e) {
        const gender = e.currentTarget.dataset.gender
        this.setData({
            ['userInfo.gender']: gender
        })
        const form = {
            userDetailUuid: this.data.userInfo.userDetailUuid,
            gender: this.data.userInfo.gender
        }

        this.saveEdit(form)
    },

    navigateTo(e) {
        wx.navigateTo({
            url: e.currentTarget.dataset.path,
        })
    },

    //保存修改的接口
    saveEdit(form) {
        /**
         * 表单预先校验 先不写
         */
        http.postRequest('/sys_user_detail/saveCustomer', form, ContentTypeEnum.Default_Sub,
            res => {
                wx.setStorageSync('userInfo', this.data.userInfo)
                this.setData({
                    userInfo: this.data.userInfo
                })
                this.changeLastPageData()
                this.hideModal()
                wx.showToast({
                    icon: "none",
                    title: res.message,
                })
            }, err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            })
    },

    //保存修改的接口
    saveAuth(form) {
        /**
         * 表单预先校验 先不写
         */
        http.postRequest('/sys_user/saveUser', form, ContentTypeEnum.Default_Sub,
            res => {
                wx.setStorageSync('userInfo', this.data.userInfo)
                this.setData({
                    userInfo: this.data.userInfo
                })
                this.hideModal()
                wx.showToast({
                    icon: "none",
                    title: res.message,
                })
            }, err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            })
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

    telephoneInput(e) {
        console.log(e.detail.value)
        const reg = /^(0|86|17951)?(13[0-9]|15[0-9]|17[3678]|18[0-9]|14[57])\d{8}$/ig;
        let flag = reg.test(e.detail.value);
        console.log(flag);
        this.setData({
            telephone: e.detail.value,
            flag: flag
        })
    },


    itemFocus() {
        console.log(1);
        this.setData({
            codeFocus: true
        })
    },

    codeInputChange(e) {
        console.log(e.detail.value);
        let codeList = this.data.codeList;

        let list = e.detail.value.split('');
        this.setData({
            curItemIndex: list.length //控制当前item显示位置
        });

        if (codeList.length < list.length) { //代表输入
            if (list) {
                for (let i = 0; i < list.length; i++) { //将input内容分割显示
                    this.setData({
                        ['codeList[' + i + ']']: list[i],
                        ['item_' + i]: list[i]
                    })
                }
            }
        } else if (codeList.length > list.length) { //代表删除
            codeList.splice(codeList.length - 1, 1);
            this.setData({
                codeList: codeList
            })
        }
        if (codeList.length >= 4) { //执行发送请求
            this.changeTelephone(codeList.join(""));
        }

    },

    changeTelephone(code) {
        /**
         * 调用校验验证码参数
         */
        http.getRequest(`/checkSMS?telephone=${this.data.telephone}&code=${code}`, null,
            res => {
                console.log(res);
                const form = {
                    userUuid: this.data.userInfo.userUuid,
                    telephone: this.data.telephone,
                }
                this.setData({
                    ['userInfo.telephone']: this.data.telephone
                })
                this.saveAuth(form)
            }, err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            })
    },

    // 发送验证码
    sendSMSCode() {
        /**
         * 发送验证码接口
         */
        http.getRequest(`/sendSMS?telephone=${this.data.telephone}`, null,
            res => {
                console.log(res);
                this.setData({
                    isSend: true,
                    toggleDelay: true
                })
                setTimeout(() => {
                    this.setData({
                        toggleDelay: false
                    })
                }, 1000)
                this.countdownTimer()
            }, err => {
                wx.showToast({
                    icon: "none",
                    title: err.message,
                })
            })

    },

    countdownTimer() {
        const timer = setInterval(() => {
            console.log(this.data.countdown);
            this.setData({
                countdown: this.data.countdown - 1
            })
            if (this.data.countdown === 0) {
                this.setData({
                    isSend: false,
                    countdown: 30
                })
                clearInterval(timer)
            }

        }, 1000)
    },



    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function () {

        this.setData({
            userInfo: wx.getStorageSync('userInfo'),
            baseURL: http.baseURL,
        })


    },


    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    }
})