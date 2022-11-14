package com.soccer.mongo.controllers;

import com.soccer.mongo.dtos.CreatePlayerDto;
import com.soccer.mongo.models.Player;
import com.soccer.mongo.models.PlayerPosition;
import com.soccer.mongo.repositories.PlayerRepository;
import com.soccer.mongo.repositories.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/players")
public class PlayersController {

    PlayerRepository playerRepository;

    public PlayersController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody CreatePlayerDto createPlayerDto) {
        Player playerCreated = playerRepository.save(createPlayerDto.toPlayer());

        return new ResponseEntity<>(playerCreated, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Player>> listOfPlayers(){
        // Sort player by position in ascending order and name in descending order
//    List<Sort.Order> orders = new ArrayList<>(){{
//      add(Sort.Order.by("position").with(Sort.Direction.ASC));
//      add(Sort.Order.by("name").with(Sort.Direction.DESC));
//    }};
        // List<Player> players = playerRepository.findAll(Sort.by(orders));
        List<Player> players = playerRepository.findAll();
        return new ResponseEntity<>(players , HttpStatus.OK);
    }

    @GetMapping("/strikers")
    public ResponseEntity<List<Player>> findByPositionAndIsAvailable(){
        List<Player> strikers = playerRepository.findByPositionAndIsAvailable(PlayerPosition.STRIKER, true);
        return new ResponseEntity<>(strikers, HttpStatus.OK);
    }

    @GetMapping("/seniors")
    public ResponseEntity<List<Player>> seniorPlayers(){
        Calendar calendar = Calendar.getInstance();

        calendar.set(1991, Calendar.JANUARY, 1);
        Date fromDate = calendar.getTime();

        calendar.set(1996, Calendar.JANUARY, 1);
        Date toDate = calendar.getTime();

        List<Player> seniors = playerRepository.findByBirthDateIsBetweenOrderByBirthDate(fromDate, toDate);

        return new ResponseEntity<>(seniors, HttpStatus.OK);
    }

    @GetMapping("/youngplayer")
    public Player youngPlayer(){
        Player player = playerRepository.findFirstByOrderByBirthDateDesc();
        return player;
    }
    @GetMapping("/players-page")
    public ResponseEntity<Page<Player>> listPlayersPage(@RequestParam int page) {
        Page<Player> players = playerRepository.findByIdIsNotNull(PageRequest.of(page - 1, 10));
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Player>> createPlayers(@RequestBody List<CreatePlayerDto> createPlayerDtoList) {
        List<Player> players = createPlayerDtoList
                .stream()
                .map(CreatePlayerDto::toPlayer)
                .collect(Collectors.toList());

        List<Player> playersCreated = playerRepository.saveAll(players);

        return new ResponseEntity<>(playersCreated, HttpStatus.CREATED);
    }
}
