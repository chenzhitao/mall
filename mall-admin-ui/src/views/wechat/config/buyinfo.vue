<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="150px">
      <el-form-item label="购物提示">
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
          v-model="form.store_buy_info"
          ref="myQuillEditor"
          :options="editorOption"
          @blur="onEditorBlur($event)"
          @focus="onEditorFocus($event)"
          @change="onEditorChange($event)"
        >
        </quill-editor>
      </el-form-item>
      <el-form-item label="">
        <el-button type="primary" @click="doSubmit">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import checkPermission from '@/utils/permission'
import initData from '@/mixins/crud'
import { del, add, get } from '@/api/yxSystemConfig'
import eForm from './form'
import picUpload from '@/components/pic-upload'
import { Message } from 'element-ui'
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
import {mapGetters} from "vuex";
export default {
  components: { eForm, picUpload,quillEditor },
  mixins: [initData],
  data() {
    return {
      delLoading: false,
      form: {
        store_postage: '',
        store_free_postage: '',
        store_buy_info: ''
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
    get().then(rese => {
      const that = this
      rese.content.map(function(key, value) {
        const keyName = key.menuName
        const newValue = key.value
        if(keyName in that.form){
          that.form[keyName] = newValue
        }
      })
    })
  },
  methods: {
    checkPermission,
    doSubmit() {
      add(this.form).then(res => {
        Message({ message: '设置成功', type: 'success' })
      }).catch(err => {
        // this.loading = false
        console.log(err.response.data.message)
      })
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
    }, // 内容改变事件


  }
}
</script>

<style lang="scss">
.ql-editor {
  height: 350px;
}


</style>
