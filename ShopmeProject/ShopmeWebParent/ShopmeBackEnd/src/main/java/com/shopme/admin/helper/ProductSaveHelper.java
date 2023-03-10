package com.shopme.admin.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.util.FileUploadUntil;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;

public class ProductSaveHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);
	
	public static void deleteExtraImagesWeredRemoveOnForm(Product product) {
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
	
//	public static void setExistingExtraImageName(String[] imageIDs, String[] imageNames, Product product) {
//		if (imageIDs.length == 0 || imageIDs == null) return;
//		
//		Set<ProductImage> images = new HashSet<>();
//		for(int count = 0; count < imageIDs.length; count++) {
//			Integer id = Integer.parseInt(imageIDs[count]);
//			String name = imageNames[count];
//			images.add(new ProductImage(id, name, product));
//		}
//		product.setImages(images);
//	}
	public static void  setExistingExtraImageNames(String[] imageIDs, String[] imageNames, 
			Product product) {
		if (imageIDs == null || imageIDs.length == 0) return;

		Set<ProductImage> images = new HashSet<>();
		for (int count = 0; count < imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];

			images.add(new ProductImage(id, name, product));
		}
		product.setImages(images);

	}
	
     public	static void setProductDetails(String[] detailIDs,
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
	
     public static void saveUploadedImages(MultipartFile mainImageMultiparts,
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
     public static void setNewExtraImageNames(MultipartFile[] extraImageMultiparts,Product product) {
		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				
				if (!product.containsImageName(fileName)) {
					product.addExtraImage(fileName);
				}
			}
			}
		}
	}
	
	//set image in main
     public static void setMainImageName(MultipartFile mainImageMultiparts, Product product) {
		if (!mainImageMultiparts.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultiparts.getOriginalFilename());

			product.setMainImage(fileName);
		}
	}
}
