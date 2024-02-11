package org.example.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.dto.CounterReadingDTO;
import org.example.model.User;
import org.example.service.CounterReadingService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "getAllData", value = "/get-all-data")
public class GetAllData extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session != null) {
            User currentUser = (User) session.getAttribute("user");
            ObjectMapper objectMapper = new ObjectMapper();
            if (currentUser != null) {
                CounterReadingService counterReadingService = new CounterReadingService();
                List<CounterReadingDTO> res = counterReadingService.getCRByUser(currentUser);
                if (!res.isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.setContentType("application/json");
                    resp.getWriter().write(objectMapper.writeValueAsString(res));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("Data not found");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("User not authenticated");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("User not authenticated");
        }
    }
}
