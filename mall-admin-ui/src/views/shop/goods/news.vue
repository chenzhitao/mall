<template>
  <el-dialog
    :append-to-body="true"
    :close-on-click-modal="false"
    :before-close="cancel"
    :visible.sync="dialog"
    :title="isAdd ? '新增' : '编辑'"
    width="900px"
  >
    <el-form
      ref="form"
      :model="form"
      :inline="true"
      :rules="rules"
      size="small"
      
      label-width="80px"
    >
      <el-form-item label="商品分类" prop="storeCategory.id">
        <treeselect
          v-model="form.storeCategory.id"
          :options="cates"
          style="width: 738px"
          placeholder="选择商品分类"
        />
      </el-form-item>
      <el-form-item label="商品名称" prop="storeName">
        <el-input v-model="form.storeName" style="width: 738px" />
      </el-form-item>
      <el-form-item label="关键字" prop="keyword">
        <el-input v-model="form.keyword" style="width: 738px" />
      </el-form-item>

      <!--<el-form-item label="产品条码">
        <el-input v-model="form.barCode" />
      </el-form-item>-->
      <el-form-item label="商品图片"  prop="imageArr">
        <MaterialList
          v-model="form.imageArr"
          style="width: 738px"
          type="image"
          :num="1"
          :width="150"
          :height="150"
        />
      </el-form-item>
      <el-form-item label="轮播图"  prop="sliderImageArr">
        <MaterialList
          v-model="form.sliderImageArr"
          style="width: 738px"
          type="image"
          :num="5"
          :width="150"
          :height="150"
        />
      </el-form-item>
      <el-form-item label="商品简介" prop="storeInfo">
        <el-input
          v-model="form.storeInfo"
          style="width: 738px"
          rows="5"
          type="textarea"
        />
      </el-form-item>
      <el-form-item label="产品描述" prop="description">
        <!-- <editor v-model="form.description" style="width: 738px;"/> -->
        <!-- <editors v-model="form.description" style="width: 738px;"/> -->

        <el-upload
          class="avatar-uploader2"
          :action="uploadUrl"
          :show-file-list="false"
          :on-exceed="handleExceed"
          :on-success="uploadSuccess"
          :on-error="uploadError"
          :before-upload="beforeUpload" 
        >
        </el-upload>

      <!-- <div    class="avatar-uploader2" @click="saveCommodity">
        <MaterialList
          v-model="form.imageArr8"
         :listDialogVisible="listDialogVisible"
          style="width: 738px"
          :type="disImgs"
          :num="5"
          :width="150"
          :height="150"
        />
          </div> -->


        <quill-editor
          v-model="form.description"
          ref="myQuillEditor"
          :options="editorOption"
          @blur="onEditorBlur($event)"
          @focus="onEditorFocus($event)"
          @change="onEditorChange($event)"
        >
        </quill-editor>
      </el-form-item>
      <!--<el-form-item label="商品价格">
        <el-input v-model="form.price" />
      </el-form-item>
      <el-form-item label="市场价">
        <el-input v-model="form.otPrice" />
      </el-form-item>
      <el-form-item label="成本价">
        <el-input v-model="form.cost" />
      </el-form-item>-->
      <el-form-item label="邮费">
        <el-input v-model="form.postage" />
      </el-form-item>
      <el-form-item label="排序">
        <el-input v-model="form.sort" />
      </el-form-item>
      <el-form-item label="销量">
        <el-input v-model="form.sales" />
      </el-form-item>
      <!--<el-form-item label="库存">
        <el-input v-model="form.stock" />
      </el-form-item>
      <el-form-item label="单位名称">
        <el-input v-model="form.unitName" />
      </el-form-item>-->
      <el-form-item label="热卖单品">
        <el-radio v-model="form.isHot" :label="1">是</el-radio>
        <el-radio v-model="form.isHot" :label="0" style="width: 200px"
          >否</el-radio
        >
      </el-form-item>
      <el-form-item label="促销单品">
        <el-radio v-model="form.isBenefit" :label="1">是</el-radio>
        <el-radio v-model="form.isBenefit" :label="0" style="width: 200px"
          >否</el-radio
        >
      </el-form-item>
      <el-form-item label="为您推荐">
        <el-radio v-model="form.isBest" :label="1">是</el-radio>
        <el-radio v-model="form.isBest" :label="0" style="width: 200px"
          >否</el-radio
        >
      </el-form-item>
      <el-form-item label="首发新品">
        <el-radio v-model="form.isNew" :label="1">是</el-radio>
        <el-radio v-model="form.isNew" :label="0" style="width: 200px"
          >否</el-radio
        >
      </el-form-item>
      <el-form-item label="是否包邮">
        <el-radio v-model="form.isPostage" :label="1">是</el-radio>
        <el-radio v-model="form.isPostage" :label="0" style="width: 200px"
          >否</el-radio
        >
      </el-form-item>
      <el-form-item label="优品推荐">
        <el-radio v-model="form.isGood" :label="1">是</el-radio>
        <el-radio v-model="form.isGood" :label="0" style="width: 200px"
          >否</el-radio
        >
      </el-form-item>
      <el-form-item label="获得积分">
        <el-input v-model="form.giveIntegral" />
      </el-form-item>
      <el-form-item label="虚拟销量">
        <el-input v-model="form.ficti" />
      </el-form-item>
      <el-form-item label="包装规格">
        <el-input
          v-model="form.packaging"
          placeholder="请填写商品最小包装规格，如：个、袋"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit('form')"
        >{{isAdd ? '下一步' : '确认'}}</el-button
      >
    </div>
    <eAttr v-if="eattrInfo.visible" :dialogInfo="eattrInfo" @backInfo="init" /> 
  </el-dialog>
    
</template>

<script>
const toolbarOptions = [
  ["bold", "italic", "underline", "strike"], // toggled buttons
  ["blockquote", "code-block"],

  [{ header: 1 }, { header: 2 }], // custom button values
  [{ list: "ordered" }, { list: "bullet" }],
  [{ script: "sub" }, { script: "super" }], // superscript/subscript
  [{ indent: "-1" }, { indent: "+1" }], // outdent/indent
  [{ direction: "rtl" }], // text direction

  [{ size: ["small", false, "large", "huge"] }], // custom dropdown
  [{ header: [1, 2, 3, 4, 5, 6, false] }],

  [{ color: [] }, { background: [] }], // dropdown with defaults from theme
  [{ font: [] }],
  [{ align: [] }],
  ["link", "image", "video"],
  ["clean"], // remove formatting button
];
import { getCates } from "@/api/yxStoreCategory";
import { add, edit } from "@/api/yxStoreProduct";
import editor from "../../components/Editor";
import editors from "../../components/editors";
import picUpload from "@/components/pic-upload";
import mulpicUpload from "@/components/mul-pic-upload";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import MaterialList from "@/components/material";
import Editors from "../../components/editors.vue"; 

import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
import { quillEditor } from "vue-quill-editor";

import eAttr from './attr'
export default {
  components: {
    editor,
    picUpload,
    mulpicUpload,
    Treeselect,
    MaterialList,
    quillEditor,
    editors,
    eAttr
  },
  props: {
    isAdd: {
      type: Boolean,
      required: true,
    }, 
  },
  data() {
    return {
       eattrInfo:{
            visible:false,
            data:{}
        },
      loading: false,
      dialog: false,
      disImgs:"null",    
      listDialogVisible:false, 
      cates: [],
      uploadUrl: "http://apishop.huijusz.com/api/upload",
        // uploadUrl: "http://apiwx.huijusz.com/api/upload",
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
                if (value) {         ;
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
      form: {
        id: "",
        merId: 0,
        image: "",
        sliderImage: "",

        imageArr: [],
        sliderImageArr: [],
        storeName: "",
        storeInfo: "",
        keyword: "",
        barCode: "",
        cateId: 1,
        storeCategory: { id: null },
        price: 0,
        vipPrice: 0,
        otPrice: 0,
        postage: 0,
        unitName: "",
        packaging: "",
        sort: 0,
        sales: 0,
        stock: 0,
        isShow: 1,
        isHot: 0,
        isBenefit: 0,
        isBest: 0,
        isNew: 0,
        description: "",
        addTime: "",
        isPostage: 0,
        isDel: 0,
        merUse: 0,
        giveIntegral: 0,
        cost: 0,
        isSeckill: 0,
        isBargain: 0,
        isGood: 0,
        ficti: 0,
        browse: 0,
        codePath: "",
        soureLink: "",
      },
      rules: {
        "storeCategory.id":[
            { required: true, message: "请选择商品分类", trigger: "blur" },
        ],
         storeName:[
            { required: true, message: "请输入商品名称", trigger: "blur" },
        ],
         keyword:[
            { required: true, message: "请输入关键字", trigger: "blur" },
        ],
         imageArr:[
            { required: true, message: "请添加商品图片", trigger: "blur" },
        ], 
         sliderImageArr:[
            { required: true, message: "请添加轮播图片", trigger: "blur" },
        ],
         storeInfo:[
            { required: true, message: "请输入商品简介", trigger: "blur" },
        ],
        description:[
           { required: true, message: "请完善产品详情", trigger: "blur" },
        ]
      },
    };
  },
  watch: {
    "form.imageArr": function (val) {
      if (val) {
        this.form.image = val.join(",");
      }
    },
    "form.sliderImageArr": function (val) {
      if (val) {
        this.form.sliderImage = val.join(",");
      }
    },
  },
  computed: {
    editor() {
      return this.$refs.myQuillEditor.quill;
    },
  },
   created() {
    this.$nextTick(() => {
      // this.init()
    })
  },

  methods: {
    saveCommodity() {
      console.log(123)
      this.disImgs = "image";
    
      this.listDialogVisible = true
        console.log(this.listDialogVisible,'--------------------')
      console.log(123, this.disImgs )
    },

    uploadError(err) {
      console.log(err);
    },
    handleExceed(res, file){
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
    onEditorBlur() {}, // 失去焦点事件
    onEditorFocus() {}, // 获得焦点事件
    onEditorChange(e) {
      console.log(e, "图片666");
    }, // 内容改变事件

    cancel() {
      this.resetForm();
    },
    doSubmit(ruleForm) {
         this.$refs[ruleForm].validate((valid) => {
           if(valid ) {
               this.loading = true;
                if (this.isAdd) {
                  this.doAdd();
                } else this.doEdit();
           }
               
         })
    
    },
    doAdd() {
      add(this.form)
        .then((res) => {
          this.resetForm();
          this.$notify({
            title: "添加成功",
            type: "success",
            duration: 2500,
          });
            this.eattrInfo.data=res;
            this.eattrInfo.visible=true;
            // console.log(res,'---------返回数据')
          this.loading = false;
          this.$parent.init();
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    doEdit() {
      let that = this
      edit(this.form)
        .then((res) => {
          this.resetForm();
          this.$notify({
            title: "修改成功",
            type: "success",
            duration: 2500,
          });
          //   console.log(that.form,'------------数据')
          //   console.log( res,'--------------修改')
          //  this.eattrInfo.data=that.form;
          //   this.eattrInfo.visible=true;
          this.loading = false;
          this.$parent.init();
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    resetForm() {
      this.dialog = false;
      this.$refs["form"].resetFields();
      this.form = {
        id: "",
        merId: 0,
        image: "",
        sliderImage: "",
        imageArr: [],
        sliderImageArr: [],
        storeName: "",
        storeInfo: "",
        keyword: "",
        barCode: "",
        cateId: 1,
        storeCategory: {},
        price: 0,
        vipPrice: 0,
        otPrice: 0,
        postage: 0,
        unitName: "",
        packaging: "",
        sort: 0,
        sales: 0,
        stock: 0,
        isShow: 1,
        isHot: 0,
        isBenefit: 0,
        isBest: 0,
        isNew: 0,
        description: "",
        addTime: "",
        isPostage: 0,
        isDel: 0,
        merUse: 0,
        giveIntegral: 0,
        cost: 0,
        isSeckill: 0,
        isBargain: 0,
        isGood: 0,
        ficti: 0,
        browse: 0,
        codePath: "",
      };
    },
    getCates() {
      getCates({ enabled: true }).then((res) => {
        this.cates = res.content;
      });
    },
  },
};
</script>

<style scoped>
</style>
