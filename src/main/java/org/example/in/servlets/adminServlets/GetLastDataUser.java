package org.example.in.servlets.adminServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.CounterReadingDTO;
import org.example.dto.adminDTO.UserNameDTO;
import org.example.model.User;
import org.example.service.AdminServiceImpl;

import java.io.IOException;
import java.util.List;

/**
 * Сервлет просмотр последних внесенных данных пользователем
 */
@WebServlet(name = "LastDataUser", value = "/admin/get-last-data-user")
public class GetLastDataUser extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser;
        try {
            currentUser = (User) req.getSession(true).getAttribute("user");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("User not authenticated");
            return;
        }

        if (currentUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Admin not authenticated");
            return;
        }
        if (currentUser.getRoleAsString().equals("ADMIN")) {
            AdminServiceImpl adminService = new AdminServiceImpl();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            UserNameDTO userData;
            try {
                userData = objectMapper.readValue(req.getReader(), UserNameDTO.class);
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid data");
                return;
            }
            User user = new User();
            user.setUsername(userData.getUsername());
            List<CounterReadingDTO> data = adminService.getLastUserInfo(user);

            if (!data.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(data));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.setContentType("application/json");
                resp.getWriter().write("Data not found");
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("application/json");
            resp.getWriter().write("Access denied");
        }
    }
}
