/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.order.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.modules.order.entity.YxStoreOrderCartInfo;
import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;

import java.util.List;

/**
 * <p>
 * 订单购物详情表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
public interface YxStoreOrderCartInfoService extends BaseService<YxStoreOrderCartInfo> {

    void saveCartInfo(Integer oid, List<YxStoreCartQueryVo> cartInfo);

    YxStoreOrderCartInfo findByUni(String unique);


}
