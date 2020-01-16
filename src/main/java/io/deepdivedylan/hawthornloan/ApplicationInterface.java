package io.deepdivedylan.hawthornloan;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ApplicationInterface extends JFrame {
    String csvFilename;

    public ApplicationInterface() {
        JButton button = new JButton("press the kitty nose");
        JLabel label = new JLabel("don't feed him");
        button.addActionListener(new PressTheKittyNoseActionListener(this));

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addComponent(label)
                .addGap(16, 32, 32)
                .addComponent(button)
        );
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(label)
                .addComponent(button)
        );

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(groupLayout);
        setVisible(true);
        pack();
    }

    public void setCsvFilename(String newCsvFilename) {
        csvFilename = newCsvFilename;
        System.out.println("file: " + csvFilename);
    }

    private static class PressTheKittyNoseActionListener implements ActionListener {
        private ApplicationInterface parent;

        public PressTheKittyNoseActionListener(ApplicationInterface applicationInterface) {
            parent = applicationInterface;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("press the kitty nose");
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("CSV Files", "csv");
            fileChooser.setFileFilter(fileNameExtensionFilter);
            int chooserResult = fileChooser.showOpenDialog(parent);
            if (chooserResult == JFileChooser.APPROVE_OPTION) {
                parent.setCsvFilename(fileChooser.getSelectedFile().getPath());
            }
        }
    }
}
