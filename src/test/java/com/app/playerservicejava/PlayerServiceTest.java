package com.app.playerservicejava;

import com.app.playerservicejava.dto.AdminDto;
import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.repository.PlayerRepository;
import com.app.playerservicejava.service.PlayerRoleMapper;
import com.app.playerservicejava.service.PlayerService;
import com.app.playerservicejava.service.RoleMapperFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private RoleMapperFactory roleMapperFactory;

    @Mock
    private PlayerRoleMapper adminMapper;

    @InjectMocks
    private PlayerService playerService;

    private Player samplePlayer;

    @BeforeEach
    void setup() {
        samplePlayer = new Player();
        samplePlayer.setFirstName("John");
        samplePlayer.setLastName("Doe");
        samplePlayer.setBirthYear("1985");
    }

    @Test
    void shouldReturnMappedPageForValidAdminRole() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Player> playerPage = new PageImpl<>(List.of(samplePlayer));

        when(playerRepository.findAll(pageable)).thenReturn(playerPage);
        when(roleMapperFactory.getMapper("ADMIN")).thenReturn(adminMapper);
        when(adminMapper.map(samplePlayer)).thenReturn(new AdminDto("John", "Doe", "1985"));

        Page<?> result = playerService.getAllPlayersBasedOnRoles("ADMIN", pageable);

        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0) instanceof AdminDto);
    }

    @Test
    void shouldThrowExceptionForInvalidPageSize() {
        Pageable pageable = PageRequest.of(0, 0); // size = 0

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> playerService.getAllPlayersBasedOnRoles("ADMIN", pageable));

        assertEquals("Invalid page or size", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionForNegativePageNumber() {
        Pageable pageable = PageRequest.of(-1, 10);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> playerService.getAllPlayersBasedOnRoles("ADMIN", pageable));

        assertEquals("Invalid page or size", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionForNullRole() {
        Pageable pageable = PageRequest.of(0, 10);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> playerService.getAllPlayersBasedOnRoles(null, pageable));

        assertEquals("Role parameter cannot be null or empty", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyRole() {
        Pageable pageable = PageRequest.of(0, 10);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> playerService.getAllPlayersBasedOnRoles("   ", pageable));

        assertEquals("Role parameter cannot be null or empty", ex.getMessage());
    }
}
