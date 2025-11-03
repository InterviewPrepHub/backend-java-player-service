package com.app.playerservicejava.service;

import com.app.playerservicejava.dto.AdminResponseDto;
import com.app.playerservicejava.dto.UserResponseDto;
import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.model.Players;
import com.app.playerservicejava.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
