package OneCoin.Server.rank.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RankControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getTest() throws Exception {
        Thread.sleep(1100);
        //when
        ResultActions actions =
                mockMvc.perform(get("/api/ranks"));
        //then
        MvcResult result = actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].rank").value(1))
                .andExpect(jsonPath("$.users[0].ROI", endsWith("%")))
                .andReturn();
    }
}
