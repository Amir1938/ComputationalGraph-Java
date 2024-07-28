package graph;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The ParallelAgent class implements the Agent interface and processes messages in parallel using a blocking queue.
 */
public class ParallelAgent implements Agent {
	private final Agent agent;
	private final BlockingQueue<MessageWithTopic> messageQueue;
	private volatile boolean stop = false;
	private Thread t;

	/**
	 * Constructs a ParallelAgent with the specified agent and queue capacity.
	 *
	 * @param agent the agent to be wrapped
	 * @param capacity the capacity of the blocking queue
	 */
	public ParallelAgent(Agent agent, int capacity) {
		this.agent = agent;
		this.messageQueue = new ArrayBlockingQueue<>(capacity);
		startMessageProcessing();
	}

	/**
	 * Constructs a ParallelAgent with the specified agent, queue capacity, and name.
	 *
	 * @param agent the agent to be wrapped
	 * @param capacity the capacity of the blocking queue
	 * @param name the name of the agent (not used in this implementation)
	 */
	public ParallelAgent(Agent agent, int capacity, String name) {
		this.agent = agent;
		this.messageQueue = new ArrayBlockingQueue<>(capacity);
		startMessageProcessing();
	}

	/**
	 * Returns the name of the agent.
	 *
	 * @return the name of the agent
	 */
	@Override
	public String getName() {
		return "";
	}

	/**
	 * Resets the agent's state.
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	/**
	 * Callback method that is invoked when a message is received on a subscribed topic.
	 * Puts the message into the blocking queue for processing.
	 *
	 * @param topic the topic on which the message was received
	 * @param msg the message received
	 */
	@Override
	public void callback(String topic, Message msg) {
		try {
			messageQueue.put(new MessageWithTopic(topic, msg));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Starts the message processing thread.
	 */
	public void startMessageProcessing() {
		Thread t = new Thread(() -> {
			while (!stop) {
				try {
					MessageWithTopic msgt = messageQueue.take();
					String topic = msgt.getTopicName();
					Message originalMessage = msgt.getMessage();
					agent.callback(topic, originalMessage);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});
		t.start();
		this.t = t;
	}

	/**
	 * Closes the agent and stops the message processing thread.
	 */
	@Override
	public void close() {
		stop = true;
		t.interrupt();
	}

	/**
	 * The MessageWithTopic class represents a message with its associated topic.
	 */
	private static class MessageWithTopic {
		private final String topic;
		private final Message message;

		/**
		 * Constructs a MessageWithTopic with the specified topic and message.
		 *
		 * @param topic the topic of the message
		 * @param message the message
		 */
		public MessageWithTopic(String topic, Message message) {
			this.topic = topic;
			this.message = message;
		}

		/**
		 * Returns the topic of the message.
		 *
		 * @return the topic of the message
		 */
		public String getTopicName() {
			return this.topic;
		}

		/**
		 * Returns the message.
		 *
		 * @return the message
		 */
		public Message getMessage() {
			return message;
		}
	}
}
