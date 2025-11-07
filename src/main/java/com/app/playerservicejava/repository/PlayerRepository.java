package com.app.playerservicejava.repository;

import com.app.playerservicejava.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String>, JpaSpecificationExecutor<Player> {

    Page<Player> findAll(Pageable pageable);

    @Query("SELECT p FROM Player p ORDER BY p.firstName ASC")
    List<Player> findAllSortedByFirstName();

    @Query(value = "SELECT * FROM PLAYERS WHERE NAMEFIRST IS NOT NULL ORDER BY NAMEFIRST ASC, NAMELAST ASC", nativeQuery = true)
    Page<Player> findAllSortedByNameFirstAndLast(Pageable pageable);

    @Query("SELECT p.firstName AS firstName, p.lastName AS lastName, p.birthYear AS birthYear, p.birthCountry AS birthCountry, COUNT(p) AS count " +
            "FROM Player p " +
            "GROUP BY p.firstName, p.lastName, p.birthYear, p.birthCountry " +
            "HAVING COUNT(p) > 1")
    List<Player> findDuplicatePlayers();


}
