package br.com.fiap.micronectar.config.jpa; // Ou outro pacote apropriado

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false) // Não aplicar automaticamente a todos os Booleans
public class BooleanToStringSNConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null) {
            return "N"; // Ou lançar exceção se null não for permitido pela lógica de negócio
        }
        return attribute ? "S" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        // Considera apenas 'S' (case-insensitive) como true, qualquer outra coisa (incluindo null ou 'N') será false.
        return "S".equalsIgnoreCase(dbData);
    }
}