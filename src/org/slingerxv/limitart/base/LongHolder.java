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
package org.slingerxv.limitart.base;

/**
 * Long持有器
 *
 * @author hank
 * @version 2017/12/18 0018 19:39
 */
@ThreadUnsafe
public class LongHolder {
    private long value;

    public static LongHolder of(long value) {
        return empty().set(value);
    }

    public static LongHolder empty() {
        return new LongHolder();
    }

    private LongHolder() {
    }

    public long get() {
        return this.value;
    }

    public LongHolder set(long value) {
        this.value = value;
        return this;
    }
}
