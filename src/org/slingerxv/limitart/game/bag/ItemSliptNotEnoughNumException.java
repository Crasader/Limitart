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
package org.slingerxv.limitart.game.bag;

import org.slingerxv.limitart.base.LimitartNonRuntimeException;

/**
 * 拆分数量不足异常
 * 
 * @author hank
 *
 */
public class ItemSliptNotEnoughNumException extends LimitartNonRuntimeException {
	private static final long serialVersionUID = 1L;

	public ItemSliptNotEnoughNumException(int haveNum, int yourNum) {
		super("have num:" + haveNum + ",your num:" + yourNum);
	}
}
