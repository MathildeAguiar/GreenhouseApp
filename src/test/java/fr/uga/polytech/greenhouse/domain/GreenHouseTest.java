package fr.uga.polytech.greenhouse.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.uga.polytech.greenhouse.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GreenHouseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GreenHouse.class);
        GreenHouse greenHouse1 = new GreenHouse();
        greenHouse1.setId(1L);
        GreenHouse greenHouse2 = new GreenHouse();
        greenHouse2.setId(greenHouse1.getId());
        assertThat(greenHouse1).isEqualTo(greenHouse2);
        greenHouse2.setId(2L);
        assertThat(greenHouse1).isNotEqualTo(greenHouse2);
        greenHouse1.setId(null);
        assertThat(greenHouse1).isNotEqualTo(greenHouse2);
    }
}
