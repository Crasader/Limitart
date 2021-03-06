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
package top.limitart.mapping;


import top.limitart.base.Func;
import top.limitart.base.Func1;
import top.limitart.base.Proc1;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * 路由器
 *
 * @author hank
 * @version 2018/10/8 0008 20:08
 */
public interface Router<M, C extends RequestContext<M>> {

    /**
     * 创造一个空的消息工厂
     *
     * @return
     */
    static <M, C extends RequestContext<M>> Router empty(Class<M> mClass, Class<C> cClass) {
        return RouterImpl.empty(mClass, cClass);
    }

    /**
     * 通过包扫描创建消息工厂
     *
     * @param scanPackage 包名
     * @return
     * @throws ReflectiveOperationException
     * @throws IOException
     */
    static <M, C extends RequestContext<M>>
    Router create(Class<M> mClass, Class<C> cClass, String scanPackage) throws RequestDuplicatedException, ReflectiveOperationException, IOException {
        return RouterImpl.create(mClass, cClass, scanPackage);
    }

    /**
     * 通过扫描包创建消息工厂
     *
     * @param scanPackage
     * @param confirmInstance 指定manager的 实例
     * @return
     * @throws IOException
     * @throws ReflectiveOperationException
     */
    static <M, C extends RequestContext<M>> Router create(Class<M> mClass, Class<C> cClass, String scanPackage, Func1<Class<?>, Object> confirmInstance) throws Exception {
        return RouterImpl.create(mClass, cClass, scanPackage, confirmInstance);
    }

    /**
     * 注册一个manager
     *
     * @param mapperClass
     */
    Router<M, C> registerMapperClass(Class<?> mapperClass) throws Exception;

    /**
     * 注册一个manager
     *
     * @param mapperClass     类
     * @param confirmInstance 指定manager的实例
     */
    Router<M, C> registerMapperClass(Class<?> mapperClass, Func1<Class<?>, Object> confirmInstance) throws Exception;

    /**
     * 替换掉manager的实例
     *
     * @param request
     * @param newInstance
     */
    void replaceInstance(Class<?> mapperClass, M request, Object newInstance) throws Exception;

    void foreachRequestClass(Consumer<Class<M>> consumer);

    /**
     * 根据ID获取一个消息实例
     *
     * @param requestClass
     * @return
     * @throws ReflectiveOperationException
     */
    M requestInstance(Class<M> requestClass) throws Exception;

    void request(M request, Func<C> contextInstance, Proc1<MethodInvoker> proc);

    interface MethodInvoker {
        void invoke();
    }
}
