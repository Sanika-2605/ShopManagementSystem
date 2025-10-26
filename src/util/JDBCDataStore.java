package util;

import model.User;

import java.security.MessageDigest;
import java.util.*;

/**
 * Minimal in-memory stub implementation of a JDBCDataStore used by AuthManager.
 * Seeds a default admin user (username: admin, password: admin) so LoginFrame is usable.
 */
public class JDBCDataStore {
	private static JDBCDataStore instance;
	private final Map<String, User> usersByUsername = new HashMap<>();

	private JDBCDataStore() {
		try {
			String adminPass = "admin";
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(adminPass.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) sb.append(String.format("%02x", b));
			String hash = sb.toString();
			User admin = new User("u1", "admin", hash, "ADMIN");
			addUser(admin);
		} catch (Exception e) {
			// ignore hashing errors in stub
		}
	}

	public static synchronized JDBCDataStore getInstance() {
		if (instance == null) instance = new JDBCDataStore();
		return instance;
	}

	public void addUser(User u) {
		if (u == null) return;
		usersByUsername.put(u.getUsername(), u);
	}

	public Optional<User> findUserByUsername(String username) {
		if (username == null) return Optional.empty();
		return Optional.ofNullable(usersByUsername.get(username));
	}

    
}
