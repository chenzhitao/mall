/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package co.yixiang.modules.shop.wrapper;

import co.yixiang.modules.shop.web.vo.YxStoreProductQueryVo;

import java.util.List;
import java.util.Objects;

/**
 * 物流商品id包装类,返回视图层所需的字段
 *
 * @author godly.strong
 * @since 2020-08-28
 */
public class StoreProductWrapper
{

    public static YxStoreProductQueryVo entityVO(YxStoreProductQueryVo coreOrderCommodity)
    {
        coreOrderCommodity.setDescription(null);
        return coreOrderCommodity;
    }


    public static List<YxStoreProductQueryVo> listVO(List<YxStoreProductQueryVo> storeProductQueryVoList)
    {
        storeProductQueryVoList.forEach(coreOrderCommodity ->
        {
            coreOrderCommodity.setShowType(1);
            coreOrderCommodity.setDescription(null);
        });
        return storeProductQueryVoList;
    }
}

