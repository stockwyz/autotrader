# McWilliam Auto 前端 (Vue3 + Element Plus)

## 技术栈
Vue 3 + Vite + Element Plus + Vue Router 4 + Pinia + Axios

## 启动步骤

```bash
npm install
npm run dev
```

`npm install` 后会自动执行 `postinstall` 脚本，把 `node_modules/tinymce` 复制一份到 `public/tinymce`——
后台富文本编辑器(About Us等)用的TinyMCE是自托管方式加载的，不依赖外部CDN/API Key。
如果手动改动过 `node_modules` 或者该目录丢失，可以单独重跑一次：`node scripts/copy-tinymce.mjs`。

默认运行在 http://localhost:5173，已在 `vite.config.js` 中配置代理，
`/api` 和 `/uploads` 请求会转发到后端 `http://localhost:8080`。

## 页面路由

### 前台官网
- `/` 首页
- `/used-cars` 二手车列表（筛选/排序/分页），支持 query: `sold=1`(已售) `featured=1`(特价) `bodyStyleId=` 等
- `/cars/:id` 车辆详情
- `/carfinder` Car Finder 选车顾问
- `/trade-in` 以旧换新估价
- `/finance` 金融部门介绍
- `/car-loan-calculator` 贷款计算器
- `/car-loan-application` 信贷申请
- `/contact-us` 联系我们
- `/about-us` 关于我们

### 后台管理（需登录）
- `/admin/login` 登录
- `/admin/vehicles` 车辆库存管理（列表/上下架/标记已售特价/删除）
- `/admin/vehicles/edit/:id?` 车辆新增/编辑（含图片上传、分类配置、选装包）
- `/admin/leads` 留资线索管理（询价/试驾/以旧换新/CarFinder/贷款申请/联系我们）

## 目录结构
```
src/
├─ api/            axios封装 + 各模块接口
├─ store/           Pinia（登录态、筛选条件）
├─ router/          路由配置
├─ components/
│  ├─ layout/       顶部导航、页脚、后台侧边栏
│  ├─ home/         首页组件
│  ├─ inventory/    车辆列表筛选/排序/卡片
│  ├─ vehicle/      车辆详情相册/规格表/配置分类
│  └─ forms/        各类留资表单（询价/试驾/以旧换新/CarFinder/联系我们）
└─ views/           页面级组件（含 views/admin 后台页面）
```

## 默认管理员账号
见后端 README 中 `PasswordHashGenerator` 生成说明，默认用户名 `admin`。
