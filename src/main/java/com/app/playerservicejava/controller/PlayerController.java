package com.app.playerservicejava.controller;

import com.app.playerservicejava.dto.GuestPlayerDto;
import com.app.playerservicejava.dto.PlayerSummary;
import com.app.playerservicejava.exception.PlayerNotFoundException;
import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.model.Players;
import com.app.playerservicejava.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "v1/players", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PlayerController {
    @Resource
    private PlayerService playerService;

//    @RequestMapping(method = RequestMethod.GET)
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<Players> getPlayers() {
        Players players = playerService.getPlayers();
        return ok(players);
    }

    @GetMapping("/guest")
    public ResponseEntity<List<GuestPlayerDto>> getPlayersForGuests() {
        List<GuestPlayerDto> guestPlayerDtoList = playerService.getPlayersForGuest();
        return ok(guestPlayerDtoList);
    }


    /*@GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable("id") String id) {
        Optional<Player> player = playerService.getPlayerById(id);

        if (player.isPresent()) {
            return new ResponseEntity<>(player.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable("id") String id) {
        Player player = playerService.getByIdOrThrow(id);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }


    /*
    Our service receives requests from a client that includes the query param isAdmin=true or isAdmin=false
    As an Admin user, I should see the first and last names of all players.
    As a regular user, I should only see the first names of all players.

    Scope: Plan and implement a new feature in your service Add test coverage
     */
    @GetMapping("/roles")
    public ResponseEntity<List<?>> getAllPlayers(@RequestParam(name="isAdmin", defaultValue = "false") boolean isAdmin) {
        List<?> players = playerService.getAllPlayersBasedOnRoles(isAdmin);
        return ResponseEntity.ok(players);
    }

    @GetMapping("/paged/roles")
    public ResponseEntity<Page<?>> getAllPlayers(@RequestParam(name="isAdmin", defaultValue = "false") boolean isAdmin,
                                                             @PageableDefault(size=20, sort="birthYear", direction= Sort.Direction.DESC)  Pageable pageable) {
        Pageable pageable1 = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Order.desc("birthYear"))
        );

        return ResponseEntity.ok(playerService.getAllPlayersBasedOnRoles(isAdmin, pageable1));
    }
}
