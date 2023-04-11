package com.barbieboutique.bucket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    private Order order;
    private PostDetails postDetails;
}
