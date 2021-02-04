package bg.sofia.uni.fmi.mjt.wish.list;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.Random;
import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.nio.channels.SocketChannel;


class WishListDatabase {
    private Map<String, Set<String>> studentWishList;
    private Map<String, String> registeredUserAccounts;
    private Map<SocketChannel, String> loggedInUsers;


    public WishListDatabase() {

        studentWishList = new HashMap<>();
        registeredUserAccounts = new HashMap<>();
        loggedInUsers = new HashMap<>();
    }

    public String processRequest(String request, SocketChannel channel) {

        List<String> tokens = tokenizeCommand(request);

        String response;

        if (tokens.size() > 0) {
            String commandType = tokens.get(0).toLowerCase();
            response = switch (commandType) {
                case "register" -> getRegisterResponse(tokens, channel);
                case "login" -> getLoginResponse(tokens, channel);
                case "post-wish" -> getPostWishResponse(tokens, channel);
                case "get-wish" -> getWishResponse(tokens, channel);
                case "logout" -> getLogoutResponse(tokens, channel);
                case "disconnect" -> getDisconnectResponse(tokens, channel);
                default -> "Unknown command";
            };
        } else {
            response = "Unknown command";
        }
        return "[ " + response + " ]\n";
    }

    private List<String> tokenizeCommand(String command) {
        String formattedCommand = command.trim().replaceAll(" +", " ");
        return Arrays.asList(formattedCommand.split(" ", 3));
    }

    private String getPostWishResponse(List<String> tokens, SocketChannel channel) {
        if (tokens.size() != 3) {
            return "Unknown command";
        }

        String response;
        String name = tokens.get(1);
        String gift = tokens.get(2);

        if (!loggedInUsers.containsKey(channel)) {
            response = "You are not logged in";
        } else if (!registeredUserAccounts.containsKey(name)) {
            response = "Student with username " + name + " is not registered";
        } else {
            if (!studentWishList.containsKey(name)) {
                studentWishList.put(name, new HashSet<>());
            }

            if (studentWishList.get(name).contains(gift)) {
                response = "The same gift for student " + name + " was already submitted";
            } else {
                response = "Gift " + gift + " for student " + name + " submitted successfully";
                studentWishList.get(name).add(gift);
            }
        }
        return response;
    }

    private String getRegisterResponse(List<String> tokens, SocketChannel channel) {
        if (tokens.size() != 3) {
            return "Unknown command";
        }

        String response;
        String name = tokens.get(1);
        String password = tokens.get(2);

        if (!isValidUsername(name)) {
            response = "Username " + name + " is invalid, select a valid one";
        } else if (registeredUserAccounts.containsKey(name)) {
            response = "Username " + name + " is already taken, select another one";
        } else {
            response = "Username " + name + " successfully registered";
            registeredUserAccounts.put(name, password);
            //studentWishList.put(name, new HashSet<String>());
        }
        return response;
    }

    private String getLoginResponse(List<String> tokens, SocketChannel channel) {
        if (tokens.size() != 3) {
            return "Unknown command";
        }

        String response;
        String name = tokens.get(1);
        String password = tokens.get(2);

        if (registeredUserAccounts.containsKey(name)
                && registeredUserAccounts.get(name).equals(password)) {
            response = "User " + name + " successfully logged in";
            loggedInUsers.put(channel, name);
        } else {
            response = "Invalid username/password combination";
        }
        return response;
    }

    private String getWishResponse(List<String> tokens, SocketChannel channel) {
        if (tokens.size() != 1) {
            return "Unknown command";
        }

        if (!loggedInUsers.containsKey(channel)) {
            return "You are not logged in";
        }

        String response;
        String username = loggedInUsers.get(channel);

        if (studentWishList.size() == 0 ||
                (studentWishList.size() == 1 && studentWishList.containsKey(username))) {
            response = "There are no students present in the wish list";
        } else {

            Random generator = new Random();
            int randomIndex = generator.nextInt(studentWishList.size());
            List<String> keys = new ArrayList<>(studentWishList.keySet());
            String randomKey = keys.get(randomIndex);
            if (randomKey.equals(username)) {
                randomIndex = (randomIndex + 1) % studentWishList.size();
            }
            randomKey = keys.get(randomIndex);
            Collection<String> wishList = studentWishList.get(randomKey);
            response = randomKey + ": " + wishList.toString();
            studentWishList.remove(randomKey);
        }
        return response;
    }

    private String getLogoutResponse(List<String> tokens, SocketChannel channel) {
        if (tokens.size() != 1) {
            return "Unknown command";
        }

        String response;
        if (!loggedInUsers.containsKey(channel)) {
            response = "You are not logged in";
        } else {
            loggedInUsers.remove(channel);
            response = "Successfully logged out";
        }
        return response;
    }

    private String getDisconnectResponse(List<String> tokens, SocketChannel channel) {
        if (tokens.size() != 1) {
            return "Unknown command";
        }
        getLogoutResponse(tokens, channel);
        return "Disconnected from server";
    }

    private static Boolean isValidUsername(String username) {
        return username.matches("^[-.A-Za-z0-9_]*$");
    }
}
