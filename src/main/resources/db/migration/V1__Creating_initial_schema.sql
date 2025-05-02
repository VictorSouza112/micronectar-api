-- Flyway Migration Script
-- Version: V1
-- Description: Create initial schema for Micronectar application

--=============================================================================--
--                              SEQUENCES                                      --
--=============================================================================--
CREATE SEQUENCE SEQ_MNT_USUARIO START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;


--=============================================================================--
--                              TABLES                                         --
--=============================================================================--

-- Tabela Central de Usuários
CREATE TABLE TBL_MNT_USUARIOS (
                                  id_usuario   NUMBER NOT NULL PRIMARY KEY, -- PK definida, mas não ligada à sequence aqui explicitamente no DDL padrão
                                  nome         VARCHAR2(150) NOT NULL,
                                  email        VARCHAR2(100) NOT NULL UNIQUE,
                                  senha        VARCHAR2(100) NOT NULL, -- Será hash no futuro
                                  tipo_usuario VARCHAR2(20) NOT NULL,  -- 'CLIENTE', 'INVESTIDOR', 'MICROEMPREENDEDOR'
                                  dt_cadastro  DATE DEFAULT sysdate,
                                  CONSTRAINT ck_usuario_tipo CHECK (tipo_usuario IN ('CLIENTE', 'INVESTIDOR', 'MICROEMPREENDEDOR'))
);
COMMENT ON TABLE TBL_MNT_USUARIOS IS 'Tabela central de usuários da plataforma Micronectar.';
COMMENT ON COLUMN TBL_MNT_USUARIOS.tipo_usuario IS 'Tipo funcional do usuário, usado também para controle de acesso.';

-- Tabela Microempreendedor (Herda de Usuário)
CREATE TABLE TBL_MNT_MICROEMPREENDEDORES (
                                             id_usuario           NUMBER NOT NULL PRIMARY KEY, -- PK (mesmo ID do usuário)
                                             nr_cnpj              VARCHAR2(18) NOT NULL UNIQUE,
                                             nm_fundador          VARCHAR2(150),
                                             ds_categoria         VARCHAR2(100),
                                             ds_historia          CLOB,
                                             ds_historia_fundador CLOB,
                                             pitch_url            VARCHAR2(500),
                                             qt_votos_promissor   NUMBER DEFAULT 0 NOT NULL,
                                             nr_media_avaliacao   NUMBER(5, 2) DEFAULT 0,
                                             CONSTRAINT fk_me_usuario FOREIGN KEY (id_usuario) REFERENCES TBL_MNT_USUARIOS (id_usuario) ON DELETE CASCADE
);
COMMENT ON TABLE TBL_MNT_MICROEMPREENDEDORES IS 'Dados específicos para usuários do tipo Microempreendedor.';
COMMENT ON COLUMN TBL_MNT_MICROEMPREENDEDORES.pitch_url IS 'URL para o pitch (vídeo ou documento).';
COMMENT ON COLUMN TBL_MNT_MICROEMPREENDEDORES.qt_votos_promissor IS 'Contagem total de votos "Promissor" recebidos.';