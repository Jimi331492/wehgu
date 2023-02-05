<!--
 * @Author: 龙际妙
 * @Date: 2022-05-18 03:22:29
 * @Description: 
 * @FilePath: \wehgu-vue-admin\src\views\audit\Auth.vue
 * @LastEditTime: 2022-05-19 07:36:45
 * @LastEditors: Please set LastEditors
-->
<template>
  <div class="container">
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>审核管理</el-breadcrumb-item>
      <el-breadcrumb-item>认证管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片内容区 -->
    <el-card>
      <!-- 搜索与添加区域 -->
      <el-row :gutter="20">
        <el-col :span="7">
          <el-input placeholder="请输入" v-model="query.name" clearable @clear="queryPage">
            <template #append>
              <el-button icon="el-icon-search" @click="queryPage"></el-button>
            </template>
          </el-input>
        </el-col>
        <el-col :span="3" style="display: flex; justify-content: flex-end; align-self: flex-end">
          <el-dropdown size="small" :disabled="this.batchList === null || this.batchList.length < 1" @command="handleCommand">
            <el-button size="small" :disabled="this.batchList === null || this.batchList.length < 1" type="success">
              批量操作<el-icon><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-for="item in operateList" :key="item" :command="item">
                  {{ item }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-col>
      </el-row>
      <!-- 列表区域 -->
      <el-table :data="list" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index"></el-table-column>
        <el-table-column prop="name" label="用户" width="100px" align="center">
          <template v-slot="scope">
            <div>{{ scope.row.nickname }}</div>
            <el-avatar :size="50" :src="scope.row.avatar" />
          </template>
        </el-table-column>
        <el-table-column prop="" label="身份信息" align="center">
          <template v-slot="scope">
            <el-table :data="[scope.row.authDTO]" border stripe>
              <el-table-column prop="studentNo" label="学号" align="center"> </el-table-column>
              <el-table-column prop="grade" label="入学年份" width="100px" align="center"> </el-table-column>
              <el-table-column prop="college" label="学院" align="center"> </el-table-column>
              <el-table-column prop="major" label="专业" width="150px" align="center"> </el-table-column>
              <el-table-column prop="name" label="姓名" width="100px" align="center"> </el-table-column>
              <el-table-column prop="sex" label="性别" width="100px" align="center"> </el-table-column>
              <el-table-column prop="campus" label="校区" width="100px" align="center"> </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <el-table-column prop="" label="学生证" width="250px" align="center">
          <template v-slot="scopes">
            <el-table :data="[{ ...scopes.row.studentCardURI }]" border stripe>
              <el-table-column prop="0" label="正面" width="100px" align="center">
                <template v-slot="scope">
                  <el-image :src="scope.row['0']" :hide-on-click-modal="true" :preview-src-list="scopes.row.studentCardURI" :initial-index="0"> </el-image>
                </template>
              </el-table-column>
              <el-table-column prop="1" label="反面" width="100px" align="center">
                <template v-slot="scope">
                  <el-image :src="scope.row['1']" :hide-on-click-modal="true" :preview-src-list="scope.row.studentCardURI" :initial-index="0"> </el-image>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200px" align="center">
          <template v-slot="scope">
            <!-- 编辑按钮 -->
            <el-tooltip class="item" effect="dark" content="编辑" placement="top" :enterable="false">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row)"></el-button>
            </el-tooltip>
            <!-- 删除按钮 -->
            <el-tooltip class="item" effect="dark" content="删除" placement="top" :enterable="false">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="confirmDeleteBox(scope.row.UID)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页区域 -->
      <el-pagination
        :currentPage="query.page"
        :page-size="query.limit"
        :page-sizes="[5, 10, 20, 40]"
        :disabled="false"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
    <!-- 弹窗 -->
    <el-dialog :title="titles" v-model="dialogVisible" width="50%" @close="dialogClosed">
      <!-- 内容主体 -->
      <el-form ref="formRef" :model="form" label-width="80px">
        <el-form-item label="审核结果" prop="name">
          <el-select v-model="form.roleUuid" placeholder="请选择角色">
            <el-option v-for="item in roleList" :key="item.roleUuid" :label="item.roleName" :value="item.roleUuid" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="description">
          <el-input v-model="form.description" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <!-- 底部区域 -->
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script>
import { query, save, remove, authSuccess } from '../../api/auth'
export default {
  data() {
    return {
      query: { storeTypeTable: 'auth' },
      list: [],
      total: 0,
      titles: '',
      dialogVisible: false,
      form: {},
      batchList: [],
      operateList: ['审核通过', '审核不通过', '删除'],
    }
  },
  created() {
    this.queryPage()
  },
  mounted() {},
  methods: {
    async queryPage() {
      // 分页查询
      const { data: res } = await query(this.query)
      if (res.code !== 200) this.$message.error(res.message)
      console.log('res', res)
      this.list = res.data
      this.total = res.data.length
    },
    handleSizeChange(e) {
      // 分页控件
      this.query.limit = e
      this.queryPage()
    },
    handleCurrentChange(e) {
      this.query.page = e
      this.queryPage()
    },
    //增改
    showAddDialog() {
      this.titles = '审核'
      this.dialogVisible = true
    },
    showEditDialog() {
      this.titles = '审核'

      this.dialogVisible = true
    },
    // 弹窗关闭重置
    dialogClosed() {
      this.$refs['formRef'].resetFields()
    },
    async save() {
      const { data: res } = await save(this.form)
      if (res.code !== 200) this.message.error(res.message)
      this.queryPage()
      this.dialogVisible = false
    },
    //删除
    async confirmDeleteBox(UID) {
      // 弹窗提示用户是否要删除
      const Result = await this.$confirm('此操作将永久删除该数据!!!是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).catch((err) => err)
      if (Result !== 'confirm') {
        return this.$message.info('已经取消删除')
      }
      const { data: res } = await remove(UID)
      if (res.code !== 200) return this.$message.error(res.message)
      this.queryPage()
      this.$message.success('删除成功！')
    },

    //批量选择
    handleSelectionChange(e) {
      this.batchList = e.map((item) => {
        return item.userDetailUuid
      })
    },

    //批量审核
    async handleCommand(e) {
      console.log('e', e)
      if (e === '审核通过') {
        // 分页查询
        const { data: res } = await authSuccess(this.batchList)
        if (res.code !== 200) this.$message.error(res.message)
        this.$message.success(res.message)
        this.batchList = []
      } else if (e === '审核不通过') {
        this.titles = '审核不通过理由'
        this.dialogVisible = true
      } else {
        // 弹窗提示用户是否要删除
        const Result = await this.$confirm(`确认删除所选内容?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).catch((err) => err)
        if (Result !== 'confirm') {
          return this.$message.info('已经取消操作')
        }

        // this.batchSave(e)
      }
    },
  },
}
</script>
<style scoped lang="less"></style>
