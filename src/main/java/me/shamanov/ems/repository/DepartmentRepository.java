package me.shamanov.ems.repository;

import me.shamanov.ems.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("select d from Department d order by d.employees.size desc")
    Page<Department> findAllOrderByEmployeeCountDesc(Pageable pageable);

    @Query("select d from Department d order by d.employees.size asc")
    Page<Department> findAllOrderByEmployeeCountAsc(Pageable pageable);
}
