package configs;

import java.util.Objects;
import java.util.function.BinaryOperator;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;

/**
 * The BinOpAgent class implements the Agent interface and performs binary operations on messages received from two input topics.
 */
public class BinOpAgent implements Agent {
	private final String name;
	private final String inputTopic1;
	private final String inputTopic2;
	private final String outputTopic;
	private final BinaryOperator<Double> operation;
	private Double x;
	private Double y;

	/**
	 * Constructs a BinOpAgent with the specified name, input topics, output topic, and binary operation.
	 *
	 * @param name the name of the agent
	 * @param inputTopic1 the first input topic
	 * @param inputTopic2 the second input topic
	 * @param outputTopic the output topic
	 * @param operation the binary operation to be performed on the input values
	 */
	public BinOpAgent(String name, String inputTopic1, String inputTopic2, String outputTopic, BinaryOperator<Double> operation) {
		this.name = name;
		this.inputTopic1 = inputTopic1;
		this.inputTopic2 = inputTopic2;
		this.outputTopic = outputTopic;
		this.operation = operation;

		TopicManagerSingleton.get().getTopic(inputTopic1).subscribe(this);
		TopicManagerSingleton.get().getTopic(inputTopic2).subscribe(this);
		TopicManagerSingleton.get().getTopic(this.outputTopic);
		TopicManagerSingleton.get().getTopic(this.outputTopic).addPublisher(this);
	}

	/**
	 * Returns the name of the agent.
	 *
	 * @return the name of the agent
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Resets the agent's state by setting the input values to null.
	 */
	@Override
	public void reset() {
		this.x = null;
		this.y = null;
	}

	/**
	 * Callback method that is invoked when a message is received on a subscribed topic.
	 * If both input values are available, performs the binary operation and publishes the result to the output topic.
	 *
	 * @param topic the topic on which the message was received
	 * @param msg the message received
	 */
	@Override
	public void callback(String topic, Message msg) {
		if (Objects.equals(topic, inputTopic1)) {
			this.x = msg.asDouble;
		}
		if (Objects.equals(topic, inputTopic2)) {
			this.y = msg.asDouble;
		}
		if (x != null && y != null) {
			Double result = this.operation.apply(x, y);
			TopicManagerSingleton.get().getTopic(this.outputTopic).publish(new Message(result));
		}
	}

	/**
	 * Closes the agent and releases any resources held by it.
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
	}
}
