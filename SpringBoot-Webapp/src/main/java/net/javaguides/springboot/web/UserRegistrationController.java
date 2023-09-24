package net.javaguides.springboot.web;
/**
 * Author: Akid Tamjid Rahman
 */
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.javaguides.springboot.service.UserService;
import net.javaguides.springboot.web.dto.UserRegistrationDto;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

	private UserService userService;

	public UserRegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }
	
	@GetMapping
	public String showRegistrationForm() {
		return "registration";
	}


	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
			bindingResult.rejectValue("confirmPassword", null, "Passwords do not match");
		}
		if (bindingResult.hasErrors()) {
			return "registration";
		}
		if (userService.findByEmail(registrationDto.getEmail()) != null) {
			bindingResult.rejectValue("email", null, "Email already in use");
			redirectAttributes.addFlashAttribute("error", true);
			return "registration";

		}
		userService.save(registrationDto);
		return "redirect:/registration?success";
	}


}
