package com.barbieboutique.filter.service;

import com.barbieboutique.filter.entity.Attribute;

public interface AttributeService {
//    List<Attribute> getALL();

    void save(Attribute filter);

    Attribute getById(Long id);

    void deleteById(Long id);
}
