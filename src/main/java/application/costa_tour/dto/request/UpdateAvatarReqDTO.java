package application.costa_tour.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateAvatarReqDTO {
    @NotNull(message = "UserId is required")
    private Long userId;
    @NotNull
    private MultipartFile avatar;
}
