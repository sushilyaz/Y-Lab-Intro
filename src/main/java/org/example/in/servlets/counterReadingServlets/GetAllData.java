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
///**
// * Сервлет просмотра всех данных аутентифицированного пользователя
// */
//@WebServlet(name = "getAllData", value = "/get-all-data")
//public class GetAllData extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        User currentUser;
//        try {
//            currentUser = (User) req.getSession(true).getAttribute("user");
//        } catch (Exception e) {
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            resp.getWriter().write("User not authenticated");
//            return;
//        }
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        if (currentUser != null) {
//            CounterReadingService counterReadingService = new CounterReadingServiceImpl();
//            List<CounterReadingDTO> res = counterReadingService.getCRByUser(currentUser);
//            if (!res.isEmpty()) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setContentType("application/json");
//                objectMapper.registerModule(new JavaTimeModule());
//                resp.getWriter().write(objectMapper.writeValueAsString(res));
//            } else {
//                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                resp.getWriter().write("Data not found");
//            }
//        } else {
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            resp.getWriter().write("User not authenticated");
//        }
//
//    }
//}
