<template>
  <el-dialog id="printDialog" :append-to-body="false" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialogInfo.visible" title="订单打印" width="230mm">

    <el-button
      type="warning"
      class="filter-item"
      size="mini"
      icon="el-icon-printer"
      onclick="doPrint()"
    >打印</el-button>

    <div id="printList">
        <div class="order-list" v-for="(list, index) in printList" :key="index">
            <div v-for="number in Math.ceil((list.cartInfoList.length)/29)">
                <div>
                    <el-header class="order-title">天山澳洋批发总惠订货单</el-header>
                    <div class="order-info">
                        <span class="info">客户名称 : {{list.realName}}</span>
                        <span class="info">联系人 : {{list.userDTO.account}}</span>
                        <span class="info">联系电话 : {{list.userPhone}}</span>
                    </div>
                    <div class="order-info">
                        <span class="info">订单号 : {{list.orderId}}</span>
                        <span>下单日期 : {{formatTimeTwo(list.addTime)}}</span>
                    </div>
                    <div class="order-info">
                        <span>业务经理：{{list.sotreUser}}</span>
                    </div>
                    <div class="order-info">
                        <span>收货地址 : {{list.userAddress}}</span>
                    </div>
                </div>
                <div class="table">
                    <div class="table-box">
                        <div class="table-cell">商品编号</div>
                            <!--<div class="table-cell">商品名称</div>-->
                            <div class="table-cell">商品名称规格</div>
                            <div class="table-cell">包装规格</div>
                            <div class="table-cell">单位</div>
                            <div class="table-cell">数量</div>
                            <div class="table-cell">单价</div>
                            <div class="table-cell">小计(元)</div>
                            <div class="table-cell">备注</div>
                        </div>
                        
                        <div class="table-box" v-for="(item,iindex) in list.cartInfoList" :key="iindex" v-if="(number-1)*29<=iindex&&iindex<number*29">
                            <div class="table-cell">{{item.cartInfoMap.productInfo&&item.cartInfoMap.productInfo.attrInfo&&item.cartInfoMap.productInfo.attrInfo.barCode}}</div>
                            <!--<div class="table-cell">{{item.cartInfoMap.productInfo&&item.cartInfoMap.productInfo.storeName}}</div>-->
                            <div class="table-cell">{{item.cartInfoMap.productInfo&&item.cartInfoMap.productInfo.attrInfo&&item.cartInfoMap.productInfo.attrInfo.suk}}</div>
                            <div class="table-cell">{{item.cartInfoMap.productInfo.attrInfo.packaging}}</div>
                            <div class="table-cell">{{item.cartInfoMap.packaging}}</div>
                            <div class="table-cell">{{item.cartInfoMap.cartNum}}</div>
                            <div class="table-cell">{{item.cartInfoMap&&item.cartInfoMap.truePrice}}</div>
                            <div class="table-cell">{{(item.cartInfoMap.truePrice*item.cartInfoMap.cartNum).toFixed(2)}}</div>
                            <div class="table-cell">{{item.cartInfoMap.mark}}</div>
                        </div>

                        <div class="table-box">
                            <div class="table-cell">合计</div>
                            <div class="table-cell" style="width:65%;">{{getChinese(list.cartInfoList)}}</div>
                            <div class="table-cell" style="width:10%;">{{getCount(list.cartInfoList)}}</div>
                            <div class="table-cell" style="width:10%;"></div>
                        </div>
                        <div class="table-box">
                            <div class="table-cell">批发地址</div>
                            <div class="table-cell" style="width: 40%;">新疆乌鲁木齐市月明楼批发市场 16-1-2 </div>
                            <div class="table-cell" style="width:12%;">客服电话</div>
                            <div class="table-cell" style="width: 34%;">0991-5621689, 18195960650</div>
                        </div>
                        <div class="table-box">
                            <div class="table-cell">温馨提示</div>
                            <div class="table-cell" style="width: 76%;">本商城产品如有质量问题, 请在保质期内退货, 过期食品退回一概不计</div>
                            <div class="table-cell" style="width:10%;">{{number}} / {{Math.ceil(list.cartInfoList.length/29)}}</div>
                        </div>
                    </div>
                    <div v-if="number==Math.ceil(list.cartInfoList.length/29)" :style="{height:28*(29-list.cartInfoList.length%29)+'px'}">
                    </div>
                </div>
            </div>
        </div>
    </div>
  </el-dialog>
</template>

<script>
import { formatTime, parseTime, formatTimeTwo } from '@/utils/index'
export default {
 props:["dialogInfo","printList"],
  data() {
    return {
        loading: false,
        UpperPrice:[],
        changePrice:[],
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
    //   this.dialog = false
        this.dialogInfo.visible=false;
    },
    indexMethod(index){
      return index+1;
    },
    doPrint(){
      let printbox = document.querySelector("#printList").innerHTML;
      document.querySelector("body").innerHTML = printbox;
      window.print();
      this.cancel();
      window.location.reload();
    },
    getCount:function(list){
        let count=0,num=0;
        for(let i=0;i<list.length;i++){
            //这样是为了避免精度问题
            num=(Number(list[i].cartInfoMap.truePrice)*Number(list[i].cartInfoMap.cartNum)).toFixed(2);
            count=(Number(count)+Number(num)).toFixed(2);
        }
        return count;
    },
    getChinese:function(list){
        //只有再次计算，求中文，因为循环中vue状态变量只能改变一次
        let count=0,num=0;
        for(let i=0;i<list.length;i++){
            //这样是为了避免精度问题
            num=(Number(list[i].cartInfoMap.truePrice)*Number(list[i].cartInfoMap.cartNum)).toFixed(2);
            count=(Number(count)+Number(num)).toFixed(2);
        }
        return this.numberChinese(count);
    },
     numberChinese:function(str) {
        var num = parseFloat(str);
        var strOutput = "",
            strUnit = '仟佰拾亿仟佰拾万仟佰拾元角分';
        num += "00";
        var intPos = num.indexOf('.');  
        if (intPos >= 0){
            num = num.substring(0, intPos) + num.substr(intPos + 1, 2);
        }
        strUnit = strUnit.substr(strUnit.length - num.length);
        for (var i=0; i < num.length; i++){
            strOutput += '零壹贰叁肆伍陆柒捌玖'.substr(num.substr(i,1),1) + strUnit.substr(i,1);
        }
        return strOutput.replace(/零角零分$/, '整').replace(/零[仟佰拾]/g, '零').replace(/零{2,}/g, '零').replace(/零([亿|万])/g, '$1').replace(/零+元/, '元').replace(/亿零{0,3}万/, '亿').replace(/^元/, "零元")
    },
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计';
          return;
        }else if(index === 7){
          const values = data.map(item => Number(item.cartInfoMap.truePrice*item.cartInfoMap.cartNum));
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
  },
}
</script>

<style scoped lang="scss">
  /*打印单样式编辑*/
    /deep/ .el-dialog__body{
        padding: 30px 10mm;
    }
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
            width: 100%;
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
                        width: 15%;
                    }
                    &:nth-of-type(2){
                        width: 30%;
                    }
                    &:nth-of-type(3){
                        width: 15%;
                    }
                    &:nth-of-type(4){
                        width: 6%;
                    }
                    &:nth-of-type(5){
                        width: 6%;
                    }
                    &:nth-of-type(6){
                        width: 8%;
                    }
                    &:nth-of-type(7){
                        width: 10%;
                    }
                    &:nth-of-type(8){
                        width: 10%;
                    }
                }
            }
        }
    }
    

</style>
