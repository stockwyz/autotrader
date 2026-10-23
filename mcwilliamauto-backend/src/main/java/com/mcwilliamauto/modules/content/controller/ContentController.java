package com.mcwilliamauto.modules.content.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.content.entity.CmsPage;
import com.mcwilliamauto.modules.content.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** 前台公开接口：轮播图/客户评价/招聘信息/富文本页面，均无需登录 */
@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {

    private final CmsBannerService bannerService;
    private final CmsTestimonialService testimonialService;
    private final CmsCareerService careerService;
    private final CmsPageService pageService;
    private final CmsNavMenuService navMenuService;

    @GetMapping("/banners")
    public Result<?> banners() {
        return Result.success(bannerService.list(Wrappers.<com.mcwilliamauto.modules.content.entity.CmsBanner>lambdaQuery()
                .eq(com.mcwilliamauto.modules.content.entity.CmsBanner::getStatus, 1)
                .orderByAsc(com.mcwilliamauto.modules.content.entity.CmsBanner::getSortNo)));
    }

    @GetMapping("/testimonials")
    public Result<?> testimonials() {
        return Result.success(testimonialService.list(Wrappers.<com.mcwilliamauto.modules.content.entity.CmsTestimonial>lambdaQuery()
                .eq(com.mcwilliamauto.modules.content.entity.CmsTestimonial::getStatus, 1)
                .orderByAsc(com.mcwilliamauto.modules.content.entity.CmsTestimonial::getSortNo)));
    }

    @GetMapping("/careers")
    public Result<?> careers() {
        return Result.success(careerService.list(Wrappers.<com.mcwilliamauto.modules.content.entity.CmsCareer>lambdaQuery()
                .eq(com.mcwilliamauto.modules.content.entity.CmsCareer::getStatus, 1)));
    }

    @GetMapping("/careers/{id}")
    public Result<?> careerDetail(@PathVariable Long id) {
        return Result.success(careerService.getById(id));
    }

    @GetMapping("/pages/{code}")
    public Result<?> pageByCode(@PathVariable String code) {
        CmsPage page = pageService.getOne(Wrappers.<CmsPage>lambdaQuery().eq(CmsPage::getCode, code));
        return Result.success(page);
    }

    @GetMapping("/nav-menu")
    public Result<?> navMenu() {
        return Result.success(navMenuService.list(Wrappers.<com.mcwilliamauto.modules.content.entity.CmsNavMenu>lambdaQuery()
                .eq(com.mcwilliamauto.modules.content.entity.CmsNavMenu::getStatus, 1)
                .orderByAsc(com.mcwilliamauto.modules.content.entity.CmsNavMenu::getSortNo)));
    }
}
