package org.example.civic_govt.service;

import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.District;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.users.FetchUserDTO;
import org.example.civic_govt.payload.users.FetchUsersDTO;
import org.example.civic_govt.repository.DistrictRepository;
import org.example.civic_govt.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {

    @Autowired
    private DistrictRepository districtRepository;
    
    @Autowired
    private PageUtil pageUtil;

    public void createDistrict(String name, Department department) {
        if(districtRepository.findDistrictByDepartmentName(department, name) != null) {
            throw new IllegalArgumentException("District with name " + name + " already exists in department " + department.getName());
        }
        District district = new District();
        district.setName(name);
        district.setDepartment(department);
        districtRepository.save(district);
    }

    public District findDistrictByName(String name) {
        return districtRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("District not found with name " + name));
    }

    public FetchUsersDTO getZoneHeadsByDistrict(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable = pageUtil.createPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<User> zoneHeadsPage = districtRepository.findZoneHeadsByDistrictId(id, pageable);

        FetchUsersDTO fetchUsersDTO = new FetchUsersDTO();
        List<FetchUserDTO> usersDTO = zoneHeadsPage.getContent().stream().map(user -> {
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
        fetchUsersDTO.setPageNumber(zoneHeadsPage.getNumber());
        fetchUsersDTO.setPageSize(zoneHeadsPage.getSize());
        fetchUsersDTO.setTotalPages(zoneHeadsPage.getTotalPages());
        fetchUsersDTO.setTotalElements(zoneHeadsPage.getTotalElements());
        fetchUsersDTO.setLastPage(zoneHeadsPage.isLast());
        return fetchUsersDTO;
        
    }
}
