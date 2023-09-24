package net.javaguides.springboot.service;

/**
 * Author: Akid Tamjid Rahman
 */
import net.javaguides.springboot.model.Project;
import net.javaguides.springboot.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }


    @Override
    public Project saveProject(Project project) {
        return this.projectRepository.save(project);
    }



    @Override
    public Project getProjectById(long id) {
        Optional<Project> optional = projectRepository.findById(id);
        Project project = null;
        if (optional.isPresent()) {
            project = optional.get();
        } else {
            throw new RuntimeException(" Project not found for id :: " + id);
        }
        return project;
    }

    @Override
    public void deleteProjectById(long id) {
        this.projectRepository.deleteById(id);
    }

    @Override
    public Page<Project> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.projectRepository.findAll(pageable);
    }



    @Override
    public List<Project> getProjectsByDateRange(Date startDateTime, Date endDateTime) {
        List<Project> projectList = new ArrayList<>();


        String s_date = new SimpleDateFormat("yyyy-MM-dd").format(startDateTime);
        String e_date = new SimpleDateFormat("yyyy-MM-dd").format(endDateTime);

        projectList = projectRepository.findProjectsByDateRange(s_date, e_date);
        return projectList;
        
    }


    @Override
    public List<Project> getProjectsByUserId(Long userId) {
        return projectRepository.findByUserId(userId);
    }

    @Override
    public List<Project> getProjectsByEmail(String email) {
        return projectRepository.findByEmail(email);

    }



}
