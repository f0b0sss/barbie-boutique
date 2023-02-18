package com.barbieboutique.image.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "images")
public class Image {
    private static final String SEQ_NAME =  "image_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;
    private String name;
    private String contentType;
    private String originalFileName;
    private Long size;

//    @Lob
//    private byte[] bytes;

    @Lob
    private String image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image1 = (Image) o;
        return Objects.equals(id, image1.id) && Objects.equals(name, image1.name) && Objects.equals(contentType, image1.contentType) && Objects.equals(originalFileName, image1.originalFileName) && Objects.equals(size, image1.size) && Objects.equals(image, image1.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contentType, originalFileName, size, image);
    }
}
