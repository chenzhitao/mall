<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增' : '编辑'" width="500px">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
      <div style="display: flex; padding-right: 10px;">
        <el-form-item label="用户昵称">
          <el-input v-model="form.nickname" :disabled="true" style="width: 150px;" />
        </el-form-item>
        <el-form-item label="会员等级">
          <el-select v-model="form.yxSystemUserLevel.id" @change="changeLevel" clearable>
            <el-option v-for="item in levelOptions" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
      </div>
      <el-form-item label="真实姓名">
        <el-input v-model="form.realName" style="width: 370px;" />
      </el-form-item>
      <el-form-item label="用户备注">
        <el-input v-model="form.mark" style="width: 370px;" />
      </el-form-item>
      <el-form-item label="手机号码">
        <el-input v-model="form.phone" style="width: 370px;" />
      </el-form-item>
<!--      <el-form-item label="用户积分">-->
<!--        <el-input v-model="form.integral" style="width: 370px;" />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="推广员">-->
<!--        <el-radio v-model="form.isPromoter" :label="1">开启</el-radio>-->
<!--        <el-radio v-model="form.isPromoter" :label="0">关闭</el-radio>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="商户管理">-->
<!--        <el-radio v-model="form.adminid" :label="1">开启</el-radio>-->
<!--        <el-radio v-model="form.adminid" :label="0">关闭</el-radio>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="资质材料">-->
<!--        <el-upload-->
<!--          :action="uploadApi"-->
<!--          :headers="headers"-->
<!--          list-type="picture-card"-->
<!--          :file-list="certInfoList"-->
<!--          multiple-->
<!--          :before-upload="beforeUpload"-->
<!--          :on-success="handleSuccess"-->
<!--          :on-preview="handlePictureCardPreview"-->
<!--          :on-remove="handleRemove"-->
<!--        >-->
<!--          <i class="el-icon-plus"></i>-->
<!--        </el-upload>-->
<!--        <el-dialog :visible.sync="dialogVisible">-->
<!--          <img width="100%" :src="dialogImageUrl" alt="">-->
<!--        </el-dialog>-->
<!--      </el-form-item>-->
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { add, edit } from '@/api/yxUser'
import { getLevels } from  '@/api/yxSystemUserLevel'
import { mapGetters } from 'vuex'
import { getToken } from '@/utils/auth'

export default {
  mixins: [],
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
        uid: '',
        account: '',
        pwd: '',
        realName: '',
        birthday: '',
        cardId: '',
        mark: '',
        partnerId: '',
        groupId: '',
        nickname: '',
        avatar: '',
        phone: '',
        addTime: '',
        addIp: '',
        lastTime: '',
        lastIp: '',
        nowMoney: '',
        brokeragePrice: '',
        integral: '',
        signNum: '',
        status: '',
        // level: '',
        yxSystemUserLevel:{},
        spreadUid: '',
        spreadTime: '',
        userType: '',
        isPromoter: 0,
        payCount: '',
        spreadCount: '',
        cleanTime: '',
        addres: '',
        adminid: 0,
        loginType: '',
        isCheck: 0,
        isPass: 0,
        applyLevel: 0,
        certInfo: []
      },
      rules: {},
      levelOptions: [],
      levelId : null,
      dialogImageUrl: '',
      dialogVisible: false,
      headers: {
        Authorization: getToken()
      },
      certInfoList: [],
    }
  },
  created() {
  },
  mounted(){
    getLevels({}).then(({ content })=>{
      this.levelOptions = content;
    })
    this.levelId = this.form.yxSystemUserLevel.id;
  },
  watch: {
    form:function(val){
      let certInfo = [];
      let certInfoList = [];
      if(val.certInfo != null){
        certInfo = val.certInfo.startsWith('[') ? JSON.parse(val.certInfo) : [val.certInfo];
      }

      for(let item of certInfo){
        if(item.indexOf('base64')){
          certInfoList.push({name:'',url:item});
        }else {
          certInfoList.push(item);
        }
      }
      this.certInfoList = certInfoList;
    },
  },
  computed: {
    ...mapGetters([
      'uploadApi'
    ])
  },
  methods: {
    handleSuccess(response, file, fileList) {
      const that = this;
      const certInfoList = [];
      for(const item of fileList){
        if(item.url != null){
          certInfoList.push(item.url);
        }else {
          certInfoList.push(item.response.link);
        }
      }
      that.form.certInfo = JSON.stringify(certInfoList);
      that.certInfoList.push(response.link);
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
    handleRemove(file, fileList) {
      const that = this;
      const certInfoList = [];
      for(const item of fileList){
        if(item.url != null){
          certInfoList.push(item.url);
        }else {
          certInfoList.push(item.response.link);
        }
      }
      that.form.certInfo = JSON.stringify(certInfoList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
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
    changeLevel(val){
      let levelId = this.levelId;
      if(val != levelId){
        this.form.isCheck = 1;
        this.form.isPass = 1;
      }else if(val == 1){
        this.form.isCheck = 1;
        this.form.isPass = 0;
      }else {
        this.form.isCheck = 0;
        this.form.isPass = 0;
      }
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
        uid: '',
        account: '',
        pwd: '',
        realName: '',
        birthday: '',
        cardId: '',
        mark: '',
        partnerId: '',
        groupId: '',
        nickname: '',
        avatar: '',
        phone: '',
        addTime: '',
        addIp: '',
        lastTime: '',
        lastIp: '',
        nowMoney: '',
        brokeragePrice: '',
        integral: '',
        signNum: '',
        status: '',
        // level: '',
        yxSystemUserLevel:{},
        spreadUid: '',
        spreadTime: '',
        userType: '',
        isPromoter: '',
        payCount: '',
        spreadCount: '',
        cleanTime: '',
        addres: '',
        adminid: '',
        loginType: ''
      }
    },
    beforeInit() {
      this.url = 'api/yxSystemUserLevel'
      const sort = 'id,desc'
      this.params = { page: this.page, size: this.size, sort: sort}
      const query = this.query
      const type = query.type
      const value = query.value
      if (type && value) { this.params[type] = value }
      return true
    },

  }
}
</script>

<style scoped>
  /deep/ .el-upload-list--picture-card .el-upload-list__item {
    width: 60px;
    height: 60px;
  }
  /deep/ .el-upload--picture-card {
    width: 60px;
    height: 60px;
    line-height:66px;
  }
</style>
