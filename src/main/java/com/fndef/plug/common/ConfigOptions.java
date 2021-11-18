package com.fndef.plug.common;

import com.fndef.plug.ConfigFormat;

public class ConfigOptions {
    private boolean strictLookup;
    private boolean gatherStats;
    private ConfigFormat configFormat;
    private boolean keepResolvables;

    public ConfigOptions(ConfigFormat format) {
        this.configFormat = format;
        strictLookup = true;
    }

    public boolean strictLookup() {
        return strictLookup;
    }

    public boolean gatherStats() {
        return gatherStats;
    }

    public boolean keepResolvables() {
        return keepResolvables;
    }

    public ConfigFormat configFormat() {
        return configFormat;
    }

    public ConfigOptions strictLookup(boolean isStrict) {
        this.strictLookup = isStrict;
        return this;
    }

    public ConfigOptions gatherStats(boolean gatherStats) {
        this.gatherStats = gatherStats;
        return this;
    }

    public ConfigOptions configFormat(ConfigFormat format) {
        this.configFormat = format;
        return this;
    }

    public ConfigOptions keepResolvables(boolean keepResolvables) {
        this.keepResolvables = keepResolvables;
        return this;
    }
}
