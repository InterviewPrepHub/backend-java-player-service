package com.app.playerservicejava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RoleMapperFactory {

    private final Map<String, PlayerRoleMapper> registry;

    @Autowired
    public RoleMapperFactory(List<PlayerRoleMapper> mappers) {
        this.registry = mappers.stream()
                .collect(Collectors.toMap(
                        mapper -> mapper.getClass().getAnnotation(Component.class).value().toUpperCase(),
                        Function.identity()
                ));
    }

    public PlayerRoleMapper getMapper(String role) {
        PlayerRoleMapper mapper = registry.get(role.toUpperCase());
        if (mapper == null) throw new IllegalArgumentException("Unknown role: " + role);
        return mapper;
    }
}
