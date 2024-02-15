//package org.example.in.servlets.counterReadingServlets;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.example.dto.CounterReadingDTO;
//import org.example.model.User;
//import org.example.service.CounterReadingService;
//import org.example.service.CounterReadingServiceImpl;
//
//import java.io.IOException;
//import java.util.List;
//
//
///**
// * Сервлет просмотра последних внесенных данных аутентифицированного пользователя
// */
//@WebServlet(name = "LatestData", value = "/latest-data-for-current-user")
//public class GetLatestData extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        User currentUser;
//
//        try {
//            currentUser = (User) req.getSession(true).getAttribute("user");
//        } catch (Exception e) {
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            resp.getWriter().write("User not authenticated");
//            return;
//        }
//
//        if (currentUser != null) {
//            CounterReadingService counterReadingService = new CounterReadingServiceImpl();
//            List<CounterReadingDTO> counterReadingDTO = counterReadingService.getLastUserInfo(currentUser);
//            if (!counterReadingDTO.isEmpty()) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setContentType("application/json");
//                ObjectMapper objectMapper = new ObjectMapper();
//                objectMapper.registerModule(new JavaTimeModule());
//                resp.getWriter().write(objectMapper.writeValueAsString(counterReadingDTO));
//            } else {
//                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                resp.getWriter().write("Data not found");
//            }
//
//        } else {
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            resp.getWriter().write("User not authenticated");
//        }
//    }
//}
//
