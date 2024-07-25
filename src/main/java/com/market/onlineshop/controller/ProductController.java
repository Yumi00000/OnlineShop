package com.market.onlineshop.controller;


import com.market.onlineshop.Product;
import com.market.onlineshop.services.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Controller
public class ProductController {

    private final HomePageService homePageService;

    @Autowired
    public ProductController(HomePageService homePageService) {
        this.homePageService = homePageService;
    }

    @GetMapping("/product")
    public String productPage(Model model, @RequestParam("product-id") Long productId) {
        Optional<Product> product = homePageService.getProductById(productId);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product-page";
        }
        return "error";
    }

}
