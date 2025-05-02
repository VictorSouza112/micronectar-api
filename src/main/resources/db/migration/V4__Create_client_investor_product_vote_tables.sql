-- Flyway Migration Script V4
-- Description: Create tables for Clientes, Investidores, Produtos, and Votacoes

--=============================================================================--
--                              SEQUENCES                                      --
--=============================================================================--
CREATE SEQUENCE SEQ_MNT_PRODUTO START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_MNT_VOTACAO START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;


--=============================================================================--
--                              TABLES                                         --
--=============================================================================--

-- Tabela Cliente (Herda de Usuário)
CREATE TABLE TBL_MNT_CLIENTES (
                                  id_usuario         NUMBER NOT NULL PRIMARY KEY, -- PK (mesmo ID do usuário)
                                  nr_cpf             VARCHAR2(14) NOT NULL UNIQUE,
                                  dt_nascimento      DATE NOT NULL,
                                  nr_media_avaliacao NUMBER(5, 2) DEFAULT 0,
                                  CONSTRAINT fk_cliente_usuario FOREIGN KEY (id_usuario) REFERENCES TBL_MNT_USUARIOS (id_usuario) ON DELETE CASCADE
);
COMMENT ON TABLE TBL_MNT_CLIENTES IS 'Dados específicos para usuários do tipo Cliente.';

-- Tabela Investidor (Herda de Usuário)
CREATE TABLE TBL_MNT_INVESTIDORES (
                                      id_usuario           NUMBER NOT NULL PRIMARY KEY, -- PK (mesmo ID do usuário)
                                      nr_documento         VARCHAR2(20) NOT NULL UNIQUE, -- Pode ser CPF ou CNPJ
                                      ds_perfil_investidor VARCHAR2(200),
                                      dt_nascimento        DATE, -- Nascimento ou Fundação
                                      CONSTRAINT fk_investidor_usuario FOREIGN KEY (id_usuario) REFERENCES TBL_MNT_USUARIOS (id_usuario) ON DELETE CASCADE
);
COMMENT ON TABLE TBL_MNT_INVESTIDORES IS 'Dados específicos para usuários do tipo Investidor.';

-- Tabela Produto/Serviço
-- Nota: A PK id_produto usará a sequence SEQ_MNT_PRODUTO via JPA/Hibernate (@GeneratedValue)
CREATE TABLE TBL_MNT_PRODUTOS (
                                  id_produto           NUMBER NOT NULL PRIMARY KEY,
                                  id_microempreendedor NUMBER NOT NULL,
                                  nm_produto           VARCHAR2(150) NOT NULL,
                                  vl_produto           NUMBER(10, 2) NOT NULL,
                                  qt_produto           NUMBER DEFAULT NULL,
                                  ds_produto           VARCHAR2(1000),
                                  foto_url             VARCHAR2(500),
                                  CONSTRAINT fk_prod_me FOREIGN KEY (id_microempreendedor) REFERENCES TBL_MNT_MICROEMPREENDEDORES (id_usuario) ON DELETE CASCADE
);
COMMENT ON TABLE TBL_MNT_PRODUTOS IS 'Produtos/Serviços oferecidos pelos Microempreendedores.';
COMMENT ON COLUMN TBL_MNT_PRODUTOS.qt_produto IS 'Quantidade em estoque do produto (pode ser nulo para serviços).';
COMMENT ON COLUMN TBL_MNT_PRODUTOS.foto_url IS 'URL para a foto do produto/serviço.';

-- Tabela Votacao (Votos no Pitch/ME)
-- Nota: A PK id_votacao usará a sequence SEQ_MNT_VOTACAO via JPA/Hibernate (@GeneratedValue)
CREATE TABLE TBL_MNT_VOTACOES (
                                  id_votacao           NUMBER NOT NULL PRIMARY KEY,
                                  id_votador_usuario   NUMBER NOT NULL,
                                  id_microempreendedor NUMBER NOT NULL,
                                  dt_votacao           DATE DEFAULT sysdate,
                                  fl_promissor         VARCHAR2(1) DEFAULT 'N' NOT NULL,
                                  ds_comentario        VARCHAR2(500),
                                  CONSTRAINT fk_votacao_votador FOREIGN KEY (id_votador_usuario) REFERENCES TBL_MNT_USUARIOS (id_usuario) ON DELETE CASCADE,
                                  CONSTRAINT fk_votacao_me FOREIGN KEY (id_microempreendedor) REFERENCES TBL_MNT_MICROEMPREENDEDORES (id_usuario) ON DELETE CASCADE,
                                  CONSTRAINT ck_votacao_promissor CHECK (fl_promissor IN ('N', 'S')),
                                  CONSTRAINT uk_votacao_unica UNIQUE (id_votador_usuario, id_microempreendedor)
);
COMMENT ON TABLE TBL_MNT_VOTACOES IS 'Registra votos e comentários sobre os MEs (pitches).';
COMMENT ON COLUMN TBL_MNT_VOTACOES.fl_promissor IS 'Flag indicando se o negócio é considerado promissor (S/N).';