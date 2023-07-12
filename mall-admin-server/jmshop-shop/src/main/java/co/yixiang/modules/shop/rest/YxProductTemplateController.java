package co.yixiang.modules.shop.rest;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.aop.log.Log;
import co.yixiang.modules.shop.domain.YxProductTemplate;
import co.yixiang.modules.shop.domain.YxProductTemplateItem;
import co.yixiang.modules.shop.domain.YxStoreProduct;
import co.yixiang.modules.shop.service.YxProductTemplateItemService;
import co.yixiang.modules.shop.service.YxProductTemplateService;
import co.yixiang.modules.shop.service.YxStoreProductService;
import co.yixiang.modules.shop.service.dto.YxProductTemplateItemDto;
import co.yixiang.modules.shop.service.dto.YxProductTemplateItemQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxProductTemplateQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxStoreProductDTO;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
* @author yushen
* @date 2023-05-11
*/
@Api(tags = "api管理")
@RestController
@RequestMapping("/api/yxProductTemplate")
public class YxProductTemplateController {

    private final YxProductTemplateService yxProductTemplateService;

    private final YxProductTemplateItemService yxProductTemplateItemService;

    private final YxStoreProductService yxStoreProductService;

    public YxProductTemplateController(YxProductTemplateService yxProductTemplateService,YxProductTemplateItemService yxProductTemplateItemService,YxStoreProductService yxStoreProductService) {
        this.yxProductTemplateService = yxProductTemplateService;
        this.yxProductTemplateItemService = yxProductTemplateItemService;
        this.yxStoreProductService = yxStoreProductService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('yxProductTemplate:list')")
    public void download(HttpServletResponse response, YxProductTemplateQueryCriteria criteria) throws IOException {
        yxProductTemplateService.download(yxProductTemplateService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api")
    @ApiOperation("查询api")
    @PreAuthorize("@el.check('yxProductTemplate:list')")
    public ResponseEntity<Object> getYxProductTemplates(YxProductTemplateQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxProductTemplateService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api")
    @ApiOperation("新增api")
    @PreAuthorize("@el.check('yxProductTemplate:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxProductTemplate resources){
        return new ResponseEntity<>(yxProductTemplateService.create(resources),HttpStatus.CREATED);
    }

    @PostMapping(value = "/addTemplateItems")
    @ApiOperation(value = "模板增加商品",notes = "模板增加商品")
    public ResponseEntity<Object> addTemplateItems(@RequestBody YxProductTemplate productTemplate){
        if(ObjectUtil.isNull(productTemplate.getId())){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(ObjectUtil.isNull(productTemplate.getItemList())){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        productTemplate.getItemList().stream().forEach(item->{
            item.setTemplateId(productTemplate.getId().intValue());
            Long time=System.currentTimeMillis()/1000;
            item.setAddTime(time.intValue());
            YxProductTemplateItemQueryCriteria param=new YxProductTemplateItemQueryCriteria();
            param.setTemplateId(productTemplate.getId());
            param.setProductId(item.getProductId());
            List<YxProductTemplateItemDto> dtoList=yxProductTemplateItemService.queryAll(param);
            if(dtoList.size()==0) {
                yxProductTemplateItemService.create(item);
            }
        });

        return ResponseEntity.ok("ok");
    }

    @PostMapping(value = "/editTemplateItems")
    @ApiOperation(value = "编辑增加商品",notes = "模板编辑商品")
    public ResponseEntity<Object> editTemplateItems(@RequestBody YxProductTemplate productTemplate){
        if(ObjectUtil.isNull(productTemplate.getItemList())){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        productTemplate.getItemList().stream().forEach(item->{
                yxProductTemplateItemService.update(item);
        });

        return ResponseEntity.ok("ok");
    }

    @PutMapping
    @Log("修改api")
    @ApiOperation("修改api")
    @PreAuthorize("@el.check('yxProductTemplate:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxProductTemplate resources){
        yxProductTemplateService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api")
    @ApiOperation("删除api")
    @PreAuthorize("@el.check('yxProductTemplate:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        yxProductTemplateService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
