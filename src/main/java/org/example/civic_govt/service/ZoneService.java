package org.example.civic_govt.service;

import org.example.civic_govt.model.District;
import org.example.civic_govt.model.Zone;
import org.example.civic_govt.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    public void createZone(String name, District district) {
        if(zoneRepository.findZoneByDistrictName(district, name) != null) {
            throw new RuntimeException("Zone with name " + name + " already exists in district " + district.getName());
        }
        Zone zone = new Zone();
        zone.setName(name);
        zone.setDistrict(district);
        zoneRepository.save(zone);
    }
}
