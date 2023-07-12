/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.monitor.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RedisService {

    /**
     * findById
     * @param key
     * @return
     */
    Page findByKey(String key, Pageable pageable);

    /**
     * 查询验证码的值
     * @param key
     * @return
     */
    String getCodeVal(String key);

    Object getObj(String key);

    /**
     * 保存验证码
     * @param key
     * @param val
     */
    void saveCode(String key, Object val,Long time);

    /**
     * delete
     * @param key
     */
    void delete(String key);

    /**
     * 清空所有缓存
     */
    void flushdb();
}
