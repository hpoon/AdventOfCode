package com.aoc;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.annotation.Nullable;

@Value
@EqualsAndHashCode
@AllArgsConstructor
public class MatrixElement<T> {

    private int row;

    private int col;

    @Nullable
    @EqualsAndHashCode.Exclude
    private T value;

    public MatrixElement(MatrixElement<T> element) {
        this.row = element.getRow();
        this.col = element.getCol();
        this.value = element.getValue();
    }

}
