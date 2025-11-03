package com.app.playerservicejava;

import com.app.playerservicejava.dto.AdminResponseDto;
import com.app.playerservicejava.dto.UserResponseDto;
import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.repository.PlayerRepository;
import com.app.playerservicejava.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    public void testGetPlayersAsAdmin() {
        List<Player> mockPlayers = List.of(new Player("Virat", "Kholi"), new Player("Rohit", "Sharma"));

        when(playerRepository.findAll()).thenReturn(mockPlayers);

        //Act
        List<?> result = playerService.getAllPlayersBasedOnRoles(true);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof AdminResponseDto);

        AdminResponseDto admin = (AdminResponseDto) result.get(0);
        assertEquals("Virat", admin.getFirstName());
        assertEquals("Kohli", admin.getLastName());

    }

    @Test
    public void testGetPlayersAsClient() {
        List<Player> mockPlayers = List.of(new Player("Virat"), new Player("Rohit"));

        when(playerRepository.findAll()).thenReturn(mockPlayers);

        //Act
        List<?> result = playerService.getAllPlayersBasedOnRoles(false);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof UserResponseDto);

        UserResponseDto client = (UserResponseDto) result.get(0);
        assertEquals("Virat", client.getFirstName());

    }
}
