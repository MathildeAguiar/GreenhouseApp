package fr.uga.polytech.greenhouse.service.mapper;

import fr.uga.polytech.greenhouse.domain.*;
import fr.uga.polytech.greenhouse.service.dto.ProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profile} and its DTO {@link ProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface ProfileMapper extends EntityMapper<ProfileDTO, Profile> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    ProfileDTO toDto(Profile s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProfileDTO toDtoName(Profile profile);
}
