package com.app.playerservicejava.controller;

import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.model.Players;
import com.app.playerservicejava.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        Optional<Player> player = playerService.getPlayerById(id);

        if (player.isPresent()) {
            return new ResponseEntity<>(player.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
}
