package com.arsen.internet.shop.soap.app.controller;

import com.arsen.internet.shop.soap.app.model.Category;
import com.arsen.internet.shop.soap.app.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Category controller
 * @author Arsen Sydoryk
 */
@Controller
@RequestMapping("/category")
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * Root. Shows all categories
     * @param page number
     * @param model model
     * @return view
     */
    @GetMapping({"/", ""})
    public String categories(@RequestParam(value = "page", defaultValue = "1", required = false) int page, Model model){
        log.trace("Loading page with categories... Page: " + page);
        Page<Category> categories = categoryService.getAll(page);

        log.trace("Setting attributes to model...");
        model.addAttribute("categories", categories.getContent());
        model.addAttribute("page", page);
        log.trace("Total pages: " + categories.getTotalPages());
        model.addAttribute("pages", categories.getTotalPages());
        return "category/category";
    }


    /**
     * Add new category page
     * @param model model
     * @return view
     */
    @GetMapping("/add")
    public String add(Model model){
        log.trace("Setting category to model attribute...");
        model.addAttribute("category", new Category());
        return "category/category_add";
    }


    /**
     * Add new category
     * @param category to add
     * @param errors Errors
     * @return view if there are any errors, otherwise redirect
     */
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("category") Category category, Errors errors){

        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error -> log.trace("Error code: " + error.getDefaultMessage()));
            return "category/category_add";
        }

        log.trace("Creating new category...");
        category = categoryService.create(category);
        log.trace("Created new category with id " + category.getId());

        log.trace("[" + category.getId() + "] Category identifier: " + category.getIdentifier());
        log.trace("[" + category.getId() + "] Category locale EN: " + category.getLocaleEn());
        log.trace("[" + category.getId() + "] Category locale UA: " + category.getLocaleUa());

        return "redirect:/category";
    }


    /**
     * Delete category
     * @param id of category
     * @return redirect
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id){
        log.trace("Deleting category with id " + id + "...");
        categoryService.delete(id);
        return "redirect:/category";
    }


    /**
     * Edit category page
     * @param id of category
     * @param model model
     * @return view
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable long id, Model model){
        log.trace("Loading category with id " + id);
        log.trace("Setting category with id " + id + " to attributes...");
        model.addAttribute("category", categoryService.readById(id));
        return "category/category_edit";
    }


    /**
     * Edit category
     * @param id of category
     * @param category Category
     * @param errors Errors
     * @return redirect if there are no errors, otherwise view
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable long id, @Valid @ModelAttribute("category") Category category, Errors errors){

        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error -> log.trace("Error code: " + error.getDefaultMessage()));
            return "category/category_edit";
        }

        category.setId(id);
        log.trace("Updating category with id " + id + "...");
        category = categoryService.update(category);

        log.trace("[" + category.getId() + "] Category identifier: " + category.getIdentifier());
        log.trace("[" + category.getId() + "] Category locale EN: " + category.getLocaleEn());
        log.trace("[" + category.getId() + "] Category locale UA: " + category.getLocaleUa());

        return "redirect:/category";
    }

}
