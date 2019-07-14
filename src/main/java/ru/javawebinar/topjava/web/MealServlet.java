package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = getLogger(MealServlet.class);
    private AtomicInteger atomicId = new AtomicInteger(0);

    private static String INSERT_OR_EDIT = "/meal.jsp";
    private static String LIST_USER = "/meals.jsp";
    private MealDao dao;

    public MealServlet() {
        super();
        dao = new MealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward="";
        String action = request.getParameter("action");

        if(action == null) {
            forward = LIST_USER;
            request.setAttribute("meals", MealsUtil.getAllWithExcess(dao.getAll(), 2000));
        } else if (action.equalsIgnoreCase("delete")){
            int id = Integer.parseInt(request.getParameter("id"));
            dao.delete(id);
            forward = LIST_USER;
            request.setAttribute("meals", MealsUtil.getAllWithExcess(dao.getAll(), 2000));
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int id = Integer.parseInt(request.getParameter("id"));
            Meal meal = dao.get(id);
            request.setAttribute("meal", meal);
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), formatter);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(dateTime, description, calories);
        String id = request.getParameter("id");
        if(id == null || id.isEmpty()) {
            meal.setId(atomicId.getAndIncrement());
            dao.save(meal);
        }
        else {
            meal.setId(Integer.parseInt(id));
            dao.update(meal);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
        request.setAttribute("meals", MealsUtil.getAllWithExcess(dao.getAll(), 2000));
        view.forward(request, response);
    }
}
