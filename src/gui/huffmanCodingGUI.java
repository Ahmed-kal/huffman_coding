package gui;

import logic.HuffmanCoding;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Set;

public class huffmanCodingGUI implements Runnable {

    private HuffmanCoding huffmanCoding;
    private JFrame frame;
    private JPanel mainPanel;
    private JComboBox charsetComboBox;
    private JRadioButton decodeRadioButton, encodeRadioButton;
    private JLabel inputLabel;
    private JButton encodeDecodeButton, directoryButton, huffmanTreeButton, sourceButton, inputButton;
    private JTextField directoryTextBox, encodeDecodeTextBox, sourceTextBox, inputTextBox;

    @Override
    public void run() {
        frame = new JFrame("Huffman Coding");
        frame.setPreferredSize(new Dimension(420, 550));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createComponents(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }

    private void createComponents(Container contentPane) {

        sourceButton.addActionListener(new SourceButtonActionListener());
        inputButton.addActionListener(new InputButtonActionListener());
        huffmanTreeButton.addActionListener(new HuffmanTreeButtonActionListener());
        directoryButton.addActionListener(new DirectoryButtonActionListener());
        encodeDecodeButton.addActionListener(new EncodeDecodeButtonActionListener());
        decodeRadioButton.addActionListener(actionEvent -> {
            encodeDecodeButton.setText("Decode");
            inputLabel.setText("File to Decode Path");
            clearSelection();
        });
        encodeRadioButton.addActionListener(actionEvent -> {
            encodeDecodeButton.setText("Encode");
            inputLabel.setText("File to Encode Path");
            clearSelection();
        });
        charsetComboBox.addActionListener(actionEvent -> {
            if(!inputTextBox.getText().equalsIgnoreCase(""))
                huffmanCoding = new HuffmanCoding(inputTextBox.getText(), charsetComboBox.getSelectedItem().toString());
        });
        contentPane.add(mainPanel);
    }

    private void clearSelection() {
        inputTextBox.setText("");
        directoryTextBox.setText("");
        encodeDecodeTextBox.setText("");
        encodeDecodeButton.setEnabled(false);
        directoryButton.setEnabled(false);
    }

    private class SourceButtonActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text File", "txt"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                sourceTextBox.setText(file.getPath());
                huffmanCoding = new HuffmanCoding(file.getPath());
                huffmanTreeButton.setEnabled(true);
                Set<String> charsets = huffmanCoding.getAvailableCharsets();
                for (String charset : charsets)
                    charsetComboBox.addItem(charset);
                charsetComboBox.setSelectedItem(Charset.defaultCharset().toString());
                charsetComboBox.setEnabled(true);
                encodeRadioButton.setEnabled(true);
                decodeRadioButton.setEnabled(true);
                inputButton.setEnabled(true);
            }
        }
    }

    private class HuffmanTreeButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String fileName = sourceTextBox.getText();
            File graph = new File(fileName.substring(0, fileName.length()-4) + "_HuffmanTreeGraph.png");
            if(!Files.exists(graph.toPath())) {
                graph = huffmanCoding.createTreeGraph(sourceTextBox.getText());
            }
            EventQueue.invokeLater(new ImageFrame(new ImageIcon(graph.getPath())));
        }
    }


    private class InputButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileChooser = new JFileChooser(".");
            if(encodeRadioButton.isSelected()) {
                fileChooser.setFileFilter(new FileNameExtensionFilter("Text File", "txt"));
            } else {
                fileChooser.setFileFilter(new FileNameExtensionFilter("Huffman Encoded Text File", "hetf"));
            }
            fileChooser.setAcceptAllFileFilterUsed(false);
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String fileName = file.getName().substring(0, file.getName().lastIndexOf(".") + 1);
                String path = file.getPath();
                inputTextBox.setText(path);
                StringBuilder sb = new StringBuilder(path.substring(0, path.length() - (fileName.length() + 3)));
                if(encodeRadioButton.isSelected()) {
                    sb.append("ENCODED_" + fileName);
                    sb.append("hetf");
                } else {
                    sb.append("DECODED_" + fileName);
                    sb.append("txt");
                }
                directoryTextBox.setText(sb.toString());
                directoryButton.setEnabled(true);
                encodeDecodeButton.setEnabled(true);
            }
        }
    }

    private class DirectoryButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String fileName = inputTextBox.getText().substring(inputTextBox.getText().lastIndexOf("/") + 1);
                if(encodeRadioButton.isSelected()) {
                    directoryTextBox.setText(file.getPath() + "/ENCODED_" + fileName.substring(0, fileName.length() - 3) + "hetf");
                } else {
                    directoryTextBox.setText(file.getPath() + "/DECODED_" + fileName.substring(0, fileName.length() - 4) + "txt");
                }
            }
        }
    }

    private class EncodeDecodeButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(encodeRadioButton.isSelected()) {
                int result = huffmanCoding.encodeFile(inputTextBox.getText(), directoryTextBox.getText());
                if(result == -1) {
                    encodeDecodeTextBox.setText("Operation unsuccessful: unable to write to file.");
                } else {
                    encodeDecodeTextBox.setText("Encoded file successfully: file size " + result + " bytes");
                }
            } else {
                double result = huffmanCoding.decodeFile(inputTextBox.getText(), directoryTextBox.getText());
                String strResult = String.format("%.3f", result);
                if(result == -1) {
                    encodeDecodeTextBox.setText("Operation unsuccessful: unable to write to file.");
                } else {
                    encodeDecodeTextBox.setText("Decoded file successfully: compression rate " + strResult);
                }
            }
        }
    }
}

