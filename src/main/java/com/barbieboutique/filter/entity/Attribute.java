package com.barbieboutique.filter.entity;


import com.barbieboutique.language.entity.Language;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "attributes")
public class Attribute {
    private static final String SEQ_NAME = "attribute_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "attribute_titles_translator",
            joinColumns = {@JoinColumn(name = "attribute_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "code")
    @Column(name = "title")
    private Map<Language, String> attributeTitles;
}
