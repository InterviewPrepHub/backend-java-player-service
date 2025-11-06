package com.app.playerservicejava.service;

import com.app.playerservicejava.dto.GuestDto;
import com.app.playerservicejava.model.Player;
import org.springframework.stereotype.Component;

@Component("GUEST")
public class GuestMapper implements PlayerRoleMapper {
    @Override
    public Object map(Player player) {
        return new GuestDto(player.getFirstName(), player.getLastName());
    }
}
