package br.com.alura.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Serie(
        @JsonAlias("Title") String titulo,
        @JsonAlias("totalSeasons") Integer totalTemporadas,
        @JsonAlias("imdbRating") String avaliacao) {

}
