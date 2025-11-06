package com.app.playerservicejava.service;

import com.app.playerservicejava.exception.PlayerNotFoundException;
import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.model.Players;
import com.app.playerservicejava.repository.PlayerRepository;
import com.app.playerservicejava.repository.RoleAttributeAccessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RoleAttributeAccessRepository roleAttributeAccessRepository;

    @Autowired
    private RoleMapperFactory roleMapperFactory;

    public Players getPlayers() {
        Players players = new Players();
        playerRepository.findAll()
                .forEach(players.getPlayers()::add);
        return players;
    }

    public Player getPlayerById(String playerId) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player id: " + playerId + " not found."));

        return player;
        /*try {
            Player player = playerRepository.findById(playerId)
                    .orElseThrow(() -> new PlayerNotFoundException("Player id: " + playerId + " not found."));
            Thread.sleep((long)(Math.random() * 2000));
            return Optional.of(player);
        } catch (Exception e) {
            LOGGER.error("Unexpected error in getPlayerById: {}", e.toString());
            return Optional.empty();
        }*/
    }

    public Page<?> getAllPlayersBasedOnRoles(String role, Pageable pageable) {

        /*
        This used Offset-based pagination.
        Internal code:
            SELECT * FROM players ORDER BY player_id DESC LIMIT 20 OFFSET 40;
        This is the classic offset-limit type.

        offset = page * size
         */
        if (pageable.getPageNumber() < 0 || pageable.getPageSize() <= 0) {
            throw new IllegalArgumentException("Invalid page or size");
        }

        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role parameter cannot be null or empty");
        }

        Page<Player> page = playerRepository.findAll(pageable);

        PlayerRoleMapper playerRoleMapper = roleMapperFactory.getMapper(role.toUpperCase());

        return page.map(playerRoleMapper::map);


        /*try {
            Page<Player> page = playerRepository.findAll(pageable);

            return switch(role.toUpperCase()) {
                case "ADMIN" ->page.map(player -> new AdminDto(player.getFirstName(), player.getLastName(), player.getBirthYear()));

                case "GUEST" -> page.map(player -> new GuestDto(player.getFirstName(), player.getLastName()));

                default -> throw new IllegalArgumentException("Unknown role");

            };
        } catch (DataAccessException e) {
            LOGGER.error("Database error while fetching players: {}", e.getMessage());
            throw new RuntimeException("Error accessing player data. Please try again later.", e);
        }*/

    }

}
