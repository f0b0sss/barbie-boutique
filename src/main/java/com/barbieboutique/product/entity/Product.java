package com.barbieboutique.product.entity;


import com.barbieboutique.category.entity.Category;
import com.barbieboutique.comment.Comment;
import com.barbieboutique.filter.entity.Attribute;
import com.barbieboutique.image.entity.Image;
import com.barbieboutique.language.entity.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    private static final String SEQ_NAME = "product_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "product_titles_translator",
            joinColumns = {@JoinColumn(
                    name = "product_id",
                    referencedColumnName = "id")})
    @MapKeyColumn(name = "code")
    @Column(name = "title")
    private Map<Language, String> productTitles;

    @ElementCollection
    @CollectionTable(name = "product_description_translator",
            joinColumns = {@JoinColumn(
                    name = "product_id",
                    referencedColumnName = "id")})
    @MapKeyColumn(name = "code")
    @Column(name = "description")
    private Map<Language, String> descriptions;

    @ManyToMany
    @JoinTable(name = "product_products",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "relative_id"))
    private List<Product> products;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(name = "product_attributes",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id"))
    private List<Attribute> attributes;

    @Enumerated(EnumType.STRING)
    private Type type;

    private BigDecimal price;

    private int discount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "product_images",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<Image> images;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @OneToOne
    private Image previewImage;

    private boolean available;

    private Integer orderCount;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return discount == product.discount && available == product.available && Objects.equals(id, product.id) && Objects.equals(productTitles, product.productTitles) && Objects.equals(categories, product.categories) && Objects.equals(attributes, product.attributes) && Objects.equals(price, product.price) && Objects.equals(images, product.images) && Objects.equals(createdDate, product.createdDate) && Objects.equals(previewImage, product.previewImage) && Objects.equals(comments, product.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productTitles, categories, attributes, price, discount, images, createdDate, previewImage, available, comments);
    }
}
