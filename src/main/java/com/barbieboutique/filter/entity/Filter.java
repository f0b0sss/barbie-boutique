package com.barbieboutique.filter.entity;


import com.barbieboutique.language.entity.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "filters")
public class Filter {
    private static final String SEQ_NAME = "filter_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "filter_titles_translator",
            joinColumns = {@JoinColumn(name = "filter_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "code")
    @Column(name = "title")
    private Map<Language, String> filterTitles;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filter")
//    private List<Attribute> attributes;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "filter_attributes",
            joinColumns = @JoinColumn(name = "filter_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id"))
    private List<Attribute> attributes;
}
