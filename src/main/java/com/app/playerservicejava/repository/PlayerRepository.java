package com.app.playerservicejava.repository;
import com.app.playerservicejava.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {

    Page<Player> findAll(Pageable pageable);

    @Query("SELECT p FROM Player p ORDER BY p.firstName ASC")
    Page<Player> findAllSortedByFirstName(Pageable pageable);

    @Query(value = "SELECT * FROM PLAYERS WHERE NAMEFIRST IS NOT NULL ORDER BY NAMEFIRST ASC, NAMELAST ASC", nativeQuery = true)
    Page<Player> findAllSortedByNameFirstAndLast(Pageable pageable);

}
