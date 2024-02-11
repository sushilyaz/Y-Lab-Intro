package org.example.in.servlets.adminServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.model.User;
import org.example.service.AdminService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetAllKeys", value = "/admin/get-all-keys")
public class GetAllKeys extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Admin not authenticated");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Admin not authenticated");
            return;
        }
        if (currentUser.getRoleAsString().equals("ADMIN")) {
            AdminService adminService = new AdminService();
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> keys = adminService.getAllKey();

            if (!keys.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(keys));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.setContentType("application/json");
                resp.getWriter().write("Keys not found");
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("application/json");
            resp.getWriter().write("Access denied");
        }
    }
}
