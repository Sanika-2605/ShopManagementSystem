import ui.LoginFrame;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ShopManagementSystem {
	public static void main(String[] args) {
		// Install an EventQueue that catches uncaught exceptions from AWT event dispatch
		EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
		queue.push(new EventQueue() {
			@Override
			protected void dispatchEvent(AWTEvent event) {
				try {
					super.dispatchEvent(event);
				} catch (Throwable t) {
					// Print stack trace to terminal for debugging
					t.printStackTrace();
					// Show a simple modal dialog with the error message
					try {
						EventQueue.invokeLater(() -> {
							Frame f = new Frame();
							Dialog d = new Dialog(f, "Unhandled error", true);
							d.setSize(500, 300);
							d.setLayout(new BorderLayout());
							TextArea ta = new TextArea();
							StringWriter sw = new StringWriter();
							PrintWriter pw = new PrintWriter(sw);
							t.printStackTrace(pw);
							ta.setText(sw.toString());
							d.add(ta, BorderLayout.CENTER);
							Button ok = new Button("OK");
							ok.addActionListener(e -> d.dispose());
							d.add(ok, BorderLayout.SOUTH);
							d.setVisible(true);
						});
					} catch (Throwable ignored) {
						// ignore any exceptions while showing the dialog
					}
				}
			}
		});

		// Start login UI (will open MainFrameWithAuth after successful login)
		new LoginFrame();
	}
}