package com.app.playerservicejava.service;

import com.app.playerservicejava.dto.AdminResponseDto;
import com.app.playerservicejava.dto.PlayerSummary;
import com.app.playerservicejava.dto.UserResponseDto;
import com.app.playerservicejava.exception.BadRequestException;
import com.app.playerservicejava.exception.PlayerNotFoundException;
import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.model.Players;
import com.app.playerservicejava.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private PlayerRepository playerRepository;

    public Players getPlayers() {
        Players players = new Players();
        playerRepository.findAll()
                .forEach(players.getPlayers()::add);
        return players;
    }

    public Optional<Player> getPlayerById(String playerId) {
        Optional<Player> player = null;

        /* simulated network delay */
        try {
            player = playerRepository.findById(playerId);
            Thread.sleep((long)(Math.random() * 2000));
        } catch (Exception e) {
            LOGGER.error("message=Exception in getPlayerById; exception={}", e.toString());
            return Optional.empty();
        }
        return player;
    }

    @Transactional(readOnly = true)
    public Player getByIdOrThrow(String id) {
        try {
            return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
        } catch (DataAccessException dae) {
            // Optional: wrap infra/db issues
            throw new RuntimeException("DB error", dae);
        }
    }

    public List<?> getAllPlayersBasedOnRoles(boolean isAdmin) {

        if(isAdmin) {
            return playerRepository.findAll()
                    .stream()
                    .map(player -> new AdminResponseDto(player.getFirstName(), player.getLastName()))
                    .collect(Collectors.toList());
        }
        return playerRepository.findAll()
                .stream()
                .map(player -> new UserResponseDto(player.getFirstName()))
                .collect(Collectors.toList());

    }

    public Page<?> getAllPlayersBasedOnRoles(boolean isAdmin, Pageable pageable) {

        /*
        This used Offset-based pagination.
        Internal code:
            SELECT * FROM players ORDER BY player_id DESC LIMIT 20 OFFSET 40;
        This is the classic offset-limit type.

        offset = page * size
         */
        if (pageable.getPageNumber() < 0 || pageable.getPageSize() <= 0) {
            throw new BadRequestException("Invalid page or size");
        }

        try {
            Page<Player> page = playerRepository.findAll(pageable);

            return page.map(player -> new PlayerSummary(
                    player.getPlayerId(),
                    player.getPlayerId(),
                    isAdmin ? player.getLastName():null, //hide last name if its not an admin
                    player.getBirthYear())
            );
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching paginated players", e);
        }

    }

}
