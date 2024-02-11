package org.example.in.servlets;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(name = "putdata", value = "/put-counter-reading")
public class PutData extends HttpServlet {

}
