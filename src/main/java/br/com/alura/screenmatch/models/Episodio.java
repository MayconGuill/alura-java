package br.com.alura.screenmatch.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer temporada;
    private String titulo;
    private Integer numero;
    private Double avaliacao;
    private LocalDate dataLancamento;
    @ManyToOne
    private Serie serie;

    public Episodio() {
    }

    public Episodio(Integer temporada, EpisodioDto episodioDTO) {
        this.temporada = temporada;
        this.titulo = episodioDTO.titulo();
        this.numero = episodioDTO.numero();

        try {
            this.avaliacao = Double.valueOf(episodioDTO.avaliacao());
        } catch (NumberFormatException e) {
            this.avaliacao = 0.0;
        }

        try {
            this.dataLancamento = LocalDate.parse(episodioDTO.dataDeLancamento());
        } catch (DateTimeParseException e) {
            this.dataLancamento = null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getNumero() {
        return numero;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        return "temporada=" + temporada +
                ", titulo=" + titulo +
                ", numero=" + numero +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento;
    }
}
