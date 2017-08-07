package org.slingerxv.limitart.taskqueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slingerxv.limitart.funcs.Proc1;
import org.slingerxv.limitart.funcs.Proc3;
import org.slingerxv.limitart.funcs.Procs;
import org.slingerxv.limitart.funcs.Test1;
import org.slingerxv.limitart.funcs.Tests;
import org.slingerxv.limitart.taskqueue.define.ITaskQueue;
import org.slingerxv.limitart.taskqueue.exception.TaskQueueException;
import org.slingerxv.limitart.thread.NamedThreadFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 消息队列线程
 * 
 * @author Hank
 *
 */
public class DisruptorTaskQueue<T> implements ITaskQueue<T> {
	private static Logger log = LogManager.getLogger();
	private Disruptor<DisruptorTaskQueueEvent> disruptor;
	private NamedThreadFactory threadFactory;
	private Test1<T> intercept;
	private Proc1<T> handle;
	private Proc3<DisruptorTaskQueueEvent, Throwable, Long> exception;

	public DisruptorTaskQueue(String threadName) {
		this(threadName, 2 << 12);//4096
	}

	/**
	 * 构造函数
	 * 
	 * @param threadName
	 * @param bufferSize
	 *            指定RingBuffer的大小
	 * @param handler
	 */
	@SuppressWarnings("unchecked")
	public DisruptorTaskQueue(String threadName, int bufferSize) {
		this.threadFactory = new NamedThreadFactory() {

			@Override
			public String getThreadName() {
				return threadName;
			}
		};
		disruptor = new Disruptor<>(DisruptorTaskQueueEvent::new, bufferSize, this.threadFactory, ProducerType.MULTI,
				new BlockingWaitStrategy());
		disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
			if (Tests.invoke(DisruptorTaskQueue.this.intercept, event.getMsg())) {
				return;
			}
			try {
				Procs.invoke(DisruptorTaskQueue.this.handle, event.getMsg());
			} catch (Exception e) {
				log.error(e, e);
			} finally {
				event.setMsg(null);
			}
		});
		// prevent Worker Threads from dying
		disruptor.setDefaultExceptionHandler(new ExceptionHandler<DisruptorTaskQueueEvent>() {

			@Override
			public void handleEventException(Throwable ex, long sequence, DisruptorTaskQueueEvent event) {
				log.error("sequence " + sequence + " error!", ex);
				Procs.invoke(exception, event, ex, sequence);
			}

			@Override
			public void handleOnStartException(final Throwable ex) {
				log.error("Exception during onStart()", ex);
			}

			@Override
			public void handleOnShutdownException(final Throwable ex) {
				log.error("Exception during onShutdown()", ex);
			}
		});
	}

	public ITaskQueue<T> intercept(Test1<T> intercept) {
		this.intercept = intercept;
		return this;
	}

	public ITaskQueue<T> handle(Proc1<T> handle) {
		this.handle = handle;
		return this;
	}

	public ITaskQueue<T> exception(Proc3<DisruptorTaskQueueEvent, Throwable, Long> exception) {
		this.exception = exception;
		return this;
	}

	@Override
	public void startServer() {
		disruptor.start();
		log.info("thread " + threadFactory.getThreadName() + " start!");
	}

	@Override
	public void stopServer() {
		if (disruptor != null) {
			disruptor.shutdown();
			log.info("thread " + threadFactory.getThreadName() + " stop!");
			disruptor = null;
			threadFactory = null;
		}
	}

	@Override
	public void addCommand(T command) throws TaskQueueException {
		if (this.disruptor == null) {
			throw new TaskQueueException(getThreadName() + " has not start yet!");
		}
		disruptor.getRingBuffer().publishEvent((event, sequence) -> event.setMsg(command));
	}

	@Override
	public String getThreadName() {
		return threadFactory.getThreadName();
	}

	private class DisruptorTaskQueueEvent {
		private T msg;

		public T getMsg() {
			return msg;
		}

		public void setMsg(T msg) {
			this.msg = msg;
		}
	}
}
