package fr.uga.polytech.greenhouse.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GreenHouseMapperTest {

    private GreenHouseMapper greenHouseMapper;

    @BeforeEach
    public void setUp() {
        greenHouseMapper = new GreenHouseMapperImpl();
    }
}
