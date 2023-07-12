/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.web.dto.PromUserDTO;
import co.yixiang.modules.user.web.param.PromParam;
import co.yixiang.modules.user.web.param.YxUserQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemLevelProduct;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-16
 */
public interface YxUserService extends BaseService<YxUser> {

    /**
     * 获取最优等级价格
     *
     * @param price
     * @return
     */
    YxSystemLevelProduct setYxSystemLevelProduct(Double price);

    double setLevelPrice(double price, int uid, int id);

    void incMoney(int uid, double price);

    void incIntegral(int uid, double integral);

    YxUserQueryVo getNewYxUserById(Serializable id);

    boolean backOrderBrokerage(YxStoreOrderQueryVo order);

    boolean backOrderBrokerageTwo(YxStoreOrderQueryVo order);

    void setUserSpreadCount(int uid);

    int getSpreadCount(int uid, int type);

    List<PromUserDTO> getUserSpreadGrade(PromParam promParam, int uid);

    boolean setSpread(int spread, int uid);

    void decIntegral(int uid, double integral);

    void incPayCount(int uid);

    void decPrice(int uid, double payPrice);

    YxUser findByName(String name);

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    YxUserQueryVo getYxUserById(Serializable id);

    /**
     * 获取分页对象
     *
     * @param yxUserQueryParam
     * @return
     */
    Paging<YxUserQueryVo> getYxUserPageList(YxUserQueryParam yxUserQueryParam) throws Exception;

}
