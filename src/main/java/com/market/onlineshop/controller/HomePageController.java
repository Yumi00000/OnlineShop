package com.market.onlineshop.controller;


import com.market.onlineshop.Product;
import com.market.onlineshop.services.HomePageService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomePageController {

    private final HomePageService homePageService;

    @Autowired
    public HomePageController(HomePageService homePageService) {
        this.homePageService = homePageService;
    }

    @GetMapping("/home")
    public String homePage(Model model) {

        List<Product> products = homePageService.getProducts();


        model.addAttribute("products", products);

        return "index";
    }

    @GetMapping("/productsAutocomplete")
    @ResponseBody
    public List<ProductDTO> productsAutocomplete(@RequestParam(value = "term", required = false, defaultValue = "") String term) {
        List<Product> products = homePageService.getProducts();
        return products.stream()
                .filter(product -> product.getName().toLowerCase().contains(term.toLowerCase()))
                .map(product -> new ProductDTO(product.getId(), product.getName()))
                .collect(Collectors.toList());
    }

    // Data Transfer Object (DTO) for returning product data
    @Getter
    public static class ProductDTO {
        private final Long id;
        private final String name;

        public ProductDTO(Long id, String name) {
            this.id = id;
            this.name = name;
        }

    }

}
