package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;

    static String token;

    static UserDTO manager;
    static ProjectDTO project;

    @BeforeAll
    static void setUp(){

        token = "Bearer " + "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI5azJWcXdtMkhadDRZLXdyLUpOWnpDc19GVTV6LUFRTXNSUXZfbVZjUk5FIn0.eyJleHAiOjE2ODc2NjU3ODQsImlhdCI6MTY4NzY0Nzc4NCwianRpIjoiZTUwMTY0YTAtNjA5My00MDQ1LWE3OGUtM2RiNGY1NWM0YmYzIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL2N5ZGVvLWRldiIsImF1ZCI6WyJ0aWNrZXRpbmctYXBwIiwiYWNjb3VudCJdLCJzdWIiOiI0Y2Y3Yzc3YS0xZTQxLTQ0NDctYjNlOS04YzRiODYyMjc1MDIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0cmFpbmluZyIsInNlc3Npb25fc3RhdGUiOiIxZWM1YTkxNi1hYTM2LTQxMzktYTQ2Yy1mZDg0YjUzNzE2M2YiLCJhY3IiOiIxIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy1jeWRlby1kZXYiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InRpY2tldGluZy1hcHAiOnsicm9sZXMiOlsiTWFuYWdlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsInNpZCI6IjFlYzVhOTE2LWFhMzYtNDEzOS1hNDZjLWZkODRiNTM3MTYzZiIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJvenp5In0.rd-45mxOVNAXvhySHiIQ2sKTA6Glw9jbwFxa4Jk2sMy8KmfH8vWuV6QDPXx0Ic_An1amj8vOvgJrh2e5pD4VzOOXf91ZOwKmDDEEKZHK-mhX8opCDI6Ael-Ng3GOyIJ2Xuh_GH_w7iJk6tBOsqqUtKK9flJwuGqm3JYLgSHjpiLOe3dtaQ82b0wOtxjH8GLb__XtAi4jjxipy3OhtiRhRX6VIm2hdCzGVZCow581jH9s8RQYsceb1EaeY7AEZZSqQKOrOSodtrvPk0KdpRLZD8YKi_Qm_exZDK58jaVOKf0OrBao8OTaD1i9ymImJprfiUYSrE06FgyJOwo38uLpOQ";

        manager = new UserDTO(2L,
                "",
                "",
                "ozzy",
                "Abc1",
                "",
                true,
                "",
                new RoleDTO(2L, "Manager"),
                Gender.MALE);

        project = new ProjectDTO(
                "API Project",
                "PR001",
                manager,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                "Some details",
                Status.OPEN
        );
    }

    @Test
    void givenNoToken_getProjects() throws Exception{

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project"))
                .andExpect(status().is4xxClientError());

    }

    @Test
    void givenToken_getProjects() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project")
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].projectCode").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").isNotEmpty())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").isString())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").value("ozzy"));

    }

    @Test
    void givenToken_createProject() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/project")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(toJsonString(project)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Project is successfully created"));
    }

    @Test
    void givenToken_updateProject() throws Exception {

        project.setProjectName("API Project-2");

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/project")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJsonString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Project is successfully updated"));
    }

    @Test
    void givenToken_deleteProject() throws Exception {

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/project/"+project.getProjectCode())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Project is successfully deleted"));

    }

    private String toJsonString(final Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }



}
