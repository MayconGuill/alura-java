package br.com.alura.screenmatch;

import br.com.alura.screenmatch.models.Serie;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        ConsumoAPI consumo = new ConsumoAPI();

        try {
            String json = consumo.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=91086834");
            ConverteDados converte = new ConverteDados();

            var serie = converte.obterDados(json, Serie.class);
            System.out.println(serie);
        }  catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
