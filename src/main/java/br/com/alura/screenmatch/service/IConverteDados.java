package br.com.alura.screenmatch.service;

public interface IConverteDados {
    <T> T desserializa(String json, Class<T> classe);
}
