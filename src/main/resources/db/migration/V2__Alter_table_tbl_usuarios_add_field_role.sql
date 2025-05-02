-- Flyway Migration Script V2
-- Description: Add ROLE column to TBL_MNT_USUARIOS table

-- Adiciona a coluna ROLE, definindo um padrão.
ALTER TABLE TBL_MNT_USUARIOS
    ADD ROLE VARCHAR2(30) DEFAULT 'ROLE_CLIENTE' NOT NULL;

-- Adiciona um comentário à nova coluna
COMMENT ON COLUMN TBL_MNT_USUARIOS.ROLE IS 'Papel do usuário para controle de acesso (Spring Security)';

-- Garante que a constraint UNIQUE para email existe (Idempotente)
-- Tenta remover primeiro para evitar erro se já existir. Ignora erro ORA-02443 se não existir.
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE TBL_MNT_USUARIOS DROP CONSTRAINT UK_USUARIO_EMAIL';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2443 THEN -- ORA-02443: constraint does not exist
            RAISE;
        END IF;
END;
/

-- Adiciona a constraint UNIQUE para email
ALTER TABLE TBL_MNT_USUARIOS
    ADD CONSTRAINT UK_USUARIO_EMAIL UNIQUE (EMAIL);

-- Commit necessário para efetivar o UPDATE em scripts Oracle Flyway se não for a última instrução.
COMMIT;