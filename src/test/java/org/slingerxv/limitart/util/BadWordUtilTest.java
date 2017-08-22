/*
 * Copyright (c) 2016-present The Limitart Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.slingerxv.limitart.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author hank
 *
 */
public class BadWordUtilTest {
	private String source = "aabefgcdeegh你好世界234$%^&sdg";

	@Test
	public void test() {
		List<String> words = new ArrayList<>();
		words.add("g");
		words.add("世界");
		BadWordUtil.init(words);
		assertTrue("aabef*cdee*h你好**234$%^&sd*".equals(BadWordUtil.filter(source, "*")));
		assertTrue(BadWordUtil.hasBadWords(source));
	}

}