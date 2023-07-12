<template>
  <el-dialog id="printDialog" :append-to-body="false" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" title="订单打印" width="220mm">

    <el-button
      type="warning"
      class="filter-item"
      size="mini"
      icon="el-icon-printer"
      onclick="doPrint()"
    >打印</el-button>

    <div id="printList">
      <div class="order-list" v-for="(list, index) in printList">
      <el-header class="order-title">天山澳洋批发总惠核销单</el-header>
      <div class="order-info">
            <span class="info">提货人 : {{list.realName}}</span>
            <span class="info">联系电话 : {{list.userPhone}}</span>
      </div>
      <div class="order-info">
        <span class="info">订单号 : {{list.orderId}}</span>
        <span>下单日期 : {{formatTimeTwo(list.addTime)}}</span>
      </div>
    <!-- <div class="order-info">
        <span>业务经理：{{list.sotreUser}}</span>
    </div> -->
    <div class="order-info">
        <span>提货地址 : <span v-if="list.storeId>0">{{list.storeAddress}}</span><span v-else>{{list.userAddress }}</span></span>
    </div>
    <div class="table">
        <div class="table-box">
            <div class="table-cell">商品编号</div>
            <!--<div class="table-cell">商品名称</div>-->
            <div class="table-cell">商品规格</div>
          <div class="table-cell">包装规格</div>
            <div class="table-cell">单位</div>
            <div class="table-cell">数量</div>
            <div class="table-cell">单价</div>
            <div class="table-cell">小计(元)</div>
            <div class="table-cell">备注</div>
        </div>
        <div class="table-box" v-for="item in list.cartInfoList">
            <div class="table-cell">{{item.cartInfoMap.productInfo&&item.cartInfoMap.productInfo.attrInfo&&item.cartInfoMap.productInfo.attrInfo.barCode}}</div>
            <!--<div class="table-cell">{{item.cartInfoMap.productInfo&&item.cartInfoMap.productInfo.storeName}}</div>-->
            <div class="table-cell">{{item.cartInfoMap.productInfo&&item.cartInfoMap.productInfo.attrInfo&&item.cartInfoMap.productInfo.attrInfo.suk}}</div>
            <div class="table-cell">{{item.cartInfoMap.productInfo.attrInfo.packaging}}</div>
            <div class="table-cell">{{item.cartInfoMap.packaging}}</div>
            <div class="table-cell">{{item.cartInfoMap.cartNum}}</div>
            <div class="table-cell">{{item.cartInfoMap.productInfo&&item.cartInfoMap.productInfo.price}}</div>
            <div class="table-cell">{{(item.cartInfoMap.productInfo.price*item.cartInfoMap.cartNum).toFixed(2)}}</div>
            <div class="table-cell">{{item.cartInfoMap.mark}}</div>
        </div>
        <div class="table-box">
            <div class="table-cell">合计</div>
            <div class="table-cell"></div>
            <div class="table-cell"></div>
            <div class="table-cell"></div>
            <div class="table-cell"></div>
            <div class="table-cell"></div>
            <div class="table-cell">{{getCount(list.cartInfoList)}}</div>
            <div class="table-cell"></div>
        </div>
        <div class="table-box">
            <div class="table-cell">批发地址</div>
            <div class="table-cell" style="width: 40%;">新疆乌鲁木齐市月明楼批发市场 16-1-2 </div>
            <div class="table-cell">服务电话</div>
            <div class="table-cell" style="width: 35%;">0991-5621689, 18195960650</div>
        </div>
        <div class="table-box">
            <div class="table-cell">温馨提示</div>
            <div class="table-cell" style="width: 86%;">本商城产品如有质量问题, 请在保质期内退货, 过期食品退回一概不计</div>
        </div>
    </div>
    </div>
    </div>

  </el-dialog>
</template>

<script>
import { formatTime, parseTime, formatTimeTwo } from '@/utils/index'
export default {
 props: {
    printList: {
      type: Array,
      required: true
    },
    toQuery: {
      type: Function,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      dialog: false
    }
  },
  mounted() {
    window.doPrint = this.doPrint;
  },
  methods: {
    formatTime,
    parseTime,
    formatTimeTwo,
    cancel() {
      this.dialog = false
    },
    indexMethod(index){
      return index+1;
    },
    doPrint(){
      let printbox = document.querySelector("#printList").innerHTML;
      document.querySelector("body").innerHTML = printbox;
      window.print();
      this.cancel();
      // this.toQuery();
      window.location.reload();
    },
    getCount:function(list){
        let count=0;
        for(let i=0;i<list.length;i++){
            count+=Number(list[i].cartInfoMap.productInfo.price*list[i].cartInfoMap.cartNum)
        }
        return count+' 元';
    },
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计';
          return;
        }else if(index === 7){
          const values = data.map(item => Number(item.cartInfoMap.productInfo.price*item.cartInfoMap.cartNum));
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr);
            if (!isNaN(value)) {
              return prev + curr;
            } else {
              return prev;
            }
          }, 0);
          sums[index] += ' 元';
        }
      });
      return sums;
    }
  }
}
</script>

<style scoped lang="scss">
  /*打印单样式编辑*/
  .order-list {
    /*  height: 297mm;*/
    margin: 0 auto;
    width: 210mm;
    font-family: "宋体";
    .order-title {
      height: 16mm;
      line-height: 16mm;
      font-size: 30px;
      font-weight: bolder;
      text-align: center;
      font-weight: bold;
    }
    .order-info {
      span {
        display: inline-block;
        padding: 0 0 5px 0;
        font-size: 16px;
        font-weight: bold;
      }
      span.info {
        width: 32%;
      }
    }
    // .el-table thead{
    //     font-weight: normal;
    //     color: #000;
    // }
    .table{
        font-family: "宋体";
        font-size: 16px;
        margin-bottom: 30px;
        .table-box{
            display: flex;
            width: 100%;
            min-height: 30px;
            border-bottom: 2px solid #000;
            &:nth-of-type(1){
                border-top: 2px solid #000;
            }
            .table-cell{
                width: 100%;
                min-height: 100%;
                display: flex;
                align-items: center;
                justify-content: center;
                border-right: 2px solid #000;
                &:nth-of-type(1){
                    border-left: 2px solid #000;
                    width: 14%;
                }
                &:nth-of-type(2){
                    width: 29%;
                }
                &:nth-of-type(3){
                    width: 11%;
                }
                &:nth-of-type(4){
                    width: 8%;
                }
                &:nth-of-type(5){
                    width: 7%;
                }
                &:nth-of-type(6){
                    width: 8%;
                }
                &:nth-of-type(7){
                    width: 13%;
                }
                &:nth-of-type(8){
                    width: 10%;
                }
            }
        }
    }
  }

</style>
