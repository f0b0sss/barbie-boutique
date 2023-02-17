package com.barbieboutique.filter.service;


import com.barbieboutique.filter.entity.Filter;

import java.util.List;

public interface FilterService {
    List<Filter> getALL();

    void save(Filter filter);

    Filter getById(Long id);

    void deleteById(Long id);
}
