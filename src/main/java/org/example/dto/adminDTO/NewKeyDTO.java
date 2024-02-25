package org.example.dto.adminDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewKeyDTO {
    @NotBlank
    @Size(min = 1)
    private String newKey;
}
