package com.soccer.mongo.controllers;

import com.soccer.mongo.dtos.CreateTeamDto;
import com.soccer.mongo.models.Player;
import com.soccer.mongo.models.Team;
import com.soccer.mongo.repositories.TeamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teams")
public class TeamsController {
    TeamRepository teamRepository;
    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody CreateTeamDto createTeamDto) {
        Team teamCreated = teamRepository.save(createTeamDto.toTeam());

        return new ResponseEntity<>(teamCreated, HttpStatus.CREATED);
    }

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> listOfTeams(){
        // Sort teams by name in descending order
        //List<Team> teams = teamRepository.findAll(Sort.by(Direction.DESC, "name"));
        List<Team> teams = teamRepository.findAll();
        return new ResponseEntity<>(teams , HttpStatus.OK);
    }

    @PutMapping("/teams/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable String id, @RequestBody CreateTeamDto createTeamDto) {
        Optional<Team> optionalTeam = teamRepository.findById(id);

        if (optionalTeam.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        Team teamToUpdate = optionalTeam.get()
                .setAddress(createTeamDto.getAddress())
                .setName(createTeamDto.getName())
                .setAcronym(createTeamDto.getAcronym());

        Team teamUpdated =  teamRepository.save(teamToUpdate);

        return new ResponseEntity<>(teamUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable String id, @RequestBody CreateTeamDto createTeamDto) {
        teamRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
