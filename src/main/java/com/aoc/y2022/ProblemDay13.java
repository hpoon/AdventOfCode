package com.aoc.y2022;

import com.aoc.NestedList;
import com.aoc.ProblemDay;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProblemDay13 extends ProblemDay<Integer, Integer> {

    @Override
    public Integer solveA() {
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

    @Override
    public Integer solveB() {
        List<NestedList> lists = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (StringUtils.isNotBlank(line)) {
                lists.add(new NestedList(line));
            }
        }
        NestedList divider1 = new NestedList("[[2]]");
        NestedList divider2 = new NestedList("[[6]]");
        lists.add(divider1);
        lists.add(divider2);
        lists.sort(NestedList::compareTo);
        int idx1 = lists.indexOf(divider1) + 1;
        int idx2 = lists.indexOf(divider2) + 1;
        return idx1 * idx2;
    }

}
