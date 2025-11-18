package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.AvaliacaoCreateRequest;
import br.com.futurehub.futurehubgs.application.service.AvaliacaoService;
import br.com.futurehub.futurehubgs.domain.Avaliacao;
import br.com.futurehub.futurehubgs.domain.Ideia;
import br.com.futurehub.futurehubgs.infrastructure.repository.AvaliacaoRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.IdeiaRepository;
import br.com.futurehub.futurehubgs.messaging.AvaliacaoEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AvaliacaoServiceImpl implements AvaliacaoService {

    private final AvaliacaoRepository repo;
    private final IdeiaRepository ideiaRepo;
    private final AvaliacaoEventPublisher avaliacaoEventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public void avaliar(AvaliacaoCreateRequest req) {
        var ideia = ideiaRepo.findById(req.idIdeia())
                .orElseThrow(() -> new IllegalArgumentException("erro.ideia.nao.encontrada"));

        var avaliacao = Avaliacao.builder()
                .ideia(ideia)
                .nota(req.nota())
                .dataAvaliacao(LocalDateTime.now())
                .build();

        repo.save(avaliacao);

        atualizarEstatisticasIdeia(ideia, req.nota());

        // evento assíncrono para ranking
        avaliacaoEventPublisher.publishAvaliacao(ideia.getId(), req.nota());
    }

    private void atualizarEstatisticasIdeia(Ideia ideia, int novaNota) {
        int totalAnterior = ideia.getTotalAvaliacoes();
        double mediaAnterior = ideia.getMediaNotas() != null ? ideia.getMediaNotas() : 0.0;

        int novoTotal = totalAnterior + 1;
        double somaNotas = mediaAnterior * totalAnterior + novaNota;
        double novaMedia = somaNotas / novoTotal;

        ideia.setTotalAvaliacoes(novoTotal);
        ideia.setMediaNotas(novaMedia);
        // JPA faz o flush automático ao final da transação
    }
}
