package br.com.alura.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SerieDto(
        @JsonAlias("Title") String titulo,
        @JsonAlias("totalSeasons") Integer totalTemporadas,
        @JsonAlias("imdbRating") String avaliacao,
        @JsonAlias("Genre") String genero,
        @JsonAlias("Actors") String atores,
        @JsonAlias("Poster") String poster,
        @JsonAlias("Plot") String sinopse
) {
    @Override
    public String toString() {
        return "Serie [Titulo=" + titulo +
                ", TotalTemporada=" + totalTemporadas +
                ", Avaliação=" + avaliacao +
                ", Genero=" + genero +
                ", Atores=" + atores +
                ", Poster=" + poster +
                ", Sinopse=" + sinopse + "]";
    }
}
