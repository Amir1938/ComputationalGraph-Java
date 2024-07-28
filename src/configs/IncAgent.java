package configs;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;

/**
 * The IncAgent class implements the Agent interface and increments the value received from a subscribed topic by 1.
 */
public class IncAgent implements Agent {
	private final String name;
	private double x;
	private String[] subs;
	private String[] pubs;
	private static int counter = 0;

	/**
	 * Constructs an IncAgent with the specified subscription and publication topics.
	 *
	 * @param subs the subscription topics (must have exactly 1 element)
	 * @param pubs the publication topics
	 * @throws InvalidSubsLengthException if the subs array does not have exactly 1 element
	 */
	public IncAgent(String[] subs, String[] pubs) {
		if (subs.length != 1) {
			throw new InvalidSubsLengthException("Error: 'subs' array must have exactly 1 element.");
		}

		counter++;
		this.pubs = pubs;
		this.subs = subs;
		this.name = ("+1" + counter);

		// Subscribe to the first topic from subs
		TopicManagerSingleton.get().getTopic(subs[0]).subscribe(this);

		// Add this agent as a publisher to the first topic from pubs
		TopicManagerSingleton.get().getTopic(pubs[0]).addPublisher(this);
	}

	/**
	 * Returns the name of the agent.
	 *
	 * @return the name of the agent
	 */
	@Override
	public String getName() {
		return name;
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
	 * Increments the received value by 1 and publishes it to the first publication topic.
	 *
	 * @param topic the topic on which the message was received
	 * @param msg the message received
	 */
	@Override
	public void callback(String topic, Message msg) {
		if (topic.equals(subs[0])) {
			this.x = msg.asDouble;
		}
		if (!Double.isNaN(x)) {
			TopicManagerSingleton.get().getTopic(this.pubs[0]).publish(new Message(x + 1));
		}
	}

	/**
	 * Closes the agent and releases any resources held by it.
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

	/**
	 * Exception thrown when the subs array does not have exactly 1 element.
	 */
	public static class InvalidSubsLengthException extends RuntimeException {
		public InvalidSubsLengthException(String message) {
			super(message);
		}
	}
}
