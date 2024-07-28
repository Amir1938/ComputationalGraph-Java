package graph;

import java.util.ArrayList;
import java.util.List;

/**
 * The Topic class represents a topic in a publish-subscribe system, managing subscribers and publishers.
 */
public class Topic {
    public final String name;
    private final List<Agent> subs = new ArrayList<>();
    private final List<Agent> pubs = new ArrayList<>();
    private Message msg = new Message("0");

    /**
     * Constructs a Topic with the specified name.
     *
     * @param name the name of the topic
     */
    Topic(String name) {
        this.name = name;
    }

    /**
     * Subscribes an agent as a listener to the topic.
     *
     * @param a the agent to subscribe
     */
    public void subscribe(Agent a) {
        if (!subs.contains(a)) {
            subs.add(a);
        }
    }

    /**
     * Unsubscribes an agent from the topic.
     *
     * @param a the agent to unsubscribe
     */
    public void unsubscribe(Agent a) {
        subs.remove(a);
    }

    /**
     * Publishes a message to all subscribed agents.
     *
     * @param m the message to publish
     */
    public void publish(Message m) {
        this.msg = m;
        for (Agent a : subs) {
            a.callback(this.name, m);
        }
    }

    /**
     * Returns the list of agents that publish to this topic.
     *
     * @return the list of publishing agents
     */
    public List<Agent> getPubs() {
        return pubs;
    }

    /**
     * Returns the list of agents that subscribe to this topic.
     *
     * @return the list of subscribing agents
     */
    public List<Agent> getSubs() {
        return subs;
    }

    /**
     * Returns the name of the topic.
     *
     * @return the name of the topic
     */
    public String getName() {
        return name;
    }

    /**
     * Adds an agent as a publisher to the topic.
     *
     * @param a the agent to add as a publisher
     */
    public void addPublisher(Agent a) {
        if (!pubs.contains(a)) {
            pubs.add(a);
        }
    }

    /**
     * Returns the current message of the topic.
     *
     * @return the current message
     */
    public Message getMsg() {
        return this.msg;
    }

    /**
     * Removes an agent from the list of publishers.
     *
     * @param a the agent to remove as a publisher
     */
    public void removePublisher(Agent a) {
        pubs.remove(a);
    }
}
