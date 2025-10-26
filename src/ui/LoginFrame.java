package ui;


import model.User;
import util.AuthManager;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;


public class LoginFrame extends Frame {
private TextField tfUser, tfPass;
private Label lblMsg;


public LoginFrame() {
super("Login - Shop Management");
setSize(350,200); setLayout(new GridLayout(4,2));
add(new Label("Username:")); tfUser = new TextField(); add(tfUser);
add(new Label("Password:")); tfPass = new TextField(); tfPass.setEchoChar('*'); add(tfPass);
Button btnLogin = new Button("Login"); Button btnExit = new Button("Exit");
lblMsg = new Label(""); add(btnLogin); add(btnExit); add(lblMsg);


btnLogin.addActionListener(e -> {
try {
AuthManager am = AuthManager.getInstance();
Optional<User> u = am.authenticate(tfUser.getText().trim(), tfPass.getText().trim());
if (u.isPresent()) {
lblMsg.setText("Welcome, " + u.get().getUsername() + " (" + u.get().getRole() + ")");
// open main frame with role-based access
new MainFrameWithAuth(u.get());
dispose();
} else lblMsg.setText("Invalid credentials");
} catch (Exception ex) { lblMsg.setText("Error: " + ex.getMessage()); }
});


btnExit.addActionListener(e -> { dispose(); System.exit(0); });
addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { dispose(); System.exit(0); } });
setVisible(true);
}
}