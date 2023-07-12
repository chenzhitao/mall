package co.yixiang.modules.shop.service;

import co.yixiang.modules.shop.entity.YxSystemStoreStaff;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.web.param.YxSystemStoreStaffQueryParam;
import co.yixiang.modules.shop.web.vo.YxSystemStoreStaffQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 门店店员表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-03-23
 */
public interface YxSystemStoreStaffService extends BaseService<YxSystemStoreStaff> {

    boolean checkStatus(int uid,int storeId);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSystemStoreStaffQueryVo getYxSystemStoreStaffById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxSystemStoreStaffQueryParam
     * @return
     */
    Paging<YxSystemStoreStaffQueryVo> getYxSystemStoreStaffPageList(YxSystemStoreStaffQueryParam yxSystemStoreStaffQueryParam) throws Exception;

}
