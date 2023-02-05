// components/modal.js
Component({
    /**
     * 组件的属性列表
     */
    properties: {
        name: {
            type: String,
            value: 'modalName'
        },
        modalName: {
            type: String,
            value: ''
        },
        bodyHeight:{
            type: Number,
            value: 400
        },
        isHead:{
            type:Boolean,
            value:false
        },
        isBar:{
            type:Boolean,
            value:false
        }

    },

    /**
     * 组件的初始数据
     */
    data: {

    },

    /**
     * 组件的方法列表
     */
    methods: {
        hiddlen: function () {
            console.log(1);
            this.triggerEvent("hidden")
        },

        sure: function () {
            console.log("sure");
        },
    }
})