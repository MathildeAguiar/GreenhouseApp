package fr.uga.polytech.greenhouse.service.mapper;

import fr.uga.polytech.greenhouse.domain.*;
import fr.uga.polytech.greenhouse.service.dto.AlertDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alert} and its DTO {@link AlertDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlertMapper extends EntityMapper<AlertDTO, Alert> {}
