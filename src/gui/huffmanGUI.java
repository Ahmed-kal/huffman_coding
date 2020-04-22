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

public class huffmanGUI implements Runnable {

    private HuffmanCoding huffmanCoding;
    private JFrame frame;
    private JPanel mainPanel;
    private JButton inputButton;
    private JTextField inputTextBox;
    private JComboBox charsetComboBox;
    private JRadioButton decodeRadioButton;
    private JRadioButton encodeRadioButton;
    private JButton sourceButton;
    private JTextField sourceTextBox;
    private JButton huffmanTreeButton;
    private JLabel inputLabel;
    private JButton encodeDecodeButton;
    private JButton directoryButton;
    private JTextField directoryTextBox;
    private JTextField encodeDecodeTextBox;

    @Override
    public void run() {
        frame = new JFrame("Huffman Coding");
        frame.setPreferredSize(new Dimension(420, 450));
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
        decodeRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                encodeDecodeButton.setText("Decode");
                inputLabel.setText("File to Decode Path");
                inputTextBox.setText("");
                directoryTextBox.setText("");
                encodeDecodeTextBox.setText("");
                encodeDecodeButton.setEnabled(false);
                directoryButton.setEnabled(false);

            }
        });
        encodeRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                encodeDecodeButton.setText("Encode");
                inputLabel.setText("File to Encode Path");
                inputTextBox.setText("");
                directoryTextBox.setText("");
                encodeDecodeTextBox.setText("");
                encodeDecodeButton.setEnabled(false);
                directoryButton.setEnabled(false);
            }
        });
        charsetComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                huffmanCoding.setCharset(charsetComboBox.getSelectedItem().toString());
            }
        });
        contentPane.add(mainPanel);
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
                Set<String> charsets = huffmanCoding.getAvailableCharset();
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
            File graph = new File("output/" + sourceTextBox.getText() + "_HuffmanTreeGraph.png");
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
                String path = fileChooser.getSelectedFile().getPath();
                inputTextBox.setText(path);
                StringBuilder sb = new StringBuilder();
                if(encodeRadioButton.isSelected()) {
                    sb.append(path, 0, path.length()-3);
                    sb.insert(sb.lastIndexOf("/") + 1, "ENCODED_");
                    sb.append("hetf");
                } else {
                    sb.append(path, 0, path.length()-4);
                    sb.insert(sb.lastIndexOf("/") + 1, "DECODED_");
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

