package ru.irlix.booking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.irlix.booking.dto.workplace.WorkplaceCreateRequest;
import ru.irlix.booking.dto.workplace.WorkplaceUpdateRequest;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WorkplaceControllerTest extends BaseIntegrationTest {

    @Test
    void getAllTest_shouldReturnWorkplaceList() throws Exception {
        mockMvc.perform(get("/workplaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getWorkplaceByIdTest_shouldReturnWorkplaceById() throws Exception {
        UUID id = UUID.fromString("77777777-7777-7777-7777-777777777777");
        mockMvc.perform(get("/workplaces/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.description").value("Desk near the window"))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    void createWorkplaceTest_shouldCreateNewWorkplace_and_shouldReturnCreatedWorkplace() throws Exception {
        UUID roomId = UUID.fromString("55555555-5555-5555-5555-555555555555");
        WorkplaceCreateRequest createRequest = new WorkplaceCreateRequest(5, "Create test description", roomId);
        String jsonCreateRequest = getMapper().writeValueAsString(createRequest);

        mockMvc.perform(post("/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value(5))
                .andExpect(jsonPath("$.description").value("Create test description"));
    }

    @Test
    void updateWorkplaceTest_shouldUpdateWorkplaceById_and_shouldReturnUpdatedWorkplace() throws Exception {
        UUID workplaceId = UUID.fromString("88888888-8888-8888-8888-888888888888");
        UUID roomId = UUID.fromString("66666666-6666-6666-6666-666666666666");

        WorkplaceUpdateRequest updateRequest = new WorkplaceUpdateRequest(3, "Update test description", roomId);
        String jsonUpdateRequest = getMapper().writeValueAsString(updateRequest);

        mockMvc.perform(patch("/workplaces/{id}", workplaceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(3))
                .andExpect(jsonPath("$.description").value("Update test description"));
    }

    @Test
    void deleteWorkplaceTest_shouldSetIsDeleteWorkplaceTrue() throws Exception {
        UUID id = UUID.fromString("99999999-9999-9999-9999-999999999999");

        mockMvc.perform(delete("/workplaces/{id}", id))
                .andExpect(status().isOk());

        ResultActions perform = mockMvc.perform(get("/workplaces/{id}", id));
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDelete").value(true));
    }

    @Test
    void notFoundTest_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/workplaces/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}