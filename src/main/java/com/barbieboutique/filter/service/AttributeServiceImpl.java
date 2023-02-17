package com.barbieboutique.filter.service;


import com.barbieboutique.filter.entity.Attribute;
import com.barbieboutique.filter.repository.AttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AttributeServiceImpl implements AttributeService{
    private final AttributeRepository attributeRepository;

//    @Override
//    public List<Attribute> getALL() {
//        return attributeRepository.findAll();
//    }

    @Override
    public void save(Attribute filter) {
        attributeRepository.save(filter);
    }

    @Override
    public Attribute getById(Long id) {
        return attributeRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Long id) {
        attributeRepository.deleteById(id);
    }
}
