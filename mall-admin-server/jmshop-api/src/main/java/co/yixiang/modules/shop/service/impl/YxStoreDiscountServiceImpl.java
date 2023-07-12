/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.modules.shop.entity.YxStoreDiscount;
import co.yixiang.modules.shop.mapper.YxStoreDiscountMapper;
import co.yixiang.modules.shop.service.YxStoreDiscountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;


/**
 *
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreDiscountServiceImpl extends BaseServiceImpl<YxStoreDiscountMapper, YxStoreDiscount> implements YxStoreDiscountService {

    private final YxStoreDiscountMapper yxStoreDiscountMapper;

}
