package fr.uga.polytech.greenhouse.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.uga.polytech.greenhouse.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GreenHouseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GreenHouseDTO.class);
        GreenHouseDTO greenHouseDTO1 = new GreenHouseDTO();
        greenHouseDTO1.setId(1L);
        GreenHouseDTO greenHouseDTO2 = new GreenHouseDTO();
        assertThat(greenHouseDTO1).isNotEqualTo(greenHouseDTO2);
        greenHouseDTO2.setId(greenHouseDTO1.getId());
        assertThat(greenHouseDTO1).isEqualTo(greenHouseDTO2);
        greenHouseDTO2.setId(2L);
        assertThat(greenHouseDTO1).isNotEqualTo(greenHouseDTO2);
        greenHouseDTO1.setId(null);
        assertThat(greenHouseDTO1).isNotEqualTo(greenHouseDTO2);
    }
}
