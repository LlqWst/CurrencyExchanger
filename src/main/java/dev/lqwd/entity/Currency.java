package dev.lqwd.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency{

    private Long id;
    private String name;
    private String code;
    private String sign;

}
