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

import jdk.incubator.vector.VectorSpecies;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;

/**
 * A vector with integer values.
 *
 * @version 03Apr2022
 */
public class HighLevelIntVector extends HighLevelVector {

    /**
     * The value of the stored hash code if the calculated hash code is
     * zero. Therefore the value may not be zero.
     */
    private static final int HASHCODE_DEFAULT = 1;

    /**
     * The species of the base vectors.
     */
    private static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

    /**
     * The number of lanes in the base vectors.
     */
    private static final int LANE_NUMBER = SPECIES.length();

    /**
     * The low level vectors with the first elements of this vector.
     */
    private final IntVector[] baseVectors;

    /**
     * The last elements of this vector which cannot belong to the low
     * level vectors.
     */
    private final int[] tailElements;

    /**
     * The number of the elements of the vector.
     */
    private final int length;

    /**
     * The stored hash code of this instance. If the value is zero, no
     * hash code was calculated yet.
     */
    private int hashCode = 0;

    /**
     * Creates a new instance without values.
     *
     * @param elementNo  The number of elemens of the new vector.
     */
    protected HighLevelIntVector(int elementNo) {

        length = elementNo;
        baseVectors = new IntVector[length / LANE_NUMBER];
        tailElements = new int[length % LANE_NUMBER];
    }

    /**
     * Creates a new instance.
     *
     * @param intArray  The array from which the values shall be taken.
     * @param offset    The index of the first value in the array to take
     *                  for the vector.
     */
    protected HighLevelIntVector(int[] intArray, int offset) {
        int  baseVecNo, tailElemNo;

        if (offset >= intArray.length) {
            baseVectors = new IntVector[0];
            tailElements = new int[0];
            length = 0;

            return;
        }

        length = intArray.length - offset;
        baseVectors = new IntVector[length / LANE_NUMBER];
        tailElements = new int[length % LANE_NUMBER];

        baseVecNo = -1;
        for (int intElemNo = offset; intElemNo + LANE_NUMBER <= intArray.length; intElemNo += LANE_NUMBER) {
            baseVecNo++;
            baseVectors[baseVecNo] = IntVector.fromArray(SPECIES, intArray, intElemNo);
        }

        tailElemNo = -1;
        for (int intElemNo = intArray.length - offset - tailElements.length; intElemNo < intArray.length; intElemNo++) {
            tailElemNo++;
            tailElements[tailElemNo] = intArray[intElemNo];
        }
    }

    /**
     * Returns the base vectors of the vector.
     *
     * @return  The base vectors of the vector.
     */
    protected IntVector[] baseVectors() {
        return baseVectors;
    }

    /**
     * Returns the tail elemens of the vector.
     *
     * @return  The tail elemens of the vector.
     */
    protected int[] tailElements() {
        return tailElements;
    }

    /**
     * Loads a vector from an array of type <code>int[]</code> starting at
     * an offset.
     *
     * @param intArray  The array from which the values shall be taken.
     * @param offset    The index of the first value in the array to take
     *                  for the vector.
     *
     * @return  An new instance with the given values.
     */
    public static HighLevelIntVector fromArray(int[] intArray, int offset) {
        return new HighLevelIntVector(intArray, offset);
    }

    /**
     * Loads a vector from an array of type <code>int[]</code>.
     *
     * @param intArray  The array from which the values shall be taken.
     *
     * @return  An new instance with the given values.
     */
    public static HighLevelIntVector fromArray(int[] intArray) {
        return fromArray(intArray, 0);
    }

    /**
     * Returns the number of the elements of the vector.
     *
     * @return  The number of the elements of the vector.
     */
    public int length() {
        return length;
    }

    /**
     * Returs this vector as an array of type <code>int[]</code>.
     *
     * @return  This vector as an array of type <code>int[]</code>.
     */
    public int[] toArray() {
        int[]  intArray;
        int    offset;

        intArray = new int[length];

        for (int baseVecNo = 0; baseVecNo < baseVectors.length; baseVecNo++) {
            baseVectors[baseVecNo].intoArray(intArray, baseVecNo * LANE_NUMBER);
        }

        offset = baseVectors.length * LANE_NUMBER;
        for (int tailElemNo = 0; tailElemNo < tailElements.length; tailElemNo++) {
            intArray[offset + tailElemNo] = tailElements[tailElemNo];
        }

        return intArray;
    }

    /**
     * Returs this vector as an vector of <code>float</code> values.
     *
     * @return  This vector as an vector of <code>float</code> values.
     */
/*
    public HighLevelFloatVector asHighLevelFloatVector() {
        HighLevelFloatVector  hlFloatVec;
        int[]                 intArray;

        hlFloatVec = new float[length];

        intArray = toArray();
        for (int i = 0; i < length, i++) {
            hlFloatVec[i] = (float)intArray[i];
        }

        return HighLevelFloatVector.fromArray(hlFloatVec);
    }
*/

    /**
     * Applys the operation to the given value.
     *
     * @param op   The operation to apply.
     * @param val  The value the operation to apply to.
     *
     * @return  The result of the application of the operation to the
     *          given value.
     */
    public int resultOf(VectorOperators.Unary op, int val) {

        if (op.name().equals(VectorOperators.NEG.name())) {
            return -val;
        } else {
            throw new UnsupportedOperationException("Not supported yet");
        }
    }

    /**
     * Applys the operation to the given values.
     *
     * @param op    The operation to apply.
     * @param val1  The first value the operation to apply to.
     * @param val2  The second value the operation to apply to.
     *
     * @return  The result of the application of the operation to the
     *          given value.
     */
    public int resultOf(VectorOperators.Binary op, int val1, int val2) {

        if (op.name().equals(VectorOperators.ADD.name())) {
            return (val1 + val2);
        } else if (op.name().equals(VectorOperators.MUL.name())) {
            return (val1 * val2);
        } else {
            throw new UnsupportedOperationException("Not supported yet");
        }
    }

    /**
     * Applys the operation on the values of this vector.
     *
     * @param op  The operation used to process values.
     *
     * @return  The result of applying the operation element-wise to this
     *          vector.
     */
    public HighLevelIntVector calculate(VectorOperators.Unary op) {
        HighLevelIntVector  resultVec;
        IntVector[]         resultBaseVec;
        int[]               resultTailElems;

        resultVec = new HighLevelIntVector(length);
        resultBaseVec = resultVec.baseVectors();
        resultTailElems = resultVec.tailElements();

        for (int baseVecNo = 0; baseVecNo < baseVectors.length; baseVecNo++) {
            resultBaseVec[baseVecNo] = baseVectors[baseVecNo].lanewise(op);
        }

        for (int tailElemNo = 0; tailElemNo < tailElements.length; tailElemNo++) {
            resultTailElems[tailElemNo] = resultOf(op, tailElements[tailElemNo]);
        }

        return resultVec;
    }

    /**
     * Combines values of this vector with the given vector.
     *
     * @param op   The operation used to process values.
     * @param vec  The vector to combine with this vector.
     *
     * @return  The result of applying the operation element-wise to this
     *          vector and the given vector.
     */
    public HighLevelIntVector calculate(VectorOperators.Binary op, HighLevelIntVector vec) {
        HighLevelIntVector  resultVec;
        IntVector[]         paramBaseVec;
        IntVector[]         resultBaseVec;
        int[]               paramTailElems;
        int[]               resultTailElems;

        paramBaseVec = vec.baseVectors();
        paramTailElems = vec.tailElements();

        resultVec = new HighLevelIntVector(length);
        resultBaseVec = resultVec.baseVectors();
        resultTailElems = resultVec.tailElements();

        for (int baseVecNo = 0; baseVecNo < baseVectors.length; baseVecNo++) {
            resultBaseVec[baseVecNo] = baseVectors[baseVecNo].lanewise(op, paramBaseVec[baseVecNo]);
        }

        for (int tailElemNo = 0; tailElemNo < tailElements.length; tailElemNo++) {
            resultTailElems[tailElemNo] = resultOf(op, tailElements[tailElemNo], paramTailElems[tailElemNo]);
        }

        return resultVec;
    }

    /**
     * Combines values of this vector with the vector of the broadcast
     * scalar.
     *
     * @param op   The operation used to process lane values.
     * @param val  The input scalar.
     *
     * @return  The result of applying the operation element-wise to this
     *          vector and the vector of the broadcast scalar.
     */
    public HighLevelIntVector calculate(VectorOperators.Binary op, int val) {
        HighLevelIntVector  resultVec;
        IntVector[]         resultBaseVec;
        int[]               resultTailElems;

        resultVec = new HighLevelIntVector(length);
        resultBaseVec = resultVec.baseVectors();
        resultTailElems = resultVec.tailElements();

        for (int baseVecNo = 0; baseVecNo < baseVectors.length; baseVecNo++) {
            resultBaseVec[baseVecNo] = baseVectors[baseVecNo].lanewise(op, val);
        }

        for (int tailElemNo = 0; tailElemNo < tailElements.length; tailElemNo++) {
            resultTailElems[tailElemNo] = resultOf(op, tailElements[tailElemNo], val);
        }

        return resultVec;
    }

    /**
     * Negates this vector.
     *
     * @return  The negation of this vector.
     */
    public HighLevelIntVector neg() {
        return calculate(VectorOperators.NEG);
    }

    /**
     * Adds this vector element-wise to the given vector.
     *
     * @param vec  The vector to add to this vector.
     *
     * @return  The result of the element-wise addition of this vector
     *          and the given vector.
     */
    public HighLevelIntVector add(HighLevelIntVector vec) {
        return calculate(VectorOperators.ADD, vec);
    }

    /**
     * Multiplies this vector element-wise to the given vector.
     *
     * @param vec  The vector to multiplies to this vector.
     *
     * @return  The result of the element-wise multiplication of this
     *          vector and the given vector.
     */
    public HighLevelIntVector mul(HighLevelIntVector vec) {
        return calculate(VectorOperators.MUL, vec);
    }

    /**
     * Adds this vector element-wise to the vector of the broadcast scalar.
     *
     * @param val  The input scalar.
     *
     * @return  The result of the element-wise addition of this vector
     *          and the vector of the broadcast scalar.
     */
    public HighLevelIntVector add(int val) {
        return calculate(VectorOperators.ADD, val);
    }

    /**
     * Multiplies this vector element-wise to the vector of the broadcast
     * scalar.
     *
     * @param val  The input scalar.
     *
     * @return  The result of the element-wise multiplication of this
     *          vector and the vector of the broadcast scalar.
     */
    public HighLevelIntVector mul(int val) {
        return calculate(VectorOperators.MUL, val);
    }

    /**
     * Returns the hash code of the vector.
     *
     * @return  The hash code of the vector.
     */
    public int hashCode() {
        long hashCodeSum;

        if (hashCode == 0) {
            // The hash code was not calculated yet.

            hashCodeSum = 0;
            for (IntVector baseVector : baseVectors) {
                hashCodeSum += baseVector.hashCode();
            }
            hashCodeSum += Arrays.hashCode(tailElements);

            hashCode = (int)hashCodeSum;
            if (hashCode == 0) {
                hashCode = HASHCODE_DEFAULT;
            }
        }

        return hashCode;
    }

    /**
     * Tests if the given object is an instance of this class and contains
     * the some vales as this instance.
     *
     * @return  <code>true</code> if the given instance is equal to this
     *          instance, otherwise <code>false</code>.
     */
    public boolean equals(Object obj) {
        IntVector[]         otherBaseVectors;
        HighLevelIntVector  otherVector;

        if (! (obj instanceof HighLevelIntVector)) {
            return false;
        }

        otherVector = (HighLevelIntVector)obj;

        if ((otherVector.length() != length()) || (otherVector.hashCode() != hashCode())) {
            return false;
        }

        otherBaseVectors = otherVector.baseVectors();
        for (int i = 0; i < baseVectors.length; i++) {
            if (! baseVectors[i].equals(otherBaseVectors[i])) {
                return false;
            }
        }

        return (Arrays.equals(tailElements, otherVector.tailElements()));
    }

    /**
     * Returns a string representation of this vector, of the form
     * "[0,1,2...]".
     *
     * @return  A string representation of this vector.
     */
    public String toString() {
        return Arrays.toString(toArray());
    }
}

