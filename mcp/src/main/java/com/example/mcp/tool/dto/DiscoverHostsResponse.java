/**
 * DTO representing the response for the Nmap 'discover hosts' operation.
 * <p>
 * Used to return a list of discovered hosts (IP addresses or hostnames) to the client.
 * If an error occurs, the list may contain a single string describing the error.
 */
package com.example.mcp.tool.dto;
import java.util.List;

public class DiscoverHostsResponse {
    /**
     * List of discovered hosts (IP addresses or hostnames).
     * If an error occurs, this may contain a single error message string.
     */
    private List<String> hosts;

    /**
     * Default constructor for serialization/deserialization.
     */
    public DiscoverHostsResponse() {}

    /**
     * Constructs a response with the given list of hosts.
     *
     * @param hosts List of discovered hosts, or a single error message string if an error occurred.
     */
    public DiscoverHostsResponse(List<String> hosts) { this.hosts = hosts; }

    /**
     * Returns the list of discovered hosts, or a single error message string if an error occurred.
     *
     * @return List of discovered hosts or error message.
     */
    public List<String> getHosts() { return hosts; }

    /**
     * Sets the list of discovered hosts, or a single error message string if an error occurred.
     *
     * @param hosts List of discovered hosts or error message.
     */
    public void setHosts(List<String> hosts) { this.hosts = hosts; }
}
