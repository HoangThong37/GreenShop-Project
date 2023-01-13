package com.shopme.admin.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUntil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.User;

@Controller
public class AccountController {

	@Autowired
	private UserService userService;

	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser, Model model) {
		String email = loggedUser.getUsername();
		User user = userService.getEmail(email);
		model.addAttribute("user", user);

		return "users/account_form";
	}

	@PostMapping("/account/update")
	public String updateDetails(User user, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser, @RequestParam("image") MultipartFile multipartFile)
			throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.updateUserAccount(user);

			String uploadDir = "user-photos/" + savedUser.getId();

			FileUploadUntil.cleanDir(uploadDir);
			FileUploadUntil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if (user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}
			loggedUser.setFirstName(user.getFirstName());
			loggedUser.setLastName(user.getLastName());

			userService.updateUserAccount(user);
		}
		redirectAttributes.addFlashAttribute("message", "Your account details heve been updated");
		return "redirect:/account";
	}
}
