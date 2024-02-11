package org.example.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.UserCreateDTO;
import org.example.dto.UserDTO;
import org.example.repository.BaseRepository;
import org.example.service.UserService;

import java.io.IOException;

@WebServlet(name = "signup", value = "/signup")
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = new UserService();
        ObjectMapper objectMapper = new ObjectMapper();
        BaseRepository.initializeConnection();
        UserCreateDTO userCreateDTO = objectMapper.readValue(req.getReader(), UserCreateDTO.class);
        if (userCreateDTO.getUsername() == null || userCreateDTO.getPassword() == null ||
                userCreateDTO.getUsername().length() < 4 || userCreateDTO.getPassword().length() < 4) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Data not valid");
            return;
        }

        UserDTO dto = userService.registerUser(userCreateDTO);
        if (dto == null) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("Username already exists");
        } else {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(dto));
        }
    }
}
