package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {

    @Autowired
    private MealService service;

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        service.delete(id, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "", 1000));
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        model.addAttribute(service.get(id, SecurityUtil.authUserId()));
        return "mealForm";
    }

    @PostMapping
    public String save(HttpServletRequest request) {
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));
        if(request.getParameter("id").isEmpty()) {
            service.create(meal, SecurityUtil.authUserId());
        } else {
            assureIdConsistent(meal, Integer.valueOf(request.getParameter("id")));
            service.update(meal, SecurityUtil.authUserId());
        }
        return "redirect:/meals";
    }

    @PostMapping("/filter")
    public String getFiltered(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        List<Meal> mealsDateFiltered = service.getBetweenDates(startDate, endDate, SecurityUtil.authUserId());
        model.addAttribute("meals", MealsUtil.getFilteredWithExcess(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }
}
