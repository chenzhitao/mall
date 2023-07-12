<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增' : '编辑'" width="700px">
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
       <!-- <el-form-item  label="优惠券开启时间">
        <template>
          <el-date-picker
            v-model="form.startTimeDate"
            type="datetime"
            placeholder="选择日期时间"
          />
        </template>
      </el-form-item> -->


      <el-form-item label="优惠券类型" v-show="isAdd">
        <el-radio v-model="form.type" :label="0">领取券(需要用户手动领取)</el-radio>
        <el-radio v-model="form.type" :label="1">推送券(定向推送用户)</el-radio>
      </el-form-item>


      <!-- didi -->
     <!-- <el-form-item label="优惠券ID">
        <el-input v-model="form.cid" style="width: 300px;" :disabled="true" />
      </el-form-item>
      <el-form-item label="优惠券名称">
        <el-input v-model="form.cname" style="width: 300px;" :disabled="true" />
      </el-form-item> -->

      <div v-if = isAdd>

        <el-form-item label="优惠券有效期类型">
          <el-radio v-model="form.timeType" :label="0">固定有效期</el-radio>
          <el-radio v-model="form.timeType" :label="1">静态有效期</el-radio>
        </el-form-item>
        <el-form-item label="优惠券有效期限" v-show="form.timeType===1">
          <el-input-number v-model="form.timeNum" :min="0" controls-position="right" style="width: 300px;" />
          <br><span>设置用户自领取后多少天内有效（比如说7天内有效）。</span>
        </el-form-item>
      <el-form-item  label="优惠券开启时间" v-show="form.timeType===0">
        <template>
          <el-date-picker
            v-model="form.startTimeDate"
            type="date"
            placeholder="选择日期时间"
          />
        </template>
      </el-form-item>
      <el-form-item label="优惠券结束时间" v-show="form.timeType===0">
        <template>
          <el-date-picker
            v-model="form.endTimeDate "
            type="date"
            placeholder="选择日期时间"
             value-format="yyyy-MM-dd"
            dateType="time"
          />
        </template>
      </el-form-item>
      <el-form-item label="发布数量">
        <el-input-number v-model="form.totalCount" :min="1" controls-position="right" style="width: 300px;" />
      </el-form-item>
      <!-- <el-form-item label="是否发布">
        <el-radio v-model="form.issueStatus" :label="1">开启</el-radio>
        <el-radio v-model="form.issueStatus" :label="0">关闭</el-radio>
      </el-form-item> -->

        <el-form-item style="margin-bottom: 0;" label="限定商品分类" prop="categoryId">
          <treeselect v-model="form.categoryId" :options="depts" style="width: 300px;" placeholder="选择上级分类"/>
        </el-form-item>
        <el-form-item label="">
        </el-form-item>
        <el-form-item label="优惠券描述">
          <el-upload
            class="avatar-uploader2"
            :action="uploadUrl"
            :show-file-list="false"
            :on-exceed="handleExceed"
            :on-success="uploadSuccess"
            :on-error="uploadError"
            :before-upload="beforeUpload"
            multiple
          >
          </el-upload>
          <quill-editor
            v-model="form.remark"
            ref="myQuillEditor"

            :options="editorOption"
            @blur="onEditorBlur($event)"
            @focus="onEditorFocus($event)"
            @change="onEditorChange($event)"
          >
          </quill-editor>
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model="form.sort" style="width: 300px;" />
        </el-form-item>

        <el-form-item label="是否开启">
          <el-radio v-model="form.status" :label="1">开启</el-radio>
          <el-radio v-model="form.status" :label="0">关闭</el-radio>
        </el-form-item>
      </div>
      <div v-else >

           <el-form-item  label="优惠券开启时间">
        <template>
          <el-date-picker
            v-model="form.startTime"
              value-format="timestamp"
            type="date"
            placeholder="选择日期时间"
          />
        </template>
      </el-form-item>

          <el-form-item label="优惠券结束时间"   prop="couponTime"  >
        <template>
          <el-date-picker
            v-model="form.couponTime"
            type="date"
             value-format="timestamp"
            placeholder="选择日期时间"
          />
        </template>
      </el-form-item>
        <el-form-item label="优惠券描述">
          <el-upload
            class="avatar-uploader2"
            :action="uploadUrl"
            :show-file-list="false"
            :on-exceed="handleExceed"
            :on-success="uploadSuccess"
            :on-error="uploadError"
            :before-upload="beforeUpload"
            multiple
          >
          </el-upload>
          <quill-editor
            v-model="form.remark"
            ref="myQuillEditor"

            :options="editorOption"
            @blur="onEditorBlur($event)"
            @focus="onEditorFocus($event)"
            @change="onEditorChange($event)"
          >
          </quill-editor>
        </el-form-item>

      </div>


    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { add, edit ,adds} from '@/api/yxStoreCoupon'
import { formatTime ,  formatDate } from '@/utils/index'
import crudDept from "@/api/yxStoreCategory";
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {mapGetters} from "vuex";
import picUpload from '@/components/pic-upload'
const toolbarOptions = [
  ["bold", "italic", "underline", "strike"], // toggled buttons
  ["blockquote", "code-block"],

  [{header: 1}, {header: 2}], // custom button values
  [{list: "ordered"}, {list: "bullet"}],
  [{script: "sub"}, {script: "super"}], // superscript/subscript
  [{indent: "-1"}, {indent: "+1"}], // outdent/indent
  [{direction: "rtl"}], // text direction

  [{size: ["small", false, "large", "huge"]}], // custom dropdown
  [{header: [1, 2, 3, 4, 5, 6, false]}],

  [{color: []}, {background: []}], // dropdown with defaults from theme
  [{font: []}],
  [{align: []}],
  ["link", "image", "video"],
  ["clean"], // remove formatting button
];
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
import {quillEditor} from "vue-quill-editor";
import fileUpload from '@/components/file-upload'
export default {
  components:{Treeselect,quillEditor,fileUpload,picUpload},
  props: {
    isAdd: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      loading: false, dialog: false,
      depts:[],
      form: {
        id: '',
        title: '',
        integral: 0,
        couponPrice: 0,
        useMinPrice: 0,
        couponTime: null,
        type: 0,
        sort: 0,
        status: 1,
        addTime: null,
        issueStatus:1,
        isPermanent:0,
        categoryId:null,
        remark:null,
        timeType:0,
        timeNum:0,
        startTimeDate:null,
        endTimeDate:null
      },
      uploadUrl: "http://apiwx.huijusz.com/api/upload",
      editorOption: {
        // modules: {
        //工具栏定义的
        // toolbar: toolbarOptions
        // },
        // disImgs : "null",

        placeholder: "",
        theme: "snow", // or ‘bubble’
        modules: {
          toolbar: {
            container: toolbarOptions, // 工具栏
            handlers: {
              image: function (value) {
                if (value) {
                  ;
                  // console.log(disImgs);
                  document.querySelector(".avatar-uploader2 input").click();
                } else {
                  this.quill.format("image", false);
                }
              },
            },
          },
        },
      },
      rules: {
        couponTime:[
           { required: true, message: '请输入时间', trigger: 'blur' }
        ],
        totalCount:[
           { required: true, message: '请输入优惠券数量', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'baseApi',
      'uploadUrl'
    ]),
    editor() {
      return this.$refs.myQuillEditor.quill;
    },
  },
  created() {
    const baseUrl = process.env.VUE_APP_BASE_API
    this.uploadUrl = baseUrl + "/api/upload";
    crudDept.getCates({isShow: true}).then(res => {
      this.depts = []
      const dept = {id: 0, label: '顶级类目', children: []}
      dept.children = res.content
      this.depts.push(dept)
    })
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
      this.form.totalCount = parseInt(  this.form.totalCount )
        console.log(this.form)
      adds(this.form).then(res => {
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

      // if( this.form.couponTime  != "") {
           this.form.totalCount = parseInt(  this.form.totalCount )
          this.form.couponTime =   this.form.couponTime /1000
           this.form.startTime =   this.form.startTime /1000
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
      // }else {
      //   this.$message.warning('结束时间不能为空')
      // }

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
        couponTime: null,
        sort: 0,
        type: 0,
        status: 1,
         startTime: null,
        endTime: '',
        totalCount: 0,
        remainCount: '',
        isPermanent: 0,
        issueStatus: 1,
        isDel: '',
        addTime: null,
        categoryId: null,
        timeType:0,
        timeNum:0,
        startTimeDate:null,
        endTimeDate:null
      }
    },
    uploadError(err) {
      console.log(err);
    },
    handleExceed(res, file) {
      this.$message.warning(`当前限制选择 3 个文件，`);
    },
    beforeUpload() {

    },
    uploadSuccess(res, file) {
      // res为图片服务器返回的数据
      // 获取富文本组件实例
      let quill = this.$refs.myQuillEditor.quill;
      // 如果上传成功
      if (res.errno == "0") {
        // 获取光标所在位置
        let length = quill.getSelection().index;
        // 插入图片  res.info为服务器返回的图片地址
        quill.insertEmbed(length, "image", res.link);
        // 调整光标到最后
        quill.setSelection(length + 1);
      } else {
        this.$message.error("图片插入失败");
      }
      // loading动画消失
      this.quillUpdateImg = false;
    },

    onEditorReady(editor) {
      // 准备编辑器
    },
    onEditorBlur() {
    }, // 失去焦点事件
    onEditorFocus() {
    }, // 获得焦点事件
    onEditorChange(e) {
      console.log(e, "图片666");
    }
  }
}
</script>

<style  lang="scss">
.ql-editor {
  height: 200px;
}
</style>
