package co.yixiang.modules.shop.rest;

import co.yixiang.aop.log.Log;
import co.yixiang.modules.shop.domain.YxVideoProduct;
import co.yixiang.modules.shop.service.YxVideoProductService;
import co.yixiang.modules.shop.service.dto.YxVideoProductQueryCriteria;
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
* @date 2023-06-08
*/
@Api(tags = "api管理")
@RestController
@RequestMapping("/api/yxVideoProduct")
public class YxVideoProductController {

    private final YxVideoProductService yxVideoProductService;

    public YxVideoProductController(YxVideoProductService yxVideoProductService) {
        this.yxVideoProductService = yxVideoProductService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('yxVideoProduct:list')")
    public void download(HttpServletResponse response, YxVideoProductQueryCriteria criteria) throws IOException {
        yxVideoProductService.download(yxVideoProductService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api")
    @ApiOperation("查询api")
    @PreAuthorize("@el.check('yxVideoProduct:list')")
    public ResponseEntity<Object> getYxVideoProducts(YxVideoProductQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxVideoProductService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api")
    @ApiOperation("新增api")
    @PreAuthorize("@el.check('yxVideoProduct:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxVideoProduct resources){
        return new ResponseEntity<>(yxVideoProductService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api")
    @ApiOperation("修改api")
    @PreAuthorize("@el.check('yxVideoProduct:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxVideoProduct resources){
        yxVideoProductService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api")
    @ApiOperation("删除api")
    @PreAuthorize("@el.check('yxVideoProduct:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        yxVideoProductService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}