/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service;

import co.yixiang.modules.user.entity.YxUserRecharge;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.user.web.param.RechargeParam;
import co.yixiang.modules.user.web.param.YxUserRechargeQueryParam;
import co.yixiang.modules.user.web.vo.YxUserRechargeQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * @author : godly.strong
 * mail : huangjunquan1109@163.com
 * @since : 2023/2/9 10:41
 * describe ：用户充值表-服务类
 */
public interface YxUserRechargeService extends BaseService<YxUserRecharge> {

    /**
     * 更新记录
     *
     * @param userRecharge
     */
    void updateRecharge(YxUserRecharge userRecharge);

    /**
     * 获取订单的记录
     *
     * @param orderId
     * @return
     */
    YxUserRecharge getInfoByOrderId(String orderId);

    /**
     * 新增记录
     *
     * @param param
     * @param uid
     */
    void addRecharge(RechargeParam param, int uid);

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    YxUserRechargeQueryVo getYxUserRechargeById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param yxUserRechargeQueryParam
     * @return
     */
    Paging<YxUserRechargeQueryVo> getYxUserRechargePageList(YxUserRechargeQueryParam yxUserRechargeQueryParam) throws Exception;

}
