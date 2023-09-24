package net.javaguides.springboot.repository;

/**
 * Author: Akid Tamjid Rahman
 */
import net.javaguides.springboot.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = "SELECT * FROM Project WHERE start_date >= :startDateTime AND end_date <= :endDateTime", nativeQuery = true)
    List<Project> findProjectsByDateRange(@Param("startDateTime") String startDate, @Param("endDateTime") String endDate);


    @Query(value = "SELECT * FROM project p JOIN user_project up ON p.id = up.project_id WHERE up.user_id = :userId", nativeQuery = true)
    List<Project> findByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT p.* FROM project p JOIN user u ON p.owner_id = u.id WHERE u.email = :email", nativeQuery = true)
    List<Project> findByEmail(@Param("email") String email);


}
