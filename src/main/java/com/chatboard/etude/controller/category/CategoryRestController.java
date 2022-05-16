package com.chatboard.etude.controller.category;

import com.chatboard.etude.dto.category.CategoryCreateRequest;
import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.service.category.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "CategoryController", tags = "Category")
@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation(value = "read all category",
            notes = "This request reads all categories.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response readAll() {
        return Response.success(categoryService.readAllCategory());
    }

    @ApiOperation(value = "create categories",
            notes = "This request create a category.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody CategoryCreateRequest request) {
        categoryService.createCategory(request);
        return Response.success();
    }

    @ApiOperation(value = "delete category",
            notes = "This request deletes category.")
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(
            @ApiParam(value = "category id", required = true) @PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return Response.success();
    }
}
