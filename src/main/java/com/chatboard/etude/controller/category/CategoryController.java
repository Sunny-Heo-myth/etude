package com.chatboard.etude.controller.category;

import com.chatboard.etude.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView readAll() {
        ModelAndView modelAndView = new ModelAndView("/api/categories");
        modelAndView.addObject(categoryService.readAllCategory());
        return modelAndView;
    }

}
