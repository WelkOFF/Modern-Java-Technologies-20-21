package bg.sofia.uni.fmi.mjt.revolut.account;

public abstract class Account {

    private double amount;
    private String IBAN;
    private static double exchangeRate = 1.95583;

    public static double convertCurrency( String from, String to, double amount )
    {
        if(from.equals(to))
        {
            return amount;
        }
        else if (from.equals("BGN"))
        {
            return amount/exchangeRate;
        }
        else
        {
            return amount*exchangeRate;
        }
    }


    public Account(String IBAN) {
        this(IBAN, 0);
    }

    public Account(String IBAN, double amount) {
        this.IBAN = IBAN;
        this.amount = amount;
    }

    public abstract String getCurrency();

    public double getAmount() {
        return amount;
    }

    public String getIBAN()
    {
        return IBAN;
    }

    public boolean depositAmount(double amount)
    {
        this.amount += amount;
        return true;
    }

    public boolean widthdrawAmount(double amount)
    {
        if(getAmount()>=amount)
        {
            this.amount -= amount;
            return true;
        }
        else
        {
            return false;
        }

    }


    // complete the implementation

}