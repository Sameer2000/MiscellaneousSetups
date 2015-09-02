package com.app.src;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.app.src.model.Person;
import com.app.src.service.CrudService;
import com.app.src.service.CrudServiceImpl;

/**
 * Servlet implementation class SavePerson
 */
@WebServlet("/saveperson")
public class SavePerson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private CrudService<Long> crudService = null;
	
	public SavePerson() {
		crudService = new CrudServiceImpl();
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Person person = new Person();
		try {
			BeanUtils.populate(person, request.getParameterMap());
			System.out.println(person.toString());
			if(person.getAmount()!= null && person.getName() != null){
				crudService.save(Integer.parseInt(person.getAmount()), person.getName());
				request.setAttribute("response", "Success");
			}
			response.sendRedirect("/TestPerson/");
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			request.setAttribute("response", "Set all properties");
		}
	}

}
