package co.yixiang.modules.activity.rest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.aop.log.Log;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.activity.domain.YxStoreCoupon;
import co.yixiang.modules.activity.domain.YxStoreCouponAll;
import co.yixiang.modules.activity.domain.YxStoreCouponIssue;
import co.yixiang.modules.activity.service.YxStoreCouponIssueService;
import co.yixiang.modules.activity.service.YxStoreCouponService;
import co.yixiang.modules.activity.service.dto.YxStoreCouponDTO;
import co.yixiang.modules.activity.service.dto.YxStoreCouponQueryCriteria;
import co.yixiang.modules.shop.service.YxStoreCategoryService;
import co.yixiang.modules.shop.service.dto.YxStoreCategoryDTO;
import co.yixiang.mp.utils.JsonUtils;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.SecurityUtils;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hupeng
 * @date 2019-11-09
 */
@Api(tags = "商城:优惠券管理")
@RestController
@RequestMapping("api")
public class StoreCouponController {

    private final YxStoreCouponService yxStoreCouponService;

    private final YxStoreCouponIssueService yxStoreCouponIssueService;

    private final YxStoreCouponIssueService couponIssueService;

    private final YxStoreCategoryService yxStoreCategoryService;


    public StoreCouponController(YxStoreCouponService yxStoreCouponService, YxStoreCouponIssueService yxStoreCouponIssueService,YxStoreCouponIssueService couponIssueService,YxStoreCategoryService yxStoreCategoryService) {
        this.yxStoreCouponService = yxStoreCouponService;
        this.yxStoreCouponIssueService = yxStoreCouponIssueService;
        this.couponIssueService = couponIssueService;
        this.yxStoreCategoryService = yxStoreCategoryService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/yxStoreCoupon")
    @PreAuthorize("@el.check('admin','YXSTORECOUPON_ALL','YXSTORECOUPON_SELECT')")
    public ResponseEntity getYxStoreCoupons(YxStoreCouponQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(yxStoreCouponService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/yxStoreCoupon")
    @PreAuthorize("@el.check('admin','YXSTORECOUPON_ALL','YXSTORECOUPON_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YxStoreCoupon resources) {
        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        return new ResponseEntity(yxStoreCouponService.create(resources), HttpStatus.CREATED);
    }

    @Log("联合新增")
    @ApiOperation(value = "联合新增")
    @PostMapping(value = "/yxStoreCouponAll")
    @PreAuthorize("@el.check('admin','YXSTORECOUPON_ALL','YXSTORECOUPON_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YxStoreCouponAll yxStoreCouponAll) {
        //新增优惠券
        YxStoreCoupon yxStoreCoupon = new YxStoreCoupon();
        BeanUtil.copyProperties(yxStoreCouponAll, yxStoreCoupon);
        yxStoreCoupon.setAddTime(OrderUtil.getSecondTimestampTwo());
        yxStoreCoupon.setIsDel(0);
        yxStoreCoupon.setType(yxStoreCouponAll.getType());

        yxStoreCoupon.setRemark(yxStoreCouponAll.getRemark());
        yxStoreCoupon.setTimeType(yxStoreCouponAll.getTimeType());
        if(yxStoreCouponAll.getTimeType()!=null && yxStoreCouponAll.getTimeType().intValue()==0){
            if(ObjectUtil.isNull(yxStoreCouponAll.getStartTimeDate())){
                return new ResponseEntity("优惠券开启时间不能为空",HttpStatus.NO_CONTENT);
            }
            if(ObjectUtil.isNull(yxStoreCouponAll.getEndTimeDate())){
                return new ResponseEntity("优惠券结束时间不能为空",HttpStatus.NO_CONTENT);
            }
            yxStoreCoupon.setStartTime(OrderUtil.dateToTimestamp(yxStoreCouponAll.getStartTimeDate()));
            yxStoreCoupon.setCouponTime(OrderUtil.dateToTimestamp(yxStoreCouponAll.getEndTimeDate()));
        }else{
            yxStoreCoupon.setTimeNum(yxStoreCouponAll.getTimeNum());
        }
        yxStoreCouponService.create(yxStoreCoupon);

        //新增发布
        YxStoreCouponIssue resources = new YxStoreCouponIssue();
        resources.setCid(yxStoreCoupon.getId());
        resources.setCname(yxStoreCoupon.getTitle());
        if(ObjectUtil.isNotNull(yxStoreCouponAll.getCategoryId())){
            YxStoreCategoryDTO category=yxStoreCategoryService.findById(yxStoreCouponAll.getCategoryId());
            if(category!=null) {
                resources.setCategoryId(yxStoreCouponAll.getCategoryId());
                resources.setCategoryName(category.getCateName());
            }
        }

        resources.setIsPermanent(yxStoreCouponAll.getIsPermanent());
        resources.setIsDel(0);
        resources.setType(yxStoreCouponAll.getType());
        resources.setRemainCount(yxStoreCouponAll.getTotalCount());
        resources.setStatus(yxStoreCouponAll.getIssueStatus());
        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        resources.setTotalCount(yxStoreCouponAll.getTotalCount());
        resources.setTimeType(yxStoreCouponAll.getTimeType());
        if(yxStoreCouponAll.getTimeType()!=null && yxStoreCouponAll.getTimeType().intValue()==0){
            resources.setEndTime(OrderUtil.dateToTimestamp(yxStoreCouponAll.getEndTimeDate()));
            resources.setEndTimeDate(yxStoreCouponAll.getEndTimeDate());
            resources.setStartTimeDate(yxStoreCouponAll.getStartTimeDate());
            if (ObjectUtil.isNotNull(yxStoreCouponAll.getStartTimeDate())) {
                resources.setStartTime(OrderUtil.dateToTimestamp(yxStoreCouponAll.getStartTimeDate()));
            }
        }else{
            resources.setTimeNum(yxStoreCouponAll.getTimeNum());
        }

        return new ResponseEntity(yxStoreCouponIssueService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改")
    @ApiOperation(value = "修改")
    @PutMapping(value = "/yxStoreCoupon")
    @PreAuthorize("@el.check('admin','YXSTORECOUPON_ALL','YXSTORECOUPON_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxStoreCoupon resources) {
        yxStoreCouponService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/yxStoreCoupon/{id}")
    @PreAuthorize("@el.check('admin','YXSTORECOUPON_ALL','YXSTORECOUPON_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id) {
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        YxStoreCoupon resources = new YxStoreCoupon();
        resources.setId(id);
        resources.setIsDel(1);
        yxStoreCouponService.update(resources);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log(value = "批量发放优惠券",type = 1)
    @PostMapping("/coupon/batchSendCoupon")
    @ApiOperation(value = "批量发放优惠券",notes = "批量发放优惠券")
    public ResponseEntity<Object> batchSendCoupon(@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if(ObjectUtil.isNull(jsonObject.get("issueCouponId"))){
           return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        String uids=jsonObject.getString("uids");
        if(StringUtils.isEmpty(uids)){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        couponIssueService.issueUserCoupon(Integer.valueOf(jsonObject.get("issueCouponId").toString()),uids);
        return ResponseEntity.ok("ok");
    }
}
