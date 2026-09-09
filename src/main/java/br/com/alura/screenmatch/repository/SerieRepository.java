package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.models.Categoria;
import br.com.alura.screenmatch.models.Episodio;
import br.com.alura.screenmatch.models.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCase(String nomeAtor);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :temporada AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(int temporada, double avaliacao);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nomeEpisodio")
    List<Episodio> episodiosPorTrecho(String nomeEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.titulo ILIKE %:nomeSerie% ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> encontraTop5EpisodiosPorSerie(String nomeSerie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.titulo ILIKE %:nomeSerie% AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEhAno(String nomeSerie, int anoLancamento);
}
