package com.app.playerservicejava.service;

import com.app.playerservicejava.dto.AdminDto;
import com.app.playerservicejava.model.Player;
import org.springframework.stereotype.Component;

@Component("ADMIN")
public class AdminMapper implements PlayerRoleMapper {

    @Override
    public Object map(Player player) {
        return new AdminDto(player.getFirstName(), player.getLastName(), player.getBirthYear());
    }
}
