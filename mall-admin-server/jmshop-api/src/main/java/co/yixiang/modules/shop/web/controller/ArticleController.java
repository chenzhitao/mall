/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.web.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.service.ArticleService;
import co.yixiang.modules.shop.web.param.YxArticleQueryParam;
import co.yixiang.modules.shop.web.vo.YxArticleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 文章 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2019-10-02
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/article")
@Api(value = "文章模块", tags = "商城:文章模块", description = "文章模块")
public class ArticleController extends BaseController {

    private final ArticleService articleService;


    /**
    * 获取文章文章详情
    */
    @AnonymousAccess
    @GetMapping("/details/{id}")
    @ApiOperation(value = "文章详情",notes = "文章详情",response = YxArticleQueryVo.class)
    public ApiResult<YxArticleQueryVo> getYxArticle(@PathVariable Integer id) throws Exception{
        YxArticleQueryVo yxArticleQueryVo = articleService.getYxArticleById(id);
        articleService.incVisitNum(id);
        return ApiResult.ok(yxArticleQueryVo);
    }

    /**
     * 文章列表
     */
    @AnonymousAccess
    @GetMapping("/list")
    @ApiOperation(value = "文章列表",notes = "文章列表",response = YxArticleQueryVo.class)
    public ApiResult<Paging<YxArticleQueryVo>> getYxArticlePageList(YxArticleQueryParam queryParam){
        Paging<YxArticleQueryVo> paging = articleService.getYxArticlePageList(queryParam);
        return ApiResult.ok(paging.getRecords());
    }

}

