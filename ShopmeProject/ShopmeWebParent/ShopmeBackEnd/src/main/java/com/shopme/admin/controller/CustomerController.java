package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.export.csv.CustomerCsvExporter;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.CustomerService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	// listFristName
	@GetMapping("/customers")
	public String listFristName(Model model) {
		return "redirect:/customers/page/1?sortField=firstName&sortDir=asc";
		// return listByPage(1, model, "firstName", "asc", null);
	}
	
	// listByPage    //sortDir :sắp xếp thư mục
	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName = "listCustomers", moduleURL = "/customers") PagingAndSortingHelper helper,
			                 @PathVariable(name = "pageNum") int pageNum) {
	
		customerService.listByPage(pageNum, helper);
		return "customers/customers";
	}
	
	// updateCustomerEnabledStatus
	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes ra) {
        
		customerService.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The customer ID " + id + " has been " + status;

		ra.addFlashAttribute("messageSuccess", message);

		return "redirect:/customers";
	}
	
	// editCustomer
	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {

		try {
			Customer customerId = customerService.get(id);
			List<Country> listCountries = customerService.listAllCountries(); // list all country 
			
			model.addAttribute("customer", customerId);
			model.addAttribute("listCountries", listCountries);
			model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d:)", id));

			return "customers/customer_form";
		} catch (CustomerNotFoundException ex) {

			ra.addFlashAttribute("messageError", ex.getMessage());
			return "redirect:/customers";
		}
	}

	// deleteCustomer 
	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) throws CustomerNotFoundException {
		try {
			customerService.delete(id);
			ra.addFlashAttribute("messageSuccess",
					"The customer ID " + id + " has been deleted successfully");

		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("messageError", ex.getMessage());
		}
		return "redirect:/customers";
	}
	
	// viewCustomer
	@GetMapping("/customers/detail/{id}")
	public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Customer customer = customerService.get(id);
			
			model.addAttribute("customer", customer);
			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("messageSuccess", e.getMessage());
			return "redirect:/customers";
		}
		
	}
	
	// saveCustomer
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, RedirectAttributes ra) {
		customerService.save(customer);

		ra.addFlashAttribute("messageSuccess", "The customer has been saved successfully.");
		return "redirect:/customers";
	}
	
	// code export csv list category
	@GetMapping("/customers/export/csv")
	public void exportCategoryByCsv(HttpServletResponse response) throws IOException {
	//	List<Category> listCategories = customerService.listCategoriesUsedInForm();
		List<Customer> listCustomers = customerService.listAll();
		CustomerCsvExporter customerCsv = new CustomerCsvExporter();
		customerCsv.export(listCustomers, response);
	}
}
	


