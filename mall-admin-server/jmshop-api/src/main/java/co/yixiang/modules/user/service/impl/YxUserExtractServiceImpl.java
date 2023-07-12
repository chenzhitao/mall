/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.enums.BillEnum;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.entity.YxUserExtract;
import co.yixiang.modules.user.mapper.YxUserExtractMapper;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserExtractService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.param.UserExtParam;
import co.yixiang.modules.user.web.param.YxUserExtractQueryParam;
import co.yixiang.modules.user.web.vo.YxUserExtractQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.utils.OrderUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>
 * 用户提现表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-11-11
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxUserExtractServiceImpl extends BaseServiceImpl<YxUserExtractMapper, YxUserExtract> implements YxUserExtractService {

    private final YxUserExtractMapper yxUserExtractMapper;

    private final YxUserService userService;
    private final YxUserBillService billService;

    /**
     * 开始提现
     * @param uid
     * @param param
     */
    @Override
    public void userExtract(int uid, UserExtParam param) {
        YxUserQueryVo userInfo = userService.getYxUserById(uid);
        double extractPrice = userInfo.getBrokeragePrice().doubleValue();
        if(extractPrice <= 0) {
            throw new ErrorRequestException("提现佣金不足");
        }

        double money = Double.valueOf(param.getMoney());
        if(money > extractPrice) {
            throw new ErrorRequestException("提现佣金不足");
        }

        if(money <= 0) {
            throw new ErrorRequestException("提现佣金大于0");
        }

        double balance = NumberUtil.sub(extractPrice,money);
        if(balance < 0) {
            balance = 0;
        }

        YxUserExtract userExtract = new YxUserExtract();
        userExtract.setUid(uid);
        userExtract.setExtractType(param.getExtractType());
        userExtract.setExtractPrice(new BigDecimal(param.getMoney()));
        userExtract.setAddTime(OrderUtil.getSecondTimestampTwo());
        userExtract.setBalance(BigDecimal.valueOf(balance));
        userExtract.setStatus(0);

        if(StrUtil.isNotEmpty(param.getName())){
            userExtract.setRealName(param.getName());
        }else {
            userExtract.setRealName(userInfo.getNickname());
        }

        if(StrUtil.isNotEmpty(param.getWeixin())){
            userExtract.setWechat(param.getWeixin());
        }else {
            userExtract.setWechat(userInfo.getNickname());
        }

        String mark = "";
        if(param.getExtractType().equals("alipay")){
            if(StrUtil.isEmpty(param.getAlipayCode())){
                throw new ErrorRequestException("请输入支付宝账号");
            }
            userExtract.setAlipayCode(param.getAlipayCode());
            mark = "使用支付宝提现"+param.getMoney()+"元";
        }else if(param.getExtractType().equals("weixin")){
            if(StrUtil.isEmpty(param.getWeixin())){
                throw new ErrorRequestException("请输入微信账号");
            }

            mark = "使用微信提现"+param.getMoney()+"元";
        }

        yxUserExtractMapper.insert(userExtract);

        //更新佣金
        YxUser yxUser = new YxUser();
        yxUser.setBrokeragePrice(BigDecimal.valueOf(balance));
        yxUser.setUid(uid);
        userService.updateById(yxUser);
        //插入流水
        YxUserBill userBill = new YxUserBill();
        userBill.setUid(uid);
        userBill.setTitle("佣金提现");
        userBill.setLinkId(userExtract.getId().toString());
        userBill.setCategory("now_money");
        userBill.setType("extract");
        userBill.setNumber(BigDecimal.valueOf(money));
        userBill.setBalance(BigDecimal.valueOf(balance));
        userBill.setMark(mark);
        userBill.setStatus(BillEnum.STATUS_1.getValue());
        userBill.setPm(BillEnum.PM_0.getValue());
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        billService.save(userBill);


    }

    @Override
    public double extractSum(int uid) {
        return yxUserExtractMapper.sumPrice(uid);
    }

    @Override
    public YxUserExtractQueryVo getYxUserExtractById(Serializable id) throws Exception{
        return yxUserExtractMapper.getYxUserExtractById(id);
    }

    @Override
    public Paging<YxUserExtractQueryVo> getYxUserExtractPageList(YxUserExtractQueryParam yxUserExtractQueryParam) throws Exception{
        Page page = setPageParam(yxUserExtractQueryParam,OrderItem.desc("create_time"));
        IPage<YxUserExtractQueryVo> iPage = yxUserExtractMapper.getYxUserExtractPageList(page,yxUserExtractQueryParam);
        return new Paging(iPage);
    }

}
