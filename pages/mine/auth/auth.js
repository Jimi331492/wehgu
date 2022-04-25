const http = require('../../../utils/httputils.js');
const ContentTypeEnum = require('../../../utils/ContentTypeEnum.js');
import WxValidate from '../../../utils/WxValidate.js'
const app = getApp()

// pages/mine/auth/auth.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        campusMap: null,
        multiArray: [],
        multiIndex: [0, 0],
        gradeOptions: [],
        sexOptions: ['男', '女'],
        collegeList: [], //学院list
        //表单
        form: {
            studentNo: null, //学号
            name: null, //院校
            sex: null, //院校
            university: null, //院校
            campus: null, //校区
            college: null, //学院
            grade: null, //入学年份
            major: null, //专业
        },
    },

    getMapByUniversityList(universityList) {
        return new Promise(async (resolve) => {
            const u_campusMap = new Map()
            for (let i = 0; i < universityList.length; i++) {
                const item = universityList[i];
                let {
                    records: itemList
                } = await app.getValueListBykey(item.value + '-校区')

                u_campusMap.set(item.value, itemList)
            }

            resolve(u_campusMap)

        })
    },
    // 表单事件
    studentNoInput(e) {
        this.setData({
            ['form.studentNo']: e.detail.value
        })
    },

    nameInput(e) {
        this.setData({
            ['form.name']: e.detail.value
        })
    },

    sexChange(e) {
        console.log(e);
        this.setData({
            ['form.sex']: this.data.sexOptions[e.detail.value]
        })
    },

    async universityChange(e) {
        console.log(e);
        // 先获取要改变的视图数据
        let data = {
            multiArray: this.data.multiArray,
            multiIndex: this.data.multiIndex,
            collegeList: this.data.collegeList,
            form: this.data.form
        };
        //哪一列切换到了第几个
        data.multiIndex[e.detail.column] = e.detail.value;

        //当前所选院校名
        const universityList = data.multiArray[0]
        const chooesedUniversityName = universityList[data.multiIndex[0]].value
        //如果是院校变化 
        switch (e.detail.column) {
            case 0:
                //1.从map中获取校区列表
                data.multiArray[1] = this.data.u_campusMap.get(chooesedUniversityName)

                // 2.获取学院数组
                const {
                    records: collegeList
                } = await app.getValueListBykey(chooesedUniversityName)
                data.collegeList = collegeList
        }
        //当前所选院校下的校区列表
        const campusList = data.multiArray[1]
        data.form.campus = campusList[data.multiIndex[1]].value
        data.form.university = chooesedUniversityName
        console.log(data);

        this.setData(data);

    },

    campusChange(e) {
        console.log(e);
        this.setData({
            multiIndex: e.detail.value
        })
    },

    gradeChange(e) {
        this.setData({
            ['form.grade']: this.data.gradeOptions[e.detail.value]
        })
    },

    collegeChange(e) {
        console.log(e);
        this.setData({
            ['form.college']: this.data.collegeList[e.detail.value].value
        })
    },

    majorInput(e) {
        this.setData({
            ['form.major']: e.detail.value
        })
    },


    //预校验函数
    initValidate: function () {
        const rules = {
            studentNo: {
                required: true,
                digits: true
            },
            name: {
                required: true,
            },
            sex: {
                required: true,
            },
            grade: {
                required: true,
            },
            college: {
                required: true,
            },
            major: {
                required: true,
            }
        }
        const messages = {
            studentNo: {
                required: '学号为空',
                digits: "学号类型错误"
            },
            name: {
                required: '姓名为空',
            },
            sex: {
                required: '性别未选',
            },
            grade: {
                required: '入学年份为空',
            },
            college: {
                required: '学院为空',
            },
            major: {
                required: '专业为空',
            }

        }
        this.WxValidate = new WxValidate(rules, messages)
    },

    commitAuthForm() {
        console.log(this.data.form);
        //所选信息都是必填
        if (!this.WxValidate.checkForm(this.data.form)) {
            const error = this.WxValidate.errorList[0]
            wx.showToast({
                title: error.msg,
                icon: 'error',
                duration: 2000
            })
            return false
        }

        http.postRequest("/sys_user/userAuth", this.data.form, ContentTypeEnum.Default_Sub,
            res => {
                this.updateUserInfo(wx.getStorageSync('unionId'))
                wx.showModal({
                    title: '提示',
                    content: '保存成功',
                    showCancel: false,
                    success: res => {
                        if (res.confirm) {
                            wx.navigateBack({
                                delta: 1,
                            })
                        }
                    }
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

    updateUserInfo(unionId) {
        const pages = getCurrentPages();
        var prevPage = pages[pages.length - 2]; //上一个页面
        prevPage.getMPUserInfo(unionId)
    },


    /**
     * 生命周期函数--监听页面加载
     */
    async onLoad() {

        //获取表单选项数据
        //1.获取大学院校与校区的Map
        const {
            records: universityList
        } = await app.getValueListBykey('大学院校')
        if (universityList.length === null && universityList.length < 0) {
            universityList.push('河北地质大学')
        }
        const u_campusMap = await this.getMapByUniversityList(universityList)
        const multiArray = [universityList, u_campusMap.get(universityList[0].value)]

        //2.获取入学年份选项数据
        const now = new Date()
        const year = now.getFullYear()
        const gradeOptions = [year, year - 1, year - 2, year - 3, year - 4, year - 5]

        //4.初始化表单
        this.setData({ //公共的
            gradeOptions: gradeOptions,
            u_campusMap: u_campusMap,
        })
        //如果没填写过的
        const userInfo = wx.getStorageSync('userInfo')
        if (!userInfo.authentication) {
            const university = universityList[0].value //默认院校
            const campusList = multiArray[1] //当前默认院校下的校区列表
            const campus = campusList[0].value //默认院校默认校区 第一个
            // 2.获取默认院校的学院数组
            const {
                records: collegeList
            } = await app.getValueListBykey(university)
            console.log('u_campusMap', u_campusMap);
            console.log('multiArray', multiArray);
            this.setData({
                multiArray: multiArray,
                collegeList: collegeList,
                ['form.campus']: campus,
                ['form.university']: university,
            })
        } else {
            wx.showLoading({
                title: '加载中',
            })

            //寻找Index
            const multiIndex = [0, 0]
            multiIndex[0] = universityList.findIndex(item => {
                return item.value === userInfo.university
            })

            multiIndex[1] = u_campusMap.get(userInfo.university).findIndex(item => {
                return item.value === userInfo.campus
            })
            multiArray[1] = u_campusMap.get(userInfo.university)

            // 2.获取之前用户所选择的院校学院数组
            const {
                records: collegeList
            } = await app.getValueListBykey(userInfo.university)

            const form = this.data.form
            Object.keys(form).forEach(key => {
                form[key] = userInfo[key]
            })
            this.setData({
                form: form,
                collegeList: collegeList,
                multiArray: multiArray,
                multiIndex: multiIndex
            })
            wx.hideLoading()
        }

        this.initValidate(); //初始化预校验规则
    },

})