//app.js
App({
    require: function ($uri) {
        return wx.require($uri)
    },
    onLaunch: function () {
        // console.log('进入小程序')

        // 获取当前时间
        var nowTime = Date.parse(new Date())
        var delineTime = Date.parse('2022-4-6')
        // console.log(nowTime > delineTime)
        if (nowTime > delineTime) {
            // 说明已经过了审核周期，正常显示
            this.globalData.isExamine = false
        }
        console.log(this.globalData.isExamine)

        // 获取系统状态栏信息
        wx.getSystemInfo({
            success: e => {
                this.globalData.StatusBar = e.statusBarHeight;
                let capsule = wx.getMenuButtonBoundingClientRect();
                if (capsule) {
                    this.globalData.Custom = capsule;
                    this.globalData.CustomBar = capsule.bottom + capsule.top - e.statusBarHeight;
                } else {
                    this.globalData.CustomBar = e.statusBarHeight + 50;
                }
            }
        })

        //重写分享方法
        wx.onAppRoute(function (res) {
            //获取加载的页面
            let pages = getCurrentPages(),
                //获取当前页面的对象
                view = pages[pages.length - 1],
                data;
            if (view) {
                data = view.data;
                console.log('是否重写分享方法', data.isOverShare);
                if (!data.isOverShare) {
                    data.isOverShare = true;
                    wx.showShareMenu({
                        withShareTicket: true,
                        menus: ['shareAppMessage', 'shareTimeline'],
                    });
                    view.onShareAppMessage = function () {
                        //分享配置
                        return {
                            title: 'WeHgu', // 子页面的title
                            path: 'pages/index/login/login',
                            imageUrl: '/images/custom-avatar.png'
                        };
                    };
                    view.onShareTimeline = function () {
                        //分享配置
                        return {
                            title: 'WeHgu', // 子页面的title
                            query: "?a=pyq",
                            imageUrl: '/images/custom-avatar.png'
                        };
                    };

                }
            }
        })
    },






    // 全局可用
    reloadThisPage: function () {
        let currentPages = getCurrentPages()
        let lastRoute = currentPages[currentPages.length - 1].route
        let options = currentPages[currentPages.length - 1].options
        let optionsStr = ""
        for (let key in options) {
            optionsStr += '?' + key + '=' + options[key]
        }
        wx.redirectTo({
            url: '/' + lastRoute + optionsStr,
        })
    },
    globalData: {
        isExamine: true, //过审
        currentPost: null, //当前浏览的帖子
        starList: [], //保存当前用户的点赞对象
    }
})