package com.aoc;

import java.io.IOException;
import java.util.Scanner;

public interface ProblemDay<T> {

    T solve();

    Scanner getProblemInputStream() throws IOException;

}
