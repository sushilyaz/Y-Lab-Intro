package org.example.in.servlets.adminServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.CounterReadingDTO;
import org.example.dto.adminDTO.UserNameDTO;
import org.example.model.User;
import org.example.service.AdminService;

import java.io.IOException;
import java.util.List;

/**
 * Сервлет просмотр всех поданных показателей пользователя
 */
@WebServlet(name = "getAllUserData", value = "/admin/get-user-data")
public class GetAllUserData extends HttpServlet {
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
            AdminService adminService = new AdminService();
            ObjectMapper objectMapper = new ObjectMapper();
            UserNameDTO userData = objectMapper.readValue(req.getReader(), UserNameDTO.class);
            List<CounterReadingDTO> data = adminService.getCRByUser(new User(userData.getUsername()));
            if (data != null) {
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
