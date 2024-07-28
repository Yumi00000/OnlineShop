package com.market.onlineshop.controller;


import com.market.onlineshop.models.Product;
import com.market.onlineshop.repository.ProductRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class HomePageController {


    private final ProductRepository productRepository;


    @Autowired
    public HomePageController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String homePage(Model model) {

        model.addAttribute("products",  productRepository.findAll());

        return "index";
    }

    @GetMapping("/productsAutocomplete")
    @ResponseBody
    public List<ProductDTO> productsAutocomplete(@RequestParam(value = "term", required = false, defaultValue = "") String term) {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(product -> product.getName().toLowerCase().contains(term.toLowerCase()))
                .map(product -> new ProductDTO(product.getId(), product.getName()))
                .collect(Collectors.toList());
    }


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
