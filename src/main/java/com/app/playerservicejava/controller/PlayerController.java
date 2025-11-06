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

}
