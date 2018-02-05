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
package org.slingerxv.limitart.pool;

import org.slingerxv.limitart.base.Func;

/**
 * 对象池
 *
 * @author hank
 * @version 2018/2/6 0006 0:03
 */
public interface Pool<T extends Poolable> extends AutoCloseable {
    /**
     * 创建一个默认的对象池
     *
     * @param <T>
     * @return
     */
    static <T extends Poolable> Pool<T> create(Func<T> factory, int initialSize) {
        return new SimplePool<>(factory, initialSize);
    }

    /**
     * 取出对象
     *
     * @return
     */
    T get();

    /**
     * 归还对象
     */
    void back(T t);
}
