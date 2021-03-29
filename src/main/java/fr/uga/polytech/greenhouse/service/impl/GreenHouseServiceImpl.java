package fr.uga.polytech.greenhouse.service.impl;

import fr.uga.polytech.greenhouse.domain.GreenHouse;
import fr.uga.polytech.greenhouse.repository.GreenHouseRepository;
import fr.uga.polytech.greenhouse.service.GreenHouseService;
import fr.uga.polytech.greenhouse.service.dto.GreenHouseDTO;
import fr.uga.polytech.greenhouse.service.mapper.GreenHouseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GreenHouse}.
 */
@Service
@Transactional
public class GreenHouseServiceImpl implements GreenHouseService {

    private final Logger log = LoggerFactory.getLogger(GreenHouseServiceImpl.class);

    private final GreenHouseRepository greenHouseRepository;

    private final GreenHouseMapper greenHouseMapper;

    public GreenHouseServiceImpl(GreenHouseRepository greenHouseRepository, GreenHouseMapper greenHouseMapper) {
        this.greenHouseRepository = greenHouseRepository;
        this.greenHouseMapper = greenHouseMapper;
    }

    @Override
    public GreenHouseDTO save(GreenHouseDTO greenHouseDTO) {
        log.debug("Request to save GreenHouse : {}", greenHouseDTO);
        GreenHouse greenHouse = greenHouseMapper.toEntity(greenHouseDTO);
        greenHouse = greenHouseRepository.save(greenHouse);
        return greenHouseMapper.toDto(greenHouse);
    }

    @Override
    public Optional<GreenHouseDTO> partialUpdate(GreenHouseDTO greenHouseDTO) {
        log.debug("Request to partially update GreenHouse : {}", greenHouseDTO);

        return greenHouseRepository
            .findById(greenHouseDTO.getId())
            .map(
                existingGreenHouse -> {
                    greenHouseMapper.partialUpdate(existingGreenHouse, greenHouseDTO);
                    return existingGreenHouse;
                }
            )
            .map(greenHouseRepository::save)
            .map(greenHouseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GreenHouseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GreenHouses");
        return greenHouseRepository.findAll(pageable).map(greenHouseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GreenHouseDTO> findOne(Long id) {
        log.debug("Request to get GreenHouse : {}", id);
        return greenHouseRepository.findById(id).map(greenHouseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GreenHouse : {}", id);
        greenHouseRepository.deleteById(id);
    }
}
