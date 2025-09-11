package org.example.civic_govt.service;

import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.users.FetchUserDTO;
import org.example.civic_govt.payload.users.FetchUsersDTO;
import org.example.civic_govt.repository.DepartmentRepository;
import org.example.civic_govt.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PageUtil pageUtil;

    public void createDepartment(String name) {
        if(departmentRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Department with name " + name + " already exists.");
        }
        Department department = new Department();
        department.setName(name);
        departmentRepository.save(department);
    }

    public Department findDepartmentByName(String name) {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Department not found with name " + name));
    }


    public FetchUsersDTO findDistrictOfficialsByDepartment(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable = pageUtil.createPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<User> departmentsPage = departmentRepository.findDistrictOfficialsByDepartmentId(pageable,id);

        FetchUsersDTO fetchUsersDTO = new FetchUsersDTO();
        List<FetchUserDTO> usersDTO = departmentsPage.getContent().stream().map(user -> {
            FetchUserDTO dto = new FetchUserDTO();
            dto.setId(user.getId());
            dto.setName(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole().name());
            if (user.getDepartment() != null) {
                dto.setDepartment(user.getDepartment().getName());
            }
            if (user.getDistrict() != null) {
                dto.setDistrict(user.getDistrict().getName());
            }
            if (user.getZone() != null) {
                dto.setZone(user.getZone().getName());
            }
            return dto;
        }).toList();
        fetchUsersDTO.setUsersDTO(usersDTO);
        fetchUsersDTO.setPageNumber(departmentsPage.getNumber());
        fetchUsersDTO.setPageSize(departmentsPage.getSize());
        fetchUsersDTO.setTotalPages(departmentsPage.getTotalPages());
        fetchUsersDTO.setTotalElements(departmentsPage.getTotalElements());
        fetchUsersDTO.setLastPage(departmentsPage.isLast());
        return fetchUsersDTO;
    }
}