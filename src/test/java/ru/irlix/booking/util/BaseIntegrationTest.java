package ru.irlix.booking.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public abstract class BaseIntegrationTest {

    private static MockedStatic<Clock> mockedClock;

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

        mockDateTime();
    }

    public ObjectMapper getMapper() {
        return mapper;
    }


    public static void mockDateTime() {
        LocalDateTime fixedTime = LocalDateTime.now().withNano(0);
        mockDateTime(fixedTime);
    }

    public static void mockDateTime(LocalDateTime dateTime) {
        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
        ZoneId zoneId = ZoneId.systemDefault();
        Clock fixClock = Clock.fixed(instant, zoneId);

        mockedClock = mockStatic(Clock.class);
        mockedClock.when(Clock::systemDefaultZone).thenReturn(fixClock);
        mockedClock.when(Clock::systemUTC).thenReturn(fixClock);
    }

    @AfterEach
    public void tearDown() {
        mockedClock.close();
    }

}
