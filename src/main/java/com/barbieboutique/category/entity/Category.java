package com.barbieboutique.category.entity;


import com.barbieboutique.image.entity.Image;
import com.barbieboutique.language.entity.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


import java.util.Map;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {
    private static final String SEQ_NAME = "category_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "category_titles_translator",
            joinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "code")
    @Column(name = "title")
    private Map<Language, String> categoryTitles;

    @OneToOne(cascade = CascadeType.ALL)
    private Image image;

    @ManyToOne
    private Category parentCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Language, String> getCategoryTitles() {
        return categoryTitles;
    }

    public void setCategoryTitles(Map<Language, String> categoryTitles) {
        this.categoryTitles = categoryTitles;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(categoryTitles, category.categoryTitles) && Objects.equals(image, category.image) && Objects.equals(parentCategory, category.parentCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryTitles, image, parentCategory);
    }
}