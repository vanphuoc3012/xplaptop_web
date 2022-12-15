package com.xplaptop.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.xplaptop.common.entity.product.Product;

@Service
public class ProductService {
	private static final int PRODUCTS_PER_PAGE = 2;
	
	@Autowired
	private ProductRepository productRepository;
	
	public Page<Product> pageProductByCategory(Integer categoryId, Integer pageNumber, String sortDir, String sortField) {
		Sort sort = Sort.by(sortField);
		if(sortDir.equals("asc")) {
			sort = sort.ascending();
		} else {
			sort = sort.descending();
		}
		Pageable pageable = PageRequest.of(pageNumber-1, PRODUCTS_PER_PAGE, sort);
		String categoryMatch = "-"+categoryId+"-";
		return productRepository.productsByCategory(categoryId, categoryMatch, pageable);
	}
	
	public Product getProductByAlias(String alias) throws ProductNotFoundException {
		Product p = productRepository.findByAlias(alias);
		if(p == null) {
			throw new ProductNotFoundException("No such product found with alias: "+alias);
		}
		return p;
	}
}
