package hello;

import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    EmbeddedServletContainerCustomizer containerCustomizer() throws Exception {

        return (ConfigurableEmbeddedServletContainer container) -> {

            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
                tomcat.addConnectorCustomizers(
                        (connector) -> {
                            ((AbstractProtocol) connector.getProtocolHandler()).setAcceptCount(0);
                            ((AbstractHttp11Protocol) connector.getProtocolHandler()).setMaxKeepAliveRequests(0);
                        }
                );
            }
        };
    }
}