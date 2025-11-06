package com.app.playerservicejava;

import com.app.playerservicejava.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class RoleMapperFactoryTest {

    private RoleMapperFactory factory;

    @BeforeEach
    void setUp() {
        List<PlayerRoleMapper> mappers = List.of(
                new AdminMapper(),
                new GuestMapper(),
                new ScoutMapper()
        );
        factory = new RoleMapperFactory(mappers);
    }

    @Test
    void shouldReturnAdminMapperForAdminRole() {
        PlayerRoleMapper mapper = factory.getMapper("ADMIN");
        assertTrue(mapper instanceof AdminMapper);
    }

    @Test
    void shouldReturnGuestMapperForGuestRole() {
        PlayerRoleMapper mapper = factory.getMapper("GUEST");
        assertTrue(mapper instanceof GuestMapper);
    }

    @Test
    void shouldReturnScoutMapperForScoutRole() {
        PlayerRoleMapper mapper = factory.getMapper("SCOUT");
        assertTrue(mapper instanceof ScoutMapper);
    }

    @Test
    void shouldThrowExceptionForUnknownRole() {
        assertThrows(IllegalArgumentException.class, () -> factory.getMapper("COACH"));
    }
}
