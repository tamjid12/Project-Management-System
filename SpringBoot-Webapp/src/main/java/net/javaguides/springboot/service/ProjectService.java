package net.javaguides.springboot.service;

/**
 * Author: Akid Tamjid Rahman
 */
import net.javaguides.springboot.model.Project;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface ProjectService {
     List<Project> getAllProjects();
     Project saveProject(Project project);

     Project getProjectById(long id);
     void deleteProjectById(long id);
     Page<Project> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

     List<Project> getProjectsByDateRange(Date startDate, Date endDate);
     List<Project> getProjectsByUserId(Long userId);
     List<Project> getProjectsByEmail(String email);




}
