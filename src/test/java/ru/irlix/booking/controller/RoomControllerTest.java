package ru.irlix.booking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.irlix.booking.dto.room.RoomCreateRequest;
import ru.irlix.booking.dto.room.RoomUpdateRequest;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoomControllerTest extends BaseIntegrationTest {

    @Test

    void getAllTest_shouldReturnRoomList() throws Exception {
        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getByIdTest_shouldReturnRoomById() throws Exception {
        UUID id = UUID.fromString("44444444-4444-4444-4444-444444444444");

        mockMvc.perform(get("/rooms/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Малый переговорный зал"))
                .andExpect(jsonPath("$.floorNumber").value(3))
                .andExpect(jsonPath("$.roomNumber").value(15))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    void createTest_shouldCreateNewRoom_and_shouldReturnCreatedRoom() throws Exception {
        UUID officeId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        RoomCreateRequest createRequest = new RoomCreateRequest("Create test name", (short) 14, (short) 15, officeId);
        String jsonCreateRequest = getMapper().writeValueAsString(createRequest);

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Create test name"))
                .andExpect(jsonPath("$.floorNumber").value(14))
                .andExpect(jsonPath("$.roomNumber").value(15))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    void updateTest_shouldUpdateRoomById_and_shouldReturnUpdatedRoom() throws Exception {
        UUID roomId = UUID.fromString("55555555-5555-5555-5555-555555555555");
        UUID officeId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        RoomUpdateRequest updateRequest = new RoomUpdateRequest("Update test name", (short) 14, (short) 15, officeId);
        String jsonUpdateRequest = getMapper().writeValueAsString(updateRequest);

        mockMvc.perform(patch("/rooms/{id}", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Update test name"))
                .andExpect(jsonPath("$.floorNumber").value(14))
                .andExpect(jsonPath("$.roomNumber").value(15));
    }

    @Test
    void deleteTest_shouldSetIsDeleteTrueOnRoom() throws Exception {
        UUID id = UUID.fromString("66666666-6666-6666-6666-666666666666");

        mockMvc.perform(MockMvcRequestBuilders.delete("/rooms/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rooms/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDelete").value(true));
    }

    @Test
    void notFoundCheck_shouldReturnStatusCode404() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/rooms/{id}", id))
                .andExpect(status().isNotFound());
    }
}