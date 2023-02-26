package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.admin.repository.BrandRepository;
import com.shopme.admin.service.BrandService;
import com.shopme.common.entity.Brand;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTest {
      @MockBean private BrandRepository repo;
      @InjectMocks private BrandService service;
      
      @Test
      public void testCheckUniqueInNewModeReturnDuplicate() {
    	  Integer ID = null;
    	  String name = "Acer";
    	  Brand brand = new Brand(name);
    	  
    	  Mockito.when(repo.findByName(name)).thenReturn(brand);
    	  
    	  String result = service.checkUnique(ID, name);
    	  assertThat(result).isEqualTo("Duplicate");
      }
      
      @Test
      public void testCheckUniqueInNewModeReturnOk() {
    	  Integer ID = null;
    	  String name = "Test12";
    	  Brand brand = new Brand(name);
    	  
    	  Mockito.when(repo.findByName(name)).thenReturn(brand);
    	  
    	  String result = service.checkUnique(ID, name);
    	  assertThat(result).isEqualTo("OK");
      }
}
