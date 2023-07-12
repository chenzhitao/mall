<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="id">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="模板ID">
            <el-input v-model="form.templateId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="商品ID">
            <el-input v-model="form.productId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="商品名称">
            <el-input v-model="form.productName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="form.description" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="排序编号">
            <el-input v-model="form.sortNo" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="添加时间">
            <el-input v-model="form.addTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="是否参与排名  0否  1是">
            未设置字典，请手动设置 Select
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
        <el-table-column v-if="columns.visible('id')" prop="id" label="id" />
        <el-table-column v-if="columns.visible('templateId')" prop="templateId" label="模板ID" />
        <el-table-column v-if="columns.visible('productId')" prop="productId" label="商品ID" />
        <el-table-column v-if="columns.visible('productName')" prop="productName" label="商品名称" />
        <el-table-column v-if="columns.visible('description')" prop="description" label="备注" />
        <el-table-column v-if="columns.visible('sortNo')" prop="sortNo" label="排序编号" />
        <el-table-column v-if="columns.visible('addTime')" prop="addTime" label="添加时间" />
        <el-table-column v-if="columns.visible('rankFlag')" prop="rankFlag" label="是否参与排名  0否  1是" />
        <el-table-column v-permission="['admin','yxProductTemplateItem:edit','yxProductTemplateItem:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudYxProductTemplateItem from '@/api/yxProductTemplateItem'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

// crud交由presenter持有
const defaultCrud = CRUD({ title: 'api', url: 'api/yxProductTemplateItem', sort: 'id,desc', crudMethod: { ...crudYxProductTemplateItem }})
const defaultForm = { id: null, templateId: null, productId: null, productName: null, description: null, sortNo: null, addTime: null, rankFlag: null }
export default {
  name: 'YxProductTemplateItem',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      permission: {
        add: ['admin', 'yxProductTemplateItem:add'],
        edit: ['admin', 'yxProductTemplateItem:edit'],
        del: ['admin', 'yxProductTemplateItem:del']
      },
      rules: {
      }    }
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }
  }
}
</script>

<style scoped>

</style>
