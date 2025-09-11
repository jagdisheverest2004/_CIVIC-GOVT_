package org.example.civic_govt.service;

import org.example.civic_govt.model.District;
import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Zone;
import org.example.civic_govt.payload.users.FetchUserDTO;
import org.example.civic_govt.payload.users.FetchUsersDTO;
import org.example.civic_govt.repository.ZoneRepository;
import org.example.civic_govt.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;
    
    @Autowired
    private PageUtil pageUtil;

    public void createZone(String name, District district) {
        if(zoneRepository.findZoneByDistrictName(district, name) != null) {
            throw new RuntimeException("Zone with name " + name + " already exists in district " + district.getName());
        }
        Zone zone = new Zone();
        zone.setName(name);
        zone.setDistrict(district);
        zoneRepository.save(zone);
    }

    public FetchUsersDTO getOfficialsInZone(Zone zone, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable = pageUtil.createPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<User> subordinatesPage = zoneRepository.findOfficialsByZoneId(zone.getId(), pageable);
        FetchUsersDTO fetchUsersDTO = new FetchUsersDTO();
        List<FetchUserDTO> usersDTO = subordinatesPage.getContent().stream().map(user -> {
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
        fetchUsersDTO.setPageNumber(subordinatesPage.getNumber());
        fetchUsersDTO.setPageSize(subordinatesPage.getSize());
        fetchUsersDTO.setTotalPages(subordinatesPage.getTotalPages());
        fetchUsersDTO.setTotalElements(subordinatesPage.getTotalElements());
        fetchUsersDTO.setLastPage(subordinatesPage.isLast());
        return fetchUsersDTO;
    }
}
