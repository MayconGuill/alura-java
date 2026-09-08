package br.com.alura.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record TemporadaDto(
        @JsonAlias("Season") Integer numero,
        @JsonAlias("Episodes") List<EpisodioDto> episodioDtos
) {
}
