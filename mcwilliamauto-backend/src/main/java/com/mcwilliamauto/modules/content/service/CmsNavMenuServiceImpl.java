package com.mcwilliamauto.modules.content.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mcwilliamauto.modules.content.entity.CmsNavMenu;
import com.mcwilliamauto.modules.content.mapper.CmsNavMenuMapper;
import org.springframework.stereotype.Service;

/** 继承 MyBatis-Plus ServiceImpl 即自动获得 list/save/updateById/removeById 等完整CRUD能力 */
@Service
public class CmsNavMenuServiceImpl extends ServiceImpl<CmsNavMenuMapper, CmsNavMenu> implements CmsNavMenuService {
}
