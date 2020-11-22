package bg.sofia.uni.fmi.mjt.revolut;

import bg.sofia.uni.fmi.mjt.revolut.account.Account;
import bg.sofia.uni.fmi.mjt.revolut.card.AbstractCard;
import bg.sofia.uni.fmi.mjt.revolut.card.Card;
import bg.sofia.uni.fmi.mjt.revolut.card.PhysicalCard;

import java.time.LocalDate;
import java.util.Arrays;

public class Revolut implements  RevolutAPI{


    private Account[] accounts;
    private Card[] cards;


    public Revolut(Account[] accounts, Card[] cards)
    {
        this.accounts = accounts;
        this.cards = cards;
    }

    public boolean accountExists(Account account)
    {
        return Arrays.asList(accounts).contains(account);
    }

    public boolean cardExists(Card card)
    {
        return Arrays.asList(cards).contains(card);
    }


    /**
     * Executes a card payment using a POS terminal
     *
     * @param card     the card used for the payment. Only physical cards are accepted
     * @param pin      4-digit PIN
     * @param amount   the amount paid
     * @param currency the currency of the payment ("BGN" or "EUR")
     * @return true, if the operation is successful and false otherwise.
     * Payment is successful, if the card is available in Revolut, valid, unblocked,
     * the specified PIN is correct and an account with sufficient amount in the specified currency exists.
     * In case of three consecutive incorrect PIN payment attempts, the card should be blocked.
     **/
    @Override
    public boolean pay(Card card, int pin, double amount, String currency )
    {
        if(cardExists(card) && card.getType().equals("PHYSICAL")&&!card.isBlocked() && card.checkPin(pin) )
        {
            for(Account account: accounts)
            {
                if(account.getCurrency().equals(currency) && account.getAmount() >= amount)
                {
                    if(account.widthdrawAmount(amount))return true;
                }
            }
        }
        return false;
    }


    /**
     * Executes an online card payment
     *
     * @param card     the card used for the payment. Any type of card is accepted
     * @param pin      4-digit PIN
     * @param amount   the amount paid
     * @param currency the currency of the payment ("BGN" or "EUR")
     * @param shopURL  the shop's domain name. ".biz" top level domains are currently banned and payments should be rejected
     * @return true, if the operation is successful and false otherwise.
     * Payment is successful, if the card is available in Revolut, valid, unblocked,
     * the specified PIN is correct and an account with sufficient amount in the specified currency exists.
     * In case of three consecutive incorrect PIN payment attempts, the card should be blocked.
     **/
    @Override
    public boolean payOnline(Card card, int pin, double amount, String currency, String shopURL)
    {
        if(shopURL.endsWith(".biz"))return false;

        if(cardExists(card)&& !card.isBlocked() && card.checkPin(pin) )
        {
            for(Account account: accounts)
            {
                if(account.getCurrency().equals(currency) && account.getAmount() >= amount)
                {
                    if(account.widthdrawAmount(amount))return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds money to a Revolut account
     *
     * @param account the account to debit
     * @param amount  the amount to add to the account, in the @account's currency
     * @return true, if the acount exists in Revolut and false otherwise
     **/
    @Override
    public boolean addMoney(Account account, double amount)
    {
        return accountExists(account) && account.depositAmount(amount);
    }

    /**
     * Transfers money between accounts, doing currency conversion, if needed.
     * The official fixed EUR to BGN exchange rate is 1.95583.
     *
     * @param from   the account to credit
     * @param to     the account to debit
     * @param amount the amount to transfer, in the @from account's currency
     * @return true if both accounts exist and are different (with different IBANs) and false otherwise
     **/
    @Override
    public boolean transferMoney(Account from, Account to, double amount)
    {
        boolean  accountsDiffer= !from.getIBAN().equals(to.getIBAN());
        return  accountExists(from) &&
                accountExists(to) &&
                accountsDiffer &&
                from.widthdrawAmount(amount) &&
                to.depositAmount(Account.convertCurrency(from.getCurrency(),to.getCurrency(),amount));

    }

    /**
     * Returns the total available amount
     *
     * @return The total available amount (the sum of amounts for all accounts), in BGN
     **/
    @Override
    public double getTotalAmount()
    {
        double totalAmount = 0;
        for(Account currentAccount : accounts)
        {
            totalAmount += Account.convertCurrency(currentAccount.getCurrency(), "BGN",currentAccount.getAmount());
        }
        return totalAmount;
    }

    public static void main(String[] args) {

    }
}

