package util;


import model.User;


import java.security.MessageDigest;
import java.util.Optional;


public class AuthManager {
private JDBCDataStore jdbc;
private static AuthManager instance;


private AuthManager() throws Exception { jdbc = JDBCDataStore.getInstance(); }


public static AuthManager getInstance() throws Exception { if (instance == null) instance = new AuthManager(); return instance; }


public boolean registerUser(String userId, String username, String password, String role) throws Exception {
String hashed = hash(password);
User u = new User(userId, username, hashed, role);
jdbc.addUser(u);
return true;
}


public Optional<User> authenticate(String username, String password) throws Exception {
Optional<User> user = jdbc.findUserByUsername(username);
if (user.isEmpty()) return Optional.empty();
String hashed = hash(password);
if (user.get().getPasswordHash().equals(hashed)) return user;
return Optional.empty();
}


private String hash(String input) throws Exception {
MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] b = md.digest(input.getBytes("UTF-8"));
StringBuilder sb = new StringBuilder(); for (byte x : b) sb.append(String.format("%02x", x)); return sb.toString();
}
}