<!--
 * @Author: 龙际妙
 * @Date: 2021-10-26 11:25:27
 * @Description: 
 * @FilePath: \wehgu-vue-admin\src\views\welcome\UserInfo.vue
 * @LastEditTime: 2022-03-11 22:44:56
 * @LastEditors: Please set LastEditors
-->
<template>
  <div class="container">
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>个人信息</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片内容区 -->
    <el-card>
      <el-collapse v-model="deaultArray" @change="handleChange">
        <el-collapse-item title="基础信息" name="baseInfo">
          <div class="baseInfo">
            <el-descriptions class="baseInfo_table" :column="2" border>
              <template #extra>
                <el-button type="primary" size="small" @click="showInfoDialog">修改</el-button>
              </template>
              <el-descriptions-item width="120px">
                <template #label>
                  <i class="el-icon-user"></i>
                  用户名
                </template>
                {{ userInfo.username }}
              </el-descriptions-item>
              <el-descriptions-item width="120px">
                <template #label>
                  <i class="el-icon-tickets"></i>
                  性别
                </template>
                <el-tag v-if="userInfo.ssex == 0" size="small">男 </el-tag>
                <el-tag v-else-if="userInfo.ssex == 1" size="small" type="success">女</el-tag>
                <el-tag v-else-if="userInfo.ssex == 2" size="small" type="danger">保密</el-tag>
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-mobile-phone"></i>
                  手机号
                </template>
                {{ userInfo.mobile }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-location-outline"></i>
                  邮箱
                </template>
                {{ userInfo.email }}
              </el-descriptions-item>

              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-office-building"></i>
                  描述
                </template>
                {{ userInfo.description }}
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-collapse-item>
        <el-collapse-item title="账号信息" name="accountInfo">
          <!-- 账号信息展示表单 -->
          <el-form>
            <el-form-item label="账号状态:" prop="status">
              <div v-if="userInfo.status === 0"><el-tag type="danger">锁定</el-tag></div>
              <div v-else><el-tag type="success">有效</el-tag></div>
            </el-form-item>
            <el-form-item label="注册日期:" prop="createTime">
              {{ this.userInfo.createTime === null ? '出现bug了' : this.format(this.userInfo.createTime) }}
            </el-form-item>
            <el-form-item label="最近修改日期:" prop="modifyTime">
              {{ this.userInfo.modifyTime === null ? '无' : this.format(this.userInfo.modifyTime) }}
            </el-form-item>
            <el-form-item label="最近登录日期:" prop="lastLoginTime">
              {{ this.userInfo.lastLoginTime === null ? '出现bug了' : this.format(this.userInfo.lastLoginTime) }}
            </el-form-item>
          </el-form>
        </el-collapse-item>
        <el-collapse-item title="角色信息" name="rolesInfo">
          <!-- 角色信息展示table -->
          <div v-for="(role, index) in rolesInfo" :key="index">
            <el-tag>{{ role.roleName }}</el-tag>
            <el-tag>{{ role.remark }}</el-tag>
          </div>
        </el-collapse-item>
        <el-collapse-item title="权限信息" name="authoInfo">
          <el-row :class="['bdbottom', index1 === 0 ? 'bdtop' : '', 'rightTags']" v-for="(item1, index1) in menus" :key="item1.menuId">
            <!-- 渲染一级权限 -->
            <el-col :span="5">
              <el-tag>{{ item1.menuName }}</el-tag>
              <i class="el-icon-caret-right"></i>
            </el-col>
            <!-- 渲染二级和三级权限 -->
            <el-col :span="19">
              <!-- 通过for循环 嵌套渲染二级权限 -->
              <el-row :class="[index2 === 0 ? '' : 'bdtop', 'rightTags']" v-for="(item2, index2) in item1.menus" :key="item2.menuId">
                <el-col :span="4">
                  <el-tag type="success">{{ item2.menuName }}</el-tag>
                  <i class="el-icon-caret-right"></i>
                </el-col>
                <el-col :span="15">
                  <el-tag v-for="item3 in item2.menus" :key="item3.id" type="warning">
                    {{ item3.menuName }}
                  </el-tag>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </el-collapse-item>
      </el-collapse>
    </el-card>

    <!-- 修改用户基本信息弹窗 -->
    <el-dialog :title="titles" v-model="infoDialogVisible" width="50%" @close="dialogClosed">
      <div class="baseInfo_form">
        <!-- 修改用户表单 -->
        <el-form ref="baseInfoRef" :model="userInfo" label-width="120px" :rules="userInfoRules">
          <!-- 用户名 -->
          <el-form-item label="名称:" prop="username">
            <el-input v-model="userInfo.username" @change="usernameChange" />
          </el-form-item>

          <!-- 性别 -->
          <el-form-item label="性别:" prop="ssex">
            <el-radio-group v-model="userInfo.ssex" @change="ssexChange">
              <el-radio label="0">男</el-radio>
              <el-radio label="1">女</el-radio>
              <el-radio label="2">保密</el-radio>
            </el-radio-group>
          </el-form-item>

          <!-- 用户邮箱 -->
          <el-form-item label="邮箱:" prop="email">
            <el-input v-model="userInfo.email" @change="emailChange" />
          </el-form-item>

          <!-- 用户手机号 -->
          <el-form-item label="手机号:" prop="mobile">
            <el-input v-model="userInfo.mobile" @change="mobileChange" />
          </el-form-item>

          <!-- 描述 -->
          <el-form-item label="描述:" prop="description">
            <el-input
              class="inputDesc"
              v-model="userInfo.description"
              :autosize="{ minRows: 2, maxRows: 4 }"
              type="textarea"
              placeholder="请输入"
              @change="descriptionChange"
            />
          </el-form-item>

          <el-form-item
            ><!-- 底部按钮区域 -->
            <span class="form-footer">
              <el-button @click="this.infoDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="saveUserInfo">提交</el-button>
            </span></el-form-item
          >
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// import { updateUserInfo, getUserPermsInfo } from '../../api/system/user'

// export default {
//   data() {
//     // 邮箱验证的规则
//     var checkEmail = (rule, value, cb) => {
//       // 验证邮箱的正则表达式
//       const regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/

//       if (regEmail.test(value)) {
//         // 合法的邮箱
//         return cb()
//       }

//       cb(new Error('请输入合法的邮箱'))
//     }
//     var checkMobile = (rule, value, cb) => {
//       const regMobile = /^(0|86|17951)?(13[0-9]|15[0-9]|17[3678]|18[0-9]|14[57])\d{8}$/

//       if (regMobile.test(value)) {
//         return cb()
//       }

//       cb(new Error('请输入正确的手机号'))
//     }
//     return {
//       deaultArray: ['baseInfo', 'accountInfo'],
//       userId: 0, //id
//       userInfo: {},
//       rolesInfo: [],
//       menus: [],
//       sumbitForm: {
//         userId: 0, //用户Id
//         username: '', //用户名
//         ssex: '', //性别
//         email: '', //邮箱
//         mobile: '', //手机号
//         description: '', //描述
//       },

//       infoDialogVisible: false,
//       otherDialogVisible: false,
//       titles: '', //弹窗标题
//       userInfoRules: {
//         username: [
//           { required: true, message: '请输入用户名', trigger: 'blur' },
//           {
//             min: 3,
//             max: 10,
//             message: '用户名的长度在 3 到 10 个字符之间',
//             trigger: 'blur',
//           },
//         ],
//         email: [
//           { required: true, message: '请输入邮箱', trigger: 'blur' },
//           { validator: checkEmail, trigger: 'blur' },
//         ],
//         mobile: [
//           { required: true, message: '请输入手机号', trigger: 'blur' },
//           { validator: checkMobile, trigger: 'blur' },
//         ],
//       },
//     }
//   },
//   created() {
//     this.getUserInfo()
//   },
//   computed: {
//     getUserBaseInfo() {
//       return this.$store.getters.userBaseInfo
//     },
//     isUserId() {
//       return this.$store.getters.userId
//     },
//   },
//   methods: {
//     //   加载时获取用户信息
//     async getUserInfo() {
//       const { data: res } = await getUserPermsInfo(this.isUserId)
//       console.log(res)
//       if (res.code !== 200) return this.$message.error(res.msg)

//       this.userInfo = res.data.userBaseInfo
//       this.rolesInfo = res.data.roleInfo
//       this.menus = res.data.menusInfo
//     },
//     format(dateObject) {
//       let obj = dateObject + ''
//       let array = obj.split(',')

//       let YY = array[0]
//       let MM = array[1]
//       let DD = array[2]
//       let HH = array[3]
//       let mm = array[4]
//       if (array[3] < 10) {
//         HH = '0' + array[3]
//       }
//       if (array[4] < 10) {
//         mm = '0' + array[3]
//       }
//       var dateStr = YY + '年' + MM + '月' + DD + '日' + HH + '时' + mm + '分'
//       return dateStr
//     },

//     // baseInfo表单项Change事件
//     usernameChange(value) {
//       this.sumbitForm.username = value
//     },
//     ssexChange(value) {
//       this.sumbitForm.ssex = value
//     },
//     emailChange(value) {
//       this.sumbitForm.email = value
//     },
//     mobileChange(value) {
//       this.sumbitForm.mobile = value
//     },
//     descriptionChange(value) {
//       this.sumbitForm.description = value
//     },
//     avatarChange(value) {
//       this.sumbitForm.avatar = value
//     },

//     // 点击修改提交修改的用户信息
//     saveUserInfo() {
//       // 先表单校验
//       this.$refs.baseInfoRef.validate(async (valid) => {
//         //   校验失败返回
//         if (!valid) return
//         this.sumbitForm.userId = this.userInfo.userId
//         console.log(this.sumbitForm)
//         // 调用接口
//         const { data: res } = await updateUserInfo(this.sumbitForm)
//         console.log(res)
//         if (res.code !== 200) this.$message.error('操作失败！')
//         this.getUserInfo()
//         this.$message.success('操作成功！')
//         this.infoDialogVisible = false
//       })
//     },

//     // 展示修改头像的弹窗
//     showInfoDialog() {
//       this.titles = '修改基本信息'
//       this.infoDialogVisible = true
//     },

//     // 监听dialog弹窗关闭事件
//     dialogClosed() {
//       this.$refs.baseInfoRef.resetFields()
//     },

//     // handleChange
//     handleChange(e) {
//       console.log(e)
//     },
//   },
// }
</script>

<style scoped lang="less">
.baseInfo {
  display: flex;
  flex-direction: column;
  // .avatar {
  //   display: flex;
  //   flex-direction: row;
  //   justify-content: center;
  //   align-items: center;
  //   flex: 1;
  //   width: 150px;
  //   height: 180px;
  //   .el-button {
  //     margin-left: 10px;
  //   }
  // }
  .baseInfo_table {
    width: 60% !important;
    margin: 10px auto 40px;
  }
}

.el-dialog {
  min-width: 500px;
  .el-input {
    width: 300px !important;
  }
  .inputDesc {
    width: 300px !important;
  }
}

.el-tag {
  margin: 7px;
}

.bdtop {
  border-top: 1px solid #eee;
}

.bdbottom {
  border-top: 1px solid #eee;
}
</style>
