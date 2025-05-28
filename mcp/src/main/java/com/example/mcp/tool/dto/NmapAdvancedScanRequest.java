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
    private List<String> decoys; // For -D
    private Boolean fragmentPackets; // For -f
    private Integer sourcePort; // For --source-port
    private Boolean packetTrace; // For --packet-trace
    private Integer debugLevel; // For -d, -dd, etc.
    private Boolean arpScan; // For -PR
    private Boolean ipProtocolScan; // For -sO
    private Integer minRate; // --min-rate
    private Integer maxRate; // --max-rate
    private Integer hostTimeout; // --host-timeout (ms)
    private Boolean allOutputFormats; // -oA
    private String customOutputFile; // custom output file name (not used in container)
    private List<String> nseCategories; // NSE script categories
    private Boolean updateNseDb; // --script-updatedb
    private Boolean bannerGrab; // --script=banner
    private Integer scanDelay; // --scan-delay (ms)
    private Integer maxScanDelay; // --max-scan-delay (ms)
    private Integer dataLength; // --data-length
    private String customData; // --data, --data-string
    private List<String> excludeHosts; // --exclude
    private String excludeFile; // --excludefile
    private Boolean recordRoute; // --record-route

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
    public List<String> getDecoys() { return decoys; }
    public void setDecoys(List<String> decoys) { this.decoys = decoys; }
    public Boolean getFragmentPackets() { return fragmentPackets; }
    public void setFragmentPackets(Boolean fragmentPackets) { this.fragmentPackets = fragmentPackets; }
    public Integer getSourcePort() { return sourcePort; }
    public void setSourcePort(Integer sourcePort) { this.sourcePort = sourcePort; }
    public Boolean getPacketTrace() { return packetTrace; }
    public void setPacketTrace(Boolean packetTrace) { this.packetTrace = packetTrace; }
    public Integer getDebugLevel() { return debugLevel; }
    public void setDebugLevel(Integer debugLevel) { this.debugLevel = debugLevel; }
    public Boolean getArpScan() { return arpScan; }
    public void setArpScan(Boolean arpScan) { this.arpScan = arpScan; }
    public Boolean getIpProtocolScan() { return ipProtocolScan; }
    public void setIpProtocolScan(Boolean ipProtocolScan) { this.ipProtocolScan = ipProtocolScan; }
    public Integer getMinRate() { return minRate; }
    public void setMinRate(Integer minRate) { this.minRate = minRate; }
    public Integer getMaxRate() { return maxRate; }
    public void setMaxRate(Integer maxRate) { this.maxRate = maxRate; }
    public Integer getHostTimeout() { return hostTimeout; }
    public void setHostTimeout(Integer hostTimeout) { this.hostTimeout = hostTimeout; }
    public Boolean getAllOutputFormats() { return allOutputFormats; }
    public void setAllOutputFormats(Boolean allOutputFormats) { this.allOutputFormats = allOutputFormats; }
    public String getCustomOutputFile() { return customOutputFile; }
    public void setCustomOutputFile(String customOutputFile) { this.customOutputFile = customOutputFile; }
    public List<String> getNseCategories() { return nseCategories; }
    public void setNseCategories(List<String> nseCategories) { this.nseCategories = nseCategories; }
    public Boolean getUpdateNseDb() { return updateNseDb; }
    public void setUpdateNseDb(Boolean updateNseDb) { this.updateNseDb = updateNseDb; }
    public Boolean getBannerGrab() { return bannerGrab; }
    public void setBannerGrab(Boolean bannerGrab) { this.bannerGrab = bannerGrab; }
    public Integer getScanDelay() { return scanDelay; }
    public void setScanDelay(Integer scanDelay) { this.scanDelay = scanDelay; }
    public Integer getMaxScanDelay() { return maxScanDelay; }
    public void setMaxScanDelay(Integer maxScanDelay) { this.maxScanDelay = maxScanDelay; }
    public Integer getDataLength() { return dataLength; }
    public void setDataLength(Integer dataLength) { this.dataLength = dataLength; }
    public String getCustomData() { return customData; }
    public void setCustomData(String customData) { this.customData = customData; }
    public List<String> getExcludeHosts() { return excludeHosts; }
    public void setExcludeHosts(List<String> excludeHosts) { this.excludeHosts = excludeHosts; }
    public String getExcludeFile() { return excludeFile; }
    public void setExcludeFile(String excludeFile) { this.excludeFile = excludeFile; }
    public Boolean getRecordRoute() { return recordRoute; }
    public void setRecordRoute(Boolean recordRoute) { this.recordRoute = recordRoute; }
}
