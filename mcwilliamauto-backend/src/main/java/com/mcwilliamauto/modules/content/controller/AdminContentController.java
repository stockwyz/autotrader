package com.mcwilliamauto.modules.content.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.content.entity.*;
import com.mcwilliamauto.modules.content.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** 后台内容管理：轮播图/客户评价/招聘/富文本页面/导航菜单 的增删改查，需登录 */
@RestController
@RequestMapping("/api/admin/content")
@RequiredArgsConstructor
public class AdminContentController {

    private final CmsBannerService bannerService;
    private final CmsTestimonialService testimonialService;
    private final CmsCareerService careerService;
    private final CmsPageService pageService;
    private final CmsNavMenuService navMenuService;

    // ---------- 轮播图 Banner ----------
    @GetMapping("/banners")
    public Result<?> bannerList(@RequestParam(defaultValue = "1") Long pageNum,
                                 @RequestParam(defaultValue = "10") Long pageSize) {
        Page<CmsBanner> page = bannerService.page(new Page<>(pageNum, pageSize),
                Wrappers.<CmsBanner>lambdaQuery().orderByAsc(CmsBanner::getSortNo));
        return Result.success(page);
    }

    @PostMapping("/banners")
    public Result<?> bannerCreate(@RequestBody CmsBanner entity) {
        bannerService.save(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/banners/{id}")
    public Result<?> bannerUpdate(@PathVariable Long id, @RequestBody CmsBanner entity) {
        entity.setId(id);
        bannerService.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/banners/{id}")
    public Result<?> bannerDelete(@PathVariable Long id) {
        bannerService.removeById(id);
        return Result.success();
    }

    // ---------- 客户评价 Testimonial ----------
    @GetMapping("/testimonials")
    public Result<?> testimonialList(@RequestParam(defaultValue = "1") Long pageNum,
                                      @RequestParam(defaultValue = "10") Long pageSize) {
        Page<CmsTestimonial> page = testimonialService.page(new Page<>(pageNum, pageSize),
                Wrappers.<CmsTestimonial>lambdaQuery().orderByAsc(CmsTestimonial::getSortNo));
        return Result.success(page);
    }

    @PostMapping("/testimonials")
    public Result<?> testimonialCreate(@RequestBody CmsTestimonial entity) {
        testimonialService.save(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/testimonials/{id}")
    public Result<?> testimonialUpdate(@PathVariable Long id, @RequestBody CmsTestimonial entity) {
        entity.setId(id);
        testimonialService.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/testimonials/{id}")
    public Result<?> testimonialDelete(@PathVariable Long id) {
        testimonialService.removeById(id);
        return Result.success();
    }

    // ---------- 招聘 Career ----------
    @GetMapping("/careers")
    public Result<?> careerList(@RequestParam(defaultValue = "1") Long pageNum,
                                 @RequestParam(defaultValue = "10") Long pageSize) {
        Page<CmsCareer> page = careerService.page(new Page<>(pageNum, pageSize),
                Wrappers.<CmsCareer>lambdaQuery().orderByDesc(CmsCareer::getCreatedAt));
        return Result.success(page);
    }

    @PostMapping("/careers")
    public Result<?> careerCreate(@RequestBody CmsCareer entity) {
        careerService.save(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/careers/{id}")
    public Result<?> careerUpdate(@PathVariable Long id, @RequestBody CmsCareer entity) {
        entity.setId(id);
        careerService.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/careers/{id}")
    public Result<?> careerDelete(@PathVariable Long id) {
        careerService.removeById(id);
        return Result.success();
    }

    // ---------- 富文本页面 Page (About Us / Finance / Services 等) ----------
    @GetMapping("/pages")
    public Result<?> pageList(@RequestParam(defaultValue = "1") Long pageNum,
                               @RequestParam(defaultValue = "20") Long pageSize) {
        Page<CmsPage> page = pageService.page(new Page<>(pageNum, pageSize));
        return Result.success(page);
    }

    /** 按page code直接查，查不到返回null——给"About Us"这类固定单页编辑器用，不用先知道id */
    @GetMapping("/pages/by-code/{code}")
    public Result<?> pageByCode(@PathVariable String code) {
        CmsPage page = pageService.getOne(Wrappers.<CmsPage>lambdaQuery().eq(CmsPage::getCode, code));
        return Result.success(page);
    }

    /** 按page code保存(没有就新建，有就更新)，同样是给固定单页编辑器用，前端不用关心id */
    @PutMapping("/pages/by-code/{code}")
    public Result<?> pageSaveByCode(@PathVariable String code, @RequestBody CmsPage entity) {
        CmsPage existing = pageService.getOne(Wrappers.<CmsPage>lambdaQuery().eq(CmsPage::getCode, code));
        entity.setCode(code);
        if (existing != null) {
            entity.setId(existing.getId());
            pageService.updateById(entity);
        } else {
            entity.setId(null);
            pageService.save(entity);
        }
        return Result.success();
    }

    @PostMapping("/pages")
    public Result<?> pageCreate(@RequestBody CmsPage entity) {
        pageService.save(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/pages/{id}")
    public Result<?> pageUpdate(@PathVariable Long id, @RequestBody CmsPage entity) {
        entity.setId(id);
        pageService.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/pages/{id}")
    public Result<?> pageDelete(@PathVariable Long id) {
        pageService.removeById(id);
        return Result.success();
    }

    // ---------- 顶部导航菜单 NavMenu ----------
    @GetMapping("/nav-menu")
    public Result<?> navMenuList() {
        return Result.success(navMenuService.list(Wrappers.<CmsNavMenu>lambdaQuery().orderByAsc(CmsNavMenu::getSortNo)));
    }

    @PostMapping("/nav-menu")
    public Result<?> navMenuCreate(@RequestBody CmsNavMenu entity) {
        navMenuService.save(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/nav-menu/{id}")
    public Result<?> navMenuUpdate(@PathVariable Long id, @RequestBody CmsNavMenu entity) {
        entity.setId(id);
        navMenuService.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/nav-menu/{id}")
    public Result<?> navMenuDelete(@PathVariable Long id) {
        navMenuService.removeById(id);
        return Result.success();
    }
}
