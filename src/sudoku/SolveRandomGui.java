/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SolveRandomGui extends JFrame {
    private JTextField[][] cells;
    private JButton solveButton;
    private JButton checkButton;
private JButton helpButton;
private JButton newGenerateButton;
private JButton back;
    int[][] OverallPuzzle = new int[9][9];

    private static final int CELL_SIZE = 60;
    private static final Color HIGHLIGHT_COLOR = new Color(235, 235, 235);

    public SolveRandomGui(SudokuSolver generator,int removes) {
        
        setTitle("Random Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(9, 9));
        cells = new JTextField[9][9];

        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 25);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new JTextField();
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setFont(font);
                cells[i][j].setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                cells[i][j].addFocusListener(new CellFocusListener(i, j));
                boardPanel.add(cells[i][j]);
            }
        }

        
        generator.generate(removes);
                     int[][] grid = generator.getGrid();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int value = grid[i][j];
                if (value != 0) {
                    cells[i][j].setBackground(Color.WHITE);
                    cells[i][j].setText(String.valueOf(value));
                    cells[i][j].setEditable(false);
                }
            }
        }
    long startTime = System.nanoTime();
    SudokuSolver s = new SudokuSolver(generator.getGrid());
    s.solve();
    long endTime = System.nanoTime();
    System.out.println("Time Taken: " + (endTime - startTime) / 1000000+" ms");
    int[][] solved = s.getGrid();
          for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            this.OverallPuzzle[i][j] = solved[i][j];   
        }
    }
          
        JPanel buttonPanel = new JPanel();
checkButton = new JButton("Check");
checkButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        checkNumbers();
    }
});

for (int i = 0; i < 9; i++) {
    for (int j = 0; j < 9; j++) {
        final int row = i;
        final int col = j;

        cells[i][j].addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_ENTER) {
                    checkButton.doClick();
                } else if (keyCode == KeyEvent.VK_UP && row > 0) {
                    cells[row - 1][col].requestFocus();
                } else if (keyCode == KeyEvent.VK_DOWN && row < 8) {
                    cells[row + 1][col].requestFocus();
                } else if (keyCode == KeyEvent.VK_LEFT && col > 0) {
                    cells[row][col - 1].requestFocus();
                } else if (keyCode == KeyEvent.VK_RIGHT && col < 8) {
                    cells[row][col + 1].requestFocus();
                }
            }
        });
    }
}

helpButton = new JButton("Help");
helpButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        provideHelp();
    }
});

solveButton = new JButton("Solve All");

        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudoku();
            }
        });


newGenerateButton = new JButton("New Generate");
newGenerateButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        SudokuSolver g=new SudokuSolver();
        g.generate(removes);
        
        generateNewGrid(g);
    }
});


back = new JButton("Back");
back.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
   Levels l=new Levels();
   l.setVisible(true);
        setVisible(false);
    }
});

buttonPanel.add(solveButton);
buttonPanel.add(checkButton);
buttonPanel.add(helpButton);
buttonPanel.add(solveButton);
buttonPanel.add(newGenerateButton);
buttonPanel.add(back);
        

        add(boardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        // Fill the GUI grid with the pre-generated Sudoku grid


        
    }

   private void solveSudoku() {
        // Update the GUI with the solved puzzle
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if( cells[i][j].getText().toString().equals("") ){
                cells[i][j].setText(String.valueOf(OverallPuzzle[i][j]));
                cells[i][j].setBackground(new Color(152, 251, 152));
                cells[i][j].setEditable(false);
                }
            }
        }
    } 


    private class CellFocusListener extends FocusAdapter {
        private int row;
        private int col;

        public CellFocusListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void focusGained(FocusEvent e) {
            highlightCell(row, col);
        }

        @Override
        public void focusLost(FocusEvent e) {
            unhighlightCell(row, col);
        }
    }

    private void highlightCell(int row, int col) {
        for (int i = 0; i < 9; i++) {
            cells[i][col].setBackground(HIGHLIGHT_COLOR);
            cells[row][i].setBackground(HIGHLIGHT_COLOR);
        }

        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;

        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                cells[i][j].setBackground(HIGHLIGHT_COLOR);
            }
        }
    }

    private void unhighlightCell(int row, int col) {
        for (int i = 0; i < 9; i++) {
            cells[i][col].setBackground(Color.WHITE);
            cells[row][i].setBackground(Color.WHITE);
        }

        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                cells[i][j].setBackground(Color.WHITE); // Reset box highlight
            }
        }
    }
    
private void updateSelectedCell(int number) {
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            if (cells[i][j].isFocusOwner()) {
                cells[i][j].setText(String.valueOf(number));
                cells[i][j].setEditable(false); // Disable editing after setting the value
                //cells[i][j].transferFocus(); // Remove focus from the selected cell

                solveSudoku(); // Solve the Sudoku puzzle

                return;
            }
        }
    }
}

private void checkNumbers() {
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            if (cells[i][j].isEditable()) {
                String cellValue = cells[i][j].getText();
                if (!cellValue.isEmpty()) {
                    int value = Integer.parseInt(cellValue);
                    if (value==this.OverallPuzzle[i][j]) {
                        cells[i][j].setBackground(Color.GREEN);
                        cells[i][j].setEditable(false);
                    } else {
                        cells[i][j].setBackground(Color.RED);
                        JOptionPane.showMessageDialog(this, "Wrong Number");
                        cells[i][j].setText("");
                    }
                }
            }
        }
    }
}

private void provideHelp() {
    // Find an empty cell
    int emptyRow = -1;
    int emptyCol = -1;
    boolean foundEmptyCell = false;

    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            if (cells[i][j].getText().isEmpty()) {
                emptyRow = i;
                emptyCol = j;
                foundEmptyCell = true;
                break;
            }
        }
        if (foundEmptyCell) {
            break;
        }
    }

    if (!foundEmptyCell) {
        JOptionPane.showMessageDialog(this, "No empty cells to provide help.");
        return;
    }
    
    SudokuSolver grid = new SudokuSolver(this.OverallPuzzle);
    boolean solved = grid.solve();
  
    if (!grid.solve()) {
        JOptionPane.showMessageDialog(this, "The puzzle cannot be solved.");
        return;
    }

    int[][] solutionGrid = grid.getGrid();
    int correctValue = solutionGrid[emptyRow][emptyCol];

    // Update the empty cell with the correct value and highlight it in green
    cells[emptyRow][emptyCol].setText(String.valueOf(correctValue));
    cells[emptyRow][emptyCol].setBackground(Color.GREEN);
    cells[emptyRow][emptyCol].setEditable(false);
}

private int[][] getCurrentGrid() {
    int[][] puzzle = new int[9][9];

    // Retrieve the values from the text fields and store them in the puzzle array
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            String cellValue = cells[i][j].getText();
            if (!cellValue.isEmpty()) {
                puzzle[i][j] = Integer.parseInt(cellValue);
            }
        }
    }
    return puzzle;
}

private void generateNewGrid(SudokuSolver generator) {
    if (generator.isValidSudoku()) {
        int[][] newGrid = generator.getGrid();
        updateGrid(newGrid);
    } else {
        JOptionPane.showMessageDialog(this, "Failed to generate a valid Sudoku grid.");
    }
    long startTime = System.nanoTime();
    SudokuSolver s = new SudokuSolver(generator.getGrid());
    s.solve();
     long endTime = System.nanoTime();
     System.out.println("Time Taken: " + (endTime - startTime) / 1000000+" ms");
    int[][] solved = s.getGrid();
          for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            this.OverallPuzzle[i][j] = solved[i][j];   
        
        }
    }
    
}
private void updateGrid(int[][] grid) {
    clearBoard();
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            int value = grid[i][j];
            if (value != 0) {
                cells[i][j].setBackground(Color.WHITE);
                cells[i][j].setText(String.valueOf(value));
                cells[i][j].setEditable(false);
            }
        }
    }
    // Update the OverallPuzzle with the new grid
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            this.OverallPuzzle[i][j] = grid[i][j];
        }
    }
}
private void clearBoard() {
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            cells[i][j].setText("");
            cells[i][j].setEditable(true);
            cells[i][j].setBackground(Color.WHITE);
        }
    }
}

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                SudokuSolver generator = new SudokuSolver();

                
                if(generator.isValidSudoku())
                {SolveRandomGui gui = new SolveRandomGui(generator,30);
                 gui.setVisible(true);
                }
                else System.out.println("Try again");
            }
        });
    }
}
