package com.example.demo;


import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DojoStreamTest {

    @Test
    void converterData(){
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }

    @Test
    void jugadoresMayoresA35(){

        Predicate<Player> mayorA35 = player -> player.getAge() > 35;
        ArrayList<Player> result = CsvUtilFile.getPlayers().stream()
                .filter(mayorA35)
                .collect(Collectors.toCollection(ArrayList::new));
        result.forEach(System.out::println);

    }

    @Test
    void mejorJugadorConNacionalidadFrancia(){

        Predicate<Player> nacionalidadFrancia = player -> player.getNational().equals("France");
        Optional<Player> result = CsvUtilFile.getPlayers().stream()
                .filter(nacionalidadFrancia)
                .reduce((p1, p2) -> p1.getWinners() > p2.getWinners()?p1:p2);
        result.ifPresent(System.out::println);

    }

    @Test
    void clubsAgrupadosPorNacionalidad(){

        Map<String, Set<String>> result = CsvUtilFile.getPlayers().stream()
                .collect(Collectors.groupingBy(
                        Player::getNational,
                        HashMap::new,
                        Collectors.mapping(
                                Player::getClub,
                                Collectors.toSet()
                        )
                ));

        result.forEach((key, value) -> {
            System.out.println(key + ": \n");
            System.out.println(value);
            System.out.println("\n");
        });

    }

    @Test
    void clubConElMejorJugador(){

        Optional<Player> result = CsvUtilFile.getPlayers().stream()
                .reduce(((p1, p2) -> p1.getWinners() > p2.getWinners()?p1:p2));
        String club = result.get().getClub();
        System.out.println(club);

    }

    @Test
    void mejorJugadorSegunNacionalidad(){

        Map<String, Optional<Player>> result = CsvUtilFile.getPlayers().stream()
                .collect(Collectors.groupingBy(
                        Player::getNational,
                        Collectors.reducing((p1, p2) -> p1.getWinners() > p2.getWinners()?p1:p2)
                ));

        result.forEach((key, value) -> {
            System.out.println(key + ": \n");
            value.ifPresent(System.out::println);
            System.out.println("\n");
        });

    }

}