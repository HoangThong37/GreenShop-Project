package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.export.csv.UserCsvExporter;
import com.shopme.admin.export.execl.UserExcelExporter;
import com.shopme.admin.export.pdf.UserPdfExporter;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.UserService;
import com.shopme.admin.util.FileUploadUntil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

//	@GetMapping("/users")
//	public String listAll(Model model, @PagingAndSortingParam(listName = "", moduleURL = "") PagingAndSortingHelper helper) {
//		return listPage(helper, 1, model, "firstName", "asc", null);
//	}
	
	@GetMapping("/users")
	public String listAll() {
		return "redirect:/users/page/1?sortField=firstName&sortDir=asc";
	}
	
	// list page
	@GetMapping("/users/page/{pageNumber}")
	public String listPage(@PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
			               @PathVariable(name = "pageNumber") int page ,Model model, 
			               @Param("sortField") String sortField,
			               @Param("sortDir") String sortDir,
			               @Param("keyword") String keyword) {
		
		Page<User> pageUser = userService.listByPage(page, sortField, sortDir, keyword);
		helper.updateAttributes(page, pageUser);
		
		return "users/users";
		
//		List<User> listUser = userService.listAll();
//		model.addAttribute("listUsers", listUser);
//		return "users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = userService.listRoles();

		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "/users/user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.save(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
			
			FileUploadUntil.cleanDir(uploadDir);
			FileUploadUntil.saveFile(uploadDir, fileName, multipartFile);
		}
		else {
			if (user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}  
			userService.save(user);
		}
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
		return getRedirectURLtoAffectedUser(user);
	}

	private String getRedirectURLtoAffectedUser(User user) {
		String keywordUpdate = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + keywordUpdate;
	}

	// code update user
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User editUser = userService.get(id);
			List<Role> listRoles = userService.listRoles();

			model.addAttribute("user", editUser);
			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
			model.addAttribute("listRoles", listRoles);
			return "/users/user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}

	// code delete user
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			userService.delete(id);
			redirectAttributes.addFlashAttribute("message", "The user ID " + id + " deleted successfully");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			// return "redirect:/users";
		}
		return "redirect:/users";
	}

	// code update user enabled
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			// Model model,
			RedirectAttributes redirectAttributes) {
		userService.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The user ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("messageSuccess", message);

		return "redirect:/users";
	}
	
	// code export csv list user
	@GetMapping("/users/export/csv")
	public void exportUserByCsv(HttpServletResponse response) throws IOException {
		List<User> listUser = userService.listAll();
		UserCsvExporter userExporter = new UserCsvExporter();
		userExporter.export(listUser, response);
	}
	
	// code export excel list user
	@GetMapping("/users/export/excel")
	public void exportUserByExcel(HttpServletResponse response) throws IOException {
		List<User> listUser = userService.listAll();
		UserExcelExporter userExporter = new UserExcelExporter();
		userExporter.export(listUser, response);
	}
	
	// code export pdf list user
	@GetMapping("/users/export/pdf")
	public void exportUserByPDF(HttpServletResponse response) throws IOException {
		List<User> listUser = userService.listAll();
		UserPdfExporter userExporter = new UserPdfExporter();
		userExporter.export(listUser, response);
	}
	
}
