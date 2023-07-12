/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.monitor.rest;

import co.yixiang.annotation.Limit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 接口限流测试类
 */
@RestController
@RequestMapping("api")
@Api(value = "接口限流测试", tags = "接口限流测试", description = "接口限流测试")
public class LimitController {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    /**
     * 测试限流注解，下面配置说明该接口 60秒内最多只能访问 10次，保存到redis的键名为 limit_test，
     */
    @Limit(key = "test", period = 60, count = 10, name = "testLimit", prefix = "limit")
    @GetMapping("/limit")
    @ApiOperation(value = "测试示例",notes = "测试示例")
    public int testLimit() {
        return ATOMIC_INTEGER.incrementAndGet();
    }
}
