package org.example.in.servlets.counterReadingServlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.dto.CounterReadingCreateDTO;
import org.example.dto.CounterReadingDTO;
import org.example.mapper.CounterReadingMapper;
import org.example.model.User;
import org.example.service.CounterReadingService;

import java.io.IOException;

@WebServlet(name = "putdata", value = "/put-counter-reading")
public class PutData extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session != null) {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                CounterReadingService counterReadingService = new CounterReadingService();
                try {
                    CounterReadingCreateDTO dtoCreate = objectMapper.readValue(req.getReader(), CounterReadingCreateDTO.class);
                    CounterReadingDTO validDto = CounterReadingMapper.INSTANCE.mapCreateToDto(dtoCreate);
                    CounterReadingDTO res = counterReadingService.validationCounter(currentUser, validDto);
                    if (res != null) {
                        CounterReadingDTO dto = counterReadingService.submitCounterReading(currentUser, dtoCreate);
                        if (dto != null) {
                            resp.setStatus(HttpServletResponse.SC_CREATED);
                            resp.getWriter().write("Data submit success!");
                        } else {
                            resp.setStatus(HttpServletResponse.SC_CONFLICT);
                            resp.getWriter().write("Data already exist!");
                        }
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("Data no valid!");
                    }
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("Data no valid!");
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
