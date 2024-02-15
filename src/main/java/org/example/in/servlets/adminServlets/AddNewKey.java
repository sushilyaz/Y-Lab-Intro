//package org.example.in.servlets.adminServlets;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.example.dto.adminDTO.NewKeyDTO;
//import org.example.model.User;
//import org.example.service.AdminService;
//import org.example.service.AdminServiceImpl;
//
//import java.io.IOException;
//import java.util.Set;
//
///**
// * Сервлет добавления нового типа счетчика
// */
//@WebServlet(name = "AddNewKey", value = "/admin/add-new-key")
//public class AddNewKey extends HttpServlet {
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
//        if (currentUser == null) {
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            resp.getWriter().write("Admin not authenticated");
//            return;
//        }
//        if (currentUser.getRoleAsString().equals("ADMIN")) {
//            AdminService adminService = new AdminServiceImpl();
//            ObjectMapper objectMapper = new ObjectMapper();
//            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//            Validator validator = factory.getValidator();
//            NewKeyDTO data;
//            try {
//                data = objectMapper.readValue(req.getReader(), NewKeyDTO.class);
//            } catch (Exception e) {
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                resp.getWriter().write("Data no valid!");
//                return;
//            }
//            Set<ConstraintViolation<NewKeyDTO>> violations = validator.validate(data);
//            if (!violations.isEmpty()) {
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                resp.getWriter().write("Invalid data");
//                return;
//            }
//
//            if (adminService.addNewKey(data.getNewKey())) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setContentType("application/json");
//                resp.getWriter().write("Key added successfully");
//            } else {
//                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                resp.setContentType("application/json");
//                resp.getWriter().write("Key already exist");
//            }
//
//        } else {
//            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            resp.setContentType("application/json");
//            resp.getWriter().write("Access denied");
//        }
//    }
//}
