package co.yixiang.modules.shop.rest;

import co.yixiang.aop.log.Log;
import co.yixiang.modules.shop.domain.YxProductTemplateItem;
import co.yixiang.modules.shop.service.YxProductTemplateItemService;
import co.yixiang.modules.shop.service.dto.YxProductTemplateItemQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author yushen
* @date 2023-05-11
*/
@Api(tags = "api管理")
@RestController
@RequestMapping("/api/yxProductTemplateItem")
public class YxProductTemplateItemController {

    private final YxProductTemplateItemService yxProductTemplateItemService;

    public YxProductTemplateItemController(YxProductTemplateItemService yxProductTemplateItemService) {
        this.yxProductTemplateItemService = yxProductTemplateItemService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('yxProductTemplateItem:list')")
    public void download(HttpServletResponse response, YxProductTemplateItemQueryCriteria criteria) throws IOException {
        yxProductTemplateItemService.download(yxProductTemplateItemService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api")
    @ApiOperation("查询api")
    @PreAuthorize("@el.check('yxProductTemplateItem:list')")
    public ResponseEntity<Object> getYxProductTemplateItems(YxProductTemplateItemQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxProductTemplateItemService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api")
    @ApiOperation("新增api")
    @PreAuthorize("@el.check('yxProductTemplateItem:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxProductTemplateItem resources){
        return new ResponseEntity<>(yxProductTemplateItemService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api")
    @ApiOperation("修改api")
    @PreAuthorize("@el.check('yxProductTemplateItem:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxProductTemplateItem resources){
        yxProductTemplateItemService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api")
    @ApiOperation("删除api")
    @PreAuthorize("@el.check('yxProductTemplateItem:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        yxProductTemplateItemService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}