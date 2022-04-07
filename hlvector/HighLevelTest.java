/*
 * Copyright (c) 2022, Dietmar Lippold
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact dietmar.lippold@mailbox.org you need additional
 * information or have any questions.
 */

package hlvector;

import java.util.Arrays;

import jdk.incubator.vector.IntVector;

/**
 * Test class of the package.
 *
 * @version 03Apr2022
 */
public class HighLevelTest {

    /**
     * The number of elements of the vectors.
     */
    public static final int ELEMENT_NO = 7;

    /**
     * The start value of the first vector.
     */
    public static final int FIRST_VEC_START = 0;

    /**
     * The start value of the second vector.
     */
    public static final int SECOND_VEC_START = 10;

    /**
     * Generates a new array of <code>int</code> values with successive
     * values.
     *
     * @param length      The length of the new array.
     * @param startValue  The first value of the new array.
     *
     * @return  The new generated array.
     */
    public static int[] newArray(int length, int startValue) {
        int[]  newArray;

        newArray = new int[length];

        for (int i = 0; i < length; i++) {
            newArray[i] = startValue + i;
        }

        return newArray;
    }

    /**
     * Returns an array of <code>int</code> values which were computed
     * element-wise negating the sum of the squares of the two arrays.
     *
     * @param a  The first array.
     * @param b  The second array.
     *
     * @return  The result of the computation.
     */
    public static int[] intVectorComputation(int[] a, int[] b) {

        var va = HighLevelIntVector.fromArray(a);
        var vb = HighLevelIntVector.fromArray(b);
        var vc = va.mul(va)
                   .add(vb.mul(vb))
                   .neg();

        return vc.toArray();
    }

    /**
     * The main methode of the class.
     *
     * @param args  The command line arguments.
     */
    public static void main(String[] args) {
        HighLevelIntVector  testVector;
        int[]  firstArray, secondArray, resultArray;

        firstArray = newArray(ELEMENT_NO, FIRST_VEC_START);
        secondArray = newArray(ELEMENT_NO, SECOND_VEC_START);
        resultArray = intVectorComputation(firstArray, secondArray);

        System.out.println("First array:");
        System.out.println(Arrays.toString(firstArray));

        System.out.println("Second array:");
        System.out.println(Arrays.toString(secondArray));

        System.out.println("Result array:");
        System.out.println(Arrays.toString(resultArray));

    }
}

