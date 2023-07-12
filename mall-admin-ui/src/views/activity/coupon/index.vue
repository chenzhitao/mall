<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!-- 新增 -->
      <div style="display: inline-block;margin: 0px 2px;">
        <el-button
          v-permission="['admin','YXSTORECOUPON_ALL','YXSTORECOUPON_CREATE']"
          class="filter-item"
          size="mini"
          type="primary"
          icon="el-icon-plus"
          @click="add"
        >新增</el-button>
      </div>
    </div>
    <!--表单组件-->
    <!-- <eForm ref="form" :is-add="isAdd" /> -->
    <eForms ref="form" :is-add="isAdd" />
    <eIForm ref="form2" :is-add="isAdd" />
    <!--表格渲染-->
    <el-table v-loading="loading" :data="data" size="small"  style="width: 100%;">
      <!--<el-table-column prop="id" label="ID"/>-->
      <el-table-column prop="title" label="优惠券名称" align="center"  />
      <el-table-column prop="type" label="优惠券类型" align="center"  >
        <template slot-scope="scope">
          <span>{{ scope.row.type===0?'领取券(需要用户手动领取)':'推送券(定向推送用户)' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="couponPrice" label="优惠券面值" align="center"  width="150"  />
      <el-table-column prop="useMinPrice" label="优惠券最低消费" align="center"   width="150" />
      <el-table-column prop="timeType" label="优惠券期限类型">
        <template slot-scope="scope">
          <span>{{ scope.row.timeType===0?'固定有效期':'静态有效期' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="优惠券有效期限" align="center"  >
        <template slot-scope="scope" align="center"  >
          <div v-if="scope.row.timeType===0">
            <span>{{ scope.row.startTime | formatDate}}</span>
            <span>  到  </span>
            <span>{{ scope.row.couponTime | formatDate}}</span>
          </div>
          <div v-if="scope.row.timeType===1">
            <span>  自领取后{{scope.row.timeNum}}天有效  </span>
          </div>



        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="100" align="center"  />
      <el-table-column label="状态" width="100" align="center" >
        <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.status === 1" style="cursor: pointer" :type="''">开启</el-tag>
            <el-tag v-else :type=" 'info' ">关闭</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="addTime" align="center"  label="创建时间">
        <template slot-scope="scope">
          <span>{{ formatTime(scope.row.addTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="checkPermission(['admin','YXSTORECOUPON_ALL','YXSTORECOUPON_EDIT','YXSTORECOUPON_DELETE'])" width="200" label="操作" align="center">
        <template slot-scope="scope">
          <!-- <el-button
            v-permission="['admin','YXSTORECOUPON_ALL','YXSTORECOUPON_EDIT']"
            size="mini"
            type="primary"
            @click="edit2(scope.row)"
          >
            发布
          </el-button> -->
          <el-dropdown size="mini" split-button type="primary" trigger="click">
            操作
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button
                  v-permission="['admin','YXSTORECOUPON_ALL','YXSTORECOUPON_EDIT']"
                  size="mini"
                  type="primary"
                  icon="el-icon-edit"
                  @click="edit(scope.row)"
                >编辑</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-popover
                  :ref="scope.row.id"
                  v-permission="['admin','YXSTORECOUPON_ALL','YXSTORECOUPON_DELETE']"
                  placement="top"
                  width="180"
                >
                  <p>确定删除本条数据吗？</p>
                  <div style="text-align: right; margin: 0">
                    <el-button size="mini" type="text" @click="$refs[scope.row.id].doClose()">取消</el-button>
                    <el-button :loading="delLoading" type="primary" size="mini" @click="subDelete(scope.row.id)">确定</el-button>
                  </div>
                  <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini">删除</el-button>
                </el-popover>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
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
import { del } from '@/api/yxStoreCoupon'
// import eForm from './form'
import eForms from './forms'
import eIForm from '../couponissue/form'
import { formatTime ,  formatDate,formatTimeTwo } from '@/utils/index'
export default {
  components: {  eIForm,eForms },
  mixins: [initData],




  data() {
    return {
      delLoading: false
    }
  },
  created() {
    this.$nextTick(() => {
      this.init()
    })
  },
  methods: {
    formatTime,
    checkPermission,
    // formatDate,
    beforeInit() {
      this.url = 'api/yxStoreCoupon'
      const sort = 'id,desc'
      this.params = { page: this.page, size: this.size, sort: sort, isDel: 0 }
      return true
    },
    //   timestampToTime (row, column) {
    //     var date = new Date(row.cjsj) //时间戳为10位需*1000，时间戳为13位的话不需乘1000
    //     var Y = date.getFullYear() + '-'
    //     var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-'
    //     var D = date.getDate() + ' '
    //     var h = date.getHours() + ':'
    //     var m = date.getMinutes() + ':'
    //     var s = date.getSeconds()
    //     return Y+M+D+h+m+s
    //     console.log(timestampToTime (1533293827000))
    // },
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
    edit(data) {
      console.log(data,'修改')
      // data.couponTime =   data.couponTime * 1000
      // let arr = this.filters.formatDate(data.couponTime)
      // console.log(arr)
      this.isAdd = false
      const _this = this.$refs.form
      console.log(data )
      _this.form = {
        id: data.id,
        title: data.title,
        integral: data.integral,
        couponPrice: data.couponPrice ,
        useMinPrice: data.useMinPrice,
        couponTime: data.couponTime  * 1000 ,
        sort: data.sort,
        status: data.status,
        addTime: data.addTime,
        isDel: data.isDel,
        type: data.type,
        remark: data.remark,

          startTime: data.startTime * 1000,
        // endTime:  data.endTime,
        // totalCount:  data.totalCount,
        // remainCount:  data.remainCount,
        // isPermanent:  data.isPermanent,
        // issueStatus:  data.issueStatus,
      }
      _this.dialog = true
    },
    edit2(data) {
      this.isAdd = true
      const _this = this.$refs.form2
      _this.form = {
        cid: data.id,
        cname: data.title,
        isPermanent: 0,
        type: 0,
        status: 1,
        totalCount: 0,
        remainCount: 0,
        remark: data.remark,
        isDel: 0
      }
      _this.dialog = true
    }
  },
    filters: {
        formatDate(time) {
        time = time * 1000
        let date = new Date(time)
        // console.log(new Date(time))
        return formatDate(date, 'yyyy-MM-dd ')
      }
    //   formatDate: function (value) {
    //     let date = new Date(value);
    //     let y = date.getFullYear();
    //     console.log(y,'---------------------------66')
    //     let MM = date.getMonth() + 1;
    //     MM = MM < 10 ? ('0' + MM) : MM;
    //     let d = date.getDate();
    //     d = d < 10 ? ('0' + d) : d;
    //     let h = date.getHours();
    //     h = h < 10 ? ('0' + h) : h;
    //     let m = date.getMinutes();
    //     m = m < 10 ? ('0' + m) : m;
    //     let s = date.getSeconds();
    //     s = s < 10 ? ('0' + s) : s;
    //     return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
    //   }
    }
}
</script>

<style scoped>

</style>
