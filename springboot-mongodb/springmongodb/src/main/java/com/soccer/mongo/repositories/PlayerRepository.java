package com.soccer.mongo.repositories;

import com.soccer.mongo.models.Player;
import com.soccer.mongo.models.PlayerPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {

    List<Player> findByPositionAndIsAvailable(PlayerPosition playerPosition, boolean isAvailable);
    List<Player> findByBirthDateIsBetweenOrderByBirthDate(Date fromDate, Date toDate);
    Player findFirstByOrderByBirthDateDesc();

    Page<Player> findByIdIsNotNull(Pageable pageable);
}
