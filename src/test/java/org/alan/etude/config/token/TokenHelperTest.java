package org.alan.etude.config.token;

import org.alan.etude.handler.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TokenHelperTest {

    TokenHelper tokenHelper;

    @BeforeEach
    void beforeEach() {
        tokenHelper = new TokenHelper(new JwtHandler(), "myKey", 1000L);
    }

    @Test
    void createTokenAndParseTest() {
        // given
        String memberId = "1";
        List<String> roleTypes = List.of("NORMAL", "ADMIN");
        TokenHelper.PrivateClaims privateClaims = new TokenHelper.PrivateClaims(memberId, roleTypes);

        // when
        String token = tokenHelper.createToken(privateClaims);

        // then
        TokenHelper.PrivateClaims parsedPrivateClaims = tokenHelper.parse(token)
                .orElseThrow(RuntimeException::new);
        assertThat(parsedPrivateClaims.getMemberId()).isEqualTo(memberId);
        assertThat(parsedPrivateClaims.getRoleTypes()).contains(roleTypes.get(0), roleTypes.get(1));
    }


}
