package com.app.playerservicejava.repository;

import com.app.playerservicejava.model.RoleAttributeAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleAttributeAccessRepository extends JpaRepository<RoleAttributeAccess, String> {

    @Query("SELECT r.attribute FROM RoleAttributeAccess r WHERE r.role=:role")
    List<String> findAttributesByRole(String role);

}
