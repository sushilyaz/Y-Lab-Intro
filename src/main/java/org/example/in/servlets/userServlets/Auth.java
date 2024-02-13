package org.example.in.servlets.userServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.dto.AuthDTO;
import org.example.model.User;
import org.example.service.UserService;

import java.io.IOException;

@WebServlet(name = "Login", value = "/login")
public class Auth extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = new UserService();
        ObjectMapper objectMapper = new ObjectMapper();
        AuthDTO dto = objectMapper.readValue(req.getReader(), AuthDTO.class);
        User user = userService.authenticationUser(dto);
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Incorrect login or password");
        } else {
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write("Auth successfully!");
        }
    }
}
