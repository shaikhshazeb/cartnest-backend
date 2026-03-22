package com.sss.cartnest.admincontrollers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sss.cartnest.entities.Category;
import com.sss.cartnest.repositories.CategoryRepository;

@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @Autowired
    private CategoryRepository categoryRepo;

    // GET /admin/categories/all
    @GetMapping("/all")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> categories = categoryRepo.findAll();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // POST /admin/categories/add
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("categoryName");
            if (name == null || name.trim().isEmpty())
                return ResponseEntity.badRequest().body(Map.of("error", "Category name is required"));

            if (categoryRepo.findByCategoryName(name.trim()).isPresent())
                return ResponseEntity.badRequest().body(Map.of("error", "Category already exists"));

            Category category = new Category();
            category.setCategoryName(name.trim());
            Category saved = categoryRepo.save(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /admin/categories/edit
    @PutMapping("/edit")
    public ResponseEntity<?> editCategory(@RequestBody Map<String, Object> request) {
        try {
            int categoryId = (int) request.get("categoryId");
            String newName = (String) request.get("categoryName");

            if (newName == null || newName.trim().isEmpty())
                return ResponseEntity.badRequest().body(Map.of("error", "Category name is required"));

            Category category = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));

            category.setCategoryName(newName.trim());
            Category updated = categoryRepo.save(category);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /admin/categories/delete
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(@RequestBody Map<String, Integer> request) {
        try {
            int categoryId = request.get("categoryId");

            if (!categoryRepo.existsById(categoryId))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Category not found"));

            categoryRepo.deleteById(categoryId);
            return ResponseEntity.ok(Map.of("message", "Category deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}