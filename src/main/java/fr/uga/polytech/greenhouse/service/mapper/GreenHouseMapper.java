package fr.uga.polytech.greenhouse.service.mapper;

import fr.uga.polytech.greenhouse.domain.*;
import fr.uga.polytech.greenhouse.service.dto.GreenHouseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GreenHouse} and its DTO {@link GreenHouseDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProfileMapper.class })
public interface GreenHouseMapper extends EntityMapper<GreenHouseDTO, GreenHouse> {
    @Mapping(target = "observateur", source = "observateur", qualifiedByName = "name")
    GreenHouseDTO toDto(GreenHouse s);

    @Named("nameG")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nameG", source = "nameG")
    GreenHouseDTO toDtoNameG(GreenHouse greenHouse);
}
