package configs;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;

import java.util.Objects;

/**
 * The MinAgent class implements the Agent interface and calculates the difference between two values received from subscribed topics.
 */
public class MinAgent implements Agent {

    private final String name;
    private Double x;
    private Double y;
    private final String[] subs;
    private final String[] pubs;
    private static int counter = 0;

    /**
     * Constructs a MinAgent with the specified subscription and publication topics.
     *
     * @param subs the subscription topics (must have exactly 2 elements)
     * @param pubs the publication topics
     * @throws PlusAgent.InvalidSubsLengthException if the subs array does not have exactly 2 elements
     */
    public MinAgent(String[] subs, String[] pubs) {
        if (subs.length != 2) {
            throw new PlusAgent.InvalidSubsLengthException("Error: 'subs' array must have exactly 2 elements.");
        }

        counter++;
        this.pubs = pubs;
        this.subs = subs;
        this.name = ("-" + counter);

        // Subscribe to the first two topics from subs
        TopicManagerSingleton.get().getTopic(subs[0]).subscribe(this);
        TopicManagerSingleton.get().getTopic(subs[1]).subscribe(this);

        // Initialize x and y to 0.0
        this.x = 0.0;
        this.y = 0.0;

        // Add this agent as a publisher to the publication topics
        for (String pub : this.pubs) {
            TopicManagerSingleton.get().getTopic(pub).addPublisher(this);
        }
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
     * Resets the agent's state by setting the input values to 0.0.
     */
    @Override
    public void reset() {
        this.x = 0.0;
        this.y = 0.0;
    }

    /**
     * Callback method that is invoked when a message is received on a subscribed topic.
     * Calculates the difference between the two input values and publishes it to the first publication topic.
     *
     * @param topic the topic on which the message was received
     * @param msg the message received
     */
    @Override
    public void callback(String topic, Message msg) {
        if (Objects.equals(topic, subs[0])) {
            this.x = msg.asDouble;
        }
        if (Objects.equals(topic, subs[1])) {
            this.y = msg.asDouble;
        }

        if (this.x != null && this.y != null) {
            TopicManagerSingleton.get().getTopic(this.pubs[0]).publish(new Message(x - y));
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
