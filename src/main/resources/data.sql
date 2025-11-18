-- ================================
-- DATA.SQL – FUTUREHUB GS (H2)
-- ================================
-- Ordem de limpeza (por causa das FKs)
DELETE FROM avaliacoes;
DELETE FROM usuarios_missoes;
DELETE FROM rankings;
DELETE FROM ideias;
DELETE FROM missoes;
DELETE FROM usuarios;
DELETE FROM areas;

-- ================================
-- ÁREAS DE INTERESSE
-- ================================
INSERT INTO areas (id, nome, descricao) VALUES
                                            (1, 'Inteligência Artificial e Machine Learning',
                                             'IA aplicada ao futuro do trabalho, previsão de turnover, automação e otimização de processos.'),
                                            (2, 'Cibersegurança e Privacidade de Dados',
                                             'Proteção de informações, segurança digital e prevenção de ataques em ambientes corporativos.'),
                                            (3, 'Sustentabilidade e Ética no Trabalho',
                                             'Práticas ESG, responsabilidade socioambiental e governança ética nas empresas.'),
                                            (4, 'Trabalho Remoto e Colaboração Digital',
                                             'Modelos híbridos, produtividade remota, ferramentas e boas práticas de comunicação.');

-- ================================
-- USUÁRIOS
-- ================================
INSERT INTO usuarios (id, nome, email, pontos, id_area_interesse) VALUES
                                                                      (1,  'Sofia Mendes',   'sofia.mendes@tech.com',       3500, 1),
                                                                      (3,  'Laura Gomes',    'laura.gomes@security.com',    2800, 2),
                                                                      (5,  'Mariana Lima',   'mariana.lima@esg.com',        4100, 3),
                                                                      (12, 'Bruno Ferreira', 'bruno.ferreira@remote.com',   1800, 4);

-- ================================
-- MISSÕES (GERADAS PELA IA, COMPARTILHADAS POR ÁREA)
-- ================================
INSERT INTO missoes (id, descricao, objetivo, moral, data_criacao, id_area, status) VALUES
                                                                                        (1,
                                                                                         'Desenvolver uma proposta de algoritmo para prever fuga de talentos.',
                                                                                         'Previsão de Turnover',
                                                                                         '50',
                                                                                         CURRENT_TIMESTAMP,
                                                                                         1,
                                                                                         'ATIVA'),

                                                                                        (2,
                                                                                         'Sugerir melhorias para otimizar o uso de IA em processos internos.',
                                                                                         'Otimização IA',
                                                                                         '45',
                                                                                         CURRENT_TIMESTAMP,
                                                                                         1,
                                                                                         'ATIVA'),

                                                                                        (3,
                                                                                         'Criar um plano simples de conscientização anti-phishing para colaboradores.',
                                                                                         'Plano Anti-Phishing',
                                                                                         '40',
                                                                                         CURRENT_TIMESTAMP,
                                                                                         2,
                                                                                         'ATIVA'),

                                                                                        (5,
                                                                                         'Definir indicadores ESG para acompanhar impactos ambientais e sociais.',
                                                                                         'Métricas ESG',
                                                                                         '60',
                                                                                         CURRENT_TIMESTAMP,
                                                                                         3,
                                                                                         'ATIVA'),

                                                                                        (12,
                                                                                         'Definir boas práticas de comunicação assíncrona para times remotos.',
                                                                                         'Guia de Colaboração',
                                                                                         '30',
                                                                                         CURRENT_TIMESTAMP,
                                                                                         4,
                                                                                         'ATIVA');

-- ================================
-- IDEIAS (MURAL)
-- ================================
-- Sofia (IA)
INSERT INTO ideias (id, titulo, descricao, id_usuario, id_missao,
                    media_notas, total_avaliacoes, created_at)
VALUES
    (1,
     'Algoritmo de Fuga de Talentos',
     'Modelo preditivo para identificar colaboradores com alto risco de saída com base em engajamento, feedbacks e histórico.',
     1, 1,
     4.50, 3,
     CURRENT_TIMESTAMP - 5),

    (11,
     'Otimização de Hiperparâmetros',
     'Ferramenta para automatizar busca de hiperparâmetros em modelos de IA usados pelo RH.',
     1, 2,
     5.00, 2,
     CURRENT_TIMESTAMP - 4),

    -- Laura (Segurança)
    (3,
     'Checklist de Segurança para Home Office',
     'Lista rápida para colaboradores garantirem que seus dispositivos e redes estão seguros ao trabalhar de casa.',
     3, 3,
     4.00, 2,
     CURRENT_TIMESTAMP - 3),

    -- Mariana (ESG)
    (5,
     'Mapeamento de Stakeholders ESG',
     'Mapa visual dos principais stakeholders internos e externos impactados pelas ações ESG da empresa.',
     5, 5,
     0.00, 0,
     CURRENT_TIMESTAMP - 2),

    -- Bruno (Trabalho Remoto)
    (12,
     'Guia de Comunicação por E-mail',
     'Template de boas práticas para e-mails em times distribuídos, evitando ruídos e retrabalho.',
     12, 12,
     0.00, 0,
     CURRENT_TIMESTAMP - 1);

-- ================================
-- USUÁRIOS x MISSÕES (HISTÓRICO)
-- ================================
INSERT INTO usuarios_missoes (id_usuario, id_missao, data_conclusao, status) VALUES
                                                                                 (1, 1,  CURRENT_TIMESTAMP - 7, 'CONCLUIDA'),
                                                                                 (1, 2,  CURRENT_TIMESTAMP - 6, 'CONCLUIDA'),
                                                                                 (3, 3,  CURRENT_TIMESTAMP - 5, 'CONCLUIDA'),
                                                                                 (5, 5,  CURRENT_TIMESTAMP - 4, 'CONCLUIDA'),
                                                                                 (12,12, CURRENT_TIMESTAMP - 3, 'CONCLUIDA');

-- ================================
-- RANKING (PONTUAÇÃO POR PERÍODO)
-- ================================
-- Exemplo de período: mensal (2025-11) ou semanal (2025-W46)
INSERT INTO rankings (id, id_usuario, pontuacao_total, periodo) VALUES
                                                                    (1,  5, 4100, '2025-11'),
                                                                    (2,  1, 3500, '2025-11'),
                                                                    (3,  3, 2800, '2025-11'),
                                                                    (4, 12, 1800, '2025-11');

-- ================================
-- AVALIAÇÕES (OPCIONAL – APENAS ALGUNS EXEMPLOS)
-- ================================
-- Se quiser começar zerado e ir testando as notas via API,
-- pode comentar esse bloco.
INSERT INTO avaliacoes (id, id_ideia, nota, data_avaliacao) VALUES
                                                                (1, 1, 5, CURRENT_TIMESTAMP - 4),
                                                                (2, 1, 4, CURRENT_TIMESTAMP - 3),
                                                                (3, 1, 4, CURRENT_TIMESTAMP - 2),

                                                                (4, 11, 5, CURRENT_TIMESTAMP - 3),
                                                                (5, 11, 5, CURRENT_TIMESTAMP - 2),

                                                                (6, 3, 4, CURRENT_TIMESTAMP - 2),
                                                                (7, 3, 4, CURRENT_TIMESTAMP - 1);
