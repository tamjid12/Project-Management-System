package net.javaguides.springboot.model;
/**
 * Author: Akid Tamjid Rahman
 */
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotBlank;
import javax.persistence.*;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @NotBlank(message = "Project name is mandatory")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "introduction is mandatory")
    @Column(name = "intro")
    private String intro;


    @Column(name = "status")
    private String status;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private Date startDateTime;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private Date endDateTime;

    @ManyToMany
    @JoinTable(
            name = "user_project",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;


    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }


    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }


    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getIntro() {
        return intro;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }
    public Date getStartDateTime() {
        return startDateTime;
    }


    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }
    public Date getEndDateTime() {
        return endDateTime;
    }
}
