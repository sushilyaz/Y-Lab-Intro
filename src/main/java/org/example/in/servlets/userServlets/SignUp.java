//package org.example.in.servlets.userServlets;
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
//import org.example.dto.UserCreateDTO;
//import org.example.dto.UserDTO;
//import org.example.service.UserService;
//import org.example.service.UserServiceImpl;
//
//import java.io.IOException;
//import java.util.Set;
///**
// * Сервлет регистрации пользователя
// */
//@WebServlet(name = "signup", value = "/signup")
//public class SignUp extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        UserService userService = new UserServiceImpl();
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.findAndRegisterModules();
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        Validator validator = factory.getValidator();
//        try {
//            UserCreateDTO userCreateDTO = objectMapper.readValue(req.getReader(), UserCreateDTO.class);
//            Set<ConstraintViolation<UserCreateDTO>> violations = validator.validate(userCreateDTO);
//
//            if (!violations.isEmpty()) {
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                resp.getWriter().write("Invalid data");
//                return;
//            }
//
//            UserDTO dto = userService.registerUser(userCreateDTO);
//            if (dto == null) {
//                resp.setStatus(HttpServletResponse.SC_CONFLICT);
//                resp.getWriter().write("Username already exists");
//            } else {
//                resp.setStatus(HttpServletResponse.SC_CREATED);
//                resp.getWriter().write("User register success! ");
//            }
//        } catch (Exception e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.getWriter().write("Data no valid");
//        }
//    }
//}
