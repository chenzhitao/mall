<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="标题">
            <el-input v-model="form.title" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="form.description" style="width: 370px;" type="textarea"/>
          </el-form-item>
          <el-form-item label="模板分类"  >
            <el-select  v-model="form.type" style="width: 200px;">
              <el-option value="1" label="普通模板"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="关联商品">
            <el-input v-model="form.productName" style="width: 200px;" />&nbsp;&nbsp;<el-button   type="primary" @click="changeGoods">选择</el-button>
          </el-form-item>
          <el-form-item label="图片">
            <MaterialList v-model="form.imageUrlArr" style="width: 300px" type="image" :num="1" :width="150" :height="150" />
          </el-form-item>
          <el-form-item label="是否显示">
            <el-radio v-model="form.isShow" :label="1">是</el-radio>
            <el-radio v-model="form.isShow" :label="0" style="width: 200px;">否</el-radio>
          </el-form-item>
          <el-form-item label="排序编号">
            <el-input-number v-model="form.sortNo" controls-position="right"  style="width: 200px;"/>
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
        <el-table-column v-if="columns.visible('id')" prop="id" label="模板id" />
        <el-table-column v-if="columns.visible('title')" prop="title" label="标题" />
        <el-table-column v-if="columns.visible('description')" prop="description" label="描述" />
        <el-table-column v-if="columns.visible('type')" prop="type" label="模板分类" >
          <template slot-scope="scope">
            {{scope.row.type===1?'普通模板':''}}
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('imageUrl')" prop="imageUrl" label="图片" >
          <template slot-scope="scope">
            <a :href="scope.row.imageUrl" style="color: #42b983" target="_blank"><img :src="scope.row.imageUrl" alt="点击打开" class="el-avatar"></a>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('productId')" prop="productId" label="商品ID" />
        <el-table-column v-if="columns.visible('productName')" prop="productName" label="商品名称" />
        <el-table-column v-if="columns.visible('isShow')" prop="isShow" label="是否显示" >
          <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.isShow === 1" style="cursor: pointer" :type="''">显示</el-tag>
            <el-tag v-else style="cursor: pointer" :type=" 'info' ">不显示</el-tag>
          </div>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('sortNo')" prop="sortNo" label="排序编号" />
        <el-table-column v-if="columns.visible('addTime')" prop="addTime" label="添加时间" >
          <template slot-scope="scope">
            <span>{{ formatTime(scope.row.addTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxProductTemplate:edit','yxProductTemplate:del']" label="" width="100px" align="center">
          <template slot-scope="scope">
            <el-button  size="mini" type="primary" icon="el-icon-setting" @click="setGoodsFun(scope.row)" >设置商品</el-button>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxProductTemplate:edit','yxProductTemplate:del']" label="操作" width="120px" align="center">
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


    <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="closeSelectFun"
               :visible.sync="changeOpen" title="选择商品" width="740px">
      <div class="head-container">
        <!-- 搜索 -->
        <el-input v-model="query.storeName" clearable placeholder="输入搜索内容" style="width: 200px;" class="filter-item" @keyup.enter.native="toQuery" />
        <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="toQuery">搜索</el-button>
      </div>
      <el-table v-loading="productLoading" :data="productData" size="small" style="width: 100%;" ref="goodsTable" @selection-change="handleSelectionChange">
        <el-table-column :selectable="checkboxT" type="selection" width="50" />
        <el-table-column prop="id" label="商品id" />
        <el-table-column ref="table" prop="image" label="商品图片">
          <template slot-scope="scope">
            <a :href="scope.row.image" style="color: #42b983" target="_blank">
              <img :src="scope.row.image.indexOf('@')>=0?require('@/assets/images/defaultPic.jpg'):scope.row.image" alt="点击打开" class="el-avatar">
              <!--<img :src='getImgUrl(scope.row.image)' alt="点击打开" class="el-avatar">-->
            </a>
          </template>
        </el-table-column>
        <el-table-column prop="storeName" label="商品名称" />
        <el-table-column prop="storeCategory.cateName" label="分类名称" />
        <el-table-column prop="packaging" label="包装规格" />
        <el-table-column prop="price" label="商品价格" >
          <template slot-scope="scope">
            <span>{{scope.row.price.toFixed(2)}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sales" label="销量" />
        <el-table-column prop="stock" label="库存" />
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <div @click="onSale(scope.row.id,scope.row.isShow)">
              <el-tag v-if="scope.row.isShow === 1" style="cursor: pointer" :type="''">已上架</el-tag>
              <el-tag v-else style="cursor: pointer" :type=" 'info' ">已下架</el-tag>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <el-pagination
        :total="total"
        :current-page="page + 1"
        style="margin-top: 8px;"
        layout="total, prev, pager, next, sizes"
        @size-change="sizeChange"
        @current-change="pageChange"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="changeOpen=false">取消</el-button>
        <el-button type="primary" @click="doSelectSubmit">确认</el-button>
      </div>
    </el-dialog>

    <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="closeSetWin"
               :visible.sync="setGoodsOpen" title="设置模板商品" width="800px">
      <div class="head-container">
        <!-- 新增 -->
        <div style="display: inline-block;margin: 0px 2px;">
          <el-button
            class="filter-item"
            size="mini"
            type="primary"
            icon="el-icon-plus"
            @click="changeGoods"
          >添加商品</el-button>
        </div>
      </div>
      <el-table v-loading="productLoading" :data="templateItemList" size="small" style="width: 100%;" ref="selectGoodsTable">
        <el-table-column prop="productId" label="商品Id" />
        <el-table-column prop="productName" label="商品名称" width="360px">
          <template slot-scope="scope">
            <el-input v-model="scope.row.productName"/>
          </template>
        </el-table-column>
        <el-table-column prop="sortNo" label="编号" >
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.sortNo" controls-position="right"/>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <el-button
              class="filter-item"
              size="mini"
              type="primary"
              icon="el-icon-remove"
              @click="removeSelectGoods(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="doSaveTemplateItemSubmit">保存</el-button>
        <el-button type="text" @click="closeSetWin">取消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import crudYxProductTemplate, {addItems, updateItems} from '@/api/yxProductTemplate'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from '@/components/material'
import { formatTime } from '@/utils/index'
import {initData} from "@/api/data";
import {add} from "@/api/yxStoreCoupon";
import {delItems} from "@/api/yxProductTemplateItem";
// crud交由presenter持有
const defaultCrud = CRUD({ title: '小程序商品模板', url: 'api/yxProductTemplate', sort: 'id,desc', crudMethod: { ...crudYxProductTemplate }})
const defaultForm = { id: null, title: null, description: null, type: null, imageUrl: null,imageUrlArr:[], productId: null, productName: null, isShow: 1, sortNo: null, addTime: null, isDel: null }
export default {
  name: 'YxProductTemplate',
  components: { pagination, crudOperation, rrOperation, udOperation,MaterialList },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      changeOpen:false,
      productLoading:false,
      setGoodsOpen:false,
      goodsList: [],
      // 页码
      page: 0,
      // 每页数据条数
      size: 5,
      // 总数据条数
      total: 0,
      listContent: [],
      productData:[],
      selectGoodsList:[],
      // 等待时间
      time: 50,
      currentSelectId:null,
      templateItemtotal:0,
      templateItemList:[],
      queryTypeOptions: [
        { key: 'storeName', display_name: '商品名称' }
      ],
      query: {},
      permission: {
        add: ['admin', 'yxProductTemplate:add'],
        edit: ['admin', 'yxProductTemplate:edit'],
        del: ['admin', 'yxProductTemplate:del']
      },
      rules: {
      }    }
  },
  created() {
    this.crud.optShow = {
      add: true,
      edit: false,
      del: false,
      download: false
    }
  },
  methods: {
    formatTime,
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },

    [CRUD.HOOK.beforeToEdit](crud, form) {
      console.log(form);
      if(form.imageUrl){

        form.imageUrlArr=form.imageUrl.split(',')
        form.type=form.type+'';
      }
      return true
    },

    [CRUD.HOOK.afterValidateCU](crud) {
      if (!crud.form.imageUrlArr.length>0) {
        this.$message({
          message: '图片不能为空',
          type: 'warning'
        })
        return false
      }
      crud.form.addTime=(new Date()).getTime()/1000;
      crud.form.imageUrl = crud.form.imageUrlArr.join(',')
      return true
    },

    initProductData(){
      let url = 'api/yxStoreProduct'
      const sort = 'id,desc'
      let params = { page: this.page, size: this.size, sort: sort, isShow: 1, isDel: 0 ,...this.query}
      // 请求数据
      this.productLoading=true;
      initData(url, params).then(data => {
        this.total = data.totalElements
        this.productData = data.content
        // time 毫秒后显示表格
        setTimeout(() => {
          this.productLoading = false
        }, this.time)
      }).catch(err => {
        this.productLoading = false
      })
    },

    closeSelectFun(){
      this.changeOpen=false;
    },

    // 改变页码
    pageChange(e) {
      this.page = e - 1
      this.initProductData()
    },
    // 改变每页显示数
    sizeChange(e) {
      this.page = 0
      this.size = e
      this.initProductData()
    },

    changeGoods(){
      this.changeOpen=true;
      this.query={
        value:null,
        type:null
      }
      this.initProductData();
    },
    checkboxT(row, rowIndex) {
      return row.id !== 1
    },
    handleSelectionChange(val) {
      this.goodsList = val;
    },
    doSelectSubmit(){
      console.log(this.goodsList)
      let itemParams=[];
      if(this.setGoodsOpen){
        this.goodsList.forEach(item=>{
          this.selectGoodsList.push(item);
          let titem={
            templateId:this.currentSelectId,
            productId:item.id,
            productName:item.storeName
          }
          itemParams.push(titem);
        });
        let addParam={
          id:this.currentSelectId,
          itemList:itemParams
        }
        addItems(addParam).then(res => {
          this.$notify({
            title: '添加成功',
            type: 'success',
            duration: 2500
          })
          this.getTemplateItemsData(this.currentSelectId);
        }).catch(err => {
          console.log(err.response.data.message)
        })
      }
      else{
        if (this.goodsList.length!=1) {
          this.$message({
            message: '请选择一个商品',
            type: 'warning'
          })
          return false
        }
        let productInfo=this.goodsList[0];
        this.crud.form.productId=productInfo.id
        this.crud.form.productName=productInfo.storeName
        this.crud.form.imageUrl=productInfo.image
        this.crud.form.imageUrlArr=productInfo.image.split(',')
        console.log(this.crud.form);
      }
      this.changeOpen=false;
    },
    setGoodsFun(row){
      this.setGoodsOpen=true;
      this.currentSelectId=row.id;
      this.getTemplateItemsData(row.id);
    },

    getTemplateItemsData(id){
      this.templateItemList=[];
      initData('api/yxProductTemplateItem', {templateId:id,sort:'sortNo,asc',page:0,size:999999}).then(data => {
        this.templateItemtotal = data.totalElements
        this.templateItemList = data.content
      }).catch(err => {
      })
    },

    closeSetWin(){
      this.setGoodsOpen=false;
    },

    removeSelectGoods(row){
      let ids=[];
      ids.push(row.id)
      delItems(ids).then(res => {
        this.$notify({
          title: '删除成功',
          type: 'success',
          duration: 2500
        })
        this.getTemplateItemsData(this.currentSelectId);
      }).catch(err => {
        console.log(err.response.data.message)
      })
    },
    doSaveTemplateItemSubmit(){
      let editParam={
        id:this.currentSelectId,
        itemList:this.templateItemList
      }
      updateItems(editParam).then(res => {
        this.$notify({
          title: '保存成功',
          type: 'success',
          duration: 2500
        })
        this.setGoodsOpen=true;
        //this.getTemplateItemsData(this.currentSelectId);
      }).catch(err => {
        console.log(err.response.data.message)
      })
    },

    toQuery(){
      this.page=0;
      this.initProductData();
    }

  }
}
</script>

<style scoped>

</style>
