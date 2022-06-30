/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.ballerina.runtime.internal.scheduling;

import io.ballerina.runtime.api.async.Callback;
import io.ballerina.runtime.api.values.BError;
import io.ballerina.runtime.api.values.BFunctionPointer;
import io.ballerina.runtime.api.values.BObject;
import io.ballerina.runtime.internal.types.BFunctionType;
import io.ballerina.runtime.internal.values.FutureValue;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

import static io.ballerina.runtime.api.values.BError.ERROR_PRINT_PREFIX;

/**
 * The registry for runtime dynamic listeners and stop handlers.
 *
 * @since 2201.2.0
 */
public class RuntimeRegistry {

    private final Scheduler scheduler;
    private final Set<BObject> listenerSet = new HashSet<>();
    private final Stack<BFunctionPointer<?, ?>> stopHandlerStack = new Stack<>();

    private static final PrintStream outStream = System.err;

    public RuntimeRegistry(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public synchronized void registerListener(BObject listener) {
        listenerSet.add(listener);
        scheduler.setImmortal(true);
    }

    public synchronized void deregisterListener(BObject listener) {
        listenerSet.remove(listener);
        if (!scheduler.isListenerDeclarationFound() && listenerSet.isEmpty()) {
            scheduler.setImmortal(false);
        }
    }

    public synchronized void registerStopHandler(BFunctionPointer<?, ?> stopHandler) {
        stopHandlerStack.push(stopHandler);
    }

    public synchronized void gracefulStop(Strand strand) {
        Scheduler currentScheduler = strand.scheduler;
        if (listenerSet.isEmpty() && stopHandlerStack.isEmpty()) {
            return;
        }

        if (listenerSet.isEmpty()) {
            invokeStopHandlerFunction(strand, currentScheduler);
        } else {
            Iterator<BObject> iterator = listenerSet.iterator();
            invokeListenerGracefulStop(strand, currentScheduler, iterator);
        }
    }

    private void invokeListenerGracefulStop(Strand strand, Scheduler scheduler, Iterator<BObject> iterator) {
        ListenerCallback callback = new ListenerCallback();
        BObject listener = iterator.next();
        Function<?, ?> func = o ->  listener.call((Strand) ((Object[]) o)[0], "gracefulStop");
        callback.setIterator(iterator);
        callback.setStrand(strand);
        callback.setScheduler(scheduler);
        scheduler.schedule(new Object[1], func, null, callback, null, null,
                null, strand.getMetadata());
    }

    private void invokeStopHandlerFunction(Strand strand, Scheduler scheduler) {
        BFunctionPointer<?, ?> bFunctionPointer = stopHandlerStack.pop();
        StopHandlerCallback callback = new StopHandlerCallback();
        final FutureValue future = scheduler.createFuture(strand, callback, null,
                ((BFunctionType) bFunctionPointer.getType()).retType, null, strand.getMetadata());
        callback.setStrand(strand);
        callback.setScheduler(scheduler);
        scheduler.scheduleLocal(new Object[]{strand}, bFunctionPointer, strand, future);
    }

    /**
     * The callback implementation for runtime dynamic listeners.
     */
    public class ListenerCallback implements Callback {

        private Iterator<BObject> iterator;
        private Strand strand;
        private Scheduler scheduler;

        @Override
        public void notifySuccess(Object result) {
            if (iterator.hasNext()) {
                invokeListenerGracefulStop(strand, scheduler, iterator);
            } else {
                if (stopHandlerStack.isEmpty()) {
                    return;
                }
                invokeStopHandlerFunction(strand, scheduler);
            }
        }

        @Override
        public void notifyFailure(BError error) {
            outStream.println(ERROR_PRINT_PREFIX + error.getPrintableStackTrace());
        }

        public void setStrand(Strand strand) {
            this.strand = strand;
        }

        public void setIterator(Iterator<BObject> iterator) {
            this.iterator = iterator;
        }

        public void setScheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
        }
    }

    /**
     * The callback implementation for stop handlers.
     */
    public class StopHandlerCallback implements Callback {

        private Strand strand;
        private Scheduler scheduler;

        @Override
        public void notifySuccess(Object result) {
            if (stopHandlerStack.isEmpty()) {
                return;
            }
            invokeStopHandlerFunction(strand, scheduler);
        }

        @Override
        public void notifyFailure(BError error) {
            outStream.println(ERROR_PRINT_PREFIX + error.getPrintableStackTrace());
        }

        public void setStrand(Strand strand) {
            this.strand = strand;
        }

        public void setScheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
        }
    }
}
