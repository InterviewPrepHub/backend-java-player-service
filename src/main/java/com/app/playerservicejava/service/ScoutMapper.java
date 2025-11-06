package com.app.playerservicejava.service;

import com.app.playerservicejava.model.Player;
import org.springframework.stereotype.Component;

@Component("SCOUT")
public class ScoutMapper implements PlayerRoleMapper {

    @Override
    public Object map(Player player) {
        return new ScoutMapper();
    }
}
