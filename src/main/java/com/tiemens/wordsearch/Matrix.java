package com.tiemens.wordsearch;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Function;

public class Matrix<T> {
    private T[][] data;
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = (T[][]) new Object[rows][cols];
    }

    public Matrix(T[][] data) {
         this.rows = data.length;
         this.cols = data[0].length;
         this.data = data;
    }
  
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
  
    public T get(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return data[row][col];
    }

    public void set(int row, int col, T value) {
        if (row < 0 || row >= rows) {
            throw new IndexOutOfBoundsException("row index out of bounds, row=" + row + " max=" + rows);
        }
        if ( col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("col index out of bounds, col=" + col + " max=" + cols + " val=" + value);
        }
        data[row][col] = value;
    }
 
    public boolean printIncludeRow(int row) {
        return true;
    }

    @Override
    public String toString() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final String utf8 = StandardCharsets.UTF_8.name();
        PrintStream ps = null;
        String data = null;
        try {
            ps = new PrintStream(baos, true, utf8);
            printMatrix(ps);
            data = baos.toString(utf8);
        } catch (Exception e) {
            //
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return data;
    }

    public void printMatrix() {
        printMatrix(System.out);
    }
    public void printMatrix(PrintStream out) {
        printMatrix(" ", out, "", "", "", "", null);
    }
    public void printMatrix(String separatorCell, PrintStream out, 
                            String beforeCell, String afterCell,
                            String beforeRow, String afterRow,
                            Function<String, String> filter) {
        for (int i = 0; i < rows; i++) {
            if (printIncludeRow(i)) {
                if (! beforeRow.isEmpty()) {
                    out.println(beforeRow);
                }
                
                String ps = "";
                for (int j = 0; j < cols; j++) {
                    out.print(ps);
                    ps = separatorCell;
                    out.print(beforeCell);
                    if (filter != null) {
                        out.print(filter.apply("" + data[i][j]));
                    } else {
                        out.print(data[i][j]);
                    }
                    out.print(afterCell);
                }
                out.println();
                
                if (! afterRow.isEmpty()) {
                    out.println(afterRow);
                }
            }

        }
    }
}