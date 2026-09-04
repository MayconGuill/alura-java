package br.com.alura.screenmatch.service;

import tools.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String url, Class<T> classe) {
        try {
            return mapper.readValue(url, classe);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível serializar os dados para JSON. Tente novamente.");
        }
    }
}
