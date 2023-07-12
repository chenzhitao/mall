<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!-- 搜索 -->
      <el-input v-model="query.value" clearable placeholder="输入搜索内容" style="width: 200px;" class="filter-item" @keyup.enter.native="toQuery" />
      <el-select v-model="query.type" clearable placeholder="类型" class="filter-item" style="width: 130px">
        <el-option v-for="item in queryTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="query.yxSystemUserLevel" clearable placeholder="会员类型" class="filter-item" style="width: 130px">
        <el-option v-for="item in levelOptions" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-select v-model="userType" clearable placeholder="用户来源" class="filter-item" style="width: 130px">
        <el-option
          v-for="item in statusOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select v-model="usersExport" @change="handleExportOption" clearable placeholder="批量导出" class="filter-item export-item" style="width: 100px">
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
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="toQuery">搜索</el-button>
      <!-- 新增 -->
      <el-button
        type="danger"
        class="filter-item"
        size="mini"
        icon="el-icon-refresh"
        @click="toQuery"
      >刷新</el-button>
    </div>
    <!--表单组件-->
    <eForm ref="form" :is-add="isAdd" />
    <pForm ref="formp" :is-add="isAdd" />
    <!--表格渲染-->
    <el-table v-loading="loading" :data="data" size="small" style="width: 100%;" ref="usersTable" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" />
      <el-table-column prop="uid" label="用户id" />
      <el-table-column prop="nickname" label="用户昵称" />
      <el-table-column ref="table" prop="avatar" label="用户头像">
        <template slot-scope="scope">
          <a :href="scope.row.avatar" style="color: #42b983" target="_blank"><img :src="scope.row.avatar" alt="点击打开" class="el-avatar"></a>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号码" />
      <el-table-column prop="yxSystemUserLevel.name" label="会员等级" />

<!--      <el-table-column prop="applyLevel" label="用户申请会员等级" >-->
<!--        <template slot-scope="scope">-->
<!--          <span>-->
<!--            {{levelObj[scope.row.applyLevel]}}-->
<!--          </span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column prop="certInfo" label="会员等级申请材料" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-image-->
<!--            class="material"-->
<!--            style="color: #42b983"-->
<!--            :src="handleCertInfo(scope.row.certInfo)"-->
<!--            :preview-src-list="handleCertInfo1(scope.row.certInfo)"-->
<!--          >-->
<!--          </el-image>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column prop="isCheck" label="是否审核" >-->
<!--        <template slot-scope="scope">-->
<!--          <div>-->
<!--            <el-tag v-if="scope.row.isCheck == 0" style="background: #ff4949; color: #fff;">未审核</el-tag>-->
<!--            <el-tag v-else-if="scope.row.isCheck == 1" style="background: limegreen; color: #fff;">已审核</el-tag>-->
<!--          </div>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column prop="isPass" label="是否审核通过" >-->
<!--        <template slot-scope="scope">-->
<!--          <div @click="onIsPass(scope.row.uid, scope.row.isPass, scope.row.applyLevel)">-->
<!--            <el-tag v-if="scope.row.isPass == 1" style="cursor: pointer" :type="''">通过</el-tag>-->
<!--            <el-tag v-else-if="scope.row.isPass == 0" style="cursor: pointer" :type=" 'info' ">不通过</el-tag>-->
<!--          </div>-->
<!--        </template>-->
<!--      </el-table-column>-->

<!--      <el-table-column prop="nowMoney" label="用户余额" />-->
<!--      <el-table-column prop="brokeragePrice" label="佣金金额" />-->
<!--      <el-table-column prop="integral" label="用户积分" />-->
      <el-table-column prop="telephone" label="电话" />
<!--      <el-table-column prop="dingding" label="钉钉" />
      <el-table-column prop="weixin" label="微信" />
      <el-table-column prop="qq" label="QQ" />
      <el-table-column prop="mail" label="邮箱" />-->
      <el-table-column prop="addres" label="地址" >
        <template slot-scope="scope">
          <span>{{ scope.row.addres.indexOf('&*&') > 0 ? scope.row.addres.split('&*&')[0] + ' ' + scope.row.addres.split('&*&')[1] : scope.row.addres }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="mark" label="用户备注" />
      <el-table-column :show-overflow-tooltip="true" prop="addTime" label="创建日期">
        <template slot-scope="scope">
          <span>{{ formatTime(scope.row.addTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <div @click="onStatus(scope.row.uid,scope.row.status)">
            <el-tag v-if="scope.row.status == 1" style="cursor: pointer" :type="''">正常</el-tag>
            <el-tag v-else style="cursor: pointer" :type=" 'info' ">禁用</el-tag>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="用户来源" align="center">
        <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.userType == 'wechat'">公众号</el-tag>
            <el-tag v-else-if="scope.row.userType == 'routine'">小程序</el-tag>
            <el-tag v-else>H5</el-tag>
          </div>
        </template>
      </el-table-column>
<!--      <el-table-column prop="spreadUid" label="推荐人" />-->
      <el-table-column prop="payCount" label="购买次数" />
      <el-table-column v-if="checkPermission(['admin','YXUSER_ALL','YXUSER_EDIT','YXUSER_DELETE'])" label="操作" width="185" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button
            v-permission="['admin','YXUSER_ALL','YXUSER_EDIT']"
            size="mini"
            type="primary"
            icon="el-icon-edit"
            @click="edit(scope.row)"
          />
<!--          <el-dropdown size="mini" split-button type="primary" trigger="click">-->
<!--            操作-->
<!--            <el-dropdown-menu slot="dropdown">-->
<!--              <el-dropdown-item>-->
<!--                <el-button-->
<!--                  v-permission="['admin','YXUSER_ALL','YXUSER_EDIT']"-->
<!--                  size="mini"-->
<!--                  type="primary"-->
<!--                  @click="editP(scope.row)"-->
<!--                >修改余额</el-button>-->
<!--              </el-dropdown-item>-->
<!--            </el-dropdown-menu>-->
<!--          </el-dropdown>-->
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
    <!--<moreForm ref="form2" :is-more-add="isMoreAdd" />-->
  </div>
</template>

<script>
import checkPermission from '@/utils/permission'
import initData from '@/mixins/crud'
import { del, onStatus, onIsPass } from '@/api/yxUser'
import eForm from './form'
import pForm from './formp'
import { formatTime } from '@/utils/index'
import { getLevels } from  '@/api/yxSystemUserLevel'
/*import moreForm from './moreForm'*/
export default {
  components: { eForm, pForm },
  mixins: [initData],
  data() {
    return {
      delLoading: false,
      userType: '',
      queryTypeOptions: [
        { key: 'nickname', display_name: '用户昵称' },
        { key: 'phone', display_name: '手机号码' },
        { key: 'uid', display_name: '用户ID' }
      ],
      statusOptions: [
        { value: 'routine', label: '小程序' },
        { value: 'wechat', label: '公众号' },
        { value: 'H5', label: 'H5' }
      ],
      levelOptions:[],
      levelObj: {},
      certInfoList: [],
      tableData: [],
      isMoreAdd: false,
      usersList: [],
      usersExport: '',
      exportOptions: [
        {value: '', label: '批量导出'},
        {value: '0', label: '导出全部'},
        {value: '1', label: '导出选中'},
      ],
    }
  },
  created() {
    this.$nextTick(() => {
      this.init()
    })

  },
  mounted(){
    getLevels({}).then(({ content })=>{
        this.levelOptions=content;
      let levelObj = {};
      for(let item of content){
        levelObj[item.id] = item.name;
      }
      this.levelObj = levelObj;
    })
  },
  watch: {
  },
  methods: {
    formatTime,
    checkPermission,
    onIsPass(id, isPass, applyLevel) {
      isPass = isPass == 0?1:0;
      this.$confirm(`确定进行[${isPass ? '通过' : '不通过'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          onIsPass(id, { isPass: isPass, applyLevel: applyLevel }).then(({ data }) => {
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
    onStatus(id, status) {
      this.$confirm(`确定进行[${status ? '禁用' : '开启'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          onStatus(id, { status: status }).then(({ data }) => {
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
    beforeInit() {
      this.url = 'api/yxUser'
      const sort = 'uid,desc'
      this.params = { page: this.page, size: this.size, sort: sort, userType: this.userType }
      const query = this.query
      const type = query.type
      const value = query.value
      if (type && value) { this.params[type] = value }
      return true
    },
    subDelete(uid) {
      this.delLoading = true
      del(uid).then(res => {
        this.delLoading = false
        this.$refs[uid].doClose()
        this.dleChangePage()
        this.init()
        this.$notify({
          title: '删除成功',
          type: 'success',
          duration: 2500
        })
      }).catch(err => {
        this.delLoading = false
        this.$refs[uid].doClose()
      })
    },
    add() {
      this.isAdd = true
      this.$refs.form.dialog = true
    },
    edit(data) {
      this.isAdd = false;
      const _this = this.$refs.form
      _this.form = {
        uid: data.uid,
        account: data.account,
        pwd: data.pwd,
        realName: data.realName,
        birthday: data.birthday,
        cardId: data.cardId,
        mark: data.mark,
        partnerId: data.partnerId,
        groupId: data.groupId,
        nickname: data.nickname,
        avatar: data.avatar,
        phone: data.phone,
        addTime: data.addTime,
        addIp: data.addIp,
        lastTime: data.lastTime,
        lastIp: data.lastIp,
        nowMoney: data.nowMoney,
        brokeragePrice: data.brokeragePrice,
        integral: data.integral,
        signNum: data.signNum,
        status: data.status,
        // level: data.yxSystemUserLevel.id,
        yxSystemUserLevel: data.yxSystemUserLevel == null?{id:null}:data.yxSystemUserLevel,
        spreadUid: data.spreadUid,
        spreadTime: data.spreadTime,
        userType: data.userType,
        isPromoter: data.isPromoter,
        payCount: data.payCount,
        spreadCount: data.spreadCount,
        cleanTime: data.cleanTime,
        addres: data.addres,
        adminid: data.adminid,
        loginType: data.loginType,
        applyLevel: data.applyLevel,
        certInfo: data.certInfo,
        isCheck: data.isCheck,
        isPass: data.isPass
      }
      _this.dialog = true
    },
    editP(data) {
      this.isAdd = false
      const _this = this.$refs.formp
      _this.form = {
        uid: data.uid,
        nickname: data.nickname,
        ptype: 1,
        money: 0
      }
      _this.dialog = true
    },
    handleCertInfo(val){
      let certInfo = '';
      if(val != null) certInfo = val.startsWith('[')?JSON.parse(val)[0]:val;
      else certInfo = '';
      return certInfo;
    },
    handleCertInfo1(val){
      let certInfo = [];
      if(val != null) certInfo = val.startsWith('[')?JSON.parse(val):[val];
      else certInfo = [];
      return certInfo;
    },
    addMore(){
      this.isMoreAdd = true
      this.$refs.form2.dialog = true
      // this.$refs.form.getCates()
    },
    handleSelectionChange(val) {
      this.usersList = val;
    },
    handleExportOption(val){
      let list = this.usersList;
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
              message: '请选择会员信息!',
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
  }
}
</script>

<style scoped>

</style>
