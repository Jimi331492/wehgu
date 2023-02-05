<!--
 * @Author: 龙际妙
 * @Date: 2022-05-18 21:53:30
 * @Description: 
 * @FilePath: \wehgu-vue-admin\src\views\audit\OSS.vue
 * @LastEditTime: 2022-05-19 00:02:38
 * @LastEditors: Please set LastEditors
-->
<template>
  <div class="container">
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>审核管理</el-breadcrumb-item>
      <el-breadcrumb-item>图片审核</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片内容区 -->
    <el-card>
      <!-- 搜索与添加区域 -->
      <el-row :gutter="20">
        <el-col :span="6">
          <el-autocomplete
            v-model="query.storeTypeTable"
            :fetch-suggestions="querySearchAsync"
            clearable
            @clear="queryPage"
            class="inline-input"
            placeholder="请输入图片类型"
            @select="queryPage"
            size="small"
          >
            <template #append>
              <el-button icon="el-icon-search" @click="queryPage" size="small"></el-button>
            </template>
          </el-autocomplete>
        </el-col>

        <el-col :span="3" style="display: flex; justify-content: flex-end; align-self: flex-end">
          <el-dropdown size="small" :disabled="this.batchList === null || this.batchList.length < 1" @command="handleSelectAuditStatus">
            <el-button size="small" :disabled="this.batchList === null || this.batchList.length < 1" type="success">
              批量操作<el-icon><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-for="item in operateList" :key="item" :command="item.value">
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

        <el-table-column label="缩略图" width="200px" align="center">
          <template v-slot="scope">
            <div class="img-box">
              <el-image
                v-if="scope.row.uri"
                :src="scope.row.uri"
                :hide-on-click-modal="true"
                :preview-src-list="[scope.row.uri]"
                :initial-index="4"
                fit="contain"
              >
              </el-image>

              <img v-else src="../../assets/404.png" alt="" />
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="storeTypeTable" label="图片类型"></el-table-column>
        <el-table-column label="操作" width="400px">
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
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入" />
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
import { query, remove } from '../../api/oss'
import { queryDictionary } from '../../api/system' //路由权限
export default {
  data() {
    return {
      query: { limit: 5, page: 1 },
      roleList: [],
      total: 0,
      titles: '',
      dialogVisible: false,

      batchList: [],
      imageType: [],
      operateList: ['审核不通过', '删除'],
    }
  },
  created() {
    this.queryPage()
    this.queryDictionaryPage()
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
      this.titles = '新增'
      this.dialogVisible = true
    },
    showEditDialog(row) {
      this.titles = '编辑'
      this.$nextTick(() => {
        this.form = { ...row }
      })
      this.dialogVisible = true
    },
    // 弹窗关闭重置
    dialogClosed() {
      this.$refs['formRef'].resetFields()
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
    //分页获取自带你
    async queryDictionaryPage() {
      // 分页查询
      const query = {
        limit: '',
        page: '',
        key: '图片类型',
      }
      const { data: res } = await queryDictionary(query)
      if (res.code !== 200) this.$message.error(res.message)
      console.log('res', res)
      this.imageType = res.data.records
    },
    //批量选择
    handleSelectionChange(e) {
      this.batchList = e.map((item) => {
        return item.ossUuid
      })
    },
  },
}
</script>
<style scoped lang="less"></style>
