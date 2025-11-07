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

import java.util.List;
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
    public ResponseEntity<List<?>> getAllPlayersByRole(@RequestParam String role,
                                                       @RequestParam String sortBy,
                                                       @PageableDefault(page=0, size=20, direction= Sort.Direction.DESC)  Pageable pageable) {

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

    /*
        implement Sorting Players by Custom Criteria
        🔹 1. Define Custom Sort Keys
              Let’s say you support:

              Custom Key	Derived From
              experience	currentYear - debutYear
              impact	    matchesPlayed * battingAverage
              age	        currentYear - birthYear
     */
    @GetMapping("/sorted")
    public ResponseEntity<List<Player>> getSortedPlayers(@RequestParam String sortBy,
                                                         @PageableDefault(size=10, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(playerService.getSortedPlayers(sortBy, pageable));

    }

    /*
        ✅ When to Use Comparator-Based Sorting
        Use it after fetching data from the database, especially when:
        The sort field is not directly mapped in the entity or differs in naming
        You want to sort by derived fields (e.g., age, experience)
        You want to support custom sort keys like "lastname" that aren’t part of the JPA @Sort mapping
     */
    @GetMapping("/ranking")
    public ResponseEntity<?> getRanking(@RequestParam int topN) {
        return ResponseEntity.ok(playerService.getTopRankedPlayers(topN));
    }

}
