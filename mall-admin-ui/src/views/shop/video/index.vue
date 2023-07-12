<template>
  <div class="app-container">
    <el-form ref="query" :model="query" label-width="100px" inline>
      <el-form-item label="视频类型">
        <el-select v-model="query.typeId" filterable placeholder="请选择视频" @change="crud.toQuery()" clearable>
          <el-option
            v-for="item in videoTypeOptions"
            :key="item.id"
            :label="item.typeName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="标题">
        <el-input v-model="query.title"/>
      </el-form-item>
      <rrOperation :crud="crud" />
    </el-form>
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission"/>
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0"
                 :title="crud.status.title" width="750px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="140px">
          <el-form-item label="视频类型" prop="typeId">
            <el-select v-model="form.typeId" filterable placeholder="请选择视频" style="width: 370px;">
              <el-option
                v-for="item in videoTypeOptions"
                :key="item.id"
                :label="item.typeName"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" style="width: 370px;"/>
          </el-form-item>
          <el-form-item label="视频封面(375*671)">
            <MaterialList
              v-model="form.coverImageArr"
              style="width: 738px"
              type="image"
              :num="1"
              :width="150"
              :height="150"
            />
          </el-form-item>
          <el-form-item label="视频地址">
            <file-upload v-model="form.videoUrl" style="width: 560px;"/>
            <label>{{form.videoUrl}}</label>
          </el-form-item>
          <el-form-item label="奖励积分数量">
            <el-input v-model="form.scoreNum" style="width: 370px;"/>
          </el-form-item>
          <el-form-item label="虚拟浏览量">
            <el-input v-model="form.virtualWatchNum" style="width: 370px;"/>
          </el-form-item>
          <el-form-item label="是否展示">
            <el-radio v-model="form.showFlag" label="Y">是</el-radio>
            <el-radio v-model="form.showFlag" label="N" style="width: 200px">否
            </el-radio
            >
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="form.remark" style="width: 370px;" type="textarea"/>
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
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;">
        <el-table-column v-if="columns.visible('id')" type="index" label="编号"/>
        <el-table-column v-if="columns.visible('typeName')" prop="typeName" label="视频类型"/>
        <el-table-column v-if="columns.visible('title')" prop="title" label="标题"/>
        <el-table-column v-if="columns.visible('coverImage')" prop="coverImage" label="视频封面">
          <template slot-scope="scope">
            <img :src="scope.row.coverImage?scope.row.coverImage:require('@/assets/images/defaultPic.jpg')" alt="点击打开"
                 style="width:100px">
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('scoreNum')" prop="scoreNum" label="奖励积分数量"/>
        <el-table-column v-if="columns.visible('watchNum')" prop="watchNum" label="浏览量">
          <template slot-scope="scope">
            真实浏览量：{{ scope.row.watchNum ? scope.row.watchNum : 0 }}<br>
            虚拟浏览量：{{ scope.row.virtualWatchNum }}
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('createTime')" prop="createTime" label="创建时间">
          <template slot-scope="scope">
            <span>{{ formatTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('remark')" prop="remark" label="备注"/>
        <el-table-column v-if="columns.visible('showFlag')" prop="showFlag" label="是否展示">
          <template slot-scope="scope">
            {{ scope.row.showFlag === 'Y' ? '是' : '否' }}
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('sortNo')" prop="sortNo" label="排序"/>
        <el-table-column label="" width="100px" align="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-setting" @click="setGoodsFun(scope.row)">设置商品
            </el-button>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','yxVideo:edit','yxVideo:del']" label="操作" width="150px" align="center">
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
      <pagination/>
    </div>

    <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="closeSetWin"
               :visible.sync="setGoodsOpen" title="设置视频商品" width="800px">
      <div class="head-container">
        <!-- 新增 -->
        <div style="display: inline-block;margin: 0px 2px;">
          <el-button
            class="filter-item"
            size="mini"
            type="primary"
            icon="el-icon-plus"
            @click="changeGoods"
          >添加商品
          </el-button>
        </div>
      </div>
      <el-table v-loading="productLoading" :data="templateItemList" size="small" style="width: 100%;"
                ref="selectGoodsTable">
        <el-table-column prop="productId" label="商品Id"/>
        <el-table-column prop="productName" label="商品名称" width="360px">
          <template slot-scope="scope">
            <el-input v-model="scope.row.productName"/>
          </template>
        </el-table-column>
        <el-table-column prop="sortNo" label="编号">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.sortNo" controls-position="right"/>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              class="filter-item"
              size="mini"
              type="primary"
              icon="el-icon-remove"
              @click="removeSelectGoods(scope.row)"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="doSaveTemplateItemSubmit">保存</el-button>
        <el-button type="text" @click="closeSetWin">取消</el-button>
      </div>
    </el-dialog>

    <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="closeSelectFun"
               :visible.sync="changeOpen" title="选择商品" width="740px">
      <div class="head-container">
        <!-- 搜索 -->
        <el-input v-model="productQuery.storeName" clearable placeholder="输入搜索内容" style="width: 200px;" class="filter-item"
                  @keyup.enter.native="toQuery"/>
        <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="toQuery">搜索</el-button>
      </div>
      <el-table v-loading="productLoading" :data="productData" size="small" style="width: 100%;" ref="goodsTable"
                @selection-change="handleSelectionChange">
        <el-table-column :selectable="checkboxT" type="selection" width="50"/>
        <el-table-column prop="id" label="商品id"/>
        <el-table-column ref="table" prop="image" label="商品图片">
          <template slot-scope="scope">
            <a :href="scope.row.image" style="color: #42b983" target="_blank">
              <img :src="scope.row.image.indexOf('@')>=0?require('@/assets/images/defaultPic.jpg'):scope.row.image"
                   alt="点击打开" class="el-avatar">
              <!--<img :src='getImgUrl(scope.row.image)' alt="点击打开" class="el-avatar">-->
            </a>
          </template>
        </el-table-column>
        <el-table-column prop="storeName" label="商品名称"/>
        <el-table-column prop="storeCategory.cateName" label="分类名称"/>
        <el-table-column prop="packaging" label="包装规格"/>
        <el-table-column prop="price" label="商品价格">
          <template slot-scope="scope">
            <span>{{ scope.row.price.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sales" label="销量"/>
        <el-table-column prop="stock" label="库存"/>
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
  </div>
</template>

<script>
import crudYxVideo,{addItems, updateItems} from '@/api/yxVideo'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import {queryAllVideoTypes} from "@/api/yxVideoType";
import MaterialList from "@/components/material";
import fileUpload from '@/components/file-upload'
import {formatTime} from '@/utils/index'
import {initData} from "@/api/data";
import {delItems} from "@/api/yxVideoProduct";
// crud交由presenter持有
const defaultCrud = CRUD({title: '视频', url: 'api/yxVideo', sort: 'id,desc', crudMethod: {...crudYxVideo}})
const defaultForm = {
  id: null,
  typeId: null,
  typeName: null,
  title: null,
  coverImage: null,
  coverImageArr: [],
  videoUrl: null,
  scoreNum: null,
  watchNum: null,
  virtualWatchNum: null,
  createTime: null,
  remark: null,
  showFlag: 'Y',
  sortNo: null
}
export default {
  name: 'YxVideo',
  components: {pagination, crudOperation, rrOperation, udOperation, MaterialList, fileUpload},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      videoTypeOptions: [],
      changeOpen: false,
      productLoading: false,
      goodsList: [],
      setGoodsOpen: false,
      currentSelectId: null,
      templateItemtotal: 0,
      templateItemList: [],
      // 页码
      page: 0,
      // 每页数据条数
      size: 5,
      // 总数据条数
      total: 0,
      listContent: [],
      productData: [],
      selectGoodsList: [],
      // 等待时间
      time: 50,
      queryTypeOptions: [
        {key: 'storeName', display_name: '商品名称'}
      ],
      productQuery:{},
      permission:{ add: ['admin', 'yxVideoType:add']},
      rules: {
        typeId: [
          {required: true, message: "请选择视频类型", trigger: "blur"},
        ],
        title: [
          {required: true, message: "请输入视频标题", trigger: "blur"},
        ],
      }
    }
  },
  watch: {
    "form.coverImageArr": function (val) {
      if (val) {
        this.form.coverImage = val.join(",");
      }
    }
  },
  created() {
    this.crud.optShow.del = false
    this.crud.optShow.edit = false
    this.crud.optShow.download = false
    this.getAllVideoTypes();
  },
  methods: {
    formatTime,
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },

    // 打开编辑弹窗前做的操作
    [CRUD.HOOK.beforeToEdit](crud, form) {
      let imageArray = [];
      imageArray.push(form.coverImage)
      this.$set(form, 'coverImageArr', imageArray)
    },

    getAllVideoTypes() {
      queryAllVideoTypes({}).then((res) => {
        this.videoTypeOptions = res;
      });
    },

    setGoodsFun(row) {
      this.setGoodsOpen = true;
      this.currentSelectId = row.id;
      this.getTemplateItemsData(row.id);
    },

    initProductData() {
      let url = 'api/yxStoreProduct'
      const sort = 'id,desc'
      let params = {page: this.page, size: this.size, sort: sort, isShow: 1, isDel: 0, ...this.productQuery}
      // 请求数据
      this.productLoading = true;
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

    closeSelectFun() {
      this.changeOpen = false;
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

    changeGoods() {
      this.changeOpen = true;
      this.query = {
        value: null,
        type: null
      }
      this.initProductData();
    },
    checkboxT(row, rowIndex) {
      return row.id !== 1
    },
    handleSelectionChange(val) {
      this.goodsList = val;
    },
    doSelectSubmit() {
      console.log(this.goodsList)
      let itemParams = [];
      if (this.setGoodsOpen) {
        this.goodsList.forEach(item => {
          this.selectGoodsList.push(item);
          let titem = {
            videoId: this.currentSelectId,
            productId: item.id,
            productName: item.storeName
          }
          itemParams.push(titem);
        });
        let addParam = {
          id: this.currentSelectId,
          itemList: itemParams
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
      } else {
        if (this.goodsList.length != 1) {
          this.$message({
            message: '请选择一个商品',
            type: 'warning'
          })
          return false
        }
        let productInfo = this.goodsList[0];
        this.crud.form.productId = productInfo.id
        this.crud.form.productName = productInfo.storeName
        this.crud.form.imageUrl = productInfo.image
        this.crud.form.imageUrlArr = productInfo.image.split(',')
        console.log(this.crud.form);
      }
      this.changeOpen = false;
    },


    closeSetWin() {
      this.setGoodsOpen = false;
    },

    removeSelectGoods(row) {
      let ids = [];
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
    doSaveTemplateItemSubmit() {
      let editParam = {
        id: this.currentSelectId,
        itemList: this.templateItemList
      }
      updateItems(editParam).then(res => {
        this.$notify({
          title: '保存成功',
          type: 'success',
          duration: 2500
        })
        this.setGoodsOpen = true;
        //this.getTemplateItemsData(this.currentSelectId);
      }).catch(err => {
        console.log(err.response.data.message)
      })
    },

    toQuery() {
      this.page = 0;
      this.initProductData();
    },

    getTemplateItemsData(id) {
      this.templateItemList = [];
      initData('api/yxVideoProduct', {videoId: id, sort: 'sortNo,asc', page: 0, size: 999999}).then(data => {
        this.templateItemtotal = data.totalElements
        this.templateItemList = data.content
      }).catch(err => {
      })
    },

    copyrouter(row) {
      console.log(row, '-----------id')
      let url = '/pages/kitchen/detail?id=' + row.id;
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
