package org.example.civic_govt.service;

import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.District;
import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Zone;
import org.example.civic_govt.payload.users.FetchUserDTO;
import org.example.civic_govt.payload.users.FetchUsersDTO;
import org.example.civic_govt.payload.zones.FetchZoneDTO;
import org.example.civic_govt.payload.zones.FetchZonesDTO;
import org.example.civic_govt.repository.DistrictRepository;
import org.example.civic_govt.repository.ZoneRepository;
import org.example.civic_govt.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {

    @Autowired
    private DistrictRepository districtRepository;
    
    @Autowired
    private PageUtil pageUtil;
    @Autowired
    private ZoneRepository zoneRepository;

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

    public FetchUserDTO findDistrictHeadByDistrictName(Department department, String name) {
        User distHead = districtRepository.findDistrictByDepartmentName(department, name).getDistHead();
        if(distHead==null){
            throw new IllegalArgumentException("No District Head assigned for district " + name);
        }
        FetchUserDTO dto = new FetchUserDTO();
        dto.setId(distHead.getId());
        dto.setName(distHead.getUsername());
        dto.setEmail(distHead.getEmail());
        dto.setRole(distHead.getRole().name());
        if (distHead.getDepartment() != null) {
            dto.setDepartment(distHead.getDepartment().getName());
        }
        if (distHead.getDistrict() != null) {
            dto.setDistrict(distHead.getDistrict().getName());
        }
        return dto;
    }

    public FetchZonesDTO getZonesByDistrict(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword) {
        District district = districtRepository.findDistrictByDistHeadId(id).orElseThrow(() -> new IllegalArgumentException("District not found with id " + id));
        Pageable pageable = pageUtil.createPageable(pageNumber, pageSize, sortBy, sortOrder);
        Specification<Zone> spec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("district").get("id"), id);
        if(keyword!=null && !keyword.isEmpty()){
            String likePattern = "%" + keyword.toLowerCase() + "%";
            Specification<Zone> keywordSpec = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),likePattern);
            keywordSpec = keywordSpec.or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("zoneHead"), likePattern));
            spec = spec.and(keywordSpec);
        }
        Page<Zone> zonePage = zoneRepository.findZonesByDistrictId(district.getId(),pageable,spec);

        FetchZonesDTO fetchZonesDTO = new FetchZonesDTO();
        List<FetchZoneDTO> zonesDTO = zonePage.getContent().stream().map(zone -> {
            FetchZoneDTO dto = new FetchZoneDTO();
            dto.setId(zone.getId());
            dto.setName(zone.getName());
            dto.setDistrictName(zone.getDistrict().getName());
            dto.setZoneHeadName(zone.getZoneHead().getUsername());
            dto.setNumberOfIssues((long)zone.getIssues().size());
            return dto;
        }).toList();
        fetchZonesDTO.setZonesDTO(zonesDTO);
        fetchZonesDTO.setPageNumber(zonePage.getNumber());
        fetchZonesDTO.setPageSize(zonePage.getSize());
        fetchZonesDTO.setTotalPages(zonePage.getTotalPages());
        fetchZonesDTO.setTotalElements(zonePage.getTotalElements());
        fetchZonesDTO.setLastPage(zonePage.isLast());
        return fetchZonesDTO;
    }
}
