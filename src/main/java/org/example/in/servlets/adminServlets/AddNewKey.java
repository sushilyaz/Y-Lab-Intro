package org.example.in.servlets.adminServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.dto.adminDTO.NewKeyDTO;
import org.example.model.User;
import org.example.service.AdminService;

import java.io.IOException;

@WebServlet(name = "AddNewKey", value = "/admin/add-new-key")
public class AddNewKey extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            NewKeyDTO data;
            try {
                data = objectMapper.readValue(req.getReader(), NewKeyDTO.class);
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Data no valid!");
                return;
            }


            if (adminService.addNewKey(data.getNewKey())) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write("Key added successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.setContentType("application/json");
                resp.getWriter().write("Key already exist");
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("application/json");
            resp.getWriter().write("Access denied");
        }
    }
}
