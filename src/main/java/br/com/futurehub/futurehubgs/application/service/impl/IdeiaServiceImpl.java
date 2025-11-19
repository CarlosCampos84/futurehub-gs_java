package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.IdeiaCreateRequest;
import br.com.futurehub.futurehubgs.application.dto.IdeiaResponse;
import br.com.futurehub.futurehubgs.application.dto.IdeiaUpdateRequest;
import br.com.futurehub.futurehubgs.application.service.IdeiaService;
import br.com.futurehub.futurehubgs.domain.Ideia;
import br.com.futurehub.futurehubgs.domain.Missao;
import br.com.futurehub.futurehubgs.domain.Usuario;
import br.com.futurehub.futurehubgs.infrastructure.repository.IdeiaRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.MissaoRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.UsuarioRepository;
import br.com.futurehub.futurehubgs.messaging.IdeaEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IdeiaServiceImpl implements IdeiaService {

    private final IdeiaRepository ideiaRepo;
    private final UsuarioRepository usuarioRepo;
    private final MissaoRepository missaoRepo;
    private final IdeaEventPublisher publisher;

    private IdeiaResponse toResp(Ideia i) {

        String autorNome = null;
        if (i.getAutorId() != null) {
            autorNome = usuarioRepo.findById(i.getAutorId())
                    .map(Usuario::getNome)
                    .orElse(null);
        }

        return new IdeiaResponse(
                i.getId(),
                i.getTitulo(),
                i.getDescricao(),
                i.getAutorId(),
                autorNome,
                i.getMissaoId(),
                i.getMediaNotas(),
                i.getTotalAvaliacoes(),
                i.getCreatedAt()
        );
    }

    @Override
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public IdeiaResponse criar(IdeiaCreateRequest req) {

        Usuario autor = usuarioRepo.findById(req.idUsuario())
                .orElseThrow(() -> new IllegalArgumentException("erro.usuario.nao.encontrado"));

        Missao missao = null;
        if (req.idMissao() != null) {
            missao = missaoRepo.findById(req.idMissao())
                    .orElseThrow(() -> new IllegalArgumentException("erro.missao.nao.encontrada"));
        }

        Ideia ideia = Ideia.builder()
                .titulo(req.titulo())
                .descricao(req.descricao())
                .autorId(autor.getId())
                .missaoId(missao != null ? missao.getId() : null)
                .mediaNotas(0.0)
                .totalAvaliacoes(0)
                .createdAt(LocalDateTime.now())
                .build();

        ideia = ideiaRepo.save(ideia);

        // evento assíncrono (RabbitMQ)
        publisher.publishCreated(ideia);

        return toResp(ideia);
    }

    @Override
    @Cacheable(
            value = "ideiasPorArea",
            key = "#areaId + '-' + (#q == null ? '' : #q.trim()) + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort"
    )
    public Page<IdeiaResponse> listar(String areaId, String q, Pageable pageable) {

        List<Ideia> todas = ideiaRepo.findAll();
        String query = (q == null || q.isBlank()) ? null : q.trim().toLowerCase();

        List<Ideia> filtradas = todas.stream()
                .filter(i -> {
                    boolean matchArea = true;
                    if (areaId != null && !areaId.isBlank()) {
                        Usuario autor = usuarioRepo.findById(i.getAutorId()).orElse(null);
                        matchArea = autor != null
                                && autor.getAreaInteresseId() != null
                                && areaId.equals(autor.getAreaInteresseId());
                    }

                    boolean matchTitulo = true;
                    if (query != null) {
                        matchTitulo = i.getTitulo() != null
                                && i.getTitulo().toLowerCase().contains(query);
                    }

                    return matchArea && matchTitulo;
                })
                .sorted(Comparator.comparing(Ideia::getCreatedAt).reversed())
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtradas.size());
        if (start > end) {
            return new PageImpl<>(List.of(), pageable, filtradas.size());
        }

        List<IdeiaResponse> content = filtradas.subList(start, end)
                .stream()
                .map(this::toResp)
                .toList();

        return new PageImpl<>(content, pageable, filtradas.size());
    }

    @Override
    public IdeiaResponse buscar(String id) {
        Ideia ideia = ideiaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("erro.ideia.nao.encontrada"));
        return toResp(ideia);
    }

    @Override
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public IdeiaResponse atualizar(String id, IdeiaUpdateRequest req) {

        Ideia ideia = ideiaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("erro.ideia.nao.encontrada"));

        ideia.setTitulo(req.titulo());
        ideia.setDescricao(req.descricao());

        ideia = ideiaRepo.save(ideia);

        return toResp(ideia);
    }

    @Override
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public void deletar(String id) {

        if (!ideiaRepo.existsById(id)) {
            throw new IllegalArgumentException("erro.ideia.nao.encontrada");
        }

        ideiaRepo.deleteById(id);
    }
}











