package de.geoinfoffm.registry.client.web;

import de.geoinfoffm.registry.HibernateAwareObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(MappingJackson2HttpMessageConverter jsonMapper) {
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(jsonMapper);
        return template;
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonMapper() {
        MappingJackson2HttpMessageConverter jsonMapper = new MappingJackson2HttpMessageConverter();
        jsonMapper.setObjectMapper(new HibernateAwareObjectMapper());
        return jsonMapper;
    }

//    @Bean
//    public RestTemplate restTemplate(ClientHttpRequestFactory httpRequestFactory) {
//        RestTemplate template = new RestTemplate(httpRequestFactory);
//        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        return template;
//    }
//
//    @Bean
//    public ClientHttpRequestFactory httpRequestFactory(HttpClient httpClient) {
//        return new HttpComponentsClientHttpRequestFactory(httpClient);
//    }
//
//    @Bean
//    public HttpClient httpClient() {
//        return HttpClientBuilder.create().build();
//    }
}