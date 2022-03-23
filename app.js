//app.js
App({
  require: function ($uri) {
    return require($uri)
  },
  onLaunch: function () {
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
          view.onShareAppMessage = function () {
            //分享配置
            return {
              title: '标题', // 子页面的title
              path: 'pages/index/index',
              imageUrl: '/static/logo.jpg'
            };
          }
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
  globalData: {}
})