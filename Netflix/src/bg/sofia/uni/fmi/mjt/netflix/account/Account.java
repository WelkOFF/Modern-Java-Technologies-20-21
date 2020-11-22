package bg.sofia.uni.fmi.mjt.netflix.account;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;

public class Account {

    public Account(String username, LocalDateTime birthdayDate) {
        this.username = username;
        this.birthdayDate = birthdayDate;
    }

    public int getAge()
    {
        if (birthdayDate != null) {
            Period period = Period.between(birthdayDate.toLocalDate(),LocalDateTime.now().toLocalDate());
            return period.getYears();
        } else {
            return 0;
        }
    }


    private String username;
    private LocalDateTime birthdayDate;

}
