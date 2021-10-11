package me.shamanov.ems.controller;

import me.shamanov.ems.model.Department;
import me.shamanov.ems.repository.DepartmentRepository;
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
public class DepartmentController {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("/departments")
    public ModelAndView getEmployees(@RequestParam(defaultValue = "name") String sortBy,
                                     @RequestParam(defaultValue = "asc") String direction,
                                     @RequestParam(name = "page", defaultValue = "1") int pageNumber,
                                     @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);

        Page<Department> departmentPage;
        if (sortBy.equalsIgnoreCase("total")) {
            if (direction.equalsIgnoreCase("asc")) {
                departmentPage = this.departmentRepository.findAllOrderByEmployeeCountAsc(pageRequest);
            } else {
                departmentPage = this.departmentRepository.findAllOrderByEmployeeCountDesc(pageRequest);
            }
        } else {
            Sort.Direction sortDirection =
                    direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Sort sort = Sort.by(sortDirection, sortBy);
            departmentPage = this.departmentRepository.findAll(pageRequest.withSort(sort));
        }

        List<Department> departmentList = departmentPage.get().collect(Collectors.toList());

        ModelAndView mav = new ModelAndView("list-departments");
        mav.addObject("departments", departmentList);
        mav.addObject("currentPage", pageNumber);
        mav.addObject("pageSize", pageSize);
        mav.addObject("totalPages", departmentPage.getTotalPages());

        return mav;
    }

    @GetMapping("/addDepartment")
    public ModelAndView addDepartment() {
        ModelAndView mav = new ModelAndView("add-department");
        Department newDepartment = new Department();
        mav.addObject("department", newDepartment);
        return mav;
    }

    @PostMapping("/saveDepartment")
    public String saveDepartment(@ModelAttribute Department department) {
        this.departmentRepository.save(department);
        return "redirect:/departments";
    }

    @GetMapping("/updateDepartment")
    public ModelAndView updateDepartment(@RequestParam Long id) {
        ModelAndView mav = new ModelAndView("update-department");
        Department department = this.departmentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Unable to find department"));
        mav.addObject("department", department);
        return mav;
    }

    @GetMapping("/deleteDepartment")
    public String deleteDepartment(@RequestParam Long id) {
        this.departmentRepository.deleteById(id);
        return "redirect:/departments";
    }

}
