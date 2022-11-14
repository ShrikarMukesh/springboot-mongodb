package com.soccer.mongo.controllers;

import com.soccer.mongo.dtos.CreatePlayerDto;
import com.soccer.mongo.dtos.CreateTeamDto;
import com.soccer.mongo.models.Player;
import com.soccer.mongo.models.Team;
import com.soccer.mongo.repositories.PlayerRepository;
import com.soccer.mongo.repositories.TeamRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SoccerController {

  PlayerRepository playerRepository;
  TeamRepository teamRepository;
  public SoccerController(TeamRepository teamRepository, PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @PostMapping("/teams/{id}/players")
  public ResponseEntity<Team> addPlayersToTeam(@PathVariable String id, @RequestBody List<String> playerIds) {
    Optional<Team> optionalTeam = teamRepository.findById(id);

    if (optionalTeam.isEmpty()) {
      return new ResponseEntity<>(null, HttpStatus.OK);
    }

    Team teamToUpdate = optionalTeam.get();

    Set<Player> playersToAdd = playerIds.stream()
            .map(playerId -> playerRepository.findById(playerId))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());

    teamToUpdate.setPlayers(playersToAdd);

    Team teamUpdated =  teamRepository.save(teamToUpdate);

    return new ResponseEntity<>(teamUpdated, HttpStatus.OK);
  }

}
