<!--  eslint-disable prettier/prettier -->

<!--
 * @Author: 龙际妙
 * @Date: 2021-11-03 05:46:31
 * @Description: 
 * @FilePath: \wehgu-vue-admin\src\views\index\Notice.vue
 * @LastEditTime: 2022-05-19 22:54:47
 * @LastEditors: Please set LastEditors
-->
<template>
  <div class="container">
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>首页管理</el-breadcrumb-item>
      <el-breadcrumb-item>公告管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片内容区 -->
    <el-card>
      <!-- 搜索与添加区域 -->
      <el-row :gutter="20">
        <el-col :span="7">
          <el-input placeholder="请输入标题" v-model="query.name" clearable @clear="queryPage">
            <template #append>
              <el-button icon="el-icon-search" @click="queryPage"></el-button>
            </template>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="showAddDialog">发布公告</el-button>
        </el-col>
      </el-row>
      <!-- 列表区域 -->
      <el-table :data="list" border stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column prop="title" label="标题" width="200px" align="center"></el-table-column>
        <el-table-column prop="content" label="内容" align="center"></el-table-column>
        <el-table-column prop="type" label="类型" width="100px" align="center"></el-table-column>
        <el-table-column label="操作" width="400px" align="center">
          <template v-slot="scope">
            <!-- 编辑按钮 -->
            <el-tooltip class="item" effect="dark" content="编辑" placement="top" :enterable="false">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row)"></el-button>
            </el-tooltip>
            <!-- 删除按钮 -->
            <el-tooltip class="item" effect="dark" content="删除" placement="top" :enterable="false">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="confirmDeleteBox(scope.row.noticeUuid)"></el-button>
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
    <!-- 添加的弹窗 -->
    <el-dialog :title="titles" v-model="dialogVisible" width="50%" @close="dialogClosed">
      <!-- 内容主体 -->
      <el-form ref="formRef" :model="form" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" v-model="form.content" placeholder="请输入内容" style="width: 300px" />
        </el-form-item>
        <el-form-item label="公告类型" prop="type">
          <el-input v-model="form.type" placeholder="请选择公告类型" />
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
import { query, save, remove } from '../../api/notice'
export default {
  data() {
    return {
      query: { limit: 5, page: 1, type: null, title: null, content: null },
      list: [],
      total: 0,
      titles: '',
      dialogVisible: false,
      form: {},
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
      this.list = res.data.records
      this.total = res.data.total
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
      this.titles = '发布公告'
      this.dialogVisible = true
    },
    showEditDialog(row) {
      this.titles = '编辑公告'
      this.$nextTick(() => {
        this.form = { ...row }
      })
      this.dialogVisible = true
    },
    // 弹窗关闭重置
    dialogClosed() {
      this.$refs['formRef'].resetFields()
    },
    async save() {
      const { data: res } = await save(this.form)
      if (res.code !== 200) this.$message.error(res.message)
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
  },
}
</script>
<style scoped lang="less"></style>
