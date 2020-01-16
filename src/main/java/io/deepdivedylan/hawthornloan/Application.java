package io.deepdivedylan.hawthornloan;

import javax.swing.SwingUtilities;

public class Application {
    public static void main(String[] argv) {
        SwingUtilities.invokeLater(ApplicationInterface::new);
    }
}
