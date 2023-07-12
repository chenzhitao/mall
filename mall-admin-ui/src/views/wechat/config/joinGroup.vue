<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="150px">
      <el-form-item label="群二维码">
        <MaterialList
          v-model="form.imageArr"
          style="width: 738px"
          type="image"
          :num="1"
          :width="150"
          :height="150"
        />
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
import { Message } from 'element-ui'
import MaterialList from "@/components/material";
import {mapGetters} from "vuex";
export default {
  components: { eForm, MaterialList },
  mixins: [initData],
  data() {
    return {
      delLoading: false,
      form: {
        imageArr:[],
        store_join_group: ''
      },
      uploadUrl: "",
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
        if(keyName==='store_join_group'){
          that.form['store_join_group']=newValue
          let imgArray=[];
          imgArray.push(newValue);
          that.form.imageArr=imgArray;
        }
      })
    })
  },
  methods: {
    checkPermission,
    doSubmit() {
      if(!this.form.imageArr){
        Message({ message: '请先上传群二维码', type: 'warning' });
        return false;
      }
      this.form.store_join_group=this.form.imageArr[0];
      add(this.form).then(res => {
        Message({ message: '设置成功', type: 'success' })
      }).catch(err => {
        // this.loading = false
        console.log(err.response.data.message)
      })
    }



  }
}
</script>

<style lang="scss">
.ql-editor {
  height: 350px;
}


</style>
