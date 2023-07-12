<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!-- 新增 -->
      <div style="display: inline-block;margin: 0px 2px;">
        <!-- <el-button
          v-permission="['admin','YXSYSTEMGROUPDATA_ALL','YXSYSTEMGROUPDATA_CREATE']"
          class="filter-item"
          size="mini"
          type="primary"
          icon="el-icon-plus"
          @click="add"
          
        >新增</el-button> -->
      </div>
    </div>
    <!--表单组件-->
    <eForm ref="form" :is-add="isAdd" />
    <!--表格渲染-->
    <el-table v-loading="loading" :data="takeDate" size="small" style="width: 100%;">
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="url" label="路由" />
      <!-- <el-table-column prop="sort" label="排序" /> -->
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="danger" size="mini" @click='bindrout(scope.row)'> 复制</el-button>
        </template>
      </el-table-column>
      <!-- <el-table-column v-if="checkPermission(['admin','YXSYSTEMGROUPDATA_ALL','YXSYSTEMGROUPDATA_EDIT','YXSYSTEMGROUPDATA_DELETE'])" label="操作" width="150px" align="center">
        <template slot-scope="scope">
          <el-button v-permission="['admin','YXSYSTEMGROUPDATA_ALL','YXSYSTEMGROUPDATA_EDIT']" size="mini" type="primary" icon="el-icon-edit" @click="edit(scope.row)" />
          <el-popover
            :ref="scope.row.id"
            v-permission="['admin','YXSYSTEMGROUPDATA_ALL','YXSYSTEMGROUPDATA_DELETE']"
            placement="top"
            width="180"
          >
            <p>确定删除本条数据吗？</p>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="$refs[scope.row.id].doClose()">取消</el-button>
              <el-button :loading="delLoading" type="primary" size="mini" @click="subDelete(scope.row.id)">确定</el-button>
            </div>
            <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini" />
          </el-popover>
        </template>
      </el-table-column> -->
    </el-table>
    <!--分页组件-->
    <el-pagination
      :total="total"
      :current-page="page + 1"
      style="margin-top: 8px;"
      layout="total, prev, pager, next, sizes"
      @size-change="sizeChange"
      @current-change="pageChange"
    />
  </div>
</template>

<script>
import checkPermission from '@/utils/permission'
import initData from '@/mixins/crud'
import { del } from '@/api/yxSystemGroupData'
import eForm from './hotform'
export default {
  components: { eForm },
  mixins: [initData],
  data() {
    return {
      delLoading: false,
      takeDate:[
          { name:'我的收藏',url:'/pages/shop/GoodsCollection/index'},
           { name:'每日秒杀',url:'/pages/activity/GoodsSeckill/index'},
            { name:'邀请砍价',url:'/pages/activity/GoodsBargain/index'},
             { name:'积分签到',url:'/pages/user/signIn/Sign/index'},
              { name:'优惠券',url:'/pages/user/coupon/GetCoupon/index'},
                 { name:'商品列表',url:'/pages/shop/GoodsList/index'},
               { name:'新闻列表',url:'/pages/shop/news/NewsList/index'}
      ]

    }
  },
  created() {
    this.$nextTick(() => {
      this.init()
    })
  },
  methods: {
    checkPermission,
    beforeInit() {
      this.url = 'api/yxSystemGroupData'
      const sort = 'id,desc'
      this.params = { page: this.page, size: this.size, sort: sort, groupName: 'yshop_hot_search' }
      return true
    },
    subDelete(id) {
      this.delLoading = true
      del(id).then(res => {
        this.delLoading = false
        this.$refs[id].doClose()
        this.dleChangePage()
        this.init()
        this.$notify({
          title: '删除成功',
          type: 'success',
          duration: 2500
        })
      }).catch(err => {
        this.delLoading = false
        this.$refs[id].doClose()
        console.log(err.response.data.message)
      })
    },
    add() {
      this.isAdd = true
      this.$refs.form.dialog = true
    },
    bindrout(row){
                 let url = row.url;
                let oInput = document.createElement('input');
                oInput.value = url;
                document.body.appendChild(oInput);
                oInput.select(); // 选择对象;
                console.log(oInput.value)
                document.execCommand("Copy"); // 执行浏览器复制命令
                this.$message({
                    message: '复制成功',
                    type: 'success'
                });
                oInput.remove()


    },
    edit(data) {
      this.isAdd = false
      const _this = this.$refs.form
      _this.form = {
        id: data.id,
        groupName: data.groupName,
        title: data.map.title,
        info: data.map.info,
        url: data.map.url,
        pic: data.map.pic,
        sort: data.sort,
        status: data.status
      }
      _this.dialog = true
    }
  }
}
</script>

<style scoped>

</style>
