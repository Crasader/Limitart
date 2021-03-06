/*
 * Copyright (c) 2016-present The Limitart Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.limitart.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 集合操作
 *
 * @author hank
 */
public final class CollectionUtil {
    private CollectionUtil() {
    }

    public static boolean contains(long[] value, long[] expect) {
        if (expect == null || expect.length == 0) {
            return false;
        }
        if (value == null || value.length == 0) {
            return false;
        }
        for (long j : expect) {
            boolean has = false;
            for (long aValue : value) {
                if (aValue == j) {
                    has = true;
                    break;
                }
            }
            if (!has) {
                return false;
            }
        }
        return true;
    }

    public static boolean contains(int[] value, int[] expect) {
        if (expect == null || expect.length == 0) {
            return false;
        }
        if (value == null || value.length == 0) {
            return false;
        }
        for (int j : expect) {
            boolean has = false;
            for (int aValue : value) {
                if (aValue == j) {
                    has = true;
                    break;
                }
            }
            if (!has) {
                return false;
            }
        }
        return true;
    }

    public static int contains(int[] value, int expect) {
        if (value == null) {
            return -1;
        }
        for (int i = 0; i < value.length; ++i) {
            if (value[i] == expect) {
                return i;
            }
        }
        return -1;
    }

    public static int contains(long[] value, long expect) {
        if (value == null) {
            return -1;
        }
        for (int i = 0; i < value.length; ++i) {
            if (value[i] == expect) {
                return i;
            }
        }
        return -1;
    }

    public static int contains(byte[] value, byte expect) {
        if (value == null) {
            return -1;
        }
        for (int i = 0; i < value.length; ++i) {
            if (value[i] == expect) {
                return i;
            }
        }
        return -1;
    }

    public static int contains(short[] value, short expect) {
        if (value == null) {
            return -1;
        }
        for (int i = 0; i < value.length; ++i) {
            if (value[i] == expect) {
                return i;
            }
        }
        return -1;
    }

    public static byte[] toByteArray(Collection<? extends Number> collection) {
        if (collection == null) {
            return new byte[0];
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        byte[] array = new byte[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) boxedArray[i]).byteValue();
        }
        return array;
    }

    public static List<Byte> toByteList(byte[] value) {
        List<Byte> result = new ArrayList<>();
        for (byte temp : value) {
            result.add(temp);
        }
        return result;
    }

    public static int[] toIntArray(Collection<? extends Number> collection) {
        if (collection == null) {
            return new int[0];
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        int[] array = new int[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) boxedArray[i]).intValue();
        }
        return array;
    }

    public static List<Integer> toIntList(int[] value) {
        List<Integer> result = new ArrayList<>();
        for (int temp : value) {
            result.add(temp);
        }
        return result;
    }

    public static long[] toLongArray(Collection<? extends Number> collection) {
        if (collection == null) {
            return new long[0];
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        long[] array = new long[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) boxedArray[i]).longValue();
        }
        return array;
    }

    public static List<Long> toLongList(long[] value) {
        List<Long> result = new ArrayList<>();
        for (long temp : value) {
            result.add(temp);
        }
        return result;
    }

    public static short[] toShortArray(Collection<? extends Number> collection) {
        if (collection == null) {
            return new short[0];
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        short[] array = new short[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) boxedArray[i]).shortValue();
        }
        return array;
    }

    public static List<Short> toShortList(short[] value) {
        List<Short> result = new ArrayList<>();
        for (short temp : value) {
            result.add(temp);
        }
        return result;
    }

    public static double[] toDoubleArray(Collection<? extends Number> collection) {
        if (collection == null) {
            return new double[0];
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        double[] array = new double[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) boxedArray[i]).doubleValue();
        }
        return array;
    }

    public static List<Double> toDoubleList(double[] value) {
        List<Double> result = new ArrayList<>();
        for (double temp : value) {
            result.add(temp);
        }
        return result;
    }

    public static float[] toFloatArray(Collection<? extends Number> collection) {
        if (collection == null) {
            return new float[0];
        }
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        float[] array = new float[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) boxedArray[i]).floatValue();
        }
        return array;
    }

    public static List<Float> toFloatList(float[] value) {
        List<Float> result = new ArrayList<>();
        for (float temp : value) {
            result.add(temp);
        }
        return result;
    }
}
