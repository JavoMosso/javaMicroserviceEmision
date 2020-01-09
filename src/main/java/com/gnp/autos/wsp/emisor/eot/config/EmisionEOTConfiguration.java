package com.gnp.autos.wsp.emisor.eot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.transport.http.ClientHttpRequestMessageSender;

import com.gnp.autos.wsp.emisor.eot.soap.service.CancelacionClient;
import com.gnp.autos.wsp.emisor.eot.soap.service.ConsultaClient;
import com.gnp.autos.wsp.emisor.eot.soap.service.EmisionEOTClient;
import com.gnp.autos.wsp.emisor.eot.soap.service.EmitirEOTClient;
import com.gnp.autos.wsp.emisor.eot.soap.service.FoliadorClient;
import com.gnp.autos.wsp.emisor.eot.soap.service.MovimientoEOTClient;
import com.gnp.autos.wsp.emisor.eot.soap.service.PasosClient;
import com.gnp.autos.wsp.emisor.eot.soap.service.ProdTecComClient;
import com.gnp.autos.wsp.emisor.eot.soap.service.RegionTarifClient;

/**
 * The Class EmisionEOTConfiguration.
 */
@Configuration
public class EmisionEOTConfiguration {
    /** The time soap. */
    @Value("${wsp_timeout_soap}")
    private Integer timeSoap;

    /** The url foliador. */
    @Value("${wsp_url_urlFoliador}")
    private String urlFoliador;

    /** The url consulta CP. */
    @Value("${wsp_url_ConsultaCP}")
    private String urlConsultaCP;

    /** The url prod tec com. */
    @Value("${wsp_url_prodTecCom}")
    private String urlProdTecCom;

    /** The url pasos. */
    @Value("${wsp_url_urlPasos}")
    private String urlPasos;

    /** The url emision EOT. */
    @Value("${wsp_url_EmisionEOT}")
    private String urlEmisionEOT;

    /** The url consulta pol. */
    @Value("${wsp_url_ConsultaPol}")
    private String urlConsultaPol;

    /** The url cancelacion. */
    @Value("${wsp_url_Cancelacion}")
    private String urlCancelacion;

    /**
     * Gets the registrar emision.
     *
     * @param marshaller the marshaller
     * @return the registrar emision
     */
    @Bean
    public EmisionEOTClient getRegistrarEmision(final Jaxb2Marshaller marshaller) {
        EmisionEOTClient client = new EmisionEOTClient();
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

    /**
     * Rest template.
     *
     * @return the rest template
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Http components message sender.
     *
     * @return the client http request message sender
     */
    @Bean
    public ClientHttpRequestMessageSender httpComponentsMessageSender() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(timeSoap);
        requestFactory.setReadTimeout(timeSoap);
        return new ClientHttpRequestMessageSender(requestFactory);
    }

    /**
     * Marshaller foliador.
     *
     * @return the jaxb 2 marshaller
     */
    @Bean
    @Qualifier("foliador")
    public Jaxb2Marshaller marshallerFoliador() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.gnp.autos.wsp.emisor.eot.foliador.wsdl");
        return marshaller;
    }

    /**
     * Marshaller region tar.
     *
     * @return the jaxb 2 marshaller
     */
    @Bean
    @Qualifier("regionTar")
    public Jaxb2Marshaller marshallerRegionTar() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.gnp.autos.wsp.emisor.eot.calcp.wsdl");
        return marshaller;
    }

    /**
     * Marshaller prod tec com.
     *
     * @return the jaxb 2 marshaller
     */
    @Bean
    @Qualifier("prodTecCom")
    public Jaxb2Marshaller marshallerProdTecCom() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.gnp.autos.wsp.emisor.eot.TecCom.wsdl");
        return marshaller;
    }

    /**
     * Marshaller pasos.
     *
     * @return the jaxb 2 marshaller
     */
    @Bean
    @Qualifier("pasos")
    public Jaxb2Marshaller marshallerPasos() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.gnp.autos.wsp.emisor.eot.pasos.wsdl");
        return marshaller;
    }

    /**
     * Marshaller movimiento EOT.
     *
     * @return the jaxb 2 marshaller
     */
    @Bean
    @Qualifier("movimientoEOT")
    public Jaxb2Marshaller marshallerMovimientoEOT() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.gnp.autos.wsp.emisor.eot.movimiento.wsdl");
        return marshaller;
    }

    /**
     * Marshaller emision EOT.
     *
     * @return the jaxb 2 marshaller
     */
    @Bean
    @Qualifier("emisionEOT")
    @Primary
    public Jaxb2Marshaller marshallerEmisionEOT() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.gnp.autos.wsp.emisor.eot.wsdl");
        return marshaller;
    }

    /**
     * Marshaller consulta EOT.
     *
     * @return the jaxb 2 marshaller
     */
    @Bean
    @Qualifier("consultaEOT")
    public Jaxb2Marshaller marshallerConsultaEOT() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.gnp.autos.wsp.emisor.eot.consulta.wsdl");
        return marshaller;
    }

    /**
     * Marshaller cancelacion EOT.
     *
     * @return the jaxb 2 marshaller
     */
    @Bean
    @Qualifier("cancelacionEOT")
    public Jaxb2Marshaller marshallerCancelacionEOT() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.gnp.autos.wsp.emisor.eot.cancelacion.wsdl");
        return marshaller;
    }

    /**
     * Gets the foliador client.
     *
     * @param marshaller the marshaller
     * @param httpComponentsMessageSender the http components message sender
     * @return the foliador client
     */
    @Bean
    public FoliadorClient getFoliadorClient(@Qualifier("foliador") final Jaxb2Marshaller marshaller,
            final ClientHttpRequestMessageSender httpComponentsMessageSender) {
        FoliadorClient client = new FoliadorClient();
        client.setDefaultUri(urlFoliador);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(httpComponentsMessageSender);
        return client;
    }

    /**
     * Gets the region tarificacion.
     *
     * @param marshaller the marshaller
     * @param httpComponentsMessageSender the http components message sender
     * @return the region tarificacion
     */
    @Bean
    public RegionTarifClient getRegionTarificacion(@Qualifier("regionTar") final Jaxb2Marshaller marshaller,
            final ClientHttpRequestMessageSender httpComponentsMessageSender) {
        RegionTarifClient client = new RegionTarifClient();
        client.setDefaultUri(urlConsultaCP);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(httpComponentsMessageSender);
        return client;
    }

    /**
     * Gets the pro tec com.
     *
     * @param marshaller the marshaller
     * @param httpComponentsMessageSender the http components message sender
     * @return the pro tec com
     */
    @Bean
    public ProdTecComClient getProTecCom(@Qualifier("prodTecCom") final Jaxb2Marshaller marshaller,
            final ClientHttpRequestMessageSender httpComponentsMessageSender) {
        ProdTecComClient client = new ProdTecComClient();
        client.setDefaultUri(urlProdTecCom);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(httpComponentsMessageSender);
        return client;
    }

    /**
     * Gets the pasos.
     *
     * @param marshaller the marshaller
     * @param httpComponentsMessageSender the http components message sender
     * @return the pasos
     */
    @Bean
    public PasosClient getPasos(@Qualifier("pasos") final Jaxb2Marshaller marshaller,
            final ClientHttpRequestMessageSender httpComponentsMessageSender) {
        PasosClient client = new PasosClient();
        client.setDefaultUri(urlPasos);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(httpComponentsMessageSender);
        return client;
    }

    /**
     * Gets the movimiento EOT.
     *
     * @param marshaller the marshaller
     * @param httpComponentsMessageSender the http components message sender
     * @return the movimiento EOT
     */
    @Bean
    public MovimientoEOTClient getMovimientoEOT(@Qualifier("movimientoEOT") final Jaxb2Marshaller marshaller,
            final ClientHttpRequestMessageSender httpComponentsMessageSender) {
        MovimientoEOTClient client = new MovimientoEOTClient();
        client.setDefaultUri(urlPasos);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(httpComponentsMessageSender);
        return client;
    }

    /**
     * Gets the emision EOT.
     *
     * @param marshaller the marshaller
     * @param httpComponentsMessageSender the http components message sender
     * @return the emision EOT
     */
    @Bean
    public EmitirEOTClient getEmisionEOT(@Qualifier("emisionEOT") final Jaxb2Marshaller marshaller,
            final ClientHttpRequestMessageSender httpComponentsMessageSender) {
        EmitirEOTClient client = new EmitirEOTClient();
        client.setDefaultUri(urlEmisionEOT);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(httpComponentsMessageSender);
        return client;
    }

    /**
     * Gets the consulta EOT.
     *
     * @param marshaller the marshaller
     * @param httpComponentsMessageSender the http components message sender
     * @return the consulta EOT
     */
    @Bean
    public ConsultaClient getConsultaEOT(@Qualifier("consultaEOT") final Jaxb2Marshaller marshaller,
            final ClientHttpRequestMessageSender httpComponentsMessageSender) {
        ConsultaClient client = new ConsultaClient();
        client.setDefaultUri(urlConsultaPol);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(httpComponentsMessageSender);
        return client;
    }

    /**
     * Gets the cancelacion EOT.
     *
     * @param marshaller the marshaller
     * @param httpComponentsMessageSender the http components message sender
     * @return the cancelacion EOT
     */
    @Bean
    public CancelacionClient getCancelacionEOT(@Qualifier("cancelacionEOT") final Jaxb2Marshaller marshaller,
            final ClientHttpRequestMessageSender httpComponentsMessageSender) {
        CancelacionClient client = new CancelacionClient();
        client.setDefaultUri(urlCancelacion);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(httpComponentsMessageSender);
        return client;
    }

}