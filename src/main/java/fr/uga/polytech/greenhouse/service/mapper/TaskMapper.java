package fr.uga.polytech.greenhouse.service.mapper;

import fr.uga.polytech.greenhouse.domain.*;
import fr.uga.polytech.greenhouse.service.dto.TaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProfileMapper.class, ReportMapper.class })
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    @Mapping(target = "responsible", source = "responsible", qualifiedByName = "name")
    @Mapping(target = "rapport", source = "rapport", qualifiedByName = "titleR")
    TaskDTO toDto(Task s);
}
