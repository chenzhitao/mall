/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.web.controller;

import cn.hutool.core.util.StrUtil;
import co.yixiang.aop.log.Log;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.param.IdParam;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.user.entity.YxUserAddress;
import co.yixiang.modules.user.service.YxUserAddressService;
import co.yixiang.modules.user.web.param.AddressParam;
import co.yixiang.modules.user.web.param.YxUserAddressQueryParam;
import co.yixiang.modules.user.web.vo.YxUserAddressQueryVo;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 用户地前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2019-10-28
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "用户地址", tags = "用户:用户地址", description = "用户地址")
public class UserAddressController extends BaseController {

    private final YxUserAddressService userAddressService;

    /**
    * 添加或修改地址
    */
    @Log(value = "编辑地址",type = 1)
    @PostMapping("/address/edit")
    @ApiOperation(value = "添加或修改地址",notes = "添加或修改地址",response = ApiResult.class)
    public ApiResult<Map<String,Object>> addYxUserAddress(@Valid @RequestBody AddressParam param){
        int uid = SecurityUtils.getUserId().intValue();

        YxUserAddress userAddress = new YxUserAddress();
        userAddress.setCity(param.getAddress().getCity());
        userAddress.setDistrict(param.getAddress().getDistrict());
        userAddress.setProvince(param.getAddress().getProvince());
        if(param.getIs_default().equals("true")){
            userAddress.setIsDefault(1);
        }else{
            userAddress.setIsDefault(0);
        }
        userAddress.setDetail(param.getDetail());
        userAddress.setUid(uid);
        userAddress.setPhone(param.getPhone());
        userAddress.setPostCode(param.getPost_code());
        userAddress.setRealName(param.getReal_name());
        if(StrUtil.isEmpty(param.getId())){
            userAddress.setAddTime(OrderUtil.getSecondTimestampTwo());
            userAddressService.save(userAddress);
        }else{
            userAddress.setId(Integer.valueOf(param.getId()));
            userAddressService.updateById(userAddress);
        }
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("id",userAddress.getId());
        return ApiResult.ok(map);
    }

    /**
     * 设置默认地址
     */
    @PostMapping("/address/default/set")
    @ApiOperation(value = "设置默认地址",notes = "设置默认地址",response = ApiResult.class)
    public ApiResult<Boolean> setDefault(@Valid @RequestBody IdParam idParam){
        int uid = SecurityUtils.getUserId().intValue();
        YxUserAddress address = new YxUserAddress();
        address.setIsDefault(0);
        userAddressService.update(address,
                new QueryWrapper<YxUserAddress>().lambda().eq(YxUserAddress::getUid,uid));
        YxUserAddress userAddress = new YxUserAddress();
        userAddress.setIsDefault(1);
        userAddress.setId(Integer.valueOf(idParam.getId()));
        boolean flag = userAddressService.updateById(userAddress);
        return ApiResult.result(flag);
    }



    /**
    * 删除用户地址
    */
    @Log(value = "删除地址",type = 1)
    @PostMapping("/address/del")
    @ApiOperation(value = "删除用户地址",notes = "删除用户地址",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxUserAddress(@Valid @RequestBody IdParam idParam){
        YxUserAddress userAddress = new YxUserAddress();
        userAddress.setIsDel(1);
        userAddress.setId(Integer.valueOf(idParam.getId()));
        boolean flag = userAddressService.updateById(userAddress);
        return ApiResult.result(flag);
    }


    /**
     * 用户地址列表
     */
    @Log(value = "查看地址",type = 1)
    @GetMapping("/address/list")
    @ApiOperation(value = "用户地址列表",notes = "用户地址列表",response = YxUserAddressQueryVo.class)
    public ApiResult<Paging<YxUserAddressQueryVo>> getYxUserAddressPageList(YxUserAddressQueryParam queryParam){
        int uid = SecurityUtils.getUserId().intValue();
        queryParam.setUid(uid);
        queryParam.setIsDel(0);
        Paging<YxUserAddressQueryVo> paging = userAddressService.getYxUserAddressPageList(queryParam);
        return ApiResult.ok(paging.getRecords());
    }

    /**
     * 地址详情
     */
    @GetMapping("/address/detail/{id}")
    @ApiOperation(value = "地址详情",notes = "地址详情",response = ApiResult.class)
    public ApiResult<YxUserAddressQueryVo> addressDetail(@PathVariable Integer id){
        return ApiResult.ok(userAddressService.getYxUserAddressById(id));
    }

}

