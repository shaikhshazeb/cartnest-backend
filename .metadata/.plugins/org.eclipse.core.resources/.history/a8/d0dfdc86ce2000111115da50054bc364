package com.sss.cartnest.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sss.cartnest.entities.Category;
import com.sss.cartnest.entities.Product;
import com.sss.cartnest.entities.ProductImage;
import com.sss.cartnest.repositories.CategoryRepository;
import com.sss.cartnest.repositories.ProductImageRepository;
import com.sss.cartnest.repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository prod_repo;
	
	@Autowired
	private ProductImageRepository prod_img_repo;
	
	@Autowired
	private CategoryRepository ctg_repo;
	
	public List<Product> getProductsByCategory(String categoryName) {
		if(categoryName != null && !categoryName.isEmpty()) {
			Optional<Category> ctg = ctg_repo.findByCategoryName(categoryName);
			if(ctg.isPresent()) {
				Category category = ctg.get();
				return prod_repo.findByCategory_id(category.getCategory_id());
			} else {
				throw new RuntimeException("Category Id not found");
			}
		} else {
			return prod_repo.findAll();
		}
		
	}
	
	public List<String> getProductImages(Integer productId) {

	    List<ProductImage> productImages = prod_img_repo.findByProduct_ProductId(productId);

	    List<String> imageUrls = new ArrayList<>();

	    for (ProductImage image : productImages) {
	        imageUrls.add(image.getImage_url());
	    }

	    return imageUrls;
	}

}
