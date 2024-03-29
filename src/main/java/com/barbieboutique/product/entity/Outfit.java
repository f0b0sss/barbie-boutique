package com.barbieboutique.product.entity;


import com.barbieboutique.image.entity.Image;
import com.barbieboutique.language.entity.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "outfits")
public class Outfit {
    private static final String SEQ_NAME = "outfit_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "outfit_titles_translator",
            joinColumns = {@JoinColumn(
                    name = "outfit_id",
                    referencedColumnName = "id")})
    @MapKeyColumn(name = "code")
    @Column(name = "title")
    private Map<Language, String> outfitTitles;

    @ManyToMany
    @JoinTable(name = "outfit_products",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "outfit_id"))
    private List<Product> products;

    private BigDecimal price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "outfit_images",
            joinColumns = @JoinColumn(name = "outfit_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<Image> images;

    @OneToOne
    private Image previewImage;
}
