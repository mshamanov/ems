package me.shamanov.ems.controller;

import me.shamanov.ems.model.Department;
import me.shamanov.ems.model.Employee;
import me.shamanov.ems.repository.DepartmentRepository;
import me.shamanov.ems.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping({"/", "/employees"})
    public ModelAndView getEmployees(@RequestParam(defaultValue = "name") String sortBy,
                                     @RequestParam(defaultValue = "asc") String direction,
                                     @RequestParam(name = "page", defaultValue = "1") int pageNumber,
                                     @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort =
                Sort.by(sortDirection, sortBy.equalsIgnoreCase("department") ? "department.name" : sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<Employee> employeePage = this.employeeRepository.findAll(pageRequest);
        List<Employee> employeeList = employeePage.get().collect(Collectors.toList());

        ModelAndView mav = new ModelAndView("list-employees");
        mav.addObject("employees", employeeList);
        mav.addObject("currentPage", pageNumber);
        mav.addObject("pageSize", pageSize);
        mav.addObject("totalPages", employeePage.getTotalPages());

        return mav;
    }

    @GetMapping("/addEmployee")
    public ModelAndView addEmployee() {
        ModelAndView mav = new ModelAndView("add-employee");
        Employee newEmployee = new Employee();
        newEmployee.setDepartment(new Department());
        List<Department> departments = this.departmentRepository.findAll();
        mav.addObject("employee", newEmployee);
        mav.addObject("departments", departments);
        return mav;
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute Employee employee) {
        this.employeeRepository.save(employee);
        return "redirect:/";
    }

    @GetMapping("/updateEmployee")
    public ModelAndView updateEmployee(@RequestParam Long id) {
        ModelAndView mav = new ModelAndView("update-employee");
        Employee employee = this.employeeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Unable to find employee"));
        List<Department> departments = this.departmentRepository.findAll();
        mav.addObject("employee", employee);
        mav.addObject("departments", departments);
        return mav;
    }

    @GetMapping("/deleteEmployee")
    public String deleteEmployee(@RequestParam Long id) {
        this.employeeRepository.deleteById(id);
        return "redirect:/";
    }

}
