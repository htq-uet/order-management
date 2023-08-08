package com.ghtk.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ghtk.model.Role;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String access_token;

    @JsonProperty("refresh_token")
    private String refresh_token;

    @JsonProperty("role")
    private Role role;

}
