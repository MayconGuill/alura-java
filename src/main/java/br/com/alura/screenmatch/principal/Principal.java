package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.models.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://www.omdbapi.com/?t=";
    private static final String API_KEY = "&apikey=91086834";
    private final SerieRepository repositorio;
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados converte = new ConverteDados();

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    
                    1. Buscar séries
                    2. Buscar episódios
                    3. Listar séries buscadas
                    4. Buscar serie por título
                    5. Buscar series por ator
                    6. Buscar top 5 series
                    7. Buscar por categoria
                    8. Buscar series por temporada e avaliação
                    9. Busca episódio por texto
                    10. Buscar top 5 episódios
                    11. Buscar episódios por data
                    
                    0. Sair
                    
                    Selecione a opção desejada:
                    """;

            System.out.print(menu);
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> exibeSerieWeb();
                case 2 -> exibeEpisodioWeb();
                case 3 -> exibeListaDeSeriesBuscada();
                case 4 -> exibeSeriePorTitulo();
                case 5 -> exibeSeriesPorAtor();
                case 6 -> exibeTopCincoSeries();
                case 7 -> exibeSeriesPorCategoria();
                case 8 -> exibeSeriesPorTemporadaEAvaliacao();
                case 9 -> exibeEpisodioPorTrecho();
                case 10 -> exibeTopCincoEpisodiosDaSerie();
                case 11 -> exibeEpisodioPorData();
                case 0 -> System.out.println("Saindo do sistema...");
                default -> System.out.println("Digite uma opção válida.");
            }
        }
    }

    private void exibeSerieWeb() {
        System.out.println(buscaSerieWeb());
    }

    private SerieDto buscaSerieWeb() {
        System.out.println("\nDigite o nome da série que deseja visualizar: ");
        var nomeSerie = scanner.nextLine();

        var jsonString = consumo.obterDados(URL_BASE + nomeSerie.replace(" ", "+") + API_KEY);
        var serieDto = converte.desserializa(jsonString, SerieDto.class);
        var serie = new Serie(serieDto);
        repositorio.save(serie);
        return serieDto;
    }

    private void exibeListaDeSeriesBuscada() {
        List<Serie> series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void exibeEpisodioWeb() {
        var episodios = buscaEpisodiosDaSerie();

        episodios.forEach(System.out::println);
    }

    private List<Episodio> buscaEpisodiosDaSerie() {
        System.out.println("\nDigite o nome da série que deseja visualizar: ");
        var nomeSerie = scanner.nextLine();

        Serie serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie)
                .orElseThrow(() -> new RuntimeException("Registro não localizado"));

        List<TemporadaDto> temporadasDto = new ArrayList<>();

        for (int i = 1; i <= serie.getTotalTemporadas(); i++) {
            var jsonString = consumo.obterDados(URL_BASE + serie.getTitulo().replace(" ", "+") + "&season=%d".formatted(i) + API_KEY);
            var temporada = converte.desserializa(jsonString, TemporadaDto.class);
            temporadasDto.add(temporada);
        }
        List<Episodio> episodios = temporadasDto.stream()
                .flatMap(t -> t.episodioDtos().stream()
                        .map(ep -> new Episodio(t.numero(), ep)))
                .collect(Collectors.toList());

        serie.setEpisodios(episodios);
        repositorio.save(serie);

        return episodios;
    }

    private void exibeSeriePorTitulo() {
        System.out.println("\nDigite o nome da série que deseja visualizar: ");
        var nomeSerie = scanner.nextLine();

        Serie serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie)
                .orElseThrow(() -> new RuntimeException("Registro não localizado"));

        System.out.println(serie);
    }

    private void exibeSeriesPorAtor() {
        System.out.println("Qual nome do ator?");
        var nomeAtor = scanner.nextLine();

        List<Serie> series = repositorio.findByAtoresContainingIgnoreCase(nomeAtor);

        series.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void exibeTopCincoSeries() {
        List<Serie> series = repositorio.findTop5ByOrderByAvaliacaoDesc();
        series.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void exibeSeriesPorCategoria() {
        System.out.println("Digite uma categoria/gênero: ");
        var categoria = scanner.nextLine();

        List<Serie> series = repositorio.findByGenero(Categoria.procurarEm(categoria));
        series.forEach(System.out::println);
    }

    private void exibeSeriesPorTemporadaEAvaliacao() {
        System.out.println("Filtrar series até quantas temporadas? ");
        var numeroTemporada = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Com avaliação a partir de que valor? ");
        var avaliacao = scanner.nextDouble();
        List<Serie> series = repositorio.seriesPorTemporadaEAvaliacao(numeroTemporada, avaliacao);
        series.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void exibeEpisodioPorTrecho() {
        System.out.println("Digite o nome do episódio para busca? ");
        var nomeEpisodio = scanner.nextLine();

        List<Episodio> episodios = repositorio.episodiosPorTrecho(nomeEpisodio);
        episodios.forEach(System.out::println);
    }

    private void exibeTopCincoEpisodiosDaSerie() {
        System.out.println("Digite o nome da serie para busca: ");
        var nomeSerie = scanner.nextLine();

        List<Episodio> episodios = repositorio.encontraTop5EpisodiosPorSerie(nomeSerie);
        episodios.forEach(e ->
                System.out.println("Serie: %s - temporada %d, episódio %d, %s %.1f"
                        .formatted(e.getSerie().getTitulo(), e.getTemporada(), e.getNumero(), e.getTitulo(), e.getAvaliacao()))
        );
    }

    private void exibeEpisodioPorData() {
        System.out.println("Digite o nome da serie para busca: ");
        var nomeSerie = scanner.nextLine();

        System.out.println("Digite o ano de lançamento: ");
        var anoLancamento = scanner.nextInt();
        scanner.nextLine();

        List<Episodio> episodios = repositorio.episodiosPorSerieEhAno(nomeSerie, anoLancamento);
        episodios.forEach(e ->
                System.out.println("Serie: %s - temporada %d, ano %d, episódio %d, %s %.1f"
                        .formatted(e.getSerie().getTitulo(), e.getTemporada(), e.getDataLancamento().getYear(), e.getNumero(), e.getTitulo(), e.getAvaliacao()))
        );
    }
}
