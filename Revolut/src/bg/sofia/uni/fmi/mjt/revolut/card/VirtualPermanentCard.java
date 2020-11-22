package bg.sofia.uni.fmi.mjt.revolut.card;

import java.time.LocalDate;

public class VirtualPermanentCard extends AbstractCard{
    public VirtualPermanentCard(String number, int pin, LocalDate expirationDate) {
        super(number, pin, expirationDate);
    }

    /**
     * @return the type of the card: "PHYSICAL", "VIRTUALPERMANENT" or "VIRTUALONETIME"
     **/
    public String getType()
    {
        return "VIRTUALPERMANENT";
    }
}
