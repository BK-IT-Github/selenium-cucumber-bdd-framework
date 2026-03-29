package com.bk.automation.core.config;

/**
 * Enum representing supported execution environments.
 * Used by ConfigReader to load environment-specific properties.
 */
public enum Environment {

    QA("qa"),
    STAGING("staging"),
    PROD("prod");

    private final String name;

    Environment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Resolves Environment enum from a string value.
     *
     * @param env the environment name string
     * @return the matching Environment enum
     * @throws IllegalArgumentException if the environment is not supported
     */
    public static Environment fromString(String env) {
        for (Environment environment : Environment.values()) {
            if (environment.getName().equalsIgnoreCase(env)) {
                return environment;
            }
        }
        throw new IllegalArgumentException("Unsupported environment: " + env
                + ". Supported values: qa, staging, prod");
    }
}
