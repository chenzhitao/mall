package co.yixiang.modules.activity.rest;

import co.yixiang.aop.log.Log;
import co.yixiang.modules.activity.domain.YxStorePink;
import co.yixiang.modules.activity.service.YxStorePinkService;
import co.yixiang.modules.activity.service.dto.YxStorePinkQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
* @author hupeng
* @date 2019-11-18
*/
@Api(tags = "商城:拼团记录管理")
@RestController
@RequestMapping("api")
public class StorePinkController {

    private final YxStorePinkService yxStorePinkService;

    public StorePinkController(YxStorePinkService yxStorePinkService) {
        this.yxStorePinkService = yxStorePinkService;
    }

    @Log("查询记录")
    @ApiOperation(value = "查询记录")
    @GetMapping(value = "/yxStorePink")
    @PreAuthorize("@el.check('admin','YXSTOREPINK_ALL','YXSTOREPINK_SELECT')")
    public ResponseEntity getYxStorePinks(YxStorePinkQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(yxStorePinkService.queryAll(criteria,pageable),HttpStatus.OK);
    }


}