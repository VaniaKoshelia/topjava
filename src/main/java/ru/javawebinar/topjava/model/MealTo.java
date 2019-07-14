package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class MealTo extends Meal {

    private boolean excess;

    public MealTo() {
    }

    public MealTo(LocalDateTime dateTime, String description, int calories, boolean excess) {
        super(dateTime, description, calories);
        this.excess = excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + getDateTime() +
                ", description='" + getDescription() + '\'' +
                ", calories=" + getCalories() +
                ", excess=" + excess +
                '}';
    }

    public boolean isExcess() {
        return excess;
    }
}