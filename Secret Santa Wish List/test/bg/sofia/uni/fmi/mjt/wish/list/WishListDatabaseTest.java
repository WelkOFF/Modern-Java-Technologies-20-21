package bg.sofia.uni.fmi.mjt.wish.list;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import static org.junit.Assert.assertEquals;

public class WishListDatabaseTest {

    private static WishListDatabase database;

    @Before
    public void initialize() {
        database = new WishListDatabase();
    }

    @Test
    public void testProcessRequestRegisterValidName() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            String request = "register Zdravko Abcd1234";
            String expected = "[ Username Zdravko successfully registered ]\n";
            String actual = database.processRequest(request, socketChannel);
            assertEquals(expected, actual);
        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessRequestRegisterDuplicateName() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            String request = "register Zdravko Abcd1234";
            String expected = "[ Username Zdravko is already taken, select another one ]\n";
            database.processRequest(request, socketChannel);
            String actual = database.processRequest(request, socketChannel);
            assertEquals(expected, actual);
        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessRequestRegisterInvalidName() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            String request = "register Ko$io Abcd1234";
            String expected = "[ Username Ko$io is invalid, select a valid one ]\n";
            String actual = database.processRequest(request, socketChannel);
            assertEquals(expected, actual);
        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessRequestLoginValid() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            String registration = "register Zdravko Abcd1234";
            database.processRequest(registration, socketChannel);

            String request = "login Zdravko Abcd1234";
            String expected = "[ User Zdravko successfully logged in ]\n";
            String actual = database.processRequest(request, socketChannel);
            assertEquals(expected, actual);
        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessRequestLoginNotRegistered() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            String request = "login Zdravko Abcd1234";
            String expected = "[ Invalid username/password combination ]\n";
            String actual = database.processRequest(request, socketChannel);
            assertEquals(expected, actual);
        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessRequestLoginInvalidPassword() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            String registration = "register Zdravko Abcd1234";
            database.processRequest(registration, socketChannel);

            String request = "login Zdravko Abcd123456";
            String expected = "[ Invalid username/password combination ]\n";
            String actual = database.processRequest(request, socketChannel);
            assertEquals(expected, actual);
        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessRequestGetWishNoStudents() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            String registration = "register Zdravko Abcd1234";
            database.processRequest(registration, socketChannel);
            String login = "login Zdravko Abcd1234";
            database.processRequest(login, socketChannel);

            String request = "get-wish";
            String expected = "[ There are no students present in the wish list ]\n";
            String actual = database.processRequest(request, socketChannel);
            assertEquals(expected, actual);
        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessRequestPostWishNoStudents() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            String registration = "register Zdravko Abcd1234";
            database.processRequest(registration, socketChannel);
            String registration2 = "register Vanko Abcd1234";
            database.processRequest(registration2, socketChannel);
            String login = "login Zdravko Abcd1234";
            database.processRequest(login, socketChannel);

            String request = "post-wish Vanko kolelo";
            String expected = "[ Gift kolelo for student Vanko submitted successfully ]\n";
            String actual = database.processRequest(request, socketChannel);
            assertEquals(expected, actual);
        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessRequestLogoutLoggedIn() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            String registration = "register Zdravko Abcd1234";
            database.processRequest(registration, socketChannel);
            String login = "login Zdravko Abcd1234";
            database.processRequest(login, socketChannel);

            String request = "logout";
            String expected = "[ Successfully logged out ]\n";
            String actual = database.processRequest(request, socketChannel);
            assertEquals(expected, actual);
        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessRequestLogoutNotLoggedIn() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            String request = "logout";
            String expected = "[ You are not logged in ]\n";
            String actual = database.processRequest(request, socketChannel);
            assertEquals(expected, actual);
        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessRequestDisconnect() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            String request = "disconnect";
            String expected = "[ Disconnected from server ]\n";
            String actual = database.processRequest(request, socketChannel);
            assertEquals(expected, actual);
        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }


}
