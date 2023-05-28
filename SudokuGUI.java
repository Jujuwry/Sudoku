package GUI;

import javax.swing.*;

import java.awt.*;


import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SudokuGUI extends JFrame {
    private SudokuGrid sudokuGrid;
    private JTextField[][] textFields;

    public SudokuGUI() {
        sudokuGrid = new SudokuGrid();
        textFields = new JTextField[9][9];

        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 9));

        initializeTextFields();
        addTextFieldsToGrid();
        addBordersToGrid();

        generateRandomNumbers();
        updateTextFields();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Initializes the text fields for each cell in the Sudoku grid
    private void initializeTextFields() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField textField = new JTextField();
                textField.setHorizontalAlignment(JTextField.CENTER);
                textFields[i][j] = textField;

               

                // Adds a key listener to restrict input to digits only
                textField.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyTyped(java.awt.event.KeyEvent evt) {
                        char c = evt.getKeyChar();
                        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                            evt.consume();
                        }
                    }
                });

                // Adds a focus listener to check if the value entered is inside the region
                textField.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        String input = textField.getText();
                        if (!input.isEmpty()) {
                         
                                int value = Integer.parseInt(input);
                                if (value > 9 || value < 1) {
                                    textField.setText("");
                                    JOptionPane.showMessageDialog(SudokuGUI.this, "Enter a value between 1-9");
                                }
                        
                        }
                    }
                });
            }
        }
    }

    // Adds the text fields to the grid layout
    private void addTextFieldsToGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                add(textFields[i][j]);
            }
        }
    }

    // Adds borders and styling to the grid cells
    private void addBordersToGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField textField = textFields[i][j];
                int subgridRow = i / 3;
                int subgridCol = j / 3;
    
                // Sets background color for subgrid cells
                if (subgridRow % 2 == 0 && subgridCol % 2 == 0) {
                    textField.setBackground(new Color(220, 220, 220));
                } else if (subgridRow % 2 == 1 && subgridCol % 2 == 1) {
                    textField.setBackground(new Color(220, 220, 220));
                }
    
                // Calculate border thickness for individual cells
                int top = 1;
                int left = 1;
                int bottom = 1;
                int right = 1;
    
                // Check if the current cell is on the border of a subgrid
                if (i % 3 == 0) top = 2;
                if (j % 3 == 0) left = 2;
                if (i % 3 == 2 || i == 8) bottom = 2;
                if (j % 3 == 2 || j == 8) right = 2;
    
                // Set border color and thickness
                textField.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
    
                textField.setFont(new Font("Arial", Font.BOLD, 20));
                textField.setPreferredSize(new Dimension(40, 40));
                textField.setHorizontalAlignment(JTextField.CENTER);

         
            }
        }
    }
    
    

    
    
    
    
    
    
    


    // Generates random numbers for initial Sudoku grid values
    private void generateRandomNumbers() {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                generateRandomNumbersInSubgrid(i, j, random);
            }
        }
    }

    // Generates random numbers for a specific subgrid
    private void generateRandomNumbersInSubgrid(int subgridRow, int subgridCol, Random random) {
        LinkedList numbers = new LinkedList();
        for (int i = 1; i <= 9; i++) {
            numbers.append(i);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int row = (subgridRow * 3) + i;
                int col = (subgridCol * 3) + j;

                // Randomly selects values from the linked list and sets them in the Sudoku grid
                if (random.nextDouble() < 0.5) {
                    int value = numbers.removeRandomValue(random);
                    if (isValidValue(row, col, value)) {
                        sudokuGrid.setValue(row, col, value);
                    }
                }
            }
        }
    }

    // Checks if a value is valid for a given row, column, and subgrid
    private boolean isValidValue(int row, int col, int value) {
        return !sudokuGrid.isValueInRow(row, value)
                && !sudokuGrid.isValueInColumn(col, value)
                && !sudokuGrid.isValueInSubgrid(row, col, value);
    }

    // Updates the text fields with the values from the SudokuGrid
    private void updateTextFields() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int value = sudokuGrid.getValue(i, j);
                textFields[i][j].setText(value == 0 ? "" : Integer.toString(value));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SudokuGUI();
            }
        });
    }
}

class Node {
    private int value;
    private Node next;

    public Node(int value) {
        this.setValue(value);
        this.setNext(null);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}

class LinkedList {
    private Node head;

    public LinkedList() {
        this.head = null;
    }

    // Appends a new node with the given value to the linked list
    public void append(int value) {
        Node newNode = new Node(value);
        if (head == null) {
            head = newNode;
        } else {
            Node currentNode = head;
            while (currentNode.getNext() != null) {
                currentNode = currentNode.getNext();
            }
            currentNode.setNext(newNode);
        }
    }

    // Checks if the linked list contains a node with the given value
    public boolean contains(int value) {
        Node currentNode = head;
        while (currentNode != null) {
            if (currentNode.getValue() == value) {
                return true;
            }
            currentNode = currentNode.getNext();
        }
        return false;
    }

    // Removes a randomly selected value from the linked list
    public int removeRandomValue(Random random) {
        int index = random.nextInt(size());
        Node currentNode = head;
        Node prevNode = null;
        for (int i = 0; i < index; i++) {
            prevNode = currentNode;
            currentNode = currentNode.getNext();
        }

        if (prevNode == null) {
            head = currentNode.getNext();
        } else {
            prevNode.setNext(currentNode.getNext());
        }

        return currentNode.getValue();
    }

    // Returns the size of the linked list
    private int size() {
        int count = 0;
        Node currentNode = head;
        while (currentNode != null) {
            count++;
            currentNode = currentNode.getNext();
        }
        return count;
    }
}

class TreeNode {
    private int value;
    private TreeNode left;
    private TreeNode right;

    public TreeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}

class SudokuGrid {
    private TreeNode[][] grid;

    public SudokuGrid() {
        grid = new TreeNode[9][9];
        initializeGrid();
    }

    // Initializes the Sudoku grid with empty nodes
    private void initializeGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = new TreeNode(0);
            }
        }
    }

    // Gets the value at the given row and column in the Sudoku grid
    public int getValue(int row, int col) {
        return grid[row][col].getValue();
    }

    // Sets the value at the given row and column in the Sudoku grid
    public void setValue(int row, int col, int value) {
        grid[row][col].setValue(value);
    }

    // Checks if a value is already present in the given row
    public boolean isValueInRow(int row, int value) {
        for (int col = 0; col < 9; col++) {
            if (grid[row][col].getValue() == value) {
                return true;
            }
        }
        return false;
    }

    // Checks if a value is already present in the given column
    public boolean isValueInColumn(int col, int value) {
        for (int row = 0; row < 9; row++) {
            if (grid[row][col].getValue() == value) {
                return true;
            }
        }
        return false;
    }

    // Checks if a value is already present in the subgrid containing the given row and column
    public boolean isValueInSubgrid(int row, int col, int value) {
        int subgridStartRow = (row / 3) * 3;
        int subgridStartCol = (col / 3) * 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[subgridStartRow + i][subgridStartCol + j].getValue() == value) {
                    return true;
                }
            }
        }
        return false;
    }
}
