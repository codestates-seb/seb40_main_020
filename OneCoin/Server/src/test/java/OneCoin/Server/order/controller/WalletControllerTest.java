package OneCoin.Server.order.controller;

import OneCoin.Server.order.dto.WalletDto;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.mapper.WalletMapper;
import OneCoin.Server.order.service.WalletService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
public class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @MockBean
    private WalletMapper mapper;

    @Test
    @DisplayName("slice test: 지갑 내역 조회")
    void getMyWallet() throws Exception {
        // given
        given(walletService.findWallets()).willReturn(List.of(new Wallet()));
        given(mapper.walletToGetResponse(any())).willReturn(List.of(new WalletDto.GetResponse()));

        // when then
        mockMvc.perform(
                        get("/api/order/my-coin")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}
