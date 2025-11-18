package br.com.futurehub.futurehubgs.application.dto;

/**
 * DTO de saída para o ranking de usuários.
 */
public record RankingUsuarioResponse(
        Long usuarioId,
        String usuarioNome,
        Integer pontuacaoTotal,
        String periodo,
        Integer posicao
) {}
