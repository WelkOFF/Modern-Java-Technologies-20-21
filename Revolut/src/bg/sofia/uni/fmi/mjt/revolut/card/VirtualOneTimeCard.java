package bg.sofia.uni.fmi.mjt.revolut.card;

import java.time.LocalDate;

public class VirtualOneTimeCard extends AbstractCard {
    public VirtualOneTimeCard(String number, int pin, LocalDate expirationDate) {
        super(number, pin, expirationDate);
    }

    /**
     * @return the type of the card: "PHYSICAL", "VIRTUALPERMANENT" or "VIRTUALONETIME"
     **/
    public String getType()
    {
        return "VIRTUALONETIME";
    }

    public boolean checkPin( int pin )
    {
        if (super.checkPin(pin))
        {
            block();
            return true;
        }
        else return false;
    }

}
