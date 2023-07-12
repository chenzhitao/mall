package co.yixiang.modules.shop.rest;

import cn.hutool.core.util.StrUtil;
import co.yixiang.aop.log.Log;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.shop.domain.YxSystemUserLevel;
import co.yixiang.modules.shop.service.YxSystemUserLevelService;
import co.yixiang.modules.shop.service.YxUserService;
import co.yixiang.modules.shop.service.dto.YxSystemUserLevelQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxUserDTO;
import co.yixiang.utils.OrderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @author hupeng
* @date 2019-12-04
*/
@Api(tags = "商城:用户等级管理")
@RestController
@RequestMapping("api")
public class SystemUserLevelController {

    private final YxSystemUserLevelService yxSystemUserLevelService;
    private final YxUserService yxUserService;

    public SystemUserLevelController(YxSystemUserLevelService yxSystemUserLevelService, YxUserService yxUserService) {
        this.yxSystemUserLevelService = yxSystemUserLevelService;
        this.yxUserService = yxUserService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/yxSystemUserLevel")
    @PreAuthorize("@el.check('admin','YXSYSTEMUSERLEVEL_ALL','YXSYSTEMUSERLEVEL_SELECT')")
    public ResponseEntity getYxSystemUserLevels(YxSystemUserLevelQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(yxSystemUserLevelService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/yxSystemUserLevel")
    @PreAuthorize("@el.check('admin','YXSYSTEMUSERLEVEL_ALL','YXSYSTEMUSERLEVEL_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YxSystemUserLevel resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        return new ResponseEntity(yxSystemUserLevelService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改")
    @ApiOperation(value = "修改")
    @PutMapping(value = "/yxSystemUserLevel")
    @PreAuthorize("@el.check('admin','YXSYSTEMUSERLEVEL_ALL','YXSYSTEMUSERLEVEL_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxSystemUserLevel resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yxSystemUserLevelService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/yxSystemUserLevel/{id}")
    @PreAuthorize("@el.check('admin','YXSYSTEMUSERLEVEL_ALL','YXSYSTEMUSERLEVEL_DELETE')")
    public ResponseEntity<String> delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        List<YxUserDTO> yxUser = yxUserService.findByLevel(id);
        if(yxUser != null && yxUser.size() > 0){
            throw new BadRequestException("此会员级别已注册，不能删除!");
        }else{
            yxSystemUserLevelService.delete(id);
            return new ResponseEntity<>("删除成功!",HttpStatus.OK);
        }
    }
}