package net.javaguides.springboot.web;
/**
 * Author: Akid Tamjid Rahman
 */
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import net.javaguides.springboot.model.Project;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.ProjectRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.ProjectService;
import net.javaguides.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectRepository projectRepository;



    @GetMapping("/home")
public String viewHomePage(Model model, Principal principal) {

        String email = ((UserDetails) ((Authentication) principal).getPrincipal()).getUsername();

        model.addAttribute("email", email);

        User user = userService.findByEmail(email);
        model.addAttribute("username", user.getFirstName());

        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);

    return findPaginated(1, "name", "asc", model);
}


    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            ModelMap modelMap) {
        if(email.equalsIgnoreCase(email) && password.equalsIgnoreCase(password)) {
            session.setAttribute("email", email);
            return "/home";
        } else {
            modelMap.put("error", "Invalid Account");
            return "/login";
        }
    }


    @GetMapping("/date")
    public String dateRange(Model model,
                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDateTime,
                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDateTime,
                            Principal principal) {


        String email = ((UserDetails) ((Authentication) principal).getPrincipal()).getUsername();
        model.addAttribute("email", email);

        User user = userService.findByEmail(email);
        model.addAttribute("username", user.getFirstName());
        if (startDateTime == null || endDateTime == null) {
            model.addAttribute("errorMessage", "Please enter a date range before searching.");

            List<Project> projects = projectService.getAllProjects();
            model.addAttribute("projects", projects);


            return "index";
        }

        List<Project> projects = projectService.getProjectsByDateRange(startDateTime, endDateTime);
        model.addAttribute("projects", projects);

        return findDatePaginated(1, "name", "asc",startDateTime,endDateTime, model);

    }



    @GetMapping("/showNewProjectForm")
    public String showNewProjectForm(Model model, User user) {
        // create model attribute to bind form data
        Project project = new Project();
        model.addAttribute("project", project);

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "new_project";
    }

    @PostMapping("/saveProject")
    public String saveProject(@ModelAttribute("project") Project project, @RequestParam(value = "userIds") List<Long> userIds, Model model, Principal principal) {

        Set<User> selectedUsers = new HashSet<>();
        for (Long userId : userIds) {
            User user = userService.getUserById(userId);
            if (user != null) {
                selectedUsers.add(user);
            }
        }
        project.setUsers(selectedUsers);




        if (project.getStartDateTime() != null && project.getStartDateTime().after(project.getEndDateTime())) {
            model.addAttribute("errorMessage", "End date cannot be before start date");
            return "new_project";
         }
        if ("0".equals(project.getStatus())) {
            project.setStartDateTime(null);

        }

        if (project.getId() != 0) {
            Project existingProject = projectService.getProjectById(project.getId());
            existingProject.setName(project.getName());
            existingProject.setIntro(project.getIntro());
            existingProject.setStatus(project.getStatus());
            existingProject.setStartDateTime(project.getStartDateTime());
            existingProject.setEndDateTime(project.getEndDateTime());



           Set<User> users = userIds.stream().map(id -> userService.getUserById(id)).collect(Collectors.toSet());


            existingProject.setUsers(users);



            projectService.saveProject(existingProject);
        }

        else{

            String email = ((UserDetails) ((Authentication) principal).getPrincipal()).getUsername();
            User user = userService.findByEmail(email);

            project.setUser(user);
            projectService.saveProject(project);

        }
        return "redirect:/home";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable( value = "id") long id, Model model) {
        Project project = projectService.getProjectById(id);
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        if(project.getStartDateTime() != null && project.getStartDateTime().after(project.getEndDateTime())){
            model.addAttribute("errorMessage", "End date cannot be before start date");
            return "update_project";
        }
        model.addAttribute("project", project);
        return "update_project";
    }


    @GetMapping("/deleteProject/{id}")
    public String deleteProject(@PathVariable (value = "id") long id) {
        this.projectService.deleteProjectById(id);
        return "redirect:/home";
    }


    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 100;
        Page<Project> page = projectService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Project> listProjects = page.getContent();


        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listProjects", listProjects);
        return "index";
    }


    @GetMapping("/datepage/{pageNo}")
    public String findDatePaginated(@PathVariable (value = "pageNo") int pageNo,
                                    @RequestParam("sortField") String sortField,
                                    @RequestParam("sortDir") String sortDir,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDateTime,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDateTime,
                                    Model model) {
        int pageSize = 100;

        List<Project> listProjects = projectService.getProjectsByDateRange(startDateTime, endDateTime);

        model.addAttribute("listProjects", listProjects);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", 1);
        model.addAttribute("totalItems", listProjects.size());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "index";
    }

}
