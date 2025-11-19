package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.RankingUsuarioResponse;
import br.com.futurehub.futurehubgs.application.service.RankingService;
import br.com.futurehub.futurehubgs.domain.Ideia;
import br.com.futurehub.futurehubgs.domain.Ranking;
import br.com.futurehub.futurehubgs.domain.Usuario;
import br.com.futurehub.futurehubgs.infrastructure.repository.IdeiaRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.RankingRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final RankingRepository rankingRepo;
    private final IdeiaRepository ideiaRepo;
    private final UsuarioRepository usuarioRepo;

    @Override
    @CacheEvict(value = "rankings", allEntries = true)
    public void processarEventoAvaliacao(String ideiaId, int nota) {

        Ideia ideia = ideiaRepo.findById(ideiaId)
                .orElseThrow(() -> new IllegalArgumentException("Ideia não encontrada para ranking"));

        String usuarioId = ideia.getAutorId();
        if (usuarioId == null) {
            throw new IllegalArgumentException("Ideia sem autor não pode participar do ranking");
        }

        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado para ranking"));

        String periodoAtual = YearMonth.now().toString(); // ex.: 2025-11

        Ranking ranking = rankingRepo.findByUsuarioIdAndPeriodo(usuarioId, periodoAtual)
                .orElseGet(() -> Ranking.builder()
                        .usuarioId(usuarioId)
                        .pontuacaoTotal(0)
                        .periodo(periodoAtual)
                        .build()
                );

        ranking.setPontuacaoTotal(ranking.getPontuacaoTotal() + nota);
        rankingRepo.save(ranking);

        usuario.setPontos(usuario.getPontos() + nota);
        usuarioRepo.save(usuario);
    }

    @Override
    @Cacheable(value = "rankings", key = "#periodo == null || #periodo.isBlank() ? 'current' : #periodo")
    public List<RankingUsuarioResponse> listarPorPeriodo(String periodo) {

        String p = (periodo == null || periodo.isBlank())
                ? YearMonth.now().toString()
                : periodo;

        List<Ranking> rankings = rankingRepo.findByPeriodoOrderByPontuacaoTotalDesc(p);

        List<RankingUsuarioResponse> resposta = new ArrayList<>();
        int posicao = 1;

        for (Ranking r : rankings) {
            String usuarioId = r.getUsuarioId();

            String usuarioNome = null;
            if (usuarioId != null) {
                usuarioNome = usuarioRepo.findById(usuarioId)
                        .map(Usuario::getNome)
                        .orElse(null);
            }

            resposta.add(new RankingUsuarioResponse(
                    usuarioId,
                    usuarioNome,
                    r.getPontuacaoTotal(),
                    r.getPeriodo(),
                    posicao++
            ));
        }

        // Garante ordem desc por pontuação
        resposta.sort(Comparator.comparingInt(RankingUsuarioResponse::pontuacaoTotal).reversed());

        return resposta;
    }
}








