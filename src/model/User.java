package model;


import java.io.Serializable;


public class User implements Serializable {
private static final long serialVersionUID = 1L;
private String userId;
private String username;
private String passwordHash; // store hashed password
private String role; // ADMIN or CASHIER


public User(String userId, String username, String passwordHash, String role) {
this.userId = userId;
this.username = username;
this.passwordHash = passwordHash;
this.role = role;
}


public String getUserId() { return userId; }
public String getUsername() { return username; }
public String getPasswordHash() { return passwordHash; }
public String getRole() { return role; }


public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
public void setRole(String role) { this.role = role; }


@Override
public String toString() { return userId + " | " + username + " | " + role; }
}