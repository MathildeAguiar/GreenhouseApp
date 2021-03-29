package fr.uga.polytech.greenhouse.service.mapper;

import fr.uga.polytech.greenhouse.domain.*;
import fr.uga.polytech.greenhouse.service.dto.ReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProfileMapper.class, GreenHouseMapper.class })
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {
    @Mapping(target = "author", source = "author", qualifiedByName = "name")
    @Mapping(target = "house", source = "house", qualifiedByName = "nameG")
    ReportDTO toDto(Report s);

    @Named("titleR")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titleR", source = "titleR")
    ReportDTO toDtoTitleR(Report report);
}
