/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.activity.service;

import co.yixiang.modules.activity.entity.YxStorePink;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.activity.web.dto.PinkDTO;
import co.yixiang.modules.activity.web.dto.PinkInfoDTO;
import co.yixiang.modules.activity.web.param.YxStorePinkQueryParam;
import co.yixiang.modules.activity.web.vo.YxStorePinkQueryVo;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拼团表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-11-19
 */
public interface YxStorePinkService extends BaseService<YxStorePink> {

    void orderPinkFailAfter(int uid,int pid);

    void removePink(int uid,int cid,int pinkId);

    int surplusPeople(YxStorePink pink);

    List<YxStorePinkQueryVo> handPinkAll(List<YxStorePink> pinkAll);

    YxStorePinkQueryVo handPinkT(YxStorePink pinkT);

    YxStorePink getCurrentPink(int id,int uid);

    String getCurrentPinkOrderId(int id,int uid);

    PinkInfoDTO pinkInfo(int id, int uid);

    PinkDTO getPinkUserOneT(int id);

    void setPinkStopTime(List<Integer> idAll);

    boolean getPinkStatus(List<Integer> idAll);

    int pinkFail(List<YxStorePink> pinkAll,YxStorePink pinkT,int pinkBool);

    int pinkComplete(List<Integer> uidAll,List<Integer> idAll,int uid,YxStorePink pinkT);

    List<YxStorePink> getPinkMember(int kid);

    YxStorePink getPinkUserOne(int id);

    Map<String,Object> getPinkMemberAndPinK(YxStorePink pink);

    int  pinkIngCount(int id);

    void createPink(YxStoreOrderQueryVo order);

    int getIsPinkUid(int id,int uid);

    int getPinkOkSumTotalNum();

    List<String> getPinkOkList(int uid);

    int getPinkPeople(int kid,int people);

    Map<String,Object> getPinkAll(int cid, boolean isAll);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStorePinkQueryVo getYxStorePinkById(Serializable id);

    /**
     * 获取分页对象
     * @param yxStorePinkQueryParam
     * @return
     */
    Paging<YxStorePinkQueryVo> getYxStorePinkPageList(YxStorePinkQueryParam yxStorePinkQueryParam) throws Exception;

}
