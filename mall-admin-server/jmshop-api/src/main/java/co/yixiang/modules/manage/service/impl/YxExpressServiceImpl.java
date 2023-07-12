/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.manage.service.impl;

import co.yixiang.modules.manage.entity.YxExpress;
import co.yixiang.modules.manage.mapper.YxExpressMapper;
import co.yixiang.modules.manage.service.YxExpressService;
import co.yixiang.modules.manage.web.param.YxExpressQueryParam;
import co.yixiang.modules.manage.web.vo.YxExpressQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;


/**
 * <p>
 * 快递公司表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-13
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxExpressServiceImpl extends BaseServiceImpl<YxExpressMapper, YxExpress> implements YxExpressService {

    private final YxExpressMapper yxExpressMapper;

    @Override
    public YxExpressQueryVo getYxExpressById(Serializable id){
        return yxExpressMapper.getYxExpressById(id);
    }

    @Override
    public Paging<YxExpressQueryVo> getYxExpressPageList(YxExpressQueryParam yxExpressQueryParam) throws Exception{
        Page page = setPageParam(yxExpressQueryParam,OrderItem.desc("create_time"));
        IPage<YxExpressQueryVo> iPage = yxExpressMapper.getYxExpressPageList(page,yxExpressQueryParam);
        return new Paging(iPage);
    }

}
