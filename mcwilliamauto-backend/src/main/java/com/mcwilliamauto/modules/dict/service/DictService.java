package com.mcwilliamauto.modules.dict.service;

import com.mcwilliamauto.modules.dict.entity.*;

import java.util.List;

public interface DictService {
    List<DictMake> listMakes();
    List<DictModel> listModelsByMake(Long makeId);
    List<DictBodyStyle> listBodyStyles();
    List<DictTransmission> listTransmissions();
    List<DictEngine> listEngines();
    List<DictFuelType> listFuelTypes();
    List<DictColor> listColors();
}
