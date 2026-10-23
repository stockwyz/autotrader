package com.mcwilliamauto.modules.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mcwilliamauto.modules.dict.entity.DictModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DictModelMapper extends BaseMapper<DictModel> {

    /**
     * 联表查询车型列表，先按品牌名称升序，同品牌内再按 sort_no 升序。
     * 因为 dict_model 表本身没有品牌名字段，普通QueryWrapper排序不了跨表字段，
     * 只能写这条JOIN的原生SQL。makeName/makeLogoUrl 对应 DictModel 实体里标了
     * @TableField(exist = false) 的两个非数据库字段，靠列别名(AS)自动映射进去。
     */
    @Select("<script>" +
            "SELECT dm.id, dm.make_id AS makeId, dm.name, dm.sort_no AS sortNo, dm.status, " +
            "mk.name AS makeName, mk.logo_url AS makeLogoUrl " +
            "FROM dict_model dm LEFT JOIN dict_make mk ON dm.make_id = mk.id " +
            "<where> <if test='makeId != null'> dm.make_id = #{makeId} </if> </where> " +
            "ORDER BY mk.name ASC, dm.sort_no ASC" +
            "</script>")
    Page<DictModel> selectPageWithMake(Page<DictModel> page, @Param("makeId") Long makeId);
}
