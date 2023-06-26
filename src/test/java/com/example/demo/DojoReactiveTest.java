package com.example.demo;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

public class DojoReactiveTest {

    @Test
    void converterData(){
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }

    @Test
    void jugadoresMayoresA35(){

        List<Player> list = CsvUtilFile.getPlayers();
        Flux<Player> fluxPlayers = Flux.fromIterable(list);
        Flux<Player> result = fluxPlayers.filter(player -> player.getAge() > 35);
        result.subscribe(System.out::println);
    }

    @Test
    void mejorJugadorConNacionalidadFrancia(){

        List<Player> list = CsvUtilFile.getPlayers();
        Flux<Player> fluxPlayers = Flux.fromIterable(list);
        Mono<Player> result = fluxPlayers.filter(player -> player.getNational().equals("France"))
                .reduce((p1, p2) -> p1.getWinners() > p2.getWinners()?p1:p2);
        result.subscribe(System.out::println);
    }

    @Test
    void clubsAgrupadosPorNacionalidad(){

        List<Player> list = CsvUtilFile.getPlayers();
        Flux<Player> fluxPlayers = Flux.fromIterable(list);
        Mono<Map<String, Collection<String>>> result = fluxPlayers.distinct().collectMultimap(Player::getNational, Player::getClub);
        result.subscribe(System.out::println);

    }

    @Test
    void clubConElMejorJugador(){

        List<Player> list = CsvUtilFile.getPlayers();
        Flux<Player> fluxPlayers = Flux.fromIterable(list);
        Mono<Player> result = fluxPlayers.reduce((p1, p2) -> p1.getWinners() > p2.getWinners()?p1:p2);
        result.subscribe(player -> System.out.println(player.getClub()));

    }

    @Test
    void mejorJugadorSegunNacionalidad(){

        List<Player> list = CsvUtilFile.getPlayers();
        Flux<Player> fluxPlayers = Flux.fromIterable(list);

        Mono<Map<String, String>> result = fluxPlayers.groupBy(Player::getNational)
                .flatMap(group -> group.reduce((p1, p2) -> p1.getWinners() > p2.getWinners()?p1:p2))
                .collectMap(Player::getNational, Player::getName);
        result.subscribe(System.out::println);

    }

}