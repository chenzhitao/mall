/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service.impl;

import co.yixiang.enums.BillDetailEnum;
import co.yixiang.enums.BillEnum;
import co.yixiang.enums.BillInfoEnum;
import co.yixiang.modules.activity.entity.YxStorePink;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.mapper.YxUserBillMapper;
import co.yixiang.modules.user.mapping.BiillMap;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.web.dto.BillDTO;
import co.yixiang.modules.user.web.dto.BillOrderDTO;
import co.yixiang.modules.user.web.dto.BillOrderRecordDTO;
import co.yixiang.modules.user.web.param.YxUserBillQueryParam;
import co.yixiang.modules.user.web.vo.YxUserBillQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;
import java.util.*;


/**
 * <p>
 * 用户账单表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxUserBillServiceImpl extends BaseServiceImpl<YxUserBillMapper, YxUserBill> implements YxUserBillService {

    private final YxUserBillMapper yxUserBillMapper;

    private final BiillMap biillMap;


    /**
     * 签到了多少次
     * @param uid
     * @return
     */
    @Override
    public int cumulativeAttendance(int uid) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",uid).eq("category","integral")
                .eq("type","sign").eq("pm",1);
        return yxUserBillMapper.selectCount(wrapper);
    }

    @Override
    public Map<String, Object> spreadOrder(int uid, int page, int limit) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.in("uid",uid).eq("type","brokerage")
                .eq("category","now_money").orderByDesc("time")
                .groupBy("time");
        Page<YxUserBill> pageModel = new Page<>(page, limit);
        List<String> list = yxUserBillMapper.getBillOrderList(wrapper,pageModel);


//        QueryWrapper<YxUserBill> wrapperT = new QueryWrapper<>();
//        wrapperT.in("uid",uid).eq("type","brokerage")
//                .eq("category","now_money");

        int count = yxUserBillMapper.selectCount(Wrappers.<YxUserBill>lambdaQuery()
                .eq(YxUserBill::getUid,uid)
                .eq(YxUserBill::getType, BillDetailEnum.TYPE_2.getValue())
                .eq(YxUserBill::getCategory,BillDetailEnum.CATEGORY_1.getValue()));
        List<BillOrderDTO> listT = new ArrayList<>();
        for (String str : list) {
            BillOrderDTO billOrderDTO = new BillOrderDTO();
            List<BillOrderRecordDTO> orderRecordDTOS = yxUserBillMapper
                    .getBillOrderRList(str,uid);
            billOrderDTO.setChild(orderRecordDTOS);
            billOrderDTO.setCount(orderRecordDTOS.size());
            billOrderDTO.setTime(str);

            listT.add(billOrderDTO);
        }

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("list",listT);
        map.put("count",count);

        return map;
    }

    @Override
    public List<BillDTO> getUserBillList(int page, int limit, int uid, int type) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",uid).orderByDesc("add_time").groupBy("time");
        switch (BillInfoEnum.toType(type)){
            case PAY_PRODUCT:
                wrapper.eq("category","now_money");
                wrapper.eq("type","pay_product");
                break;
            case RECHAREGE:
                wrapper.eq("category","now_money");
                wrapper.eq("type","recharge");
                break;
            case BROKERAGE:
                wrapper.eq("category","now_money");
                wrapper.eq("type","brokerage");
                break;
            case EXTRACT:
                wrapper.eq("category","now_money");
                wrapper.eq("type","extract");
                break;
            case SIGN_INTEGRAL:
                wrapper.eq("category","integral");
                wrapper.eq("type","sign");
                break;
            default:
                wrapper.eq("category","now_money");
                String str = "recharge,brokerage,pay_product,system_add,pay_product_refund,system_sub";
                wrapper.in("type", Arrays.asList(str.split(",")));

        }
        Page<YxUserBill> pageModel = new Page<>(page, limit);
        List<BillDTO> billDTOList = yxUserBillMapper.getBillList(wrapper,pageModel);
        for (BillDTO billDTO : billDTOList) {
            QueryWrapper<YxUserBill> wrapperT = new QueryWrapper<>();
            wrapperT.in("id",Arrays.asList(billDTO.getIds().split(",")));
            billDTO.setList(yxUserBillMapper.getUserBillList(wrapperT));

        }

        return billDTOList;
    }

    @Override
    public double getBrokerage(int uid) {
        return yxUserBillMapper.sumPrice(uid);
    }

    @Override
    public double yesterdayCommissionSum(int uid) {
        return yxUserBillMapper.sumYesterdayPrice(uid);
    }

    @Override
    public List<YxUserBillQueryVo> userBillList(int uid, int page,
                                                int limit, String category) {
        QueryWrapper<YxUserBill> wrapper = new QueryWrapper<>();
        wrapper.eq("status",1).eq("uid",uid)
                .eq("category",category).orderByDesc("add_time");
        Page<YxUserBill> pageModel = new Page<>(page, limit);

        IPage<YxUserBill> pageList = yxUserBillMapper.selectPage(pageModel,wrapper);
        return biillMap.toDto(pageList.getRecords());
    }

    @Override
    public YxUserBillQueryVo getYxUserBillById(Serializable id) throws Exception{
        return yxUserBillMapper.getYxUserBillById(id);
    }

    @Override
    public Paging<YxUserBillQueryVo> getYxUserBillPageList(YxUserBillQueryParam yxUserBillQueryParam) throws Exception{
        Page page = setPageParam(yxUserBillQueryParam,OrderItem.desc("add_time"));
        IPage<YxUserBillQueryVo> iPage = yxUserBillMapper.getYxUserBillPageList(page,yxUserBillQueryParam);
        return new Paging(iPage);
    }

}
