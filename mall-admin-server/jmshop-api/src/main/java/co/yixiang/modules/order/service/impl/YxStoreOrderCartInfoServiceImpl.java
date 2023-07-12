/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.order.service.impl;

import cn.hutool.core.util.IdUtil;
import co.yixiang.modules.order.entity.YxStoreOrderCartInfo;
import co.yixiang.modules.order.mapper.YxStoreOrderCartInfoMapper;
import co.yixiang.modules.order.service.YxStoreOrderCartInfoService;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 订单购物详情表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreOrderCartInfoServiceImpl extends BaseServiceImpl<YxStoreOrderCartInfoMapper, YxStoreOrderCartInfo> implements YxStoreOrderCartInfoService {

    private final YxStoreOrderCartInfoMapper yxStoreOrderCartInfoMapper;


    @Override
    public YxStoreOrderCartInfo findByUni(String unique) {
        QueryWrapper<YxStoreOrderCartInfo> wrapper= new QueryWrapper<>();
        wrapper.eq("`unique`",unique);
        return yxStoreOrderCartInfoMapper.selectOne(wrapper);
    }

    @Override
    public void saveCartInfo(Integer oid, List<YxStoreCartQueryVo> cartInfo) {

        List<YxStoreOrderCartInfo> list = new ArrayList<>();
        for (YxStoreCartQueryVo cart : cartInfo) {
            YxStoreOrderCartInfo info = new YxStoreOrderCartInfo();
            info.setOid(oid);
            info.setCartId(cart.getId().intValue());
            info.setProductId(cart.getProductId());
            info.setCartInfo(JSONObject.toJSON(cart).toString());
            info.setUnique(IdUtil.simpleUUID());
            list.add(info);
        }

        saveBatch(list);
    }



}
