<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增' : '编辑'" width="500px">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="130px">
      <el-form-item label="优惠券名称">
        <el-input v-model="form.title" style="width: 300px;" />
      </el-form-item>
      <el-form-item label="优惠券面值">
        <el-input v-model="form.couponPrice" style="width: 300px;" />
      </el-form-item>
      <el-form-item label="优惠券最低消费">
        <el-input v-model="form.useMinPrice" style="width: 300px;" />
      </el-form-item>
      <!-- <el-form-item label="优惠券有效期限(天)">
        <el-input v-model="form.couponTime" style="width: 300px;" />
      </el-form-item> -->
      <el-form-item label="优惠券类型" v-show="isAdd">
        <el-radio v-model="form.type" :label="0">领取券(需要用户手动领取)</el-radio>
        <el-radio v-model="form.type" :label="1">推送券(定向推送用户)</el-radio>
      </el-form-item>
      <el-form-item label="排序">
        <el-input v-model="form.sort" style="width: 300px;" />
      </el-form-item>
      <el-form-item label="状态">
        <el-radio v-model="form.status" :label="1">开启</el-radio>
        <el-radio v-model="form.status" :label="0">关闭</el-radio>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { add, edit } from '@/api/yxStoreCoupon'

export default {
  props: {
    isAdd: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      loading: false, dialog: false,
      form: {
        id: '',
        title: '',
        integral: 0,
        couponPrice: 0,
        useMinPrice: 0,
        couponTime: 1,
        sort: 0,
        type: 0,
        status: 1,
        addTime: '',
        startTime: '',
        endTime: '',
        totalCount: '',
        remainCount: '',
        isPermanent: '',
        issueStatus: '',
        categoryId:null
        // isDel: 0
      },
      editFlag:false,
      rules: {
      }
    }
  },
  methods: {
    cancel() {
      this.resetForm()
    },
    doSubmit() {
      this.loading = true
      if (this.isAdd) {
        this.doAdd()
      } else this.doEdit()
    },
    doAdd() {
      if (!this.form.totalCount || this.form.totalCount <=0) {
        this.$notify({
          title: '请填写发布数量',
          type: 'error',
          duration: 2500
        })
        return false;
      }
      add(this.form).then(res => {
        this.resetForm()
        this.$notify({
          title: '添加成功',
          type: 'success',
          duration: 2500
        })
        this.loading = false
        this.$parent.init()
      }).catch(err => {
        this.loading = false
        console.log(err.response.data.message)
      })
    },
    doEdit() {
      edit(this.form).then(res => {
        this.resetForm()
        this.$notify({
          title: '修改成功',
          type: 'success',
          duration: 2500
        })
        this.loading = false
        this.$parent.init()
      }).catch(err => {
        this.loading = false
        console.log(err.response.data.message)
      })
    },
    resetForm() {
      this.dialog = false
      this.$refs['form'].resetFields()
      this.form = {
        id: '',
        title: '',
        integral: 0,
        couponPrice: 0,
        useMinPrice: 0,
        couponTime: 1,
        type: 0,
        sort: 0,
        status: 1,
        addTime: '',
        categoryId:null
      }
    }
  }
}
</script>

<style scoped>

</style>
