package com.xplaptop.admin.product.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.xplaptop.admin.paging.PagingAndSortingHelper;
import com.xplaptop.admin.paging.PagingAndSortingParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xplaptop.admin.FileUploadUtils;
import com.xplaptop.admin.brand.BrandService;
import com.xplaptop.admin.category.CategoryService;
import com.xplaptop.admin.product.ProductNotFoundException;
import com.xplaptop.admin.product.ProductService;
import com.xplaptop.common.entity.Brand;
import com.xplaptop.common.entity.Category;
import com.xplaptop.common.entity.product.Product;
import com.xplaptop.common.entity.product.ProductDetail;
import com.xplaptop.common.entity.product.ProductImage;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/products")
	public String listAllProducts() {
		
		return "redirect:/products/page/1?sortDir=asc&sortField=name";
	}
	
	@GetMapping("/products/page/{pageNumber}")
	public String listPageProducts(@PagingAndSortingParam PagingAndSortingHelper helper,
								   Model model,
								   @PathVariable(name = "pageNumber") Integer pageNumber,
								   @RequestParam(name = "sortDir", required = false) String sortDir,
								   @RequestParam(name = "sortField", required = false) String sortField,
								   @RequestParam(name = "keyword", required = false) String keyword,
								   @RequestParam(name = "categoryId", required = false) Integer categoryId) {
		if(sortDir == null || sortField == null) {
			return "redirect:/products/page/"+pageNumber+"?sortField=name&sortDir=asc";
		}
		model.addAttribute("categoryId", categoryId);
		Page<Product> page = productService.pageProductPage(pageNumber, sortDir, sortField, keyword, categoryId);
		List<Product> listProducts = page.getContent();
		model.addAttribute("listProducts", listProducts);
		List<Category> listCategories = categoryService.categoriesUsedInForm("asc");
		model.addAttribute("listCategories", listCategories);
		helper.updateModelPaginationAttributes(pageNumber, page);
		return "product/products";
	}
	
	@GetMapping("/products/new")
	public String createNewProduct(Model model,
									@RequestParam(name = "path") String path) {
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		List<Brand> listBrands = brandService.listAllBrandsForProductForm();
		
		model.addAttribute("title", "Create New Product");
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("path", path);
		
		return "product/products_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product,
								RedirectAttributes ra,
								@RequestParam(name = "path") String path,
								@RequestParam(name = "fileMainImage") MultipartFile multipartFile,
								@RequestParam(name = "newExtraImage") MultipartFile[] extratImageMultipartFiles,
								@RequestParam(name = "existingExtraImages", required = false) String[] existingExtratImageMultipartFiles,
								@RequestParam(name = "detailNames") String[] detailNames,
								@RequestParam(name = "detailValues") String[] detailValues) throws IOException {
		
		String uploadPath = "../product-images";
		if(product.getId() == null) {
			productService.save(product);
			ra.addFlashAttribute("message", "Product '"+product.getName()+"' has been save successfully");
		} else {			
			ra.addFlashAttribute("message", "All changes on product '"+product.getName()+"' has been save successfully");
		}
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		uploadPath += "/"+product.getId();
		
		/*
		 * handle main image
		 */
		if(!multipartFile.isEmpty()) {
			product.setMainImage(fileName);
			FileUploadUtils.cleanDir(uploadPath);
			FileUploadUtils.uploadFile(uploadPath, fileName, multipartFile);
		}
		
		uploadPath += "/extra-images";
		/*
		 * handle existing extra image
		 */
		saveExistingProductImage(existingExtratImageMultipartFiles, product, uploadPath);
		
		/*
		 * handle new extra image
		 */
		if(extratImageMultipartFiles.length > 0) {
			for(MultipartFile m : extratImageMultipartFiles) {
				uploadImage(m, uploadPath, product);
			}
		}
		
		saveProductDetails(product, detailNames, detailValues);
		productService.save(product);
		
		return "redirect:"+path.replace(">", "&");
	}
	

	private void saveProductDetails(Product product, String[] detailNames, String[] detailValues) {
		if(detailNames == null || detailNames.length == 0) return;
		Set<ProductDetail> newDetails = new HashSet<>();
		for(int i = 0; i < detailNames.length; i++) {
			if(detailNames[i] !=  null && !detailNames[i].isEmpty()) {
				String detailName = detailNames[i];
				String detailValue = detailValues[i];
				ProductDetail pd = product.getDetailByName(detailName);
				if(pd != null) {
					pd.setValue(detailValues[i]);
					newDetails.add(pd);
				} else {
					newDetails.add(new ProductDetail(detailName, detailValue, product));
				}
			}
		}
		product.getProductDetails().clear();
		product.getProductDetails().addAll(newDetails);
	}
	
	private void saveExistingProductImage(String[] existingImages, Product product, String uploadPath) {
		if(existingImages == null || existingImages.length == 0) return;
		Set<ProductImage> newProductImages = new HashSet<>();
		for (String existingImage : existingImages) {
			ProductImage pi = product.getProductImageByName(existingImage);
			if (pi == null) {
				newProductImages.add(new ProductImage(existingImage, product));
			} else {
				newProductImages.add(pi);
			}
		}
		List<String> listFileName = newProductImages.stream().map(ProductImage::getName).collect(Collectors.toList());
		FileUploadUtils.deleteIfNotExist(listFileName, uploadPath);
		product.getProductImages().clear();
		product.getProductImages().addAll(newProductImages);
	}
	
	private void uploadImage(MultipartFile m, String uploadPath, Product product) throws IOException {
		if(!m.isEmpty()) {			
			String fileExtraImageName = StringUtils.cleanPath(m.getOriginalFilename());
			product.addExtrasImage(fileExtraImageName);	
			FileUploadUtils.uploadFile(uploadPath, fileExtraImageName, m);
		}	
	}

	@GetMapping("/products/enable/{id}")
	public String enableProduct(@PathVariable Integer id,
								@RequestParam(name = "path") String path,
								RedirectAttributes ra) {
		try {
			String m = productService.enableStatusUpdate(id);
			ra.addFlashAttribute("message", "Product with ID: "+id+" has been "+m);
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:"+path.replace(">", "&");
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable Integer id,
								@RequestParam(name = "path") String path,
								RedirectAttributes ra) throws IOException {
		try {
			productService.deleteProductById(id);
			String uploadPath = "../product-images/"+id;
			FileUploadUtils.cleanDir(uploadPath+"/extra-images");
			FileUploadUtils.cleanDir(uploadPath);
			ra.addFlashAttribute("message", "Product with ID: "+id+" has been deleted");
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:"+path.replace(">", "&");
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct(Model model,
								@PathVariable(name = "id") Integer id,
								@RequestParam(name = "path") String path,
								RedirectAttributes ra) {
		try {
			Product product = productService.findProductById(id);
			
			List<Brand> listBrands = brandService.listAllBrandsForProductForm();
			model.addAttribute("listBrands", listBrands);
			
			model.addAttribute("product", product);
			model.addAttribute("title", product.getName());
			model.addAttribute("path", path);
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:"+path.replace(">", "&");
		}
		return "product/products_form";
	}
	
	@GetMapping("/products/detail/{id}")
	public String detailsProduct(Model model,
								@PathVariable(name = "id") Integer id,
								@RequestParam(name = "path") String path,
								RedirectAttributes ra) {
		try {
			Product product = productService.findProductById(id);
			
			model.addAttribute("product", product);
			model.addAttribute("path", path);
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:"+path.replace(">", "&");
		}
		return "product/products_details_modal";
	}
}
