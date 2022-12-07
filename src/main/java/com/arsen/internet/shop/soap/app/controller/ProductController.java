package com.arsen.internet.shop.soap.app.controller;

import com.arsen.internet.shop.soap.app.dto.product.ProductAddDto;
import com.arsen.internet.shop.soap.app.dto.product.ProductEditDto;
import com.arsen.internet.shop.soap.app.dto.product.ProductSearchDto;
import com.arsen.internet.shop.soap.app.model.Image;
import com.arsen.internet.shop.soap.app.model.Product;
import com.arsen.internet.shop.soap.app.model.data.Color;
import com.arsen.internet.shop.soap.app.model.data.SizeUnit;
import com.arsen.internet.shop.soap.app.repository.ImageRepository;
import com.arsen.internet.shop.soap.app.service.CategoryService;
import com.arsen.internet.shop.soap.app.service.ProductService;
import com.arsen.internet.shop.soap.app.utils.Generator;
import com.arsen.internet.shop.soap.app.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;


/**
 * Product controller
 * @author Arsen Sydoryk
 */
@Controller
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageRepository imageRepository;


    /**
     * Looking for product(s)
     * @param dto search params
     * @param model model
     * @return view
     */
    @GetMapping("")
    public String lookFor(@ModelAttribute("lookFor") ProductSearchDto dto, Model model){

        if(dto == null){
            dto = new ProductSearchDto();
        }

        log.trace("Starting looking for...");
        Page<Product> products = productService.getAll(dto);

        log.trace("Page: " + dto.getPage());
        log.trace("Total pages: " + products.getTotalPages());
        log.trace("Total pages: " + products.getTotalPages());
        log.trace("Products: " + products.getContent().size());
        log.trace("Looking for: " + dto.getSearchProduct());
        log.trace("Color: " + dto.getColor());
        log.trace("Category id: " + dto.getCategory());

        model.addAttribute("page", dto.getPage());
        model.addAttribute("pages", products.getTotalPages());
        model.addAttribute("products", products.getContent());
        model.addAttribute("search", dto);
        model.addAttribute("colors", Color.values());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("query", dto.buildQuery());

        return "product/products";

    }


    /**
     * Add new product page
     * @param model model
     * @return view
     */
    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String add(Model model){
        log.trace("Settings attributes....");
        model.addAttribute("product", new ProductAddDto());
        model.addAttribute("colors", Color.values());
        model.addAttribute("size_units", SizeUnit.values());
        model.addAttribute("categories", categoryService.getAll());
        return "product/product_add";
    }


    /**
     * Add new product
     * @param productDto product info
     * @param errors Errors
     * @return redirect or view (if errors)
     * @throws IOException
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String add(@Valid @ModelAttribute("product") ProductAddDto productDto, Errors errors) throws IOException {

        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error -> log.trace("Error code: " + error.getDefaultMessage()));
            return "product/product_add";
        }

        log.trace("Converting DTO to Product...");
        Product product = ProductAddDto.convertToProduct(productDto);
        log.trace("Setting category to product (id: " + productDto.getCategory() + ")");
        product.setCategory(categoryService.readById(productDto.getCategory()));


        Image image = Image.builder()
                .uuid(Generator.getRandomString())
                .data(ImageUtil.compress(productDto.getPreview().getBytes()))
                .contentType(productDto.getPreview().getContentType())
                .build();

        image = imageRepository.save(image);

        log.trace("Created new image with id " + image.getId());
        log.trace("[" + image.getId() + "] Image UUID: " + image.getUuid());
        log.trace("[" + image.getId() + "] Image content=type: " + image.getContentType());

        log.trace("Setting image to product");
        product.setImage(image);

        log.trace("Creating product...");
        product = productService.create(product);
        log.trace("Created product with id " + product.getId());

        String prefix = "[" + product.getId() + "] ";
        log.trace(prefix + "Short name: " + product.getShortName());
        log.trace(prefix + "Full name: " + product.getFullName());
        log.trace(prefix + "Description: " + product.getDescription());
        log.trace(prefix + "Price: " + product.getPrice());
        log.trace(prefix + "Quantity: " + product.getQuantity());
        log.trace(prefix + "Color: " + product.getColor());
        log.trace(prefix + "Size unit: " + product.getSizeUnit());
        log.trace(prefix + "Size value: " + product.getSizeValue());
        log.trace(prefix + "Category: " + product.getCategory().getIdentifier());

        return "redirect:/product";

    }


    /**
     * Show product
     * @param id of product
     * @param model model
     * @return view
     */
    @GetMapping("/{id}")
    public String show(@PathVariable long id, Model model){
        log.trace("Loading product with id " + id + "...");
        model.addAttribute("product", productService.readById(id));
        return "product/product_show";
    }


    /**
     * Edit product page
     * @param id of product
     * @param model model
     * @return view
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String edit(@PathVariable long id, Model model){
        log.trace("Loading product with id " + id + "...");
        log.trace("Converting Product to Product Edit DTO...");
        ProductEditDto productEditDto = ProductEditDto.convertToDto(productService.readById(id));

        log.trace("Setting values to attributes...");
        model.addAttribute("product", productEditDto);
        model.addAttribute("categories", categoryService.getAll());
        return "product/product_edit";
    }


    /**
     * Edit product
     * @param id of product
     * @param dto with information
     * @param errors Errors
     * @return redirect or view
     * @throws IOException
     */
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String edit(@PathVariable long id, @Valid @ModelAttribute("product") ProductEditDto dto, Errors errors) throws IOException {

        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error -> log.trace("Error code: " + error.getDefaultMessage()));
            return "product/product_edit";
        }

        log.trace("Loading product with id " + id + "...");
        Product product = productService.readById(id);

        product.setShortName(dto.getShortName());
        product.setFullName(dto.getFullName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setColor(dto.getColor());
        product.setSizeUnit(dto.getSizeUnit());
        product.setSizeValue(dto.getSizeValue());

        if(product.getCategory().getId() != dto.getCategory()) {
            product.setCategory(categoryService.readById(dto.getCategory()));
        }

        String prefix = "[" + product.getId() + "] ";
        log.trace(prefix + "Short name: " + product.getShortName());
        log.trace(prefix + "Full name: " + product.getFullName());
        log.trace(prefix + "Description: " + product.getDescription());
        log.trace(prefix + "Price: " + product.getPrice());
        log.trace(prefix + "Quantity: " + product.getQuantity());
        log.trace(prefix + "Color: " + product.getColor());
        log.trace(prefix + "Size unit: " + product.getSizeUnit());
        log.trace(prefix + "Size value: " + product.getSizeValue());
        log.trace(prefix + "Category: " + product.getCategory().getIdentifier());

        if(!dto.getPreview().isEmpty() && dto.getPreview().getSize() > 0){
            Image image = product.getImage();
            image.setData(ImageUtil.compress(dto.getPreview().getBytes()));
            product.setImage(imageRepository.save(image));
            log.trace(prefix + "Updated preview image!");
        }

        log.trace("Updating product (id = " + product.getId() + ")");
        productService.update(product);
        return "redirect:/product/" + id;

    }


    /**
     * Delete product
     * @param id of product
     * @return redirect
     */
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String delete(@PathVariable long id){
        log.trace("Loading product with id " + id + "...");
        Product product = productService.readById(id);

        log.trace("Deleting product with id " + id);
        productService.delete(id);
        log.trace("Deleting product image with id " + product.getImage().getId());
        imageRepository.delete(product.getImage());
        return "redirect:/product";
    }

}
