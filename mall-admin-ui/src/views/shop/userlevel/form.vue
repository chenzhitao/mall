<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增' : '编辑'" width="600px">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="100px">
      <el-form-item label="等级名称">
        <el-input v-model="form.name" style="width: 370px;" />
      </el-form-item>
      <el-form-item label="是否永久">
        <el-radio v-model="form.isForever" :label="1">是</el-radio>
        <el-radio v-model="form.isForever" :label="0" style="width: 200px;">否</el-radio>
      </el-form-item>
      <el-form-item label="有效时间(天)">
        <el-input v-model="form.validDate" style="width: 370px;" />
      </el-form-item>
      <el-form-item label="会员等级">
        <el-input v-model="form.grade" style="width: 370px;" />
      </el-form-item>
      <el-form-item label="购买价格">
        <el-input v-model="form.money" style="width: 370px;" />
      </el-form-item>
      <el-form-item label="享受折扣">
        <el-input v-model="form.discount" style="width: 370px;" />
      </el-form-item>
      <el-form-item label="会员背景">
        <MaterialList v-model="form.imageArr" style="width: 300px" type="image" :num="1" :width="150" :height="150" />
      </el-form-item>
      <el-form-item label="会员图标">
        <MaterialList v-model="form.iconArr" style="width: 300px" type="image" :num="1" :width="150" :height="150" />
      </el-form-item>
      <el-form-item label="是否显示">
        <el-radio v-model="form.isShow" :label="1">是</el-radio>
        <el-radio v-model="form.isShow" :label="0" style="width: 200px;">否</el-radio>
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
          v-model="form.explain"
          ref="myQuillEditor"

          :options="editorOption"
          @blur="onEditorBlur($event)"
          @focus="onEditorFocus($event)"
          @change="onEditorChange($event)"
        >
        </quill-editor>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { add, edit } from '@/api/yxSystemUserLevel'
import picUpload from '@/components/pic-upload'
import MaterialList from '@/components/material'
import {mapGetters} from "vuex";
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
import Treeselect from "@riophae/vue-treeselect";
export default {
  components: { picUpload, MaterialList ,quillEditor,fileUpload},
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
        merId: '',
        name: '',
        money: '',
        validDate: '',
        isForever: 1,
        isPay: '',
        isShow: 1,
        grade: '',
        discount: '',
        image: '',
        icon: '',
        imageArr: [],
        iconArr: [],
        explain: '',
        addTime: '',
        isDel: ''
      },
      rules: {
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
      }
    }
  },
  watch: {
    'form.imageArr': function(val) {
      if (val) {
        this.form.image = val.join(',')
      }
    },
    'form.iconArr': function(val) {
      if (val) {
        this.form.icon = val.join(',')
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
        merId: '',
        name: '',
        money: '',
        validDate: '',
        isForever: 1,
        isPay: '',
        isShow: 1,
        grade: '',
        discount: '',
        image: '',
        icon: '',
        imageArr: [],
        iconArr: [],
        explain: '',
        addTime: '',
        isDel: ''
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
