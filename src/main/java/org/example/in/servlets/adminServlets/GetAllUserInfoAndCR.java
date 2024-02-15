package org.example.in.servlets.adminServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.UserInfoDTO;
import org.example.model.User;
import org.example.service.AdminService;
import org.example.service.AdminServiceImpl;

import java.io.IOException;
import java.util.List;

/**
 * Сервлет просмотр всех данных всех пользователей
 */
@WebServlet(name = "GetAllUserInfoAndCR", value = "/admin/get-all-users-data-readings")
public class GetAllUserInfoAndCR extends HttpServlet {
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
            AdminService adminService = new AdminServiceImpl();
            ObjectMapper objectMapper = new ObjectMapper();
            List<UserInfoDTO> data = adminService.getAllUserInfo();
            if (!data.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                objectMapper.registerModule(new JavaTimeModule());
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
