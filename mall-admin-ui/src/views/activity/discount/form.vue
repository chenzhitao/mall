<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialogInfo.visible" title="商品折扣" width="900px">
    <el-form ref="form" :model="form" size="small" :rules="rules" label-width="90px">
        <el-row :gutter="20">
            <el-col :span="12">
                <el-form-item label="折扣名称" prop="title">
                    <el-input v-model="form.title" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="商品原价">
                    <el-input v-model="form.price" disabled />
                </el-form-item>
            </el-col>
            <el-col :span="24">
                <el-form-item label="折扣简介">
                    <el-input v-model="form.info" rows="5" type="textarea" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="开始时间">
                    <el-date-picker
                        v-model="form.startTimeDate"
                        type="datetime"
                        placeholder="选择日期时间"
                    />
                 <!-- value-format="yyyy-MM-dd HH:mm:ss"
                  format="yyyy-MM-dd HH:mm:ss"-->
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="结束时间">
                    <el-date-picker
                        v-model="form.endTimeDate"
                        type="datetime"
                        placeholder="选择日期时间"
                    />
                  <!--value-format="yyyy-MM-dd HH:mm:ss"
                  format="yyyy-MM-dd HH:mm:ss"-->
                </el-form-item>
            </el-col>

            <div v-for="(item, index) in levels" :key="index">
                <el-col :span="8">
                    <el-form-item label="会员等级">
                        <el-input v-model="item.name" disabled/>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="折扣率(%)">
                        <el-input v-model="item.discount" @focus="discountFn(item,index,'discount')"/> <!--:value="(item.discount/form.price*100).toFixed(2)"-->
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="折扣价">
                        <el-input v-model="item.disprice" @focus="priceFn(item,index,'disprice')" />  <!--(form.price*item.discount/100).toFixed(2)-->
                    </el-form-item>
                </el-col>
            </div>
        </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { add, edit, search } from '@/api/yxStoreDiscount'
import initData from '@/mixins/crud'
import { getLevels } from  '@/api/yxSystemUserLevel'
import {FormatDate} from '@/utils/Tools'
export default {
    components: { },
    props:["dialogInfo"],
    created(){
        this.form=Object.assign(this.form,this.dialogInfo.data);
        this.getInfo();
    },
    data() {
        return {
            loading: false,
            form: {
                id: '',
                productId: '',
                price: 0,
                grade: '',
                discount: '',
                title: '',
                startTimeDate: '',
                endTimeDate: '',
                status: 1,
                info: '',
                sort: 0,
                isDel: 0,
                addTime: '',
                jsonStr: ''
            },
            rules:{
                title:[
                    { required: true, trigger: 'change',message: "非空" }
                ]
            },
            levels: [],
            finishFlag:false,
            discountFlag:true,  // 默认修改折扣
            currentItem:{},
            currentIndex:"",
            current:""
        }
    },
    mounted(){

    },
    watch: {
        levels:{
            handler:function(val){
                if(this.finishFlag){
                    this.finishFlag=false;
                    let item=this.levels[this.currentIndex];
                    if(this.current=="discount"){
                        item.disprice=(Math.round(this.form.price*item.discount))/100
                    }else if(this.current=="disprice"){
                        item.discount=(Math.round((item.disprice*100*100/this.form.price).toFixed(2)))/100;
                    }
                    this.$nextTick(()=>{
                        this.finishFlag = true;
                    })
                }
            },
            deep: true,
            immediate: false
        }
    },
    methods: {
        discountFn:function(item,index,info){
            this.currentItem=item;
            this.currentIndex=index;
            this.current=info;
            this.discountFlag=true;
        },
        priceFn:function(item,index,info){
            this.currentItem=item;
            this.currentIndex=index;
            this.current=info;
            this.discountFlag=false;
        },
        getInfo:function(){
            if(this.form.productId==""){
                return ;
            }
            search(this.form.productId).then(res => {
                if(res.jsonStr != null){
                    this.form=Object.assign(this.form,res);
                    this.levels = JSON.parse(res.jsonStr);
                    this.$nextTick(()=>{
                        this.finishFlag = true;
                    })
                }else {
                    getLevels({}).then(({ content })=> {
                        for (let item of content) {
                            let levelObj = {
                                id:item.id,
                                name:item.name,
                                grade:item.grade,
                                discount:"",
                                disprice:"",
                            };
                            this.levels.push(levelObj);
                            this.$nextTick(()=>{
                                this.finishFlag = true;
                            })
                        }
                    })
                }
            })
        },
        cancel() {
            this.dialogInfo.visible=false;
        },
        doSubmit() {
            this.$refs['form'].validate((valid) => {
                if(valid){ //验证通过
                    this.loading = true
                    if (this.form.id) {
                        this.doEdit()
                    } else this.doAdd()
                }
            })
        },
        doAdd() {
            let params=Object.assign({},this.form);
            // let startTime = this.form.startTimeDate;
            // let endTime = this.form.endTimeDate;
            params.startTimeDate = params.startTimeDate == "" ? FormatDate("yyyy-MM-dd HH:mm:ss") : params.startTimeDate;
            params.endTimeDate = params.endTimeDate == "" ? FormatDate("yyyy-MM-dd HH:mm:ss","2099-01-01 00:00:00") : params.endTimeDate;
            params.jsonStr = JSON.stringify(this.levels);
            add(params).then(res => {
                this.$notify({
                title: '添加成功',
                type: 'success',
                duration: 2500
                })
                this.dialogInfo.visible=false;
                this.$emit("backInfo");
                this.loading = false
            }).catch(err => {
                this.loading = false
            })
        },
        doEdit() {
            let params=Object.assign({},this.form);
            // let startTime = this.form.startTimeDate;
            // let endTime = this.form.endTimeDate;

            params.startTimeDate = params.startTimeDate == "" ? FormatDate("yyyy-MM-dd HH:mm:ss") : params.startTimeDate;
            params.endTimeDate = params.endTimeDate == "" ? FormatDate("yyyy-MM-dd HH:mm:ss","2099-01-01 00:00:00") : params.endTimeDate;
            params.jsonStr = JSON.stringify(this.levels);
            edit(params).then(res => {
                this.$notify({
                title: '修改成功',
                type: 'success',
                duration: 2500
                })
                this.$emit("backInfo");
                this.dialogInfo.visible=false;
                this.loading = false
            }).catch(err => {
                this.loading = false
            })
        },
    }
}
</script>

<style scoped>

</style>
