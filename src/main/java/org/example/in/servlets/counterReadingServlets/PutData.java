//package org.example.in.servlets.counterReadingServlets;
//
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.example.dto.CounterReadingCreateDTO;
//import org.example.dto.CounterReadingDTO;
//import org.example.model.User;
//import org.example.service.CounterReadingService;
//import org.example.service.CounterReadingServiceImpl;
//
//import java.io.IOException;
//import java.util.List;
//
///**
// * Сервлет отправки показателей счетчика аутентифицированного пользователя
// */
//@WebServlet(name = "putdata", value = "/put-counter-reading")
//public class PutData extends HttpServlet {
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        User currentUser;
//
//        try {
//            currentUser = (User) req.getSession(true).getAttribute("user");
//        } catch (Exception e) {
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            resp.getWriter().write("User not authenticated");
//            return;
//        }
//        if (currentUser != null) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            CounterReadingService counterReadingService = new CounterReadingServiceImpl();
//            List<CounterReadingCreateDTO> dtoCreate;
//            objectMapper.registerModule(new JavaTimeModule());
//            try {
//                dtoCreate = objectMapper.readValue(req.getReader(), new TypeReference<List<CounterReadingCreateDTO>>() {});
//            } catch (Exception e) {
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                resp.getWriter().write("Data no valid!");
//                return;
//            }
//
////            List<CounterReadingDTO> validDto = CounterReadingMapper.INSTANCE.mapCreateToDTO(dtoCreate);
//
//            List<CounterReadingCreateDTO> res = counterReadingService.validationCounter(currentUser, dtoCreate);
//
//            if (!res.isEmpty()) {
//                List<CounterReadingDTO> dto = counterReadingService.submitCounterReading(currentUser, dtoCreate);
//                if (!dto.isEmpty()) {
//                    resp.setStatus(HttpServletResponse.SC_CREATED);
//                    resp.getWriter().write("Data submit success!");
//                } else {
//                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
//                    resp.getWriter().write("Data already exist!");
//                }
//            } else {
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                resp.getWriter().write("Data no valid!");
//            }
//
//        } else {
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            resp.getWriter().write("User not authenticated");
//        }
//    }
//}
