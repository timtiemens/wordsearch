package com.tiemens.wordsearch;

import com.tiemens.wordsearch.model.WordSearchModel;

import java.util.Objects;

public class RowCol {
    private final int row;
    private final int col;

    public RowCol(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RowCol rowCol = (RowCol) o;
        return row == rowCol.row && col == rowCol.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    public RowCol getNext(RowCol maxRowCol) {
        RowCol ret;
        int newrow = getRow();
        int newcol = getCol();
        newcol += 1;
        if (newcol == maxRowCol.getCol()) {
            newcol = 0;
            newrow += 1;
            if (newrow == maxRowCol.getRow()) {
                newrow = -1;
                newcol = -1;
            }
        }
        if (newrow == -1) {
            ret = null;
        } else {
            ret = new RowCol(newrow, newcol);
        }


        // safety
        if (newcol > 20) {
            throw new IllegalArgumentException("newcol OOR=" + newcol + "  cols=" + newcol);
        }

        return ret;
    }

    @Override
    public String toString() {
        return "RowCol{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    public RowCol computeDirection(WordSearchModel.Direction direction, RowCol maxCoord) {
        int newrow = this.getRow() + direction.getDeltaRow();
        int newcol = this.getCol() + direction.getDeltaCol();
        RowCol ret = new RowCol(newrow, newcol);
        if (ret.isInBounds(maxCoord)) {
            return ret;
        } else {
            return null;
        }
    }

    private boolean isInBounds(RowCol maxCoord) {
        final int row = this.getRow();
        final int col = this.getCol();
        if ( (row < 0) || (row >= maxCoord.getRow()) ) {
            return false;
        }
        if ( (col < 0) || (col >= maxCoord.getCol()) ) {
            return false;
        }
        return true;
    }
}

