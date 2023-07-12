<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!-- 搜索 -->
      <el-input v-model="query.value" clearable placeholder="输入搜索内容" style="width: 200px;" class="filter-item" @keyup.enter.native="toQuery" />
      <el-select v-model="query.type" clearable placeholder="类型" class="filter-item" style="width: 130px">
        <el-option v-for="item in queryTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <treeselect
        v-model="query.cateId"
        :options="cates"
        style="width: 238px;display: inline-block;padding-top:5px;height: 32px;"
        placeholder="选择商品分类"
      />
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="toQuery">搜索</el-button>
      <!-- 新增 -->
      <div style="display: inline-block;margin: 0px 2px;">
        <el-button
          class="filter-item"
          size="mini"
          type="primary"
          icon="el-icon-plus"
          @click="add"
        >新增</el-button>
        <el-select v-model="goodsExport" @change="handleExportOption" clearable placeholder="批量导出" class="filter-item export-item" style="width: 100px">
          <el-option
            v-for="item in exportOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-button
          class="filter-item"
          size="mini"
          type="warning"
          icon="el-icon-document"
          @click="addMore"
        >批量导入</el-button>
        <el-button
          type="danger"
          class="filter-item"
          size="mini"
          icon="el-icon-refresh"
          @click="toQuery"
        >刷新</el-button>
      </div>
    </div>
    <!--表单组件-->
    <eForm ref="form" :is-add="isAdd" />
    <eAttr v-if="eattrInfo.visible" :dialogInfo="eattrInfo" @backInfo="init" />
    <comForm ref="form3" :is-add="isAdd" />
    <killForm ref="form4" :is-add="isAdd" />
    <bargainForm ref="form5" :is-add="isAdd" />
    <moreForm ref="form6" :is-more-add="isMoreAdd" />
    <discountForm v-if="discountInfo.visible" :dialogInfo="discountInfo" />
    <!--表格渲染-->
    <el-table v-loading="loading" :data="data" size="small" style="width: 100%;" ref="goodsTable" @selection-change="handleSelectionChange">
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
      <el-table-column ref="table" prop="qrcodeImage" label="二维码">
        <template slot-scope="scope">
          <a :href="scope.row.qrcodeImage" style="color: #42b983" target="_blank">
            <img :src="scope.row.qrcodeImage?scope.row.qrcodeImage:require('@/assets/images/defaultPic.jpg')" alt="点击打开" class="el-avatar">
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
      <el-table-column prop="sort" label="排序" />
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <div @click="onSale(scope.row.id,scope.row.isShow)">
            <el-tag v-if="scope.row.isShow === 1" style="cursor: pointer" :type="''">已上架</el-tag>
            <el-tag v-else style="cursor: pointer" :type=" 'info' ">已下架</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="205px" align="center">
        <template slot-scope="scope">
          <el-button slot="reference" type="danger" size="mini" @click="attr(scope.row)">规格属性</el-button>
          <el-dropdown size="mini" split-button type="primary" trigger="click">
            操作
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button
                  size="mini"
                  type="primary"
                  icon="el-icon-edit"
                  @click="edit(scope.row)"
                >编辑</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-popover
                  :ref="scope.row.id"
                  placement="top"
                  width="180"
                >
                  <p>确定删除本条数据吗？</p>
                  <div style="text-align: right; margin: 0">
                    <el-button size="mini" type="text" @click="$refs[scope.row.id].doClose()">取消</el-button>
                    <el-button :loading="delLoading" type="primary" size="mini" @click="subDelete(scope.row.id)">确定</el-button>
                  </div>
                  <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini">删除</el-button>
                </el-popover>
              </el-dropdown-item>
<!--              <el-dropdown-item>-->
<!--                <el-button-->
<!--                  size="mini"-->
<!--                  type="success"-->
<!--                  @click="editC(scope.row)"-->
<!--                >开启拼团</el-button>-->
<!--              </el-dropdown-item>-->
<!--              <el-dropdown-item>-->
<!--                <el-button-->
<!--                  size="mini"-->
<!--                  type="primary"-->
<!--                  @click="editD(scope.row)"-->
<!--                >开启秒杀</el-button>-->
<!--              </el-dropdown-item>-->
<!--              <el-dropdown-item>-->
<!--                <el-button-->
<!--                  size="mini"-->
<!--                  type="warning"-->
<!--                  @click="editE(scope.row)"-->
<!--                >开启砍价</el-button>-->
<!--              </el-dropdown-item>-->
              <el-dropdown-item>
<!--                <el-button-->
<!--                  size="mini"-->
<!--                  type="success"-->
<!--                  @click="setLevel(scope.row)"-->
<!--                >会员折扣</el-button>-->
                  <el-button
                  size="mini"
                  type="success"
                  @click="copyrouter(scope.row)"
                >复制路由</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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
  </div>
</template>

<script>
import checkPermission from '@/utils/permission'
import initData from '@/mixins/crud'
import { del, onsale } from '@/api/yxStoreProduct'
import eForm from './form'
import eAttr from './attr'
import moreForm from './moreForm'
import comForm from '@/views/activity/combination/form'
import killForm from '@/views/activity/seckill/form'
import bargainForm from '@/views/activity/bargain/form'
import discountForm from '@/views/activity/discount/form'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {getCates} from "@/api/yxStoreCategory";
// import 'quill/dist/quill.core.css'
// import 'quill/dist/quill.snow.css'
// import 'quill/dist/quill.bubble.css'
// import { quillEditor } from 'vue-quill-editor';
export default {
  components: { eForm, eAttr, comForm, killForm, bargainForm, moreForm, discountForm,Treeselect },
  mixins: [initData],
  data() {
    return {
        delLoading: false,
        visible: false,
        cates:[],
        queryTypeOptions: [
            { key: 'storeName', display_name: '商品名称' }
        ],
        eattrInfo:{
            visible:false,
            data:{}
        },
        isMoreAdd: false,
        goodsList: [],
        goodsExport: '',
        exportOptions: [
            {value: '', label: '批量导出'},
            {value: '0', label: '导出全部'},
            {value: '1', label: '导出选中'},
        ],
        listContent: [],
        discountInfo:{
            visible:false,
            data:{}
        },
    }
  },
  created() {
    this.getCates();
    this.$nextTick(() => {
      this.init()
    })
  },
  methods: {
    checkPermission,
    beforeInit() {
      this.url = 'api/yxStoreProduct'
      const sort = 'id,desc'
      this.params = { page: this.page, size: this.size, sort: sort, isShow: 1, isDel: 0 , listContent: this.listContent}
      const query = this.query
      const type = query.type
      const value = query.value
      if (type && value) { this.params[type] = value }
      return true
    },
    getCates() {
      getCates({enabled: true}).then((res) => {
        this.cates = res.content;
      });
    },
    subDelete(id) {
      this.delLoading = true
      del(id).then(res => {
        this.delLoading = false
        this.$refs[id].doClose()
        this.dleChangePage()
        this.init()
        this.$notify({
          title: '删除成功',
          type: 'success',
          duration: 2500
        })
      }).catch(err => {
        this.delLoading = false
        this.$refs[id].doClose()
      })
    },
    onSale(id, status) {
      this.$confirm(`确定进行[${status ? '下架' : '上架'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          onsale(id, { status: status }).then(({ data }) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1000,
              onClose: () => {
                this.init()
              }
            })
          })
        })
        .catch(() => { })
    },
    add() {
      this.isAdd = true
      this.$refs.form.dialog = true
      this.$refs.form.getCates()
    },
    addMore(){
      this.isMoreAdd = true
      this.$refs.form6.dialog = true
      this.$refs.form.getCates()
    },
    edit(data) {
      this.isAdd = false
      console.log(data)
      const _this = this.$refs.form
      _this.getCates()
      _this.form = {
        id: data.id,
        merId: data.merId,
        image: data.image,
        video: data.video,
        sliderImage: data.sliderImage,
        imageArr: data.image.split(','),
        haibaoImage: data.haibaoImage,
        haibaoImageArr: data.haibaoImage?data.haibaoImage.split(','):[],
        sliderImageArr: data.sliderImage.split(','),
        storeName: data.storeName,
        storeInfo: data.storeInfo,
        keyword: data.keyword,
        barCode: data.barCode,
        storeCategory: data.storeCategory || {id:null},
        price: data.price,
        vipPrice: data.vipPrice,
        otPrice: data.otPrice,
        postage: data.postage,
        unitName: data.unitName,
        packaging: data.packaging,
        sort: data.sort,
        sales: data.sales,
        stock: data.stock,
        isShow: data.isShow,
        isHot: data.isHot,
        isBenefit: data.isBenefit,
        isBest: data.isBest,
        isNew: data.isNew,
        description: data.description,
        addTime: data.addTime,
        isPostage: data.isPostage,
        isDel: data.isDel,
        isRank: data.isRank,
        merUse: data.merUse,
        giveIntegral: data.giveIntegral,
        cost: data.cost,
        isSeckill: data.isSeckill,
        isBargain: data.isBargain,
        isGood: data.isGood,
        ficti: data.ficti,
        browse: data.browse,
        codePath: data.codePath,
        soureLink: data.soureLink
      }
      _this.dialog = true
    },
    editC(data) {
      this.isAdd = false
      const _this = this.$refs.form3
      _this.form = {
        productId: data.id,
        merId: data.merId,
        image: data.image,
        video: data.video,
        images: data.sliderImage,
        imageArr: data.image.split(','),
        sliderImageArr: data.sliderImage.split(','),
        haibaoImage: data.haibaoImage,
        haibaoImageArr: data.haibaoImage?data.haibaoImage.split(','):[],
        title: data.storeName,
        info: data.storeInfo,
        postage: data.postage,
        unitName: data.unitName,
        packaging: data.packaging,
        sort: data.sort,
        sales: data.sales,
        stock: data.stock,
        isShow: data.isShow,
        isHost: data.isHot,
        isRank: data.isRank,
        description: data.description,
        isPostage: data.isPostage,
        people: 0,
        price: 0,
        effectiveTime: 24,
        combination: 1,
        cost: data.cost,
        isDel: 0,
        browse: 0
      }
      _this.dialog = true
    },
    editD(data) {
      this.isAdd = false
      const _this = this.$refs.form4
      _this.form = {
        productId: data.id,
        merId: data.merId,
        image: data.image,
        video: data.video,
        images: data.sliderImage,
        imageArr: data.image.split(','),
        sliderImageArr: data.sliderImage.split(','),
        haibaoImage: data.haibaoImage,
        haibaoImageArr: data.haibaoImage?data.haibaoImage.split(','):[],
        title: data.storeName,
        info: data.storeInfo,
        postage: data.postage,
        unitName: data.unitName,
        packaging: data.packaging,
        sort: data.sort,
        sales: data.sales,
        stock: data.stock,
        isShow: data.isShow,
        isRank: data.isRank,
        status: 1,
        isHot: data.isHot,
        description: data.description,
        isPostage: data.isPostage,
        people: 0,
        price: 0.01,
        effectiveTime: 24,
        otPrice: data.otPrice,
        cost: data.cost,
        num: 1,
        giveIntegral: 0,
        isDel: 0,
        browse: 0
      }
      _this.dialog = true
    },
    editE(data) {
      this.isAdd = false
      const _this = this.$refs.form5
      _this.form = {
        productId: data.id,
        merId: data.merId,
        image: data.image,
        video: data.video,
        images: data.sliderImage,
        imageArr: data.image.split(','),
        sliderImageArr: data.sliderImage.split(','),
        haibaoImage: data.haibaoImage,
        haibaoImageArr: data.haibaoImage?data.haibaoImage.split(','):[],
        title: data.storeName,
        info: data.storeInfo,
        postage: data.postage,
        unitName: data.unitName,
        packaging: data.packaging,
        sort: data.sort,
        sales: data.sales,
        stock: data.stock,
        isShow: data.isShow,
        isRank: data.isRank,
        status: 1,
        isHot: data.isHot,
        description: data.description,
        isPostage: data.isPostage,
        people: 0,
        price: 0.01,
        effectiveTime: 24,
        otPrice: data.otPrice,
        cost: data.cost,
        num: 1,
        giveIntegral: 0,
        isDel: 0,
        browse: 0
      }
      _this.dialog = true
    },
    attr(data) {
        this.eattrInfo.data=data;
        this.eattrInfo.visible=true;
    },
    checkboxT(row, rowIndex) {
      return row.id !== 1
    },
    handleSelectionChange(val) {
      this.goodsList = val;
    },
    handleExportOption(val){
      let list = this.goodsList;
      switch (val) {
        case "0":
          this.page = 0;
          this.size = 100000000;
          this.listContent = "";
          this.beforeInit();
          this.downloadMethod();
          break;
        case "1":
          if(list.length == 0){
            this.$message({
              message: '请选择商品信息!',
              type: 'warning'
            });
          }else {
            this.listContent = [];
            list.forEach((item) => {
              this.listContent.push(item.id);
            })
            this.listContent = JSON.stringify(this.listContent);
            this.beforeInit();
            this.downloadMethod();
          }
          break;
        default:
          break;
      }
      this.goodsExport = "";
    },
    setLevel(data) {
        this.discountInfo.data={
            productId:data.id,
            price:data.price
        }
        this.discountInfo.visible=true;
    },
    // 复制路由
    copyrouter(row){
      console.log(row,'-----------id')
        //  document.execCommand("Copy"); // 执行浏览器复制命令
        //         this.$message({
        //             message: '复制成功',
        //             type: 'success'
        //         });
                let url = '/pages/shop/GoodsCon/index?id=' +row.id ;
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

<style scoped lang="scss">
  .head-container {
    .export-item {
      /deep/ input {
        background: #ecdf27 !important;
        color: #fff !important;
        border: none !important;
        width: 100px !important;
        font-size: 12px !important;
        height: 29px;
        line-height: 29px;
      }
      /deep/ .el-icon-arrow-up:before  {
        color: #fff !important;
      }
    }
  }
</style>
