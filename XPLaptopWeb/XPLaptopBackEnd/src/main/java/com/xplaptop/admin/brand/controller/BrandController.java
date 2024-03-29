package com.xplaptop.admin.brand.controller;

import java.io.IOException;
import java.util.List;

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
import com.xplaptop.common.exception.BrandNotFoundException;
import com.xplaptop.admin.brand.BrandService;
import com.xplaptop.admin.category.CategoryService;
import com.xplaptop.common.entity.Brand;
import com.xplaptop.common.entity.Category;

@Controller
public class BrandController {
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/brands")
	public String listAllBrands() {
		
		return "redirect:/brands/page/1?sortDir=asc&sortField=name";
	}
	
	@GetMapping("/brands/page/{pageNumber}")
	public String listAllBrandsPage(@PagingAndSortingParam PagingAndSortingHelper helper,
									@PathVariable(name = "pageNumber") Integer pageNumber,
									@RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
									@RequestParam(name = "keyword", required = false) String keyword,
									Model model) {
		if(sortDir == null) {
			return "redirect:/brands/page/"+pageNumber+"?sortField=name&sortDir=asc";
		}
		Page<Brand> page = brandService.pageBrands(pageNumber, sortDir, keyword);
		List<Brand> listBrands = brandService.listBrandPage(pageNumber, sortDir, keyword);
		model.addAttribute("listBrands", listBrands);
		helper.updateModelPaginationAttributes(pageNumber, page);
		return "brand/brands";
	}
	
	@GetMapping("/brands/new")
	public String createNewBrands(Model model,
									@RequestParam(name = "path", required = false) String path) {
		Brand brand = new Brand();
		model.addAttribute("brand", brand);
		
		List<Category> categoriesInForm = categoryService.categoriesUsedInForm("asc");
		model.addAttribute("categoriesInForm", categoriesInForm);
		
		model.addAttribute("title", "Create New Brand");
		model.addAttribute("path", path);
		return "/brand/brands_form";
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id,
							Model model,
							@RequestParam(name = "path") String path) {
		try {
			Brand brand = brandService.findById(id);
			model.addAttribute("brand", brand);
			
			List<Category> categoriesInForm = categoryService.categoriesUsedInForm("asc");
			model.addAttribute("title", "Edit Brand '"+brand.getName()+"'");
			model.addAttribute("categoriesInForm", categoriesInForm);
			model.addAttribute("path", path);
		} catch (BrandNotFoundException e) {
			model.addAttribute("mess", e.getMessage());
		}
		return "/brand/brands_form";
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand,
							RedirectAttributes ra,
							@RequestParam(name = "brandLogo") MultipartFile multipartFile,
							@RequestParam(name = "path") String path) throws IOException {
		String uploadPath = "../brand-logos";
		if(brand.getId() == null) {
			brandService.save(brand);
			ra.addFlashAttribute("message", "Brand '"+brand.getName()+"' has been save successfully");
		} else {			
			ra.addFlashAttribute("message", "All changes on brand '"+brand.getName()+"' has been save successfully");
		}
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		uploadPath += "/"+brand.getId();
		
		if(!multipartFile.isEmpty()) {
			brand.setLogo(fileName);
			FileUploadUtils.cleanDir(uploadPath);
			FileUploadUtils.uploadFile(uploadPath, fileName, multipartFile);
		}
		brandService.save(brand);
		
		return "redirect:"+path.replace(">", "&");
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id,
								@RequestParam(name = "path") String path,
								RedirectAttributes ra) {
		try {
			brandService.delete(id);
			ra.addFlashAttribute("message", "Brand has been deleted successfully");
		} catch (BrandNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:" + path.replace(">", "&");
	}
}
