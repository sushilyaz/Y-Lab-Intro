package org.example.in.servlets.counterReadingServlets;

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

@WebServlet(name = "LatestData", value = "/latest-data-for-current-user")
public class GetLatestData extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session != null) {
            User currentUser = (User) session.getAttribute("user");
            ObjectMapper objectMapper = new ObjectMapper();
            //BaseRepository.initializeConnection();
            if (currentUser != null) {
                CounterReadingService counterReadingService = new CounterReadingService();
                CounterReadingDTO counterReadingDTO = counterReadingService.getLastUserInfo(currentUser);
                if (counterReadingDTO != null) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.setContentType("application/json");
                    resp.getWriter().write(objectMapper.writeValueAsString(counterReadingDTO));
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
