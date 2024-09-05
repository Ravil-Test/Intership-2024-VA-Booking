package ru.irlix.booking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ru.irlix.booking.dto.office.OfficeCreateRequest;
import ru.irlix.booking.dto.office.OfficeResponse;
import ru.irlix.booking.dto.office.OfficeUpdateRequest;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class OfficeControllerTest extends BaseIntegrationTest {

    @Test
    void getAllTest_shouldReturnOfficeList() throws Exception {
        mockMvc.perform(get("/offices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getOfficePageTest_shouldReturnPageOfOffices() throws Exception {
        OfficeResponse expectedOffice = new OfficeResponse("123 Main St, Springfield", "Head Office", false);

        MvcResult mvcResult = mockMvc.perform(get("/offices/find")
                        .param("name", "Head Office")
                        .param("isDelete", "false")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name"))
                .andExpect(status().isOk())
                .andReturn();

        String contentResponse = mvcResult.getResponse().getContentAsString();

        List<OfficeResponse> contentList = getMapper().readValue(getMapper().readTree(contentResponse).get("content").toString(),
                new TypeReference<>() {
                });

        Assertions.assertEquals(expectedOffice, contentList.get(0));
    }

    @Test
    void getOfficeByIdTest_shouldReturnOfficeById() throws Exception {
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");

        mockMvc.perform(get("/offices/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Head Office"))
                .andExpect(jsonPath("$.address").value("123 Main St, Springfield"))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    void createOfficeTest_shouldCreateNewOffice_and_shouldReturnCreatedOffice() throws Exception {
        OfficeCreateRequest createRequest = new OfficeCreateRequest("Test address", "Test name");
        String jsonCreateRequest = getMapper().writeValueAsString(createRequest);

        mockMvc.perform(post("/offices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test name"))
                .andExpect(jsonPath("$.address").value("Test address"))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    void updateOfficeTest_shouldUpdateOfficeById_and_shouldReturnUpdatedOffice() throws Exception {
        UUID id = UUID.fromString("33333333-3333-3333-3333-333333333333");

        OfficeUpdateRequest updateRequest = new OfficeUpdateRequest("Update test address", "Update test name");
        String jsonUpdateRequest = getMapper().writeValueAsString(updateRequest);

        mockMvc.perform(patch("/offices/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Update test name"))
                .andExpect(jsonPath("$.address").value("Update test address"))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    void deleteOfficeTest_shouldSetIsDeleteTrueOnOffice() throws Exception {
        UUID id = UUID.fromString("22222222-2222-2222-2222-222222222222");

        mockMvc.perform(delete("/offices/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/offices/{id}", id))
                .andExpect(jsonPath("$.isDelete").value(true));
    }

    @Test
    void notFoundCheckTest_shouldReturnStatusCode404() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/offices/{id}", id))
                .andExpect(status().isNotFound());
    }
}