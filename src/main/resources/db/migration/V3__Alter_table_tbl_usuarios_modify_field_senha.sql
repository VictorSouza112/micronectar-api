-- Flyway Migration Script V3
-- Description: Ensure SENHA column in TBL_MNT_USUARIOS can store BCrypt hash

-- Altera (ou reafirma) o tamanho da coluna SENHA para acomodar o hash BCrypt
-- Se a coluna já for VARCHAR2(100), este comando não causará erro no Oracle.
ALTER TABLE TBL_MNT_USUARIOS
    MODIFY SENHA VARCHAR2(100);

-- Adiciona um comentário para clareza (opcional)
COMMENT ON COLUMN TBL_MNT_USUARIOS.SENHA IS 'Hash da senha do usuário (BCrypt)';