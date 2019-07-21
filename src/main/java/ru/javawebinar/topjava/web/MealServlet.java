package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            mealRestController = appCtx.getBean(MealRestController.class);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        switch(action == null ? "meals" : action) {
            case "meals":
                String id = request.getParameter("id");

                Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                        LocalDateTime.parse(request.getParameter("dateTime")),
                        request.getParameter("description"),
                        Integer.parseInt(request.getParameter("calories")));

                log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
                if(meal.isNew()) {
                    mealRestController.create(meal, SecurityUtil.authUserId());
                } else {
                    mealRestController.update(meal, SecurityUtil.authUserId());
                }
                response.sendRedirect("meals");
                break;
            case "filter":
                String fromDateStr = request.getParameter("fromDate");
                String toDateStr = request.getParameter("toDate");
                String fromTimeStr = request.getParameter("fromTime");
                String toTimeStr = request.getParameter("toTime");
                LocalDate fromDate = !fromDateStr.isEmpty() ? LocalDate.parse(fromDateStr) : LocalDate.MIN;
                LocalDate toDate = !toDateStr.isEmpty()? LocalDate.parse(toDateStr) : LocalDate.MAX;
                LocalTime fromTime = !fromTimeStr.isEmpty() ? LocalTime.parse(fromTimeStr) : LocalTime.MIN;
                LocalTime toTime = !toTimeStr.isEmpty() ? LocalTime.parse(toTimeStr) : LocalTime.MAX;

                request.setAttribute("meals",
                        MealsUtil.getFilteredWithExcessByDateTime(mealRestController.getAll(SecurityUtil.authUserId()),
                                SecurityUtil.authUserCaloriesPerDay(),
                                fromDate, toDate,
                                fromTime, toTime));

                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id, SecurityUtil.authUserId());
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request), SecurityUtil.authUserId());
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                List<Meal> mealList = mealRestController.getAll(SecurityUtil.authUserId());
                request.setAttribute("meals",
                        MealsUtil.getWithExcess(mealList != null ? mealList : Collections.emptyList(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
