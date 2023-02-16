package com.shopme.admin.product;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lowagie.text.pdf.AcroFields.Item;
import com.shopme.admin.FileUploadUntil;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;

@Controller
public class ProductController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductService serviceProduct;
	
	@Autowired
	private BrandService brandService;
	
	@GetMapping("/products")
	public String listAll(Model model) {
		List<Product> listProducts = serviceProduct.listAll(); 
		
		model.addAttribute("listProducts", listProducts);
		return "products/products";
	}
	//Brand : nhãn hiệu
	@GetMapping("/products/new") 
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.listAll();
		
		Product product = new Product();
		product.setInStock(true);
		product.setEnabled(true);
		
		model.addAttribute("listBrands",listBrands);
		model.addAttribute("product",product);
		model.addAttribute("pageTitle", "Create New Product");
		
		return "products/products_form";
	}
	
	@PostMapping("/products/save") 
	public String saveProduct(Product product,
			@RequestParam("fileImage") MultipartFile mainImageMultiparts,  // image main
			@RequestParam("extraImage") MultipartFile[] extraImageMultiparts, // image extra
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,  // id ảnh
			@RequestParam(name = "imageNames", required = false) String[] imageNames, // 
			
			RedirectAttributes ra) throws IOException {
		
		    setMainImageName(mainImageMultiparts,product);
		    setExistingExtraImageName(imageIDs, imageNames, product); // set tên hình ảnh
		    setNewExtraImageNames(extraImageMultiparts, product);
		    setProductDetails(detailIDs,detailNames, detailValues, product);
	
			Product savedProduct = serviceProduct.save(product);
			saveUploadedImages(mainImageMultiparts, extraImageMultiparts, savedProduct);
			
			deleteExtraImagesWeredRemoveOnForm(product);
			
		ra.addFlashAttribute("messageSuccess", "The product has been saved successfully.");
		return "redirect:/products";
	} 
	
	private void deleteExtraImagesWeredRemoveOnForm(Product product) {
		String extraImageDir = "../product-images/" + product.getId() + "/extras";
		Path dirPath = Paths.get(extraImageDir);
		
		try {
			Files.list(dirPath).forEach(file -> {
				String fileName = file.toFile().getName();
				
				if(!product.containsImageName(fileName)) { // ko chứa
					try {
						Files.delete(file);
						LOGGER.info("Deleted extra image : " + fileName);
					} catch (IOException e) {
						LOGGER.error("Could not delete extra image : " + fileName);
					}
				}
			});
		} catch (IOException e) {
			LOGGER.error("Could not delete extra image : " + dirPath);
		}
		
	}
	private void setExistingExtraImageName(String[] imageIDs, String[] imageNames, Product product) {
		if (imageIDs.length == 0 || imageIDs == null) return;
		
		Set<ProductImage> images = new HashSet<>();
		for(int count = 0; count < imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];
			images.add(new ProductImage(id, name, product));
		}
		product.setImages(images);
	}
	
	private void setProductDetails(String[] detailIDs,
			                       String[] detailNames,
			                       String[] detailValues, Product product) {
		if(detailNames == null || detailValues == null) { 
			return;
		}
		for (int i = 0; i < detailNames.length; i++) {
			String name = detailNames[i];
			String value = detailValues[i];
			Integer id  = Integer.parseInt(detailIDs[i]);
			if (id!=0) {
				product.addDetail(id, name, value);
			} else if (name != null && value != null) {
				product.addDetail(name, value);
			}
		}
	}
	private void saveUploadedImages(MultipartFile mainImageMultiparts,
			                        MultipartFile[] extraImageMultiparts,
			                        Product savedProduct) throws IOException {
		if (!mainImageMultiparts.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultiparts.getOriginalFilename());
			String uploadDir = "../product-images/" + savedProduct.getId();
			FileUploadUntil.cleanDir(uploadDir);
			FileUploadUntil.saveFile(uploadDir, fileName, mainImageMultiparts);
		}
		if (extraImageMultiparts.length > 0) {
			String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
			
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if(multipartFile.isEmpty()) continue;
				
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUntil.saveFile(uploadDir, fileName, multipartFile);
			}			
		} 
	}
	
	//set image in extra
	private void setNewExtraImageNames(MultipartFile[] extraImageMultiparts,Product product) {
		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				
				if (!product.containsImageName(fileName)) {
					product.addExtraImages(fileName);
				}
			}
			}
		}
	}
	
	//set image in main
	private void setMainImageName(MultipartFile mainImageMultiparts, Product product) {
		if (!mainImageMultiparts.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultiparts.getOriginalFilename());

			product.setMainImage(fileName);
		}
	}
	
	// code update product enabled
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable("id") Integer id,
			                                 @PathVariable("status") boolean enabled,
			                                 RedirectAttributes redirectAttributes) {
		serviceProduct.updateEnabled(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("messageSuccess", message);

		return "redirect:/products";
	}
	
	// code delete product
	@GetMapping("/products/delete/{id}")
	public String deleteProducts(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			serviceProduct.delete(id);
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productMainImage = "../product-images/" + id;
			FileUploadUntil.removeDir(productExtraImagesDir);
			FileUploadUntil.removeDir(productMainImage);
			
		} catch (ProductNotFoundException ex) {
			ra.addFlashAttribute("messageError", ex.getMessage());
		}
		return "redirect:/products";
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {

		try {
			Product product = serviceProduct.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();


			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "products/products_form";

		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("messageError", e.getMessage());
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {

		try {
			Product product = serviceProduct.get(id);

			model.addAttribute("product", product);

			return "products/products_detail_modal";

		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("messageError", e.getMessage());
			return "redirect:/products";
		}
	}
}
