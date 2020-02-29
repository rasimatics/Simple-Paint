package paint_project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Stack;


public class PaintApp extends JFrame implements ActionListener {
    static Stack<Figure> figures = new Stack<>(); //Main stack for collecting elements
    static Stack<Figure> removedFigures = new Stack<>(); //Stack for handling removed elements
    public static final Color BACKGROUND_COLOR = Color.WHITE;
    static int choice = -1; //Initial choice -1 means not chosen
    public static Color color;
    static FigurePanel drawing;

    public static void main(String[] args) {

        PaintApp app = new PaintApp();

        //Creating JFrame
        JFrame f = new JFrame("Draw Box Mouse 2");
        f.setBackground(BACKGROUND_COLOR);

        //Inside JFrame there mainpanel: northpanel and drawing(Panel)
        JPanel mainPanel = new JPanel(new BorderLayout());

        //Panel for buttons
        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.setBackground(Color.BLUE);
        northPanel.setBorder(new EmptyBorder(10, 10, 10, 10));


        //Creating all buttons and add them functionality
        JButton color = new JButton("Color");
        color.addActionListener(app);
        color.setBackground(Color.GREEN);

        JButton rectangle = new JButton("Rectangle");
        rectangle.addActionListener(app);
        rectangle.setBackground(Color.YELLOW);

        JButton circle = new JButton("Circle");
        circle.addActionListener(app);
        circle.setBackground(Color.YELLOW);

        JButton line = new JButton("Line");
        line.addActionListener(app);
        line.setBackground(Color.YELLOW);

        JButton clear = new JButton("Clear");
        clear.addActionListener(app);
        clear.setBackground(Color.RED);

        JButton undo = new JButton("Undo");
        undo.addActionListener(app);
        undo.setBackground(Color.magenta);

        JButton forward = new JButton("Forward");
        forward.addActionListener(app);
        forward.setBackground(Color.orange);

        JButton save = new JButton("Save");
        save.addActionListener(app);
        save.setBackground(Color.PINK);

        JButton open = new JButton("Open");
        open.addActionListener(app);

//        JButton move = new JButton("Move");
//        move.addActionListener(app);

        //northPanel.add(move);

        //Add buttons to the panel
        northPanel.add(color);
        northPanel.add(rectangle);
        northPanel.add(circle);
        northPanel.add(line);
        northPanel.add(clear);
        northPanel.add(undo);
        northPanel.add(forward);
        northPanel.add(save);
        northPanel.add(open);

        //add button panel to the mainpanel
        mainPanel.add(northPanel, BorderLayout.NORTH);

        //Creating drawing(Panel) and add it to the mainpanel
        drawing = new FigurePanel();

        mainPanel.add(drawing, BorderLayout.CENTER);
        f.add(mainPanel);

        f.setSize(960, 640);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //Add the functionalities to buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        //Buttons click and give the value for choice to handle which button is clicked
        switch (e.getActionCommand()) {
            case "Color":
                //Open JColorChooser to get needed color
                Color initialColor = Color.BLACK;
                color = JColorChooser.showDialog(new PaintApp(), "Choose Background Color", initialColor);
                FigurePanel.gc.setBackground(color);
                FigurePanel.color = color;
                choice = 0;
                break;
            case "Rectangle":
                choice = 1;
                break;
            case "Circle":
                choice = 2;
                break;
            case "Line":
                choice = 3;
                break;
            case "Undo":
                //add last figure to removed stack
                choice = 4;
                if (figures.size() > 0) {
                    removedFigures.push(figures.pop());
                    drawing.repaint();
                }
                break;
            case "Forward":
                //get last figure from removed stack
                choice = 5;
                if (removedFigures.size() > 0) {
                    figures.push(removedFigures.pop());
                    drawing.repaint();
                }
                break;
            case "Clear":
                //Empty stack then paint again
                choice = 6;
                figures.clear();
                drawing.repaint();
                break;
            case "Save":
                //Get the name for the file and serialize the stack
                choice = 7;
                String saveFilename = JOptionPane.showInputDialog("Give the name for saving file:");
                //Click on cancel or exit
                if(saveFilename == null)
                {
                    //Do nothing
                }
                else if (saveFilename.equals("")) {
                    JOptionPane.showMessageDialog(PaintApp.drawing, "Please give the name!!!");
                } else {
                    try {
                        saveToFile(saveFilename, figures);
                        JOptionPane.showMessageDialog(PaintApp.drawing, "Saved in SimpleDrawing folder(where software is situated)");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case "Open":
                //Get the name and find that file then deserialized it
                choice = 8;
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        Stack<Figure> openedFigures = (Stack<Figure>) restoreFromFile(selectedFile.getName());
                        PaintApp.figures = openedFigures;
                        drawing.repaint();
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case "Move":
                choice = 9;
        }
    }
    //Serialize the given object
    public static void saveToFile(String outputFile, Object object) throws IOException {
        FileOutputStream file = new FileOutputStream(outputFile);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(object);
        out.close();
        file.close();
    }

    //Deseriazlized the file
    public static Object restoreFromFile(String inputFile) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(inputFile);
        ObjectInputStream in = new ObjectInputStream(file);
        Stack<Figure> openFigures;
        openFigures = (Stack<Figure>) in.readObject();
        return openFigures;
    }


}



