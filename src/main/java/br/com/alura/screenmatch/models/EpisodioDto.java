package br.com.alura.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;

public record EpisodioDto(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Episode") Integer numero,
        @JsonAlias("imdbRating") String avaliacao,
        @JsonAlias("Released") String dataDeLancamento
) {
}
