package me.shamanov.ems.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

    @PreRemove
    private void preRemove() {
        this.employees.forEach(employee -> employee.setDepartment(null));
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                '}';
    }
}
