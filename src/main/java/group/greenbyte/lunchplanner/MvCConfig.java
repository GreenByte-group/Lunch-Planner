package group.greenbyte.lunchplanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Configuration
public class MvCConfig extends WebMvcConfigurerAdapter {

    private final Environment env;

    private String uploadsDirName;

    @Autowired
    public MvCConfig(Environment env) {
        this.env = env;

        uploadsDirName = this.env.getProperty("upload.location");
        if(uploadsDirName == null)
            uploadsDirName = "/tmp";
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("PUT", "GET", "POST", "OPTIONS", "DELETE").allowedOrigins("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/static/**").addResourceLocations("file:" + uploadsDirName);
    }
}
