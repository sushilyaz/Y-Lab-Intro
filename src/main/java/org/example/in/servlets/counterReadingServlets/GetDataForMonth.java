package org.example.in.servlets.counterReadingServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.dto.CounterReadingDTO;
import org.example.dto.DateDTO;
import org.example.model.User;
import org.example.service.CounterReadingService;

import java.io.IOException;
import java.util.Set;

@WebServlet(name = "getDataForMonth", value = "/get-data-for-month")
public class GetDataForMonth extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session != null) {
            User currentUser = (User) session.getAttribute("user");
            ObjectMapper objectMapper = new ObjectMapper();
            if (currentUser != null) {
                CounterReadingService counterReadingService = new CounterReadingService();
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();
                DateDTO dateDTO;
                try {
                    dateDTO = objectMapper.readValue(req.getReader(), DateDTO.class);
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("Invalid data");
                    return;
                }

                Set<ConstraintViolation<DateDTO>> violations = validator.validate(dateDTO);
                if (!violations.isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("Invalid data");
                    return;
                }
                CounterReadingDTO res = counterReadingService.getUserInfoForMonth(currentUser, dateDTO.getMonth(), dateDTO.getYear());
                if (res != null) {
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
