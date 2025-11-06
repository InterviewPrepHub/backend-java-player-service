package com.app.playerservicejava.service;

import com.app.playerservicejava.exception.PlayerNotFoundException;
import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.model.Players;
import com.app.playerservicejava.repository.PlayerRepository;
import com.app.playerservicejava.repository.RoleAttributeAccessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @CacheEvict(value = "playersByRole", allEntries = true)
    public void updatePlayer(Player player) {
        playerRepository.save(player);
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

    /*
        This used Offset-based pagination.
        Internal code:
            SELECT * FROM players ORDER BY player_id DESC LIMIT 20 OFFSET 40;
        This is the classic offset-limit type.

        offset = page * size

        Roles like GUEST and SCOUT often view public data that doesn’t change frequently.
     */

    @Cacheable(
            value = "playersByRole",
            key = "#role + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()"
    )
    public Page<?> getAllPlayersBasedOnRoles(String role, Pageable pageable) {

        if (pageable.getPageNumber() < 0 || pageable.getPageSize() <= 0) {
            throw new IllegalArgumentException("Invalid page or size");
        }

        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role parameter cannot be null or empty");
        }

        long start = System.currentTimeMillis();
        Page<Player> players = playerRepository.findAll(pageable);
        long end = System.currentTimeMillis() - start;
        System.out.println("DB call time: " + end + "ms");

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

    /*
     - For distributed caching, I’d use Redis with TTL and cache invalidation on player updates.
     - playerSearch::USA-R-2014-Jo-0-20

     1. Performance Boost for Repeated Searches
     2. Reduced Load on Database
     3. Improved User Experience

     */
    @Cacheable(
            value = "playerSearch",
            key = "#birthCountry + '-' + #bats + '-' + #debutYear + '-' + #nameLast + '-' + #pageable.pageNumber + '-' + #pageable.pageSize"
    )
    public Page<Player> searchPlayers(String birthCountry, String bats, String debutYear, String nameLast, Pageable pageable) {
        Specification<Player> spec = Specification.where(null);

        if (birthCountry != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("birthCountry"), birthCountry));
        }
        if (bats != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("bats"), bats));
        }
        if (debutYear != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("debut"), debutYear + "%"));
        }
        if (nameLast != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("lastName")), "%" + nameLast.toLowerCase() + "%"));
        }

        return playerRepository.findAll(spec, pageable);
    }

}
