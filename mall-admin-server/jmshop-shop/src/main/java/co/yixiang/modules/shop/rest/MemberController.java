package co.yixiang.modules.shop.rest;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.aop.log.Log;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.activity.service.YxStoreCouponIssueUserService;
import co.yixiang.modules.activity.service.dto.YxStoreCouponIssueUserDTO;
import co.yixiang.modules.activity.service.dto.YxStoreCouponIssueUserQueryCriteria;
import co.yixiang.modules.shop.domain.YxUser;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.service.YxUserLevelService;
import co.yixiang.modules.shop.service.YxUserService;
import co.yixiang.modules.shop.service.dto.UserMoneyDTO;
import co.yixiang.modules.shop.service.dto.YxUserDTO;
import co.yixiang.modules.shop.service.dto.YxUserQueryCriteria;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author hupeng
* @date 2019-10-06
*/
@Api(tags = "商城:会员管理")
@RestController
@RequestMapping("api")
public class MemberController {

    private final YxUserService yxUserService;
    private final YxSystemConfigService yxSystemConfigService;
    private final YxUserLevelService yxUserLevelService;

    private final YxStoreCouponIssueUserService yxStoreCouponIssueUserService;

    public MemberController(YxUserService yxUserService, YxSystemConfigService yxSystemConfigService, YxUserLevelService yxUserLevelService,YxStoreCouponIssueUserService yxStoreCouponIssueUserService) {
        this.yxUserService = yxUserService;
        this.yxSystemConfigService = yxSystemConfigService;
        this.yxUserLevelService = yxUserLevelService;
        this.yxStoreCouponIssueUserService = yxStoreCouponIssueUserService;
    }

    @Log("查询用户")
    @ApiOperation(value = "查询用户")
    @GetMapping(value = "/yxUser")
    @PreAuthorize("@el.check('admin','YXUSER_ALL','YXUSER_SELECT')")
    public ResponseEntity getYxUsers(YxUserQueryCriteria criteria, Pageable pageable){
        if(ObjectUtil.isNotNull(criteria.getIsPromoter())){
            if(criteria.getIsPromoter() == 1){
                String key = yxSystemConfigService.findByKey("store_brokerage_statu")
                        .getValue();
                if(Integer.valueOf(key) == 2){
                    return new ResponseEntity(null,HttpStatus.OK);
                }
            }
        }
        Map<String, Object> stringObjectMap = yxUserService.queryAll(criteria, pageable);
        return new ResponseEntity(stringObjectMap,HttpStatus.OK);
    }

    @Log("查询发送优惠券用户")
    @ApiOperation(value = "查询发送优惠券用户")
    @GetMapping(value = "/yxCouponUser/{id}")
    @PreAuthorize("@el.check('admin','YXUSER_ALL','YXUSER_SELECT')")
    public ResponseEntity getCouponUsers(@PathVariable Integer id){
        YxUserQueryCriteria param=new YxUserQueryCriteria();
        param.setStatus(1);
        List<YxUserDTO> stringObjectMap = yxUserService.queryAll(param);
        YxStoreCouponIssueUserQueryCriteria userCriteria=new YxStoreCouponIssueUserQueryCriteria();
        userCriteria.setIssueCouponId(id);
        List<YxStoreCouponIssueUserDTO> issuserList=yxStoreCouponIssueUserService.queryAll(userCriteria);
        List userList=new ArrayList();
        stringObjectMap.stream().forEach(user->{
            YxStoreCouponIssueUserDTO dto=issuserList.stream().filter(issuser->issuser.getUid().intValue()==user.getUid().intValue()).findFirst().orElse(null);
            if(dto==null) {
                JSONObject obj = new JSONObject();
                obj.put("uid", user.getUid());
                obj.put("key", user.getUid());
                obj.put("label","("+ user.getUid()+")"+user.getNickname() + user.getPhone());
                userList.add(obj);
            }
        });

        return new ResponseEntity(userList,HttpStatus.OK);
    }

    @Log("新增用户")
    @ApiOperation(value = "新增用户")
    @PostMapping(value = "/yxUser")
    @PreAuthorize("@el.check('admin','YXUSER_ALL','YXUSER_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YxUser resources){
        return new ResponseEntity(yxUserService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改用户")
    @ApiOperation(value = "修改用户")
    @PutMapping(value = "/yxUser")
    @PreAuthorize("@el.check('admin','YXUSER_ALL','YXUSER_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxUser resources){
        if(resources.getYxSystemUserLevel()==null || resources.getYxSystemUserLevel().getId()==null){
            resources.setYxSystemUserLevel(null);
        }
        yxUserService.update(resources);
        if(resources.getYxSystemUserLevel()!=null && resources.getYxSystemUserLevel().getId()!=null) {
            yxUserLevelService.setUserLevel(resources.getUid(), resources.getYxSystemUserLevel().getId());
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除用户")
    @ApiOperation(value = "删除用户")
    @DeleteMapping(value = "/yxUser/{uid}")
    @PreAuthorize("@el.check('admin','YXUSER_ALL','YXUSER_DELETE')")
    public ResponseEntity delete(@PathVariable Integer uid){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yxUserService.delete(uid);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "用户禁用启用")
    @PostMapping(value = "/yxUser/onStatus/{id}")
    public ResponseEntity onStatus(@PathVariable Integer id,@RequestBody String jsonStr){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        int status = Integer.valueOf(jsonObject.get("status").toString());
        yxUserService.onStatus(id,status);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "用户会员等级审核")
    @PostMapping(value = "/yxUser/onIsPass/{id}")
    public ResponseEntity onIsPass(@PathVariable Integer id,@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        int isPass = Integer.valueOf(jsonObject.get("isPass").toString());
        int applyLevel = Integer.valueOf(jsonObject.get("applyLevel").toString());
        if(isPass == 1){
            yxUserService.onIsPass(id,isPass,applyLevel);
            yxUserLevelService.setUserLevel(id,applyLevel);
            return new ResponseEntity(HttpStatus.OK);
        }else {
            applyLevel = 1;
            yxUserService.onIsPass(id,isPass,applyLevel);
            yxUserLevelService.setUserLevel(id,applyLevel);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @ApiOperation(value = "修改余额")
    @PostMapping(value = "/yxUser/money")
    @PreAuthorize("@el.check('admin','YXUSER_ALL','YXUSER_EDIT')")
    public ResponseEntity updatePrice(@Validated @RequestBody UserMoneyDTO param){
        yxUserService.updateMoney(param);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
