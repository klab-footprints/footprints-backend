package org.konkuk.klab.mtot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {

    @NotNull(message = "journeyId")
    private long journeyId;

    @NotNull(message = "title이 존재하지 않습니다.")
    private String title;

    @NotNull(message = "article이 존재하지 않습니다.")
    private String article;

}
