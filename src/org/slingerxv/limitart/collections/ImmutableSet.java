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
package org.slingerxv.limitart.collections;

import org.slingerxv.limitart.base.Conditions;
import org.slingerxv.limitart.base.NotNull;
import org.slingerxv.limitart.base.Test1;
import org.slingerxv.limitart.base.ThreadSafe;

import java.util.*;


/**
 * 不可变Set
 *
 * @author hank
 */
@ThreadSafe
public class ImmutableSet<E> implements Iterable<E> {
    private Set<E> set;

    public static <E> ImmutableSet<E> of(@NotNull Collection<E> collection) {
        Conditions.args(collection != null && !collection.isEmpty());
        ImmutableSet<E> il = new ImmutableSet<>();
        il.set.addAll(collection);
        return il;
    }

    public static <E> ImmutableSet<E> just(@NotNull E... elements) {
        Conditions.args(elements != null && elements.length > 0);
        ImmutableSet<E> il = new ImmutableSet<>();
        for (int i = 0; i < elements.length; ++i) {
            il.set.add(elements[i]);
        }
        return il;
    }

    private ImmutableSet() {
        set = new HashSet<>();
    }

    public int size() {
        return set.size();
    }


    public boolean contains(E e) {
        Conditions.notNull(e);
        return set.contains(e);
    }


    public void foreach(Test1<E> test) {
        for (E e : set) {
            if (!test.test(e)) {
                break;
            }
        }
    }

    public Set<E> copy() {
        Set<E> hashSet = new HashSet<>();
        hashSet.addAll(set);
        return hashSet;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator(this.set.toArray());
    }
}
