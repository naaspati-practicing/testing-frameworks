package com.tad.arqdevguide.chp3;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="HelloServlet", urlPatterns="/hello")
public class HelloServlet extends HttpServlet{
	private static final long serialVersionUID = -8544866718590202141L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try(PrintWriter p = resp.getWriter()){
			p.println("Hello World!");
		}
	}

}
