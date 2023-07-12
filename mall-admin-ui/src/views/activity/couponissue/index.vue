<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!-- 新增 -->
      <el-button
        type="danger"
        class="filter-item"
        size="mini"
        icon="el-icon-refresh"
        @click="toQuery"
      >刷新
      </el-button>
    </div>
    <!--表单组件-->
    <eForm ref="form" :is-add="isAdd"/>
    <!--表格渲染-->
    <el-table v-loading="loading" :data="data" size="small" style="width: 100%;">
      <el-table-column prop="id" label="id"/>
      <el-table-column prop="cname" label="优惠券名称"/>
      <el-table-column prop="type" label="优惠券类型" align="center"  >
        <template slot-scope="scope">
          <span>{{ scope.row.type===0?'领取券(需要用户手动领取)':'推送券(定向推送用户)' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="categoryName" label="限定商品分类"/>
      <el-table-column prop="addTime" label="创建时间">
        <template slot-scope="scope">
          <span>{{ formatTime(scope.row.addTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="timeType" label="优惠券期限类型">
        <template slot-scope="scope">
          <span>{{ scope.row.timeType===0?'固定有效期':'静态有效期' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="优惠券有效期限">
        <template slot-scope="scope">
          <div v-if="scope.row.timeType===0">
            <p>{{ formatTimeTwo(scope.row.startTime) }}</p>
            <p>{{ formatTimeTwo(scope.row.endTime) }}</p>
          </div>
          <div v-if="scope.row.timeType===1">
            <span>  自领取后{{scope.row.timeNum}}天有效  </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="发布数量">
        <template slot-scope="scope">
          <p>本次发出优惠券数量:{{ scope.row.totalCount }}</p>
          <o>剩余优惠券数量:{{ scope.row.remainCount }}</o>
        </template>
      </el-table-column>
      <el-table-column prop="couponPrice" label="优惠券面值"/>
      <el-table-column prop="useMinPrice" label="优惠券最低消费"/>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.status === 1" style="cursor: pointer" :type="''">开启</el-tag>
            <el-tag v-else :type=" 'info' ">关闭</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        v-if="checkPermission(['admin','YXSTORECOUPONISSUE_ALL','YXSTORECOUPONISSUE_EDIT','YXSTORECOUPONISSUE_DELETE'])"
        label="操作" width="200px" align="center">
        <template slot-scope="scope">
          <el-button v-permission="['admin','YXSTORECOUPONISSUE_ALL','YXSTORECOUPONISSUE_EDIT']" size="mini"
                     type="primary" icon="el-icon-edit" @click="edit(scope.row)"/>
          <el-button v-permission="['admin','YXSTORECOUPONISSUE_ALL','YXSTORECOUPONISSUE_EDIT']" size="mini" v-show="scope.row.type===1 && scope.row.status===1 "
                     type="success" icon="el-icon-s-promotion" @click="sendCoupon(scope.row)"/>&nbsp;&nbsp;&nbsp;
          <el-popover
            :ref="scope.row.id"
            v-permission="['admin','YXSTORECOUPONISSUE_ALL','YXSTORECOUPONISSUE_DELETE']"
            placement="top"
            width="180"
          >
            <p>确定删除本条数据吗？</p>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="$refs[scope.row.id].doClose()">取消</el-button>
              <el-button :loading="delLoading" type="primary" size="mini" @click="subDelete(scope.row.id)">确定
              </el-button>
            </div>
            <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini"/>
          </el-popover>
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


    <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="sendCancel"
               :visible.sync="isSendCoupon" title="发送优惠券" width="840px">
      <el-form ref="sendForm" :model="sendForm" size="small" label-width="100px">
        <el-transfer
          filterable
          :filter-method="filterMethod"
          filter-placeholder="请输入搜索关键字"
          :titles="['选择用户', '已选择']"
          v-model="selectUsers"
          :data="sendUsers">
        </el-transfer>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="sendCancel">取消</el-button>
        <el-button :loading="loading" type="primary" @click="doSendSubmit">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import checkPermission from '@/utils/permission'
import initData from '@/mixins/crud'
import {batchSendCoupon, del} from '@/api/yxStoreCouponIssue'
import eForm from './formt'
import {formatTimeTwo, formatTime} from '@/utils/index'
import {getSendUser} from "@/api/yxUser";

export default {
  components: {eForm},
  mixins: [initData],
  data() {
    return {
      delLoading: false,
      isSendCoupon: false,
      issueCouponId: null,
      sendUsers: [],
      selectUsers: [],
      sendForm: {}
    }
  },
  created() {
    this.$nextTick(() => {
      this.init()
    })
  },
  methods: {
    formatTime,
    formatTimeTwo,
    checkPermission,
    beforeInit() {
      this.url = 'api/yxStoreCouponIssue'
      const sort = 'id,desc'
      this.params = {page: this.page, size: this.size, sort: sort}
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
    edit(data) {
      this.isAdd = false
      const _this = this.$refs.form
      _this.form = {
        id: data.id,
        cid: data.cid,
        startTime: data.startTime,
        endTime: data.endTime,
        startTimeDate: data.startTimeDate,
        endTimeDate: data.endTimeDate,
        totalCount: data.totalCount,
        remainCount: data.remainCount,
        isPermanent: data.isPermanent,
        status: data.status,
        isDel: data.isDel,
        addTime: data.addTime
      }
      _this.dialog = true
    },
    sendCoupon(data) {
      this.isSendCoupon = true;
      this.issueCouponId = data.id;
      this.selectUsers = [];
      getSendUser(data.id).then(data => {
        this.sendUsers = data
      }).catch(err => {
      })

    },

    sendCancel() {
      this.isSendCoupon = false

    },

    filterMethod(query, item) {
      return item.label.indexOf(query) > -1;
    },
    doSendSubmit() {
      if (this.selectUsers.length === 0) {
        this.$notify({
          title: '请选择需要发送优惠券的用户',
          type: 'error',
          duration: 2500
        })
        return false;
      }
      let formData = {
        issueCouponId: this.issueCouponId,
        uids: this.selectUsers.join(",")
      }
      let formDataStr = JSON.stringify(formData);
      batchSendCoupon(formDataStr).then(res => {
        this.$notify({
          title: '操作成功',
          type: 'success',
          duration: 2500
        });
        this.isSendCoupon = false
      }).catch(err => {
        this.$notify({
          title: '操作失败' + err.response.data.msg,
          type: 'error',
          duration: 2500
        })
        console.log(err.response.data.msg)
      })
    }
  }
}
</script>
<style lang="scss">
 .el-transfer-panel {
  border: 1px solid #e6ebf5;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  display: inline-block;
  vertical-align: middle;
  width: 300px !important;
  max-height: 100%;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  position: relative;
}
</style>
