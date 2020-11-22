package bg.sofia.uni.fmi.mjt.revolut.card;
import java.time.LocalDate;

public abstract class AbstractCard implements Card {
    public AbstractCard(String number, int pin, LocalDate expirationDate)
    {
        this.number = number;
        this.pin = pin;
        this.expirationDate = expirationDate;
        this.isBlocked = false;
        this.invalidAttempts = 0;
    }
    public  boolean isExpired()
    {
        return LocalDate.now().isAfter(getExpirationDate());
    }

    /**
     * @return the type of the card: "PHYSICAL", "VIRTUALPERMANENT" or "VIRTUALONETIME"
     **/
    public abstract String getType();

    /**
     * @return the card's expiration date
     **/
    public LocalDate getExpirationDate()
    {
        return expirationDate;
    }

    /**
     * @return true if the PIN is correct and false otherwise. Correct means, equal to the PIN, set in the card upon construction (i.e. passed in its constructor). You can check it for validity, e.g. that it is a 4-digit number, but you can assume the input is valid.
     **/
    public boolean checkPin(int pin)
    {
        if(!isBlocked() && !isExpired())
        {
            if(this.pin == pin)
            {
                invalidAttempts = 0;
                return true;
            }
            else
            {
                invalidAttempts++;
                if(invalidAttempts >=3)block();
                return false;

            }
        }
        else return false;

    }

    /**
     * @return true if the card is blocked and false otherwise
     **/
    public boolean isBlocked()
    {
        return isBlocked;
    }

    /**
     * Blocks the card
     **/
    public void block()
    {
        isBlocked = true;
    }

    private String number;
    private int pin;
    private LocalDate expirationDate;
    private boolean isBlocked;
    private int invalidAttempts;
}
