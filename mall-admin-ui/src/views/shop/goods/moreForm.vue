<template>
  <el-dialog
    class="more-form"
    :append-to-body="true"
    :close-on-click-modal="false"
    :before-close="cancel"
    :visible.sync="dialog"
    title="批量导入商品"
    width="900px">

    <el-steps :active="stepActive" simple class="upload-steps">
      <el-step title="下载模板" icon="el-icon-edit" class="upload-step"></el-step>
      <el-step title="确认导入" icon="el-icon-upload" class="upload-step"></el-step>
    </el-steps>

    <el-container class="upload-container">
      <el-header class="upload-header">1、下载商品资料模板</el-header>
      <el-main class="upload-main">
        <div class="upload-info">
          <ul class="upload-defined">
            <li>模板表格使用说明:</li>
            <li>批量导入商品是指通过excel模板，批量新建多个商品，上传成功后在待上架状态；</li>
            <li>批量修改已创建的商品数据时需填写已经存在商品ID；</li>
          </ul>
          <ul class="upload-tips">
            <li>1、添加新商品时，【商品ID】默认为空，系统支持自动生成唯一的商品ID；</li>
            <li>2、【商品名称】【商品单位】等所有字段内容不允许为空值，否则导入不成功；</li>
            <li>3、【商品系列】请准确填写商品所在的分类名称；</li>
            <li>4、【商品规格】、【商品成本价】、【商品零售价基数】、【商品库存】、【商品编码】、【包装规格】及不同组【商品型号】间以 '/' 反斜杠连接(如：商品成本价：1.00/2.00/3.00)，同组【商品型号】间以 ',' 英文状态下的逗号连接，【商品规格】及【商品型号】不能有特殊字符命名；</li>
            <li>5、【商品图片】之间以 '&*&' 字符进行连接(如：商品图片：0&*&0&*&0)；</li>
            <li>6、【商品规格】【商品型号】采用sku设置规则；</li>
            <li>7、【包装规格】采用如: 1件=12箱=144个 规则；</li>
            <li>8、表格编辑过程中禁止设置隐藏项；</li>
            <li>9、表格编辑结束空白区域不能有多余空格或内容，否则导入不成功，请删除空白区域重新尝试上传；</li>
            <li>10、文件后缀名必须为： xls、xlsx 及 csv，文件大小不得大于6M；</li>
            <li>11、折扣率使用说明：以零售价为计算基价，成本价默认为“1”时，前端零售价不变。</li>
            <li>a、如：零售价5.0元的商品，成本价默认设为“1”, 1.0倍，表述为100%。前端显示商品零售价为5.0元；</li>
            <li>b、成本价设为：“1”的时候，表述为100%。这时候零售价变化计算，批量上传时零售价可以任意填写，前端的价格不会变；</li>
            <li>c、如：成本价为“1.1”表述为110%。1.1倍计算零售价。前端的零售价为5.0元的商品，显示为5.5元的商品。</li>

          </ul>
        </div>
        <el-button type="warning" @click="downloadExcel" size="mini">下载</el-button>
      </el-main>

      <el-header class="upload-header">2、上传填写好的商品资料</el-header>
      <el-main class="upload-main upload-btn">
        <el-upload
          class="filter-item"
          :action="fileMoreUploadApi"
          :headers="headers"
          :on-error="uploadFalse"
          :on-success="uploadSuccess"
          :before-upload="beforeAvatarUpload"
          accept=".xlsx,.xls"
          :show-file-list="false"
          :file-list="fileList">
          <el-button  type="warning">上传</el-button>
        </el-upload>
        <span class="upload-tips">（请选择excel文件，一次最多上传 2000 条数据）</span>
      </el-main>
    </el-container>

    <el-form ref="form6" :inline="true" size="small" label-width="80px">

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button :loading="loading" type="primary" @click="cancel">取消</el-button>
      <!--<el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>-->
    </div>
  </el-dialog>
</template>

<script>
  import { getCates } from '@/api/yxStoreCategory'
  import Treeselect from '@riophae/vue-treeselect'
  import initData from '@/mixins/crud'
  import { getToken } from '@/utils/auth'
  import { mapGetters } from 'vuex'

  export default {
    mixins: [initData],
    components: { Treeselect },
    props: {
      isMoreAdd: {
        type: Boolean,
        required: true
      }
    },
    data() {
      return {
        headers: {
          Authorization: getToken()
        },
        loading: false, dialog: false, cates: [],
        moreInfo: [],
        stepActive: 0,
        uploadUrl: 'api/yxStoreProduct/upload',
        fileList: []
      }
    },
    watch: {

    },
    computed: {
      ...mapGetters([
        'fileMoreUploadApi'
      ])
    },
    methods: {
      cancel() {
        this.dialog = false;
      },
      doSubmit() {
        this.loading = true
      },
      getCates() {
        getCates({ enabled: true }).then(res => {
          this.cates = res.content
        })
      },
      beforeInit() {
        this.url = 'api/yxStoreProduct'
        const sort = 'id,desc'
        this.params = { page: this.page, size: this.size, sort: sort, isShow: 1, isDel: 0 , listContent: 'template'}
        const query = this.query
        const type = query.type
        const value = query.value
        if (type && value) { this.params[type] = value }
        return true
      },
      downloadExcel(){
        this.beforeInit();
        this.downloadMethod();
      },
      uploadSuccess(response, file, fileList) {
        if (file.status=="success") {
          this.$message({
            message: "商品信息批量导入成功，请上传商品图片后上架商品!",
            type: 'success'
          });
          this.cancel();
          this.$parent.init()
        } else {
          this.$message({
            message:  "商品信息批量导入失败!",
            type: 'error'
          });
        }
      },
      uploadFalse(response, file, fileList) {
        this.$message({
          message: JSON.parse(response.message).message,
          type: 'error'
        });
      },
      // 上传前对文件的大小的判断
      beforeAvatarUpload(file) {
        const extension = file.name.split(".")[1] === "xls";
        const extension2 = file.name.split(".")[1] === "xlsx";
        const isLt2M = file.size / 1024 / 1024 < 10;
        if (!extension && !extension2) {
          this.$message({
            message: '上传模板只能是 xls、xlsx格式!',
            type: 'error'
          });
        }
        if (!isLt2M) {
          this.$message({
            message: '上传模板大小不能超过 10MB!',
            type: 'error'
          });
        }
        return extension || extension2 || extension3 || (extension4 && isLt2M);
      },
    }
  }
</script>

<style scoped lang="scss">
.more-form {
  .el-step.is-simple .el-step__arrow::before{
    transform: rotate(-45deg) translateY(0px);
  }
  .el-step.is-simple .el-step__arrow::after {
    transform: rotate(45deg) translateY(10px);
  }
  .el-step.is-simple .el-step__arrow::before, .el-step.is-simple .el-step__arrow::after {
    height: 34px;
    width: 12px;
    background: #fff;
  }

  .upload-steps {
    padding: 6px 8%;
    .upload-step {

    }
  }

  .upload-container {
    .upload-header {
      height: auto !important;
      margin: 20px 0 15px;
      font-size: 16px;
      font-weight: 800;
      padding: 0;
    }
    .upload-main {
      margin: 0 30px;
      padding: 0;
      .upload-info {
        padding: 12px 20px;
        border: 1px solid #f1f1f1;
        margin-bottom: 10px;
       .upload-defined {
         list-style: none;
         margin: 0;
         padding: 0;
         margin-bottom: 20px;
         li {
           margin: 10px 0;
           color: #58666e;
           font-family: 'Hiragino Sans GB',Arial,"Microsoft YaHei","Source Sans Pro","Helvetica Neue",Helvetica,sans-serif;
         }
         li:nth-child(1) {
           margin-bottom: 20px;
         }
       }
        .upload-tips {
          list-style: none;
          margin: 0;
          padding: 0;
          li {
            line-height: 20px;
            color: white;
            color: #999!important;
          }
        }
      }
      button {
        padding: 7px 30px;
      }
    }
    .upload-btn {
      display: flex;
      align-items: flex-end;
    }
  }
}
</style>
