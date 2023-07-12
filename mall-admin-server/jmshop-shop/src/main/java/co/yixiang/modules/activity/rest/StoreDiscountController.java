package co.yixiang.modules.activity.rest;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.aop.log.Log;
import co.yixiang.modules.activity.domain.YxStoreDiscount;
import co.yixiang.modules.activity.service.YxStoreDiscountService;
import co.yixiang.modules.activity.service.dto.YxStoreDiscountDTO;
import co.yixiang.modules.activity.service.dto.YxStoreDiscountQueryCriteria;
import co.yixiang.utils.OrderUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "商城:商品折扣管理")
@RestController
@RequestMapping("api")
public class StoreDiscountController {

    private final YxStoreDiscountService yxStoreDiscountService;

    public StoreDiscountController(YxStoreDiscountService yxStoreDiscountService) {
        this.yxStoreDiscountService = yxStoreDiscountService;
    }

    @Log("查询折扣")
    @ApiOperation(value = "查询折扣")
    @GetMapping(value = "/yxStoreDiscount")
    @PreAuthorize("@el.check('admin','YxStoreDiscount_ALL','YxStoreDiscount_SELECT')")
    public ResponseEntity getYxStoreDiscounts(YxStoreDiscountQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(yxStoreDiscountService.queryAll(criteria,pageable),HttpStatus.OK);
    }



    @Log("修改折扣")
    @ApiOperation(value = "修改折扣")
    @PutMapping(value = "/yxStoreDiscount")
    @PreAuthorize("@el.check('admin','YxStoreDiscount_ALL','YxStoreDiscount_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxStoreDiscount resources){

        if(ObjectUtil.isNotNull(resources.getStartTimeDate())){
            resources.setStartTime(OrderUtil.
                    dateToTimestamp(resources.getStartTimeDate()));
        }
        if(ObjectUtil.isNotNull(resources.getEndTimeDate())){
            resources.setStopTime(OrderUtil.
                    dateToTimestamp(resources.getEndTimeDate()));
        }
        if(ObjectUtil.isNull(resources.getId())){
            List<Map> list = JSON.parseArray(resources.getJsonStr().toString()).toJavaList(Map.class);
            resources.setAddTime(OrderUtil.getSecondTimestampTwo());
            for(Map map : list){
               resources.setId((int)( Math.random () * ( 352324 +1) ));
               resources.setGrade(Integer.valueOf(map.get("grade").toString()));
               resources.setDiscount(Integer.valueOf(map.get("discount").toString().equals("") ? "100":map.get("discount").toString()));
               YxStoreDiscountDTO yxStoreDiscountDTO = yxStoreDiscountService.create(resources);
            }
            return new ResponseEntity(HttpStatus.CREATED);
        }else{
            yxStoreDiscountService.update(resources);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @Log("删除折扣")
    @ApiOperation(value = "删除折扣")
    @DeleteMapping(value = "/yxStoreDiscount/{id}")
    @PreAuthorize("@el.check('admin','YxStoreDiscount_ALL','YxStoreDiscount_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        yxStoreDiscountService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("查询折扣")
    @ApiOperation(value = "查询折扣")
    @GetMapping(value = "/yxStoreDiscount/search/{productId}")
    @PreAuthorize("@el.check('admin','YxStoreDiscount_ALL','YxStoreDiscount_SELECT')")
    public ResponseEntity findByProductId(@PathVariable("productId") Integer productId){
        return new ResponseEntity(yxStoreDiscountService.findByProductId(productId), HttpStatus.OK);
    }
}