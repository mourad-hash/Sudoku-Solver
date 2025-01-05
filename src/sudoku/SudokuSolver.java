/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.*;
import javax.swing.JOptionPane;

class Pair {
    private int row;
    private int col;

    public Pair(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}

public class SudokuSolver {
    private static final int SIZE = 9;
    private int[][] board;
    private List<Integer>[][] domains;

    public SudokuSolver(int[][] initialBoard) {
        board = new int[SIZE][SIZE];
        domains = new List[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = initialBoard[i][j];
                if (initialBoard[i][j] == 0) {
                    domains[i][j] = new ArrayList<>();
                    for (int value = 1; value <= SIZE; value++) {
                        if(isSafe(i, j, value))
                        domains[i][j].add(value);
                    }
                }
            }
        }
    }

    public SudokuSolver() {
        board = new int[SIZE][SIZE];
    }

public boolean solve() {
    enforceArcConsistency();
    
    if (!isValidSudoku() || !backtrack(0, 0)) {
        System.out.println("Not solvable");
        JOptionPane.showMessageDialog(null, "Not solvable");
        return false;
    } else {
        return true;
    }
}

    private boolean backtrack(int row, int col) {
        if (row == SIZE) {
            return true; // Reached the end of the puzzle
        }
        
        if (col == SIZE) {
            return backtrack(row + 1, 0); // Move to the next row
        }

        if (board[row][col] != 0) {
            return backtrack(row, col + 1); // Cell already filled, move to the next column
        }

        for (int num : domains[row][col]) {
           
            if (isSafe(row, col, num)) {
                board[row][col] = num; // Place the number

                if (backtrack(row, col + 1)) {
                    return true; // Successfully solved the puzzle
                }

                board[row][col] = 0; // Undo the placement
            }
        }

        return false; // No valid number can be placed, backtrack
    }

    private boolean isSafe(int row, int col, int num) {
        // Check row
        for (int c = 0; c < SIZE; c++) {
            if (board[row][c] == num) {
                return false;
            }
        }

        // Check column
        for (int r = 0; r < SIZE; r++) {
            if (board[r][col] == num) {
                return false;
            }
        }

        // Check 3x3 subgrid
        int subgridRow = row - row % 3;
        int subgridCol = col - col % 3;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[subgridRow + r][subgridCol + c] == num) {
                    return false;
                }
            }
        }

        return true;
    }

private void enforceArcConsistency() {
    Queue<Pair> q = new LinkedList<>();
    // Add all cells with zero value to the queue
    for (int row = 0; row < SIZE; row++) {
        for (int col = 0; col < SIZE; col++) {
            if (board[row][col] == 0) {
                q.add(new Pair(row, col)); //I added the unassigned cells in the queue
            }
        }
    }

    while (!q.isEmpty()) {
        Pair cell = q.poll(); //we can use remove(throw an exception when the queue is empty but I don't care coz I am in the while loop with strict condition :) )
        int row = cell.getRow();
        int col = cell.getCol();

        // Print domains before revise
        System.out.println("Before revise - Domains[" + row + "][" + col + "]: " + domains[row][col]);

        if (revise(row, col)) {
            // Print domains after revise
            System.out.println("After revise - Domains[" + row + "][" + col + "]: " + domains[row][col]);

            if (domains[row][col].isEmpty()) {
                return; // Inconsistency found, stop enforcing arc consistency
            }

            // Add all neighbors of the current cell to the queue
            int subgridRow = row - row % 3;
            int subgridCol = col - col % 3;
            for (int r = subgridRow; r < subgridRow + 3; r++) {
                for (int c = subgridCol; c < subgridCol + 3; c++) {
                    if (r != row && c != col && board[r][c] == 0) {
                        q.add(new Pair(r, c));
                    }
                }
            }

            // Add all cells in the same row and column as the current cell to the queue
            for (int i = 0; i < SIZE; i++) {
                if (i != col && board[row][i] == 0) {
                    q.add(new Pair(row, i));
                }
                if (i != row && board[i][col] == 0) {
                    q.add(new Pair(i, col));
                }
            }
        }
    }
}


   private boolean revise(int row, int col) {
    boolean revised = false;
    List<Integer> values = domains[row][col];
    List<Integer> valuesToRemove = new ArrayList<>();

    for (int value : values) {
        if (!hasSupport(row, col, value)) {
            valuesToRemove.add(value);
            revised = true;
        }
    }

    // Remove values after the loop to avoid concurrent modification
    values.removeAll(valuesToRemove);

    return revised;
}

    private boolean hasSupport(int row, int col, int value) {
        for (int c = 0; c < SIZE; c++) {
            if (c != col && board[row][c] == 0 && domains[row][c].contains(value)) {
                return true;
            }
        }

        for (int r = 0; r < SIZE; r++) {
            if (r != row && board[r][col] == 0 && domains[r][col].contains(value)) {
                return true;
            }
        }

        int subgridRow = (row / 3) * 3;
        int subgridCol = (col / 3) * 3;
        /*      similar to (any of them run correctly)
        int subgridRow = row - row % 3;
        int subgridCol = col - col % 3;
        */
        for (int r = subgridRow; r < subgridRow + 3; r++) {
            for (int c = subgridCol; c < subgridCol + 3; c++) {
                if (r != row && c != col && board[r][c] == 0 && domains[r][c].contains(value)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(this.board[i][j] + " ");
            }
            System.out.println();
        }
    }
//--------------------- Generating Random Grid ------------------
   public void generate(int remove) {
    if (fillBoard(0, 0)) {
        // Grid is solvable
        removeCells(remove); 
    } else {
        System.out.println("Not Solvable");
    }
}
private boolean fillBoard(int row, int col) {
    if (col == SIZE) {
        col = 0;
        row++;
        if (row == SIZE)
            return true;
    }

    List<Integer> values = new ArrayList<>();
    for (int i = 1; i <= SIZE; i++) {
        values.add(i);
    }

    shuffleList(values);

    for (int value : values) {
        if (isValidMove(row, col, value)) {
            board[row][col] = value;
            if (fillBoard(row, col + 1))
                return true;
            board[row][col] = 0;
        }
    }

    return false;
}

  public void removeCells(int numToRemove) {
        Random rand = new Random();
        for (int i = 0; i < numToRemove; i++) {
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);
            if (board[row][col] != 0) {
                board[row][col] = 0;
            } else {
                i--; // Retry if the cell is already empty
            }
        }
    }

     private void shuffleList(List<Integer> list) {
        Random rand = new Random();
        for (int i = list.size() - 1; i > 0; i--) {
            int idx = rand.nextInt(i + 1);
            int temp = list.get(idx);
            list.set(idx, list.get(i));
            list.set(i, temp);
        }
    }

    private boolean isValidMove(int row, int col, int value) {
        // Check row and column
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == value || board[i][col] == value)
                return false;
        }

        // Check sub-grid
        int subGridRow = row - row % 3;
        int subGridCol = col - col % 3;
        
        for (int i = subGridRow; i < subGridRow + 3; i++) {
            for (int j = subGridCol; j < subGridCol + 3; j++) {
                if (board[i][j] == value)
                    return false;
            }
        }

        return true;
    }
    
   
        
          
    
     public int[][] getGrid() {
    int[][] grid = new int[SIZE][SIZE];
    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            grid[i][j] = board[i][j];
        }
    }
    return grid;
}
     
    public boolean isValidSudoku() {
        // Check rows and columns
        for (int i = 0; i < SIZE; i++) {
            if (!isValidRow(i) || !isValidColumn(i))
                return false;
        }

        // Check sub-grids
        for (int i = 0; i < SIZE; i += 3) {
            for (int j = 0; j < SIZE; j += 3) {
                if (!isValidSubGrid(i, j))
                    return false;
            }
        }

        return true;
    }
    
        private boolean isValidRow(int row) {
        boolean[] used = new boolean[SIZE];
        for (int i = 0; i < SIZE; i++) {
            int value = board[row][i];
            if (value != 0) {
                if (used[value - 1])
                    return false;
                used[value - 1] = true;
            }
        }
        return true;
    }

    private boolean isValidColumn(int col) {
        boolean[] used = new boolean[SIZE];
        for (int i = 0; i < SIZE; i++) {
            int value = board[i][col];
            if (value != 0) {
                if (used[value - 1])
                    return false;
                used[value - 1] = true;
            }
        }
        return true;
    }

    private boolean isValidSubGrid(int row, int col) {
        boolean[] used = new boolean[SIZE];
        for (int i = row; i < row + 3; i++) {
            for (int j = col; j < col + 3; j++) {
                int value = board[i][j];
                if (value != 0) {
                    if (used[value - 1])
                        return false;
                    used[value - 1] = true;
                }
            }
        }
        return true;
    }
            
public static void main(String[] args) {
    
   int[][] grid = {
       {8, 3, 9, 7, 0, 0, 2, 1, 4 },
       {5 ,2 ,0, 0, 1 ,4 ,3 ,6 ,9 },
       {0, 6, 1, 2, 0, 9 ,0 ,0 ,0 },
       {0 ,1, 8, 4, 0, 5, 6, 2, 0 },
       {0 ,4, 2, 6, 0, 1, 5, 8, 0 },
       {6 ,0, 5, 3, 0, 2, 4, 9, 0 },
       {7, 0, 0, 5, 0, 8, 0, 4, 0 },
       {1, 0, 4, 9, 0, 3, 7, 5, 0 },
       {2, 5, 6, 1, 0, 0, 9, 3, 8 }
        };



SudokuSolver solver = new SudokuSolver(grid);
if (solver.solve()) {
     solver.printBoard();
    System.out.println("Solvable");
}

// --------- Random ----------

//SudokuSolver solver = new SudokuSolver();
//solver.generate(30);
//solver.printBoard();
//if (solver.isValidSudoku()) {
//    System.out.println("Solvable");
//
//    SudokuSolver s = new SudokuSolver(solver.getGrid());
//    s.solve();
//            s.printBoard();
//} else {
//    System.out.println("Not solvable");
//}


}
}