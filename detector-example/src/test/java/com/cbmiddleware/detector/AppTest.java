package com.cbmiddleware.detector;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Collections;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        String str1 = "chenbo";
        String str2 = "chen";

        String str3 = "chenc";

        String str4 = "chena";

        int i = str1.compareTo(str2);
        System.out.println(i);

        i = str1.compareTo(str3);
        System.out.println(i);

        i = str1.compareTo(str4);
        System.out.println(i);
    }
}
