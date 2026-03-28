import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Map;

public class TagExtractorApplication extends JFrame {

    private JTextArea textArea;
    private JLabel statusLabel;

    private File textFile;
    private File stopFile;

    private TagProcessor processor;

    public TagExtractorApplication() {
        processor = new TagProcessor();

        setTitle("Tag Extractor");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();

        JButton btnText = new JButton("Choose Text File");
        JButton btnStop = new JButton("Choose Stop Words");
        JButton btnProcess = new JButton("Extract Tags");
        JButton btnSave = new JButton("Save Output");

        topPanel.add(btnText);
        topPanel.add(btnStop);
        topPanel.add(btnProcess);
        topPanel.add(btnSave);

        add(topPanel, BorderLayout.NORTH);

        statusLabel = new JLabel("No files selected");
        add(statusLabel, BorderLayout.SOUTH);

        textArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textArea);
        add(scroll, BorderLayout.CENTER);

        btnText.addActionListener(this::chooseTextFile);
        btnStop.addActionListener(this::chooseStopFile);
        btnProcess.addActionListener(this::processFiles);
        btnSave.addActionListener(this::saveFile);
    }

    private void chooseTextFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            textFile = chooser.getSelectedFile();
            updateLabel();
        }
    }

    private void chooseStopFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            stopFile = chooser.getSelectedFile();
            updateLabel();
        }
    }

    private void updateLabel() {
        String t = (textFile != null) ? textFile.getName() : "None";
        String s = (stopFile != null) ? stopFile.getName() : "None";
        statusLabel.setText("Text: " + t + " | Stop Words: " + s);
    }

    private void processFiles(ActionEvent e) {
        if (textFile == null || stopFile == null) {
            JOptionPane.showMessageDialog(this, "Pick both files first.");
            return;
        }

        try {
            processor.loadStopWords(stopFile);
            processor.processFile(textFile);

            textArea.setText("");
            for (Map.Entry<String, Integer> entry : processor.getMap().entrySet()) {
                textArea.append(entry.getKey() + " : " + entry.getValue() + "\n");
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading file.");
        }
    }

    private void saveFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File outFile = chooser.getSelectedFile();

            try {
                PrintWriter writer = new PrintWriter(outFile);
                for (Map.Entry<String, Integer> entry : processor.getMap().entrySet()) {
                    writer.println(entry.getKey() + " : " + entry.getValue());
                }
                writer.close();
                JOptionPane.showMessageDialog(this, "Saved!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TagExtractorApplication().setVisible(true));
    }
}