package com.barbieboutique.filter.service;


import com.barbieboutique.filter.entity.Filter;
import com.barbieboutique.filter.repository.FilterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FilterServiceImpl implements FilterService{
    private final FilterRepository filterRepository;

    @Override
    public List<Filter> getALL() {
        return filterRepository.findAll();
    }

    @Override
    public void save(Filter filter) {
        filterRepository.save(filter);
    }

    @Override
    public Filter getById(Long id) {
        return filterRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Long id) {
        filterRepository.deleteById(id);
    }
}
