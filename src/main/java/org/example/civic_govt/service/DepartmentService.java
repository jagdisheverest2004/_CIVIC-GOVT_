package org.example.civic_govt.service;

import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.District;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.districts.FetchDistrictDTO;
import org.example.civic_govt.payload.districts.FetchDistrictsDTO;
import org.example.civic_govt.payload.users.FetchUserDTO;
import org.example.civic_govt.payload.users.FetchUsersDTO;
import org.example.civic_govt.repository.DepartmentRepository;
import org.example.civic_govt.repository.DistrictRepository;
import org.example.civic_govt.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PageUtil pageUtil;
    @Autowired
    private DistrictRepository districtRepository;

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

    public FetchDistrictsDTO getDistrictsByDepartment(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword) {
        Department department = departmentRepository.findDepartmentByDeptHeadId(id).orElseThrow(() -> new RuntimeException("Department not found with id " + id));
        Pageable pageable = pageUtil.createPageable(pageNumber, pageSize, sortBy, sortOrder);
        Specification<District> spec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("department").get("id"), id);
        if (keyword != null && !keyword.isEmpty()) {
            String likePattern = "%" + keyword.toLowerCase() + "%";
            Specification<District> keywordSpec = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),likePattern);
            keywordSpec = keywordSpec.or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("distHead").get("name"), likePattern));
            spec = spec.and(keywordSpec);
        }
        Page<District> districtPage = districtRepository.findDistrictsByDepartmentId(department.getId(),pageable,spec);

        FetchDistrictsDTO fetchDistrictsDTO = new FetchDistrictsDTO();
        List<FetchDistrictDTO> districtDTO = districtPage.getContent().stream().map(district -> {
            FetchDistrictDTO dto = new FetchDistrictDTO();
            dto.setId(district.getId());
            dto.setName(district.getName());
            if (district.getDistHead() != null) {
                dto.setDistrictHeadName(district.getDistHead().getUsername());
            }
            if (district.getDepartment() != null) {
                dto.setDepartmentName(district.getDepartment().getName());
            }
            if(district.getZones()!=null) {
                dto.setNumberOfZones((long)district.getZones().size());
            }
            else{
                dto.setNumberOfZones(0L);
            }
            if(district.getZoneOfficials()!=null) {
                dto.setNumberOfZoneOfficials((long)district.getZoneOfficials().size());
            } else {
                dto.setNumberOfZoneOfficials(0L);
            }
            if(district.getIssues()!=null) {
                dto.setNumberOfIssues((long)district.getIssues().size());
            } else {
                dto.setNumberOfIssues(0L);
            }
            return dto;
        }).toList();

        fetchDistrictsDTO.setDistrictDTO(districtDTO);
        fetchDistrictsDTO.setPageNumber(districtPage.getNumber());
        fetchDistrictsDTO.setPageSize(districtPage.getSize());
        fetchDistrictsDTO.setTotalPages(districtPage.getTotalPages());
        fetchDistrictsDTO.setTotalElements(districtPage.getTotalElements());
        fetchDistrictsDTO.setLastPage(districtPage.isLast());
        return fetchDistrictsDTO;
    }
}