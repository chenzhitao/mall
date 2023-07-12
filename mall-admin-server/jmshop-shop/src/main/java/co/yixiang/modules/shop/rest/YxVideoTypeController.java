package co.yixiang.modules.shop.rest;

import co.yixiang.aop.log.Log;
import co.yixiang.modules.shop.domain.YxVideoType;
import co.yixiang.modules.shop.service.YxVideoTypeService;
import co.yixiang.modules.shop.service.dto.YxVideoTypeQueryCriteria;
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
@RequestMapping("/api/yxVideoType")
public class YxVideoTypeController {

    private final YxVideoTypeService yxVideoTypeService;

    public YxVideoTypeController(YxVideoTypeService yxVideoTypeService) {
        this.yxVideoTypeService = yxVideoTypeService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('yxVideoType:list')")
    public void download(HttpServletResponse response, YxVideoTypeQueryCriteria criteria) throws IOException {
        yxVideoTypeService.download(yxVideoTypeService.queryAll(criteria), response);
    }

    @GetMapping("/getAllVideoTypes")
    public ResponseEntity<Object> getAllVideoTypes(YxVideoTypeQueryCriteria criteria){
        return new ResponseEntity<>(yxVideoTypeService.queryAll(criteria),HttpStatus.OK);
    }

    @GetMapping
    @Log("查询api")
    @ApiOperation("查询api")
    @PreAuthorize("@el.check('yxVideoType:list')")
    public ResponseEntity<Object> getYxVideoTypes(YxVideoTypeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxVideoTypeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api")
    @ApiOperation("新增api")
    @PreAuthorize("@el.check('yxVideoType:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxVideoType resources){
        return new ResponseEntity<>(yxVideoTypeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api")
    @ApiOperation("修改api")
    @PreAuthorize("@el.check('yxVideoType:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxVideoType resources){
        yxVideoTypeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api")
    @ApiOperation("删除api")
    @PreAuthorize("@el.check('yxVideoType:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        yxVideoTypeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
