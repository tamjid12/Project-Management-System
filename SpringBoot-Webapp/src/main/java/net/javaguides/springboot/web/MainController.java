package net.javaguides.springboot.web;
/**
 * Author: Akid Tamjid Rahman
 */
import net.javaguides.springboot.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {

	@GetMapping("/login")
	public String login() {

		return "login";
	}

	@GetMapping("/")
	public String home() {

        return "redirect:/home";
	}


}
