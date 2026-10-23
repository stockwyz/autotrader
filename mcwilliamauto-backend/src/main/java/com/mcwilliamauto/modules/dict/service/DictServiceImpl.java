package com.mcwilliamauto.modules.dict.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mcwilliamauto.modules.dict.entity.*;
import com.mcwilliamauto.modules.dict.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {

    private final DictMakeMapper makeMapper;
    private final DictModelMapper modelMapper;
    private final DictBodyStyleMapper bodyStyleMapper;
    private final DictTransmissionMapper transmissionMapper;
    private final DictEngineMapper engineMapper;
    private final DictFuelTypeMapper fuelTypeMapper;
    private final DictColorMapper colorMapper;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${file.access-prefix}")
    private String accessPrefix;

    @Override
    public List<DictMake> listMakes() {
        List<DictMake> makes = makeMapper.selectList(Wrappers.<DictMake>lambdaQuery()
                .eq(DictMake::getStatus, 1).orderByAsc(DictMake::getSortNo));
        // 把数据库里存的相对路径(如 brand-logos/audi.svg)拼接成完整 http:// 链接返回给前端
        makes.forEach(m -> {
            if (StringUtils.hasText(m.getLogoUrl()) && !m.getLogoUrl().startsWith("http")) {
                m.setLogoUrl(baseUrl + accessPrefix + "/" + m.getLogoUrl());
            }
        });
        return makes;
    }

    @Override
    public List<DictModel> listModelsByMake(Long makeId) {
        return modelMapper.selectList(Wrappers.<DictModel>lambdaQuery()
                .eq(makeId != null, DictModel::getMakeId, makeId)
                .eq(DictModel::getStatus, 1)
                .orderByAsc(DictModel::getSortNo));
    }

    @Override
    public List<DictBodyStyle> listBodyStyles() {
        List<DictBodyStyle> list = bodyStyleMapper.selectList(Wrappers.<DictBodyStyle>lambdaQuery()
                .eq(DictBodyStyle::getStatus, 1).orderByAsc(DictBodyStyle::getSortNo));
        list.forEach(b -> {
            if (StringUtils.hasText(b.getPhotoUrl()) && !b.getPhotoUrl().startsWith("http")) {
                b.setPhotoUrl(baseUrl + accessPrefix + "/" + b.getPhotoUrl());
            }
        });
        return list;
    }

    @Override
    public List<DictTransmission> listTransmissions() {
        return transmissionMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public List<DictEngine> listEngines() {
        return engineMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public List<DictFuelType> listFuelTypes() {
        return fuelTypeMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public List<DictColor> listColors() {
        return colorMapper.selectList(Wrappers.emptyWrapper());
    }
}
