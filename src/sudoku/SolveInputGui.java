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
import java.util.HashSet;
import java.util.Set;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class SolveInputGui extends JFrame {
    private JTextField[][] cells;
    private JButton solveButton;
    private JButton clearButton;
    private JButton helpButton;
    private JButton checkButton;
    private JButton save;
    private JButton back;
    int solution[][]=new int[9][9];
    private static final int CELL_SIZE = 60;
    private static final Color HIGHLIGHT_COLOR = new Color(235, 235, 235); 

    public SolveInputGui() {
        setTitle("Input Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(9, 9));
        cells = new JTextField[9][9];

        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20); 

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

            for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            cells[i][j].setText("");
            cells[i][j].setEditable(true);
        }
    }
        
        JPanel buttonPanel = new JPanel();
        solveButton = new JButton("Solve All");
        clearButton = new JButton("Clear");

        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudoku();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearBoard();
            }
        });
        
        
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
        
        save = new JButton("Save");
save.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        save();
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
                    save.doClick();
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

back = new JButton("Back");
back.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
   Levels l=new Levels();
   l.setVisible(true);
        setVisible(false);
    }
});

solveButton.setEnabled(false);
checkButton.setEnabled(false);
helpButton.setEnabled(false);

        buttonPanel.add(save);
        buttonPanel.add(checkButton);
        buttonPanel.add(helpButton);
        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(back);

        add(boardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }
    
    private void checkNumbers() {
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            if (cells[i][j].isEditable()) {
                String cellValue = cells[i][j].getText();
                if (!cellValue.isEmpty()) {
                    int value = Integer.parseInt(cellValue);
                    if (value==this.solution[i][j] &&(value>0 && value<10)) {
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
    
private void save() {
    boolean hasDuplicate = false;
    String errorMessage = "";
try{
    
        for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            String value = cells[i][j].getText();
            if (!value.isEmpty()) {
                if (value.contains("0")) {
                    hasDuplicate = true;
                    errorMessage = "Zero not valid if you wanna it empty dont enter any value ";
                    break;
                } 
            }
        }
        if (hasDuplicate) {
            break;
        }
    }
    
    // Check for duplicates in rows
    for (int i = 0; i < 9; i++) {
        Set<String> rowValues = new HashSet<>();
        for (int j = 0; j < 9; j++) {
            String value = cells[i][j].getText();
            if (!value.isEmpty()) {
                if (rowValues.contains(value)) {
                    hasDuplicate = true;
                    errorMessage = "Duplicate value "+value+" found in Row "+(i+1);
                    break;
                } else {
                    rowValues.add(value);
                }
            }
        }
        if (hasDuplicate) {
            break;
        }
    }

    // Check for duplicates in columns
    if (!hasDuplicate) {
        for (int j = 0; j < 9; j++) {
            Set<String> colValues = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                String value = cells[i][j].getText().trim();
                if (!value.isEmpty()) {
                    if (colValues.contains(value)) {
                        hasDuplicate = true;
                        errorMessage = "Duplicate value "+value+" found in Column "+(j+1);
                        
                        break;
                    } else {
                        colValues.add(value);
                    }
                }
            }
            if (hasDuplicate) {
                break;
            }
        }
    }

    // Check for duplicates in subgrids
    if (!hasDuplicate) {
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                Set<String> subgridValues = new HashSet<>();
                for (int row = i; row < i + 3; row++) {
                    for (int col = j; col < j + 3; col++) {
                        String value = cells[row][col].getText().trim();
                        if (!value.isEmpty()) {
                            if (subgridValues.contains(value)) {
                                hasDuplicate = true;
                                errorMessage = "Duplicate value "+value+" found in subgrid starting at row "+(i+1)+", column "+(j+1);
                                break;
                            } else {
                                subgridValues.add(value);
                            }
                        }
                    }
                    if (hasDuplicate) {
                        break;
                    }
                }
                if (hasDuplicate) {
                    break;
                }
            }
            if (hasDuplicate) {
                break;
            }
        }
    }

    if (hasDuplicate) {
        JOptionPane.showMessageDialog(this, errorMessage, "Duplicate Value", JOptionPane.WARNING_MESSAGE);
    } else {
        
            int[][] grid = new int[9][9];
    
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            String input = cells[i][j].getText().toString();
            if (!input.equals("")) {
                grid[i][j] = Integer.parseInt(input);
            }
            else {grid[i][j]=0;}
        }
    }  
    long startTime = System.nanoTime();
    
        SudokuSolver s = new SudokuSolver(grid);
        s.solve();
        long endTime = System.nanoTime();
        System.out.println("Time Taken: " + (endTime - startTime) / 1000000+" ms");
            int[][] sol = s.getGrid();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.solution[i][j] = sol[i][j];
            }
        }
        
        NoEdit();
    }
}catch(Exception ex){JOptionPane.showMessageDialog(this, "Enter Valid Number From 1 to 9", "Wrong Value", JOptionPane.WHEN_FOCUSED);}
}

private void NoEdit() {
    // Make cells with values non-editable and others editable
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            String value = cells[i][j].getText().trim();
            if (!value.isEmpty()) {
                cells[i][j].setEditable(false);
            } else {
                cells[i][j].setEditable(true);
            }
        }
    }
     JOptionPane.showMessageDialog(this,"Saved Succefully");
     solveButton.setEnabled(true);
     checkButton.setEnabled(true);
     helpButton.setEnabled(true);

     save.setEnabled(false);
}

private void clearBoard() {
    // Clear the board and make all cells editable again
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            cells[i][j].setText("");
            cells[i][j].setEditable(true);
            cells[i][j].setBackground(Color.WHITE);
        }
    }
    solveButton.setEnabled(false);
    checkButton.setEnabled(false);
    helpButton.setEnabled(false);
  
    save.setEnabled(true);
    
    
    
}

    
private void solveSudoku() {
    
    
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
        JOptionPane.showMessageDialog(this, "The Grid Already Solved");
        return;
    }
    
    
    
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (cells[i][j].getText().toString().equals("")) {
                    cells[i][j].setText(Integer.toString(solution[i][j]));
                    cells[i][j].setBackground(new Color(152, 251, 152));
                    cells[i][j].setEditable(false);
                }
            }
        }
        solveButton.setEnabled(false);
        checkButton.setEnabled(false);
helpButton.setEnabled(false);
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
    
    int correctValue = solution[emptyRow][emptyCol];

    // Update the empty cell with the correct value and highlight it in green
    cells[emptyRow][emptyCol].setText(String.valueOf(correctValue));
    cells[emptyRow][emptyCol].setBackground(Color.GREEN);
    cells[emptyRow][emptyCol].setEditable(false);
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
                cells[i][j].setBackground(Color.WHITE); 
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SolveInputGui sudokuGUI = new SolveInputGui();
                sudokuGUI.setVisible(true);
                SudokuSolver s=new SudokuSolver();
            }
        });
    }
}