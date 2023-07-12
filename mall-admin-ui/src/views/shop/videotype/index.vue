<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="类型名称" prop="typeName">
            <el-input v-model="form.typeName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="form.remark" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="是否展示">
            <el-radio v-model="form.showFlag" label="Y">是</el-radio>
            <el-radio v-model="form.showFlag" label="N" style="width: 200px" >否</el-radio
            >
          </el-form-item>
          <el-form-item label="排序">
            <el-input-number v-model="form.sortNo" style="width: 370px;" controls-position="right"/>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column v-if="columns.visible('id')" prop="id" label="编号" />
        <el-table-column v-if="columns.visible('typeName')" prop="typeName" label="类型名称" />
        <el-table-column v-if="columns.visible('remark')" prop="remark" label="备注" />
        <el-table-column v-if="columns.visible('showFlag')" prop="showFlag" label="是否展示" >
          <template slot-scope="scope">{{scope.row.showFlag==='Y'?'是':'否'}}</template>
        </el-table-column>
        <el-table-column v-if="columns.visible('sortNo')" prop="sortNo" label="排序" />
        <el-table-column v-permission="['admin','yxVideoType:edit','yxVideoType:del']" label="操作" width="120px" align="right">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
        <el-table-column  prop="sortNo" label="" width="100" align="left">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="success"
              @click="copyrouter(scope.row)"
            >复制路由
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudYxVideoType from '@/api/yxVideoType'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '视频分类', url: 'api/yxVideoType', sort: 'sortNo,asc', crudMethod: { ...crudYxVideoType }})
const defaultForm = { id: null, typeName: null, remark: null, showFlag: 'Y', sortNo: null }
export default {
  name: 'YxVideoType',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      permission: {
        add: ['admin', 'yxVideoType:add']
      },
      rules: {
        typeName: [
          {required: true, message: "请填写类型名称", trigger: "blur"},
        ],
      }
    }
  },
  created() {
    this.crud.optShow.del = false
    this.crud.optShow.edit = false
    this.crud.optShow.download = false
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },

    copyrouter(row) {
      console.log(row, '-----------id')
      let url = ' pages/kitchen/index?id=' + row.id;
      let oInput = document.createElement('input');
      oInput.value = url;
      document.body.appendChild(oInput);
      oInput.select(); // 选择对象;
      console.log(oInput.value)
      document.execCommand("Copy"); // 执行浏览器复制命令
      this.$message({
        message: '复制成功',
        type: 'success'
      });
      oInput.remove()
    },
  }
}
</script>

<style scoped>

</style>
