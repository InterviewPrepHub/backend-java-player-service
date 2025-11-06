package com.app.playerservicejava.controller;

import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.model.Players;
import com.app.playerservicejava.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.app.playerservicejava.Role.ADMIN;
import static org.hibernate.cfg.JdbcSettings.USER;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "v1/players", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PlayerController {
    @Resource
    private PlayerService playerService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Players> getPlayers() {
        Players players = playerService.getPlayers();
        return ok(players);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable("id") String id) {
        Player player = playerService.getPlayerById(id);
        return ResponseEntity.ok(player);
    }


    @GetMapping("/paged/roles")
    public ResponseEntity<Page<?>> getAllPlayersByRole(@RequestParam String role,
                                                 @PageableDefault(page=0, size=20, sort="birthYear", direction= Sort.Direction.DESC)  Pageable pageable) {

        return ResponseEntity.ok(playerService.getAllPlayersBasedOnRoles(role, pageable));
    }

    /*
    Prompt: Design an endpoint GET /v1/players/search that supports filtering by birthCountry, bats, and debutYear,
    allows sorting by birthYear, nameLast, or playerId.
    Support pagination Optimize with indexes Allow partial matches (e.g., nameLast=Jo should match Johnson)
    Return metadata like total pages and current page

    Follow-ups:
    How would you design dynamic query construction?
    I used Spring Data JPA’s Specification API to build queries dynamically based on non-null parameters.
    This avoids hardcoding and supports flexible filtering.

    How would you prevent SQL injection?
    I avoid string concatenation and ensure all inputs are safely escaped

    How would you cache frequent queries?
    I’d use Spring Cache with a key based on filter + sort + page:
     */

    //Purpose: Dynamically search for Player entities based on optional filters.
    @GetMapping("/v1/players/search")
    public ResponseEntity<Page<?>> searchPlayers(
            @RequestParam(required = false) String birthCountry,
            @RequestParam(required = false) String bats,
            @RequestParam(required = false) String debutYear,
            @RequestParam(required = false) String nameLast,
            @PageableDefault(size = 20, sort = "birthYear") Pageable pageable) {

        return ResponseEntity.ok(playerService.searchPlayers(birthCountry, bats, debutYear, nameLast, pageable));
    }


}
