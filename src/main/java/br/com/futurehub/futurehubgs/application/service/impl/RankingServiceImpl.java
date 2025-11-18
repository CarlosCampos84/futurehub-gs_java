package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.RankingUsuarioResponse;
import br.com.futurehub.futurehubgs.application.service.RankingService;
import br.com.futurehub.futurehubgs.domain.Ideia;
import br.com.futurehub.futurehubgs.domain.Ranking;
import br.com.futurehub.futurehubgs.infrastructure.repository.IdeiaRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final RankingRepository rankingRepo;
    private final IdeiaRepository ideiaRepo;

    @Override
    @Transactional
    @CacheEvict(value = "rankings", allEntries = true)
    public void processarEventoAvaliacao(Long ideiaId, int nota) {
        Ideia ideia = ideiaRepo.findById(ideiaId)
                .orElseThrow(() -> new IllegalArgumentException("Ideia não encontrada para ranking"));

        var usuario = ideia.getAutor();
        if (usuario == null) {
            throw new IllegalArgumentException("Ideia sem autor não pode participar do ranking");
        }

        String periodoAtual = YearMonth.now().toString(); // ex.: 2025-11

        // Busca em memória por usuário + período (sem exigir métodos extras no repository)
        Ranking ranking = rankingRepo.findAll().stream()
                .filter(r -> r.getUsuario() != null
                        && r.getUsuario().getId().equals(usuario.getId())
                        && periodoAtual.equals(r.getPeriodo()))
                .findFirst()
                .orElseGet(() -> Ranking.builder()
                        .usuario(usuario)
                        .pontuacaoTotal(0)
                        .periodo(periodoAtual)
                        .build()
                );

        ranking.setPontuacaoTotal(ranking.getPontuacaoTotal() + nota);
        rankingRepo.save(ranking);

        // opcional: acumulado geral no usuário (não mexe em repositório de usuário)
        usuario.setPontos(usuario.getPontos() + nota);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "rankings", key = "#periodo == null || #periodo.isBlank() ? 'current' : #periodo")
    public List<RankingUsuarioResponse> listarPorPeriodo(String periodo) {
        String p = (periodo == null || periodo.isBlank())
                ? YearMonth.now().toString()
                : periodo;

        // filtra por período e ordena por pontuação desc
        var rankings = rankingRepo.findAll().stream()
                .filter(r -> p.equals(r.getPeriodo()))
                .sorted(Comparator.comparingInt(Ranking::getPontuacaoTotal).reversed())
                .toList();

        List<RankingUsuarioResponse> resposta = new ArrayList<>();
        int posicao = 1;
        for (Ranking r : rankings) {
            resposta.add(new RankingUsuarioResponse(
                    r.getUsuario() != null ? r.getUsuario().getId() : null,
                    r.getUsuario() != null ? r.getUsuario().getNome() : null,
                    r.getPontuacaoTotal(),
                    r.getPeriodo(),
                    posicao++
            ));
        }

        return resposta;
    }
}
