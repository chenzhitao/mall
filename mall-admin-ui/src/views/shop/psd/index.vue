<template>
  <div class="psd-material">
      <el-container>
        <el-aside width="unset">
          <div style="margin-bottom: 10px">
            <el-button
              class="el-icon-plus"
              size="small"
              @click="materialgroupAdd()"
            >
              添加分组
            </el-button>
          </div>
          <el-tabs v-model="materialgroupObjId" v-loading="materialgroupLoading" tab-position="left" @tab-click="tabClick">
            <el-tab-pane
              v-for="(item,index) in materialgroupList"
              :key="item.id"
              :name="item.id"
            >
              <span slot="label"> {{ item.name }}</span>
            </el-tab-pane>
          </el-tabs>
        </el-aside>
        <el-main>
          <el-card>
            <div slot="header">
              <el-row>
                <el-col :span="12">
                  <span>{{ materialgroupObj.name }}</span>
                  <span v-if="materialgroupObj.id != '-1'">
                    <el-button size="small" type="text" class="el-icon-edit" style="margin-left: 10px;" @click="materialgroupEdit(materialgroupObj)">重命名</el-button>
                    <el-button size="small" type="text" class="el-icon-delete" style="margin-left: 10px;color: red" @click="materialgroupDelete(materialgroupObj)">删除</el-button>
                  </span>
                </el-col>
                <el-col :span="12" style="text-align: right;">
                  <el-button style="margin-left: 10px; float: right;" size="small" type="success" @click.native.prevent="submitUpload">确认上传</el-button>
                  <el-upload
                    :action="uploadApi"
                    :headers="headers"
                       :file-list="fileList"
                    :on-progress="handleProgress"
                    :before-upload="beforeUpload"
                    :on-success="handleSuccess"
                    multiple
                    ref="upload"
                     :list-type="picture"
                    :http-request="uploadFile"
                    :auto-upload="false"
                  >
                    <el-button size="small" type="primary">选取文件</el-button>
                  </el-upload>
                </el-col>
              </el-row>
            </div>
            <div v-loading="tableLoading">
              <el-alert
                v-if="tableData.length <= 0"
                title="暂无数据"
                type="info"
                :closable="false"
                center
                show-icon
              />
              <el-row :gutter="5">
                <el-checkbox-group v-model="urls" :max="num - value.length">
                  <el-col v-for="(item,index) in tableData" :key="index" :span="4">
                    <el-card :body-style="{ padding: '5px' }">
                      <el-image
                        style="width: 100%;height: 100px"
                        :src="item.url"
                        fit="contain"
                        :preview-src-list="[item.url]"
                        :z-index="999"
                      />

                      <div>
                        <el-tooltip :content="item.name" placement="top" effect="light">
                          <p class="image-title">{{ item.name }}</p>
                        </el-tooltip>

                        <el-checkbox class="material-name" :label="item.url">
                          选择
                        </el-checkbox>
                        <el-row>
                          <el-col :span="24" class="col-do">
                            <el-button type="text" size="medium" @click="materialDel(item)">删除</el-button>
                          </el-col>
                        </el-row>

                      </div>
                    </el-card>
                  </el-col>
                </el-checkbox-group>
              </el-row>
              <el-pagination
                :current-page.sync="page.currentPage"
                :page-sizes="[12, 24]"
                :page-size="page.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="page.total"
                class="pagination"
                @size-change="sizeChange"
                @current-change="pageChange"
              />
            </div>
          </el-card>
        </el-main>
      </el-container>
  </div>
</template>

<script>
import { getPage as materialgroupPage, addObj as materialgroupAdd, delObj as materialgroupDel, putObj as materialgroupEdit } from '@/api/tools/materialgroup'
import { getPage, addObj, delObj, putObj } from '@/api/tools/material'
import { getToken } from '@/utils/auth'
import { mapGetters } from 'vuex'
import { upload } from '@/utils/upload'

export default {
  name: 'Psd',
  props: {
    // 素材数据
    value: {
      type: Array,
      default() {
        return []
      }
    },
    // 素材类型
    type: {
      type: String
    },
    // 素材限制数量，默认5个
    num: {
      type: Number,
      default() {
        return 5
      }
    },
    // 宽度
    width: {
      type: Number,
      default() {
        return 150
      }
    },
    // 宽度
    height: {
      type: Number,
      default() {
        return 150
      }
    }
  },
  data() {
    return {
      headers: {
        Authorization: getToken()
      },
      dialogVisible: false,
      url: '',
      listDialogVisible: false,
      materialgroupList: [],
       fileList: [], // 数组
      materialgroupObjId: '',
      materialgroupObj: {},
      materialgroupLoading: false,
      tableData: [],
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 12, // 每页显示多少条
        ascs: [], // 升序字段
        descs: 'create_time'// 降序字段
      },
      tableLoading: false,
      groupId: null,
      urls: [],
       picture:'picture'
    }
  },
  computed: {
    ...mapGetters([
      'uploadApi'
    ])
  },
  created(){
    this.toSeleteMaterial();
  },
  methods: {
    moveMaterial(index, type) {
      if (type == 'up') {
        const tempOption = this.value[index - 1]
        this.$set(this.value, index - 1, this.value[index])
        this.$set(this.value, index, tempOption)
      }
      if (type == 'down') {
        const tempOption = this.value[index + 1]
        this.$set(this.value, index + 1, this.value[index])
        this.$set(this.value, index, tempOption)
      }
    },
    zoomMaterial(index) {
      this.dialogVisible = true
      this.url = this.value[index]
    },
    deleteMaterial(index) {
      const that = this
      this.$confirm('是否确认删除？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        that.value.splice(index, 1)
        that.urls = []
      })
    },
    toSeleteMaterial() {
      this.listDialogVisible = true
      if (this.tableData.length <= 0) {
        this.materialgroupPage()
      }
    },
    materialgroupPage() {
      this.materialgroupLoading = true
      materialgroupPage({
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 100, // 每页显示多少条
        ascs: [], // 升序字段
        descs: 'create_time'// 降序字段
      }).then(response => {
        this.materialgroupLoading = false
        const materialgroupList = response.content
        materialgroupList.unshift({
          id: '-1',
          name: '全部分组'
        })
        this.materialgroupList = materialgroupList
        this.tabClick({
          index: 0
        })
      })
    },
    materialgroupDelete(materialgroupObj) {
      const that = this
      this.$confirm('是否确认删除该分组？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        materialgroupDel(materialgroupObj.id)
          .then(function() {
            that.$delete(that.materialgroupList, materialgroupObj.index)
          })
      })
    },
    materialgroupEdit(materialgroupObj) {
      const that = this
      this.$prompt('请输入分组名', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: materialgroupObj.name
      }).then(({ value }) => {
        materialgroupEdit({
          id: materialgroupObj.id,
          name: value
        }).then(function() {
          materialgroupObj.name = value
          that.$set(that.materialgroupList, materialgroupObj.index, materialgroupObj)
        })
      }).catch(() => {

      })
    },
    materialgroupAdd() {
      const that = this
      this.$prompt('请输入分组名', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        materialgroupAdd({
          name: value
        }).then(function() {
          that.materialgroupPage()
        })
      }).catch(() => {

      })
    },
    tabClick(tab, event) {
      this.urls = []
      const index = Number(tab.index)
      const materialgroupObj = this.materialgroupList[index]
      materialgroupObj.index = index
      this.materialgroupObj = materialgroupObj
      this.materialgroupObjId = materialgroupObj.id
      this.page.currentPage = 1
      this.page.total = 0
      if (materialgroupObj.id != '-1') {
        this.groupId = materialgroupObj.id
      } else {
        this.groupId = null
      }
      this.getPage(this.page)
    },
    getPage(page, params) {
      this.tableLoading = true
      getPage(Object.assign({
        page: page.currentPage - 1,
        size: page.pageSize,
        descs: this.page.descs,
        ascs: this.page.ascs,
        sort: 'createTime,desc'
      }, {
        groupId: this.groupId
      })).then(response => {
        const tableData = response.content
        this.page.total = response.totalElements
        this.page.currentPage = page.currentPage
        this.page.pageSize = page.pageSize
        this.tableData = tableData
        this.tableLoading = false
      }).catch(() => {
        this.tableLoading = false
      })
    },
    sizeChange(val) {
      console.log(val)
      this.page.currentPage = 1
      this.page.pageSize = val
      this.getPage(this.page)
    },
    pageChange(val) {
      console.log(val)
      this.page.currentPage = val
      // this.page.pageSize = val
      this.getPage(this.page)
    },
    materialRename(item) {
      const that = this
      this.$prompt('请输入素材名', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: item.name
      }).then(({ value }) => {
        putObj({
          id: item.id,
          name: value
        }).then(function() {
          that.getPage(that.page)
        })
      }).catch(() => {

      })
    },
    materialUrl(item) {
      const that = this
      this.$prompt('素材链接', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: item.url
      }).then(({ value }) => {

      }).catch(() => {

      })
    },
    materialDel(item) {
      const that = this
      this.$confirm('是否确认删除该素材？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        delObj(item.id)
          .then(function() {
            that.getPage(that.page)
          })
      })
    },
    handleCommand(command) {
      const that = this
      const s = command.split('-')
      putObj({
        id: s[0],
        groupId: s[1]
      }).then(function() {
        that.getPage(that.page)
      })
    },
    handleProgress(event, file, fileList) {
      // let uploadProgress = file.percentage.toFixed(0)
      // this.uploadProgress = uploadProgress
    },
    handleSuccess(response, file, fileList) {
      const that = this
      this.uploadProgress = 0
      addObj({
        type: '1',
        groupId: this.groupId != '-1' ? this.groupId : null,
        name: file.name,
        url: response.link
      }).then(function() {
        that.getPage(that.page)
      })
    },
    beforeUpload(file) {
      const isPic =
          file.type === 'image/jpeg' ||
          file.type === 'image/png' ||
          file.type === 'image/gif' ||
          file.type === 'image/jpg'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isPic) {
        this.$message.error('上传图片只能是 JPG、JPEG、PNG、GIF 格式!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isPic && isLt2M
    },
    sureUrls() {
      this.urls.forEach(item => {
        this.$set(this.value, this.value.length, item)
      })
      this.listDialogVisible = false
    },
    uploadFile (content) {
      let _this = this;
      upload(content.action, content.file)
        .then((response)=>{
            _this.handleSuccess(response.data, content.file, [])
      })
    },
    submitUpload(){

      this.$refs.upload.submit();

       this.$refs.upload.clearFiles();
    }

  }
}
</script>

<style lang="scss" scoped>
  /deep/ .el-icon-circle-close{
    color: red;
  }
  .material-name{
    padding: 8px 0px;
  }
  .col-do{
    text-align: center;
  }
  .button-do{
    padding: unset!important;
    font-size: 12px;
  }
  .image-title {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 100%;
    font-size: 14px;
    margin: 0;
    padding: 0;
  }
</style>

