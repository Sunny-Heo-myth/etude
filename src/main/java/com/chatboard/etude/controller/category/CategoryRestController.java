package com.chatboard.etude.controller.category;

import com.chatboard.etude.dto.category.CategoryCreateRequest;
import com.chatboard.etude.dto.response.Response;
import com.chatboard.etude.entity.category.Category;
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
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @ApiOperation(value = "read all category",
            notes = "This request reads all categories.")
    @GetMapping("api/categories")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll() {
        return Response.success(categoryService.readAll());
    }

    @ApiOperation(value = "create categories",
            notes = "This request create a category.")
    @PostMapping("api/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody CategoryCreateRequest request) {
        categoryService.create(request);
        return Response.success();
    }

    @ApiOperation(value = "delete category",
            notes = "This request deletes category.")
    @DeleteMapping("/api/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "category id", required = true)
                           @PathVariable Long id) {
        categoryService.delete(id);
        return Response.success();
    }
}
