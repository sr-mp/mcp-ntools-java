package com.example.mcp.tool.dto;
import java.util.List;
import java.util.Map;

public class NmapAdvancedScanRequest {
    private List<String> targets; // IPs, hostnames, or ranges
    private String scanType; // e.g. "SYN", "UDP", "Aggressive", etc.
    private Boolean osDetection;
    private Boolean serviceVersionDetection;
    private Boolean traceroute;
    private Boolean useNseScripts;
    private List<String> nseScripts; // script names
    private Map<String, String> nseScriptArgs; // script argument map
    private String outputFormat; // "xml", "json", "grepable"
    private Integer timing; // 0-5
    private List<String> extraFlags; // any additional nmap flags
    private Boolean ipv6;
    private String ports; // e.g. "80,443,8080"

    // Getters and setters
    public List<String> getTargets() { return targets; }
    public void setTargets(List<String> targets) { this.targets = targets; }
    public String getScanType() { return scanType; }
    public void setScanType(String scanType) { this.scanType = scanType; }
    public Boolean getOsDetection() { return osDetection; }
    public void setOsDetection(Boolean osDetection) { this.osDetection = osDetection; }
    public Boolean getServiceVersionDetection() { return serviceVersionDetection; }
    public void setServiceVersionDetection(Boolean serviceVersionDetection) { this.serviceVersionDetection = serviceVersionDetection; }
    public Boolean getTraceroute() { return traceroute; }
    public void setTraceroute(Boolean traceroute) { this.traceroute = traceroute; }
    public Boolean getUseNseScripts() { return useNseScripts; }
    public void setUseNseScripts(Boolean useNseScripts) { this.useNseScripts = useNseScripts; }
    public List<String> getNseScripts() { return nseScripts; }
    public void setNseScripts(List<String> nseScripts) { this.nseScripts = nseScripts; }
    public Map<String, String> getNseScriptArgs() { return nseScriptArgs; }
    public void setNseScriptArgs(Map<String, String> nseScriptArgs) { this.nseScriptArgs = nseScriptArgs; }
    public String getOutputFormat() { return outputFormat; }
    public void setOutputFormat(String outputFormat) { this.outputFormat = outputFormat; }
    public Integer getTiming() { return timing; }
    public void setTiming(Integer timing) { this.timing = timing; }
    public List<String> getExtraFlags() { return extraFlags; }
    public void setExtraFlags(List<String> extraFlags) { this.extraFlags = extraFlags; }
    public Boolean getIpv6() { return ipv6; }
    public void setIpv6(Boolean ipv6) { this.ipv6 = ipv6; }
    public String getPorts() { return ports; }
    public void setPorts(String ports) { this.ports = ports; }
}
