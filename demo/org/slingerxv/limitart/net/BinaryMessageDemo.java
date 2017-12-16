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
package org.slingerxv.limitart.net;

import org.slingerxv.limitart.net.binary.BinaryMessage;
import org.slingerxv.limitart.net.binary.BinaryMessages;

public class BinaryMessageDemo extends BinaryMessage {
	public String content = "hello limitart!";

	@Override
	public short messageID() {
		return BinaryMessages.createID(0X00, 0X01);
	}

}