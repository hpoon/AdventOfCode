package com.aoc.y2022;

import com.aoc.NestedList;
import com.aoc.ProblemDay;

public class ProblemDay13a extends ProblemDay<Integer> {

    @Override
    public Integer solve() {
        int index = 1;
        int sum = 0;
        while (scanner.hasNextLine()) {
            NestedList list1 = new NestedList(scanner.nextLine());
            NestedList list2 = new NestedList(scanner.nextLine());
            int compare = list1.compareTo(list2);
            if (compare < 1) {
                sum += index;
            }
            index++;
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }
        return sum;
    }

}
