package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.models.Episodio;
import br.com.alura.screenmatch.models.Serie;
import br.com.alura.screenmatch.models.SerieDto;
import br.com.alura.screenmatch.models.TemporadaDto;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

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
        var temporadasDto = buscaTemporadasDaSerie();

        temporadasDto.stream()
                .flatMap(t -> t.episodioDtos().stream()
                        .map(ep -> new Episodio(t.numero(), ep)))
                .forEach(System.out::println);
    }

    private List<TemporadaDto> buscaTemporadasDaSerie() {
        List<TemporadaDto> temporadasDto = new ArrayList<>();

        var serie = buscaSerieWeb();
        for (int i = 1; i <= serie.totalTemporadas(); i++) {
            var jsonString = consumo.obterDados(URL_BASE + serie.titulo().replace(" ", "+") + "&season=%d".formatted(i) + API_KEY);
            var temporada = converte.desserializa(jsonString, TemporadaDto.class);
            temporadasDto.add(temporada);
        }

        return temporadasDto;
    }
}
