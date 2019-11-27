/**
 * Copyright (C) Centre for Computational Geography, University of Leeds.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.agdt.generic.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

/**
 * For processing and manipulating collections including Lists, Arrays, Sets and
 * Maps.
 */
public class Generic_Collections {

    /**
     * Returns a key in M that is mapped to the value input. If there are
     * multiple keys mapped to the value, this returns the first one that is
     * come across.
     *
     * @param m Map
     * @param value Object
     * @return Object
     */
    public static Object getKeyFromValue(Map m, Object value) {
        for (Object o : m.keySet()) {
            if (m.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    /**
     *
     * @param min Min
     * @param w Interval width
     * @param map Map
     * @param mc MathContext
     * @return {@code Object[]} r of length 3 where:
     * <ul>
     * <li>r[0] = counts</li>
     * <li>r[1] = labels</li>
     * <li>r[2] = mins</li>
     * </ul>
     */
    public static Object[] getIntervalCountsLabelsMins(BigDecimal min,
            BigDecimal w, TreeMap<?, BigDecimal> map, MathContext mc) {
        Object[] r = new Object[3];
        TreeMap<Integer, Integer> counts = new TreeMap<>();
        TreeMap<Integer, String> labels = new TreeMap<>();
        TreeMap<Integer, BigDecimal> mins = new TreeMap<>();
        Iterator<BigDecimal> ite = map.values().iterator();
        while (ite.hasNext()) {
            BigDecimal v = ite.next();
            int interval;
            if (w.compareTo(BigDecimal.ZERO) == 0) {
                interval = 0;
            } else {
                interval = getInterval(min, w, v, mc);
            }
            //addToTreeMapIntegerInteger(counts, interval, 1);
            addToTreeMapValueInteger(counts, interval, 1);
            if (!labels.containsKey(interval)) {
                BigDecimal imin = getIntervalMin(min, w, interval);
                BigDecimal intervalMax = getIntervalMax(imin, w);
                labels.put(interval, "" + imin + " - " + intervalMax);
                mins.put(interval, imin);
            }
        }
        r[0] = counts;
        r[1] = labels;
        r[2] = mins;
        return r;
    }

    /**
     * @param min Minimum
     * @param w Interval width
     * @param interval Interval
     * @return {@code min.add(new BigDecimal(interval).multiply(w))}
     */
    public static BigDecimal getIntervalMin(BigDecimal min, BigDecimal w,
            int interval) {
        return min.add(new BigDecimal(interval).multiply(w));
    }

    /**
     * @param min Minimum
     * @param w Interval width
     * @return {@code min.add(w)}
     */
    public static BigDecimal getIntervalMax(BigDecimal min, BigDecimal w) {
        return min.add(w);
    }

    /**
     * @param min Minimum
     * @param w Interval width
     * @param v Value
     * @param mc MathContext
     * @return {@code (value.subtract(min)).divide(w, mc).intValue()}
     */
    public static int getInterval(BigDecimal min, BigDecimal w, BigDecimal v, 
            MathContext mc) {
        return (v.subtract(min)).divide(w, mc).intValue();
    }

    /**
     * @param map Map
     * @return The min and max values in map.
     */
    public static BigDecimal[] getMinMaxBigDecimal(Map<?, BigDecimal> map) {
        BigDecimal[] r = new BigDecimal[2];
        Iterator<BigDecimal> ite = map.values().iterator();
        BigDecimal v = ite.next();
        r[0] = v;
        r[1] = v;
        while (ite.hasNext()) {
            v = ite.next();
            r[0] = r[0].min(v);
            r[1] = r[1].max(v);
        }
        return r;
    }

    /**
     * @param map Map
     * @return The min and max values in map.
     */
    public static int[] getMinMaxInteger(Map<?, Integer> map) {
        int[] r = new int[2];
        Iterator<Integer> ite = map.values().iterator();
        int v = ite.next();
        r[0] = v;
        r[1] = v;
        while (ite.hasNext()) {
            r[0] = Math.min(r[0], v);
            r[1] = Math.max(r[1], v);
        }
        return r;
    }

    /**
     * Get the union of {@code s0} and {@code s1}.
     *
     * @param s0 Set
     * @param s1 Set
     * @return a new {@code HashSet<Integer>} which is the union of elements in
     * {@code s0} and {@code s1}.
     */
    public static HashSet<Integer> getCompleteKeySet_HashSet(
            Set<Integer> s0, Set<Integer> s1) {
        HashSet<Integer> r = new HashSet<>();
        r.addAll(s0);
        r.addAll(s1);
        return r;
    }

    /**
     * If m contain the key k, then v is added to the HashSet. Otherwise a new
     * HashSet is created and added to m using the key k and v is added to the
     * HashSet.
     *
     * @param <K> Key
     * @param <V> Value
     * @param m Map
     * @param k key
     * @param v value
     */
    public static <K, V> void addToMap(HashMap<K, HashSet<V>> m, K k, V v) {
        HashSet<V> s;
        if (m.containsKey(k)) {
            s = m.get(k);
        } else {
            s = new HashSet<>();
            m.put(k, s);
        }
        s.add(v);
    }

    /**
     * If m contains the key k, then the key value pair (k2, v) are put in to
     * the value against k in m. If m does not contain the key k a new mapping
     * is put in m against k and the key value pair (k2, v) are put in the new
     * map.
     *
     * @param <K> Key
     * @param <K2> Key2
     * @param <V> Value
     * @param m Map
     * @param k key
     * @param k2 key2
     * @param v value
     */
    public static <K, K2, V> void addToMap(HashMap<K, HashMap<K2, V>> m, K k,
            K2 k2, V v) {
        HashMap<K2, V> m2;
        if (m.containsKey(k)) {
            m2 = m.get(k);
        } else {
            m2 = new HashMap<>();
            m.put(k, m2);
        }
        m2.put(k2, v);
    }

    /**
     * If m contain the key k, then v is added to the HashSet. Otherwise a new
     * HashSet is created and added to m using the key k and v is added to the
     * HashSet.
     *
     * @param <K> Key
     * @param <V> Value
     * @param m Map
     * @param k key
     * @param v value
     */
    public static <K, V> void addToMap(TreeMap<K, HashSet<V>> m, K k, V v) {
        HashSet<V> s;
        if (m.containsKey(k)) {
            s = m.get(k);
        } else {
            s = new HashSet<>();
            m.put(k, s);
        }
        s.add(v);
    }

//    public static <K, V> void addToMap(Map<K, Set<V>> m, K k, V v) {
//        Set<V> s;
//        if (m.containsKey(k)) {
//            s = m.get(k);
//        } else {
//            s = new HashSet<>();
//            m.put(k, s);
//        }
//        s.add(v);
//    }
    /**
     * Adds to a integer counting map.
     *
     * @param <K> Key
     * @param m The map that is to be added to.
     * @param k The key which value is added to or initialised.
     * @param i The amount to be added to the map.
     */
    public static <K> void addToMap(Map<K, Integer> m, K k, Integer i) {
        if (!m.containsKey(k)) {
            m.put(k, 1);
        } else {
            m.put(k, m.get(k) + 1);
        }
    }

    /**
     * Adds v to the ArrayList in m indexed by k.If such a list does not yet
     * exist it is created.
     *
     * @param <K> Key
     * @param <V> Value
     * @param m The map that is to be added to.
     * @param k The key which value is added to or initialised.
     * @param v The value to add to the list in map.
     */
    public static <K, V> void addToListIfDifferentFromLast(
            Map<K, ArrayList<V>> m, K k, V v) {
        ArrayList<V> l;
        if (m.containsKey(k)) {
            l = m.get(k);
            if (l.size() > 1) {
                V v0 = l.get(l.size() - 1);
                if (!v.equals(v0)) {
                    l.add(v);
                }
            } else {
                l.add(v);
            }
        } else {
            l = new ArrayList<>();
            l.add(v);
            m.put(k, l);
        }
    }

    /**
     * Adds value to the value at a_TreeMapIntegerIntegerCounter.get(key) if it
     * exists or puts the key, value into a_TreeMapIntegerIntegerCounter
     *
     * @param m Map
     * @param key Integer
     * @param value Integer
     */
    @Deprecated
    public static void addToTreeMapIntegerInteger(TreeMap<Integer, Integer> m,
            Integer key, Integer value) {
        Integer currentValue = m.get(key);
        if (currentValue != null) {
            Integer newValue = currentValue + value;
            m.put(key, newValue);
        } else {
            m.put(key, value);
        }
    }

    /**
     * @param u updateIntegerIntegerCounter TreeMap
     * @param uf updateFromIntegerIntegerCounter TreeMap
     */
    public static void addToTreeMapIntegerInteger(TreeMap<Integer, Integer> u,
            TreeMap<Integer, Integer> uf) {
        if (uf != null) {
            uf.entrySet().forEach((entry) -> {
                Integer key = entry.getKey();
                Integer v = entry.getValue();
                Integer currentValue = u.get(key);
                if (currentValue != null) {
                    Integer newValue = currentValue + v;
                    u.put(key, newValue);
                } else {
                    u.put(key, v);
                }
            });
        }
    }

    /**
     * Adds value to the value at map.get(key) if it exists or puts the key,
     * value into map.
     *
     * @param <K> Key
     * @param m TreeMap
     * @param k key
     * @param v value
     * @return long
     */
    public static <K> long addToTreeMapValueLong(TreeMap<K, Long> m, K k,
            long v) {
        long r;
        Long v0 = m.get(k);
        if (v0 != null) {
            r = v0 + v;
        } else {
            r = v;
        }
        m.put(k, r);
        return r;
    }

    /**
     * Adds v to the value of m given by k if it exists or puts the v into m at
     * k.
     *
     * @param <K> Key
     * @param m TreeMap
     * @param k key
     * @param v value
     * @return The resulting value of adding v to m.
     */
    public static <K> int addToTreeMapValueInteger(TreeMap<K, Integer> m,
            K k, int v) {
        int r;
        Integer v0 = m.get(k);
        if (v0 != null) {
            r = v0 + v;
        } else {
            r = v;
        }
        m.put(k, r);
        return r;
    }

    /**
     * Adds value to the value at map.get(key) if it exists or puts the key,
     * value into map.
     *
     * @param <K> Key
     * @param map0 TreeMap
     * @param map1 TreeMap
     * @return TreeMap
     */
    public static <K> TreeMap<K, Integer> addToTreeMapValueInteger(
            TreeMap<K, Integer> map0, TreeMap<K, Integer> map1) {
        TreeMap<K, Integer> r = deepCopyTreeMapValueInteger(map0);
        Iterator<K> ite = map1.keySet().iterator();
        while (ite.hasNext()) {
            K k = ite.next();
            Integer v = map1.get(k);
            Generic_Collections.addToTreeMapValueInteger(r, k, v);
        }
        return r;
    }

    /**
     * Sets the value in map to the max of map.get(key) and value.
     *
     * @param m TreeMap
     * @param k key
     * @param v value
     */
    public static void setMaxValueTreeMapStringInteger(
            TreeMap<String, Integer> m, String k, Integer v) {
        Integer v0 = m.get(k);
        if (v0 != null) {
            int v1 = Math.max(v0, v);
            if (!(v1 == v0)) {
                m.put(k, v1);
            }
        } else {
            m.put(k, v);
        }
    }

    /**
     * Sets the value in map to the min of map.get(key) and value.
     *
     * @param m TreeMap
     * @param k key
     * @param v value
     */
    public static void setMinValueTreeMapStringInteger(
            TreeMap<String, Integer> m, String k, Integer v) {
        Integer v0 = m.get(k);
        if (v0 != null) {
            Integer v1 = Math.min(v0, v);
            if (!(v1 == v0.intValue())) {
                m.put(k, v1);
            }
        } else {
            m.put(k, v);
        }
    }

    /**
     * Adds v to the value of m corresponding with k. If there is no such k in m
     * or the value m.get(k) is null or
     *
     * @param <K> Key
     * @param m TreeMap
     * @param k key
     * @param v value
     */
    public static <K> void addToTreeMapValueBigInteger(TreeMap<K, BigInteger> m,
            K k, BigInteger v) {
        BigInteger v0 = m.get(k);
        if (v0 != null) {
            BigInteger newValue = v0.add(v);
            m.put(k, newValue);
        } else {
            m.put(k, v);
        }
    }

    /**
     * Adds v to the value of m corresponding with k. If there is no such k in m
     * or the value m.get(k) is null or
     *
     * @param <K> Key
     * @param m TreeMap
     * @param k key
     * @param v value
     */
    public static <K> void addToTreeMapValueBigDecimal(TreeMap<K, BigDecimal> m,
            K k, BigDecimal v) {
        BigDecimal v0 = m.get(k);
        if (v0 != null) {
            BigDecimal newValue = v0.add(v);
            m.put(k, newValue);
        } else {
            m.put(k, v);
        }
    }

    /**
     * For all values in set1 we count how many values are in set0, and deduce
     * how many are not. Also we check how many values that are in set0 that are
     * not in set1.
     *
     * @param s0 HashSet
     * @param s1 HashSet
     * @return long[3] result {@code
     * result[0] = Count of how many values are in both set 0 and set 1;
     * result[1] = Count of how many values are in set 1, but not in set 0;
     * result[2] = Count of how many values are in set 0, but not in set 1;
     * }
     */
    public static long[] getCounts(HashSet s0, HashSet s1) {
        long[] r = new long[3];
        r[0] = 0;
        r[1] = 0;
        r[2] = 0;
        Iterator ite = s1.iterator();
        while (ite.hasNext()) {
            if (s0.contains(ite.next())) {
                r[0]++;
            } else {
                r[1]++;
            }
        }
        ite = s0.iterator();
        while (ite.hasNext()) {
            if (!s1.contains(ite.next())) {
                r[2]++;
            }
        }
        return r;
    }

    /**
     * For all values in set1 we count how many values are in set0, and deduce
     * how many are not.Also we check how many values that are in set0 that are
     * not in set1.
     *
     * @param <T> Type
     * @param s0 HashSet
     * @param s1 HashSet
     * @return Object[2] result {@code
     * Object[0] = union set view of elements in both set0 and set1
     * Object[1] = counts
     * counts[0] = Count of how many values are in both set 0 and set 1;
     * counts[1] = Count of how many values are in set 1, but not in set 0;
     * counts[2] = Count of how many values are in set 0, but not in set 1;
     * }
     */
    public static <T> Object[] getUnionAndCounts(HashSet<T> s0, HashSet<T> s1) {
        Object[] r = new Object[2];
        HashSet<T> union = new HashSet<>();
        union.addAll(s1);
        union.retainAll(s0);
        long[] counts = new long[3];
        int unionSize = union.size();
        counts[0] = unionSize;
        counts[1] = s1.size() - unionSize;
        counts[2] = s0.size() - unionSize;
        r[0] = union;
        r[1] = counts;
        return r;
    }

    /**
     * For all values in s1 we count how many values are in s0, and deduce how
     * many are not.Also we check how many values that are in s0 that are not in
     * s1.
     *
     * @param <T> Type
     * @param s0 HashSet
     * @param s1 HashSet
     * @return Object[2] result {@code
     * Object[0] = union set view of elements in both set0 and set1
     * Object[1] = counts
     * counts[0] = Count of how many values are in both s0 and s1;
     * counts[1] = Count of how many values are in s1, but not in s0;
     * counts[2] = Count of how many values are in s0, but not in s1;
     * }
     */
    public static <T> Object[] getUnionAndUniques(HashSet<T> s0,
            HashSet<T> s1) {
        Object[] r = new Object[3];
        HashSet<T> union = new HashSet<>();
        union.addAll(s1);
        union.retainAll(s0);
        HashSet<T> set1unique = new HashSet<>();
        set1unique.addAll(s1);
        set1unique.removeAll(s0);
        HashSet<T> set0unique = new HashSet<>();
        set0unique.addAll(s0);
        set0unique.removeAll(s1);
        r[0] = union;
        r[1] = set1unique;
        r[2] = set0unique;
        return r;
    }

    public static <K> TreeMap<K, BigInteger> deepCopyValueBigInteger(
            TreeMap<K, BigInteger> m) {
        TreeMap<K, BigInteger> r = new TreeMap<>();
        Iterator<K> ite = m.keySet().iterator();
        while (ite.hasNext()) {
            K k = ite.next();
            BigInteger vToCopy = m.get(k);
            BigInteger vCopy = new BigInteger(vToCopy.toString());
            r.put(k, vCopy);
        }
        return r;
    }

    public static <K> HashMap<K, String> deepCopyHashMapValueString(
            HashMap<K, String> m) {
        HashMap<K, String> r = new HashMap<>();
        Iterator<K> ite = m.keySet().iterator();
        while (ite.hasNext()) {
            K k = ite.next();
            r.put(k, m.get(k));
        }
        return r;
    }

    public static <K> HashMap<K, Integer> deepCopyHashMapValueInteger(
            HashMap<K, Integer> m) {
        HashMap<K, Integer> r = new HashMap<>();
        Iterator<K> ite = m.keySet().iterator();
        while (ite.hasNext()) {
            K k = ite.next();
            r.put(k, m.get(k));
        }
        return r;
    }

    public static <K, Integer> TreeMap<K, Integer> deepCopyTreeMapValueInteger(
            TreeMap<K, Integer> map) {
        TreeMap<K, Integer> r = new TreeMap<>();
        Iterator<K> ite = map.keySet().iterator();
        while (ite.hasNext()) {
            K k = ite.next();
            r.put(k, map.get(k));
        }
        return r;
    }

    public static <K> TreeMap<K, BigDecimal> deepCopyTreeMapValueBigDecimal(
            TreeMap<K, BigDecimal> m) {
        TreeMap<K, BigDecimal> r = new TreeMap<>();
        Iterator<K> ite = m.keySet().iterator();
        while (ite.hasNext()) {
            K k = ite.next();
            BigDecimal v0 = m.get(k);
            BigDecimal v1 = new BigDecimal(v0.toString());
            r.put(k, v1);
        }
        return r;
    }

    public static <K> TreeMap<K, Long> deepCopyTreeMapValueLong(
            TreeMap<K, Long> m) {
        TreeMap<K, Long> r = new TreeMap<>();
        Iterator<K> ite = m.keySet().iterator();
        while (ite.hasNext()) {
            K k = ite.next();
            Long v0 = m.get(k);
            Long v1 = v0;
            r.put(k, v1);
        }
        return r;
    }

    public static <K> void addToTreeMapValueLong(TreeMap<K, Long> mapToAddTo,
            TreeMap<K, Long> mapToAdd) {
        Iterator<K> ite = mapToAdd.keySet().iterator();
        while (ite.hasNext()) {
            K k = ite.next();
            Long vToAdd = mapToAdd.get(k);
            if (mapToAddTo.containsKey(k)) {
                Long vToAddTo = mapToAddTo.get(k);
                mapToAddTo.put(k, vToAdd + vToAddTo);
            } else {
                mapToAddTo.put(k, vToAdd);
            }
        }
    }

    public static <K> void addToTreeMapValueBigDecimal(
            TreeMap<K, BigDecimal> mapToAddTo,
            TreeMap<K, BigDecimal> mapToAdd) {
        Iterator<K> ite = mapToAdd.keySet().iterator();
        while (ite.hasNext()) {
            K k = ite.next();
            BigDecimal vToAdd = mapToAdd.get(k);
            if (mapToAddTo.containsKey(k)) {
                BigDecimal vToAddTo = mapToAddTo.get(k);
                mapToAddTo.put(k, vToAdd.add(vToAddTo));
            } else {
                mapToAddTo.put(k, vToAdd);
            }
        }
    }

    public static <K> void addToTreeMapValueBigInteger(
            TreeMap<K, BigInteger> mapToAddTo,
            TreeMap<K, BigInteger> mapToAdd) {
        Iterator<K> ite = mapToAdd.keySet().iterator();
        while (ite.hasNext()) {
            K k = ite.next();
            BigInteger vToAdd = mapToAdd.get(k);
            if (mapToAddTo.containsKey(k)) {
                BigInteger vToAddTo = mapToAddTo.get(k);
                mapToAddTo.put(k, vToAdd.add(vToAddTo));
            } else {
                mapToAddTo.put(k, vToAdd);
            }
        }
    }

    /**
     * @param m TreeMap
     * @param i Integer
     * @return Integer
     */
    public static Integer getMaxKey_Integer(TreeMap<Integer, ?> m, Integer i) {
        if (m.isEmpty()) {
            return i;
        } else {
            return m.lastKey();
        }
    }

    /**
     * @param m TreeMap
     * @param i Integer
     * @return Integer
     */
    public static Integer getMinKey_Integer(TreeMap<Integer, ?> m, Integer i) {
        if (m.isEmpty()) {
            return i;
        } else {
            return m.lastKey();
        }
    }

    public static BigDecimal getMaxValue_BigDecimal(TreeMap<?, BigDecimal> m,
            BigDecimal initialMax_BigDecimal) {
        BigDecimal r = new BigDecimal(initialMax_BigDecimal.toString());
        Iterator<BigDecimal> ite = m.values().iterator();
        while (ite.hasNext()) {
            r = r.max(ite.next());
        }
        return r;
    }

    public static BigDecimal getMinValue_BigDecimal(TreeMap<?, BigDecimal> m,
            BigDecimal initialMin) {
        BigDecimal r = new BigDecimal(initialMin.toString());
        Iterator<BigDecimal> ite = m.values().iterator();
        while (ite.hasNext()) {
            r = r.min(ite.next());
        }
        return r;
    }

    public static BigInteger getMaxValue_BigInteger(TreeMap<?, BigInteger> m,
            BigInteger initialMax) {
        BigInteger r = new BigInteger(initialMax.toString());
        Iterator<BigInteger> ite = m.values().iterator();
        while (ite.hasNext()) {
            r = r.max(ite.next());
        }
        return r;
    }

    public static BigInteger getMinValue_BigInteger(TreeMap<?, BigInteger> m,
            BigInteger initialMin) {
        BigInteger r = new BigInteger(initialMin.toString());
        Iterator<BigInteger> ite = m.values().iterator();
        while (ite.hasNext()) {
            r = r.max(ite.next());
        }
        return r;
    }

    /**
     * Returns a LinkedHashMap which is ordered in terms of the values in the
     * map m.
     *
     * @param <K> KeyType
     * @param <V> ValueType
     * @param m The map that is to be ordered by it's values.
     * @return Map
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
            Map<K, V> m) {
        Map<K, V> r;
        List<Map.Entry<K, V>> list;
        list = new LinkedList<>(m.entrySet());
        Collections.sort(list, (Map.Entry<K, V> o1, Map.Entry<K, V> o2)
                -> (o1.getValue()).compareTo(o2.getValue()));
        r = new LinkedHashMap<>();
        list.forEach((entry) -> {
            r.put(entry.getKey(), entry.getValue());
        });
        return r;
    }

    /**
     * For getting the maximum value in a collection of BigDecimals.
     *
     * @param c The collection of BigDecimals.
     * @return the maximum value in {@code c}
     */
    public static BigDecimal getMax(Collection<BigDecimal> c) {
        Optional<BigDecimal> o;
        o = c.stream().parallel().max(BigDecimal::compareTo);
        return o.get();
    }

    /**
     * For getting the maximum value in a collection of BigDecimals.
     *
     * @param c The collection of BigDecimals.
     * @return the maximum value in {@code c}
     */
    public static BigDecimal getMin(Collection<BigDecimal> c) {
        Optional<BigDecimal> o;
        o = c.stream().parallel().min(BigDecimal::compareTo);
        return o.get();
    }

    /**
     * A test if b is coned in c.
     *
     * @param c The collection tested.
     * @param b The value sought.
     * @return True iff b is in c.
     */
    public static boolean containsValue(Collection<BigDecimal> c, BigDecimal b) {
        return c.stream().parallel().anyMatch(v -> v.equals(b));
    }
}