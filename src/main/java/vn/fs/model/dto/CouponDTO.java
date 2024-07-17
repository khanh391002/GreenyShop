package vn.fs.model.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponDTO {
    private Long id;

    private String code;

    private Integer discount;

    private LocalDate expirationDate;

    private Boolean expires;

    private Boolean deleted;
}
