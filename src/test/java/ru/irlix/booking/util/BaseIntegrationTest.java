package ru.irlix.booking.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {

    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void initMockWvc() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .addFilter(((request, response, chain) -> {
                    String charsetUtf8 = "UTF-8";
                    request.setCharacterEncoding(charsetUtf8);
                    response.setCharacterEncoding(charsetUtf8);
                    chain.doFilter(request, response);
                })).build();
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

}
