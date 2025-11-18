package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.RankingUsuarioResponse;
import br.com.futurehub.futurehubgs.application.service.RankingService;
import br.com.futurehub.futurehubgs.domain.Ideia;
import br.com.futurehub.futurehubgs.domain.Ranking;
import br.com.futurehub.futurehubgs.domain.Usuario;
import br.com.futurehub.futurehubgs.infrastructure.repository.IdeiaRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final RankingRepository rankingRepo;
    private final IdeiaRepository ideiaRepo;

    @Override
    @CacheEvict(value = "rankings", allEntries = true)
    public void processarEventoAvaliacao(String ideiaIdStr, int nota) {

        if (ideiaIdStr == null || ideiaIdStr.isBlank()) {
            throw new IllegalArgumentException("IdeiaId inválido para ranking");
        }

        Long ideiaId;
        try {
            ideiaId = Long.valueOf(ideiaIdStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("IdeiaId inválido para ranking");
        }

        Ideia ideia = ideiaRepo.findById(ideiaId)
                .orElseThrow(() -> new IllegalArgumentException("Ideia não encontrada para ranking"));

        Usuario usuario = ideia.getAutor();
        if (usuario == null) {
            throw new IllegalArgumentException("Ideia sem autor não pode participar do ranking");
        }

        String periodoAtual = YearMonth.now().toString(); // ex.: 2025-11

        Ranking ranking = rankingRepo.findByUsuario_IdAndPeriodo(usuario.getId(), periodoAtual)
                .orElseGet(() -> Ranking.builder()
                        .usuario(usuario)
                        .pontuacaoTotal(0)
                        .periodo(periodoAtual)
                        .build()
                );

        ranking.setPontuacaoTotal(ranking.getPontuacaoTotal() + nota);
        rankingRepo.save(ranking);

        // Atualiza também pontos acumulados do usuário
        usuario.setPontos(usuario.getPontos() + nota);
        // Como o usuário está associado à Ideia, JPA costuma gerenciar o flush.
        // Se necessário, você pode persistir explicitamente num UsuarioRepository.
    }

    @Override
    @Cacheable(value = "rankings", key = "#periodo == null || #periodo.isBlank() ? 'current' : #periodo")
    public List<RankingUsuarioResponse> listarPorPeriodo(String periodo) {
        String p = (periodo == null || periodo.isBlank())
                ? YearMonth.now().toString()
                : periodo;

        List<Ranking> rankings = rankingRepo.findByPeriodoOrderByPontuacaoTotalDesc(p);

        rankings = rankings.stream()
                .sorted(Comparator.comparingInt(Ranking::getPontuacaoTotal).reversed())
                .toList();

        int pos = 1;
        return rankings.stream()
                .map(r -> new RankingUsuarioResponse(
                        r.getUsuario() != null ? r.getUsuario().getId() : null,
                        r.getUsuario() != null ? r.getUsuario().getNome() : null,
                        r.getPontuacaoTotal(),
                        r.getPeriodo(),
                        pos++
                ))
                .toList();
    }
}





