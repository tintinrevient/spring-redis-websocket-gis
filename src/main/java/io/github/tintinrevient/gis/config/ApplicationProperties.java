package io.github.tintinrevient.gis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "raw", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Info info = new Info();

    public static class Info {
        private String message;

								public String getMessage() { return message; }
					   public void setMessage(String message) { this.message = message; }

				}

    public Info getInfo() {
        return info;
    }
}
