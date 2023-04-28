package com.barbieboutique.product.controller;


import com.barbieboutique.category.entity.Category;
import com.barbieboutique.category.service.CategoryService;
import com.barbieboutique.filter.entity.Filter;
import com.barbieboutique.filter.service.FilterService;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.product.entity.Outfit;
import com.barbieboutique.product.entity.Product;
import com.barbieboutique.product.service.OutfitService;
import com.barbieboutique.product.service.ProductService;
import com.barbieboutique.searchFilterAPI.PriceRange;
import com.barbieboutique.searchFilterAPI.SearchEntityDTO;
import com.barbieboutique.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FilterService filterService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OutfitService outfitService;
    @Autowired
    private Utils utils;

    @GetMapping
    public String products(Model model,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size) {
        Language language = utils.getCurrentLanguage();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        List<Filter> filters = filterService.getALL();
        Page<Product> productPage = productService.findAll(PageRequest.of(currentPage - 1, pageSize));
        List<Category> categories = categoryService.getALL();

        BigDecimal minPrice = productService.minPrice();
        BigDecimal maxPrice = productService.maxPrice();
        PriceRange priceRange = new PriceRange(minPrice, maxPrice);

        SearchEntityDTO searchEntityDTO = new SearchEntityDTO();
        searchEntityDTO.setPriceRange(priceRange);

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("language", language);
        model.addAttribute("categories", categories);
        model.addAttribute("filters", filters);
        model.addAttribute("searchEntityDTO", searchEntityDTO);

        return "products";
    }


    @GetMapping("/search")
    public String search(@ModelAttribute SearchEntityDTO searchEntityDTO, Model model,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        List<Product> products = productService.findByPriceBetween(
                searchEntityDTO.getPriceRange().getMin(),
                searchEntityDTO.getPriceRange().getMax());

        if (searchEntityDTO.getTitle() != null) {
            List<Product> temp = productService.findByKeyword(searchEntityDTO.getTitle().toLowerCase());
            products = products.stream()
                    .filter(temp::contains)
                    .collect(Collectors.toList());
        }
        if (searchEntityDTO.getCategories().size() != 0) {
            List<Product> temp = productService.findAllByCategoriesIn(searchEntityDTO.getCategories());
            products = products.stream()
                    .filter(temp::contains)
                    .collect(Collectors.toList());
        }
        if (searchEntityDTO.getAttributes().size() != 0) {
            List<Product> temp = productService.findAllByAttributesIn(searchEntityDTO.getAttributes());
            products = products.stream()
                    .filter(temp::contains)
                    .collect(Collectors.toList());
        }

        Language language = utils.getCurrentLanguage();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        List<Category> categories = categoryService.getALL();
        List<Filter> filters = filterService.getALL();
//        Page<Product> productPage = productService.findAll(PageRequest.of(currentPage - 1, pageSize));

//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), products.size());
//        Page<Product> productPage = new PageImpl<Product>(products.subList(start, end), pageable, products.size());
        Page<Product> productPage = new PageImpl<>(products, PageRequest.of(currentPage - 1, pageSize), products.size());


        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("language", language);
        model.addAttribute("categories", categories);
        model.addAttribute("filters", filters);
        model.addAttribute("searchEntityDTO", searchEntityDTO);


//        Language language = utils.getCurrentLanguage();
//        ProductSpecification spec1 = new ProductSpecification(new SearchCriteria("price", ">", "35"));
//
//        List<Product> products = productService.findAll(spec1);
//
////        ProductSpecification spec2 = new ProductSpecification(new SearchCriteria("categories", ":", "Sweters"));
////
////        List<Product> products = productService.getALL(Specification.where(spec1).and(spec2));
//
//        model.addAttribute("products", products);
//        model.addAttribute("language", language);

        return "products";
    }

//    @GetMapping("/search")
//    public String search(@ModelAttribute SearchEntityDTO searchEntityDTO, Model model) {
//        Specification<Product> specification = Specification
//                .where(ProductSpecification.priceGreaterThanOrEq(searchEntityDTO.getPriceRange().getMin()))
//                .and(ProductSpecification.priceLesserThanOrEq(searchEntityDTO.getPriceRange().getMax()));
//
//        if (searchEntityDTO.getTitle() != null) {
//            specification.and(ProductSpecification.titleContains(searchEntityDTO.getTitle()));
//        }
//
//        if (searchEntityDTO.getCategories() != null) {
//
//        }
//
//        if (searchEntityDTO.getAttributes() != null) {
//
//        }
//
//        System.out.println(productService.findAll(specification).size());
//
//
//        return "products";
//    }


//    @RequestMapping(method = RequestMethod.GET, value = "/users")
//    @ResponseBody
//    public List<Product> search(@RequestParam(value = "search") String search) {
//        ProductSpecificationsBuilder builder = new ProductSpecificationsBuilder();
//        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
//        Matcher matcher = pattern.matcher(search + ",");
//        while (matcher.find()) {
//            builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
//        }
//
//        Specification<Product> spec = builder.build();
//        return productService.findAll(spec);
//    }

//    @GetMapping("/search")
//    public String search(Model model) {
//        Language language = utils.getCurrentLanguage();
//
//        List<Product> products = productService.getALL(spec1);
//
//
//
//        model.addAttribute("products", products);
//        model.addAttribute("language", language);
//
//        return "products";
//    }
//
//       public String get(
//            @And({
//                    @Spec(path = "manufacturer", params = "manufacturer", spec = Like.class),
//                    @Spec(path = "model", params = "model", spec = Like.class),
//                    @Spec(path = "country", params = "country", spec = In.class),
//                    @Spec(path = "type", params = "type", spec = Like.class),
//                    @Spec(path = "createDate", params = "createDate", spec = Equal.class),
//                    @Spec(path = "createDate", params = {"createDateGt", "createDateLt"}, spec = Between.class)
//            }) Specification<Product> spec,
//            Sort sort,
//            @RequestHeader HttpHeaders headers) {
//
//        final PagingResponse response = get(spec, headers, sort);
//        return new ResponseEntity<>(response.getElements(), returnHttpHeaders(response), HttpStatus.OK);
//    }
//
//    public PagingResponse get(Specification<Product> spec, HttpHeaders headers, Sort sort) {
//        if (isRequestPaged(headers)) {
//            return get(spec, buildPageRequest(headers, sort));
//        } else {
//            final List<Product> entities = get(spec, sort);
//            return new PagingResponse((long) entities.size(), 0L, 0L, 0L, 0L, entities);
//        }
//    }


    @Transactional
    @GetMapping("/{id}")
    public String product(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        Language language = utils.getCurrentLanguage();

        List<Category> categories = product.getCategories().stream().toList();
        List<Filter> filters = filterService.getALL();
        List<Outfit> outfits = outfitService.findAllByProductsContaining(product);

//        List<Image> images = product.getImages().stream().toList();

        model.addAttribute("product", product);
        model.addAttribute("outfits", outfits);
        model.addAttribute("categories", categories);
        model.addAttribute("filters", filters);
        model.addAttribute("images",  product.getImages());
        model.addAttribute("language", language);

        return "product";
    }

//    @GetMapping
//    public ProductDTO sayHEllo(@PathVariable Long id){
//        Product product = productService.getById(id);
//        return ProductMapper.MAPPER.toProductDTO(product);
//    }

    @GetMapping("{id}/card")
    public String addToCard(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        productService.addToUserCard(id, principal.getName());

        return "redirect:/products/" + id;
    }


//
//    public ResponseEntity<Void> addProduct(ProductDTO productDTO, List<MultipartFile> images){
//        System.out.println("title - " + productDTO.getTitle());
//        productService.addProduct(productDTO, images);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

//    @MessageMapping("/products")
//    public void messageProduct(ProductDTO productDTO, List<MultipartFile> images){
//        productService.addProduct(productDTO, images);
//    }

}
