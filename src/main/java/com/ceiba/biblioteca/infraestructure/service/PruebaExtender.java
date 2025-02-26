//package com.ceiba.biblioteca.infraestructure.service;
//
//import com.ceiba.biblioteca.conectorCliente.MQ.principal.ConectorCliente;
//import com.ceiba.biblioteca.domain.model.*;
//import com.ceiba.biblioteca.domain.model.gateway.IPrestamoService;
//import com.ceiba.biblioteca.infraestructure.repository.IPrestamoRepository;
//import com.ibm.mq.*;
//import com.ibm.mq.constants.CMQC;
//import com.ibm.mq.constants.MQConstants;
//import com.ibm.mq.jms.MQQueueConnectionFactory;
//import com.ibm.msg.client.wmq.WMQConstants;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.util.UriComponentsBuilder;
//import reactor.core.publisher.Mono;
//
//import javax.jms.JMSException;
//import javax.jms.QueueConnectionFactory;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.net.URI;
//import java.util.List;
//
//@Service
//public class PruebaExtender implements IPrestamoService {
//
//    @Autowired
//    private IPrestamoRepository prestamoRepository;
//
//    @Autowired
//    private WebClient webClient;
//
//    protected MQQueueManager queueManager = null;
//    protected MQPoolToken token = null;
//    protected QueueConnectionFactory mqConnFactory = null;
//
//
//
//    @Override
//    public Mono<String> pruebaServicio(Long id) throws Exception {
//
//        return messagePut("Prueba", 1, 2);
//    }
//
//
//    private List<Prestamo> consultarByIdUsuario(String idUsuario) {
//        return prestamoRepository.findByidentificacionUsuario(idUsuario);
//    }
//
//
//
//    protected Mono<String> messagePut(String queueReqName, int aleatorio, int priority) throws Exception {
//        //creamos el mensaje basico
//        MQMessage mqMessage = new MQMessage();
//        mqMessage.clearMessage();
//        mqMessage.messageId = CMQC.MQMI_NONE;
//        mqMessage.format = CMQC.MQFMT_STRING;
//        mqMessage.applicationIdData = "123" + "-" + "456";
//        mqMessage.priority = priority;
//
//        MQPutMessageOptions pmo = new MQPutMessageOptions();
//        pmo.options = CMQC.MQOO_SET_IDENTITY_CONTEXT;
//
//        //manejamos los grupos de mensajes
//        pmo.options += CMQC.MQPMO_SYNCPOINT;
//        messageGroupSettings(aleatorio, mqMessage);
//
//        mqMessage.persistence = CMQC.MQPER_NOT_PERSISTENT;
//        //seteamos la expiracion del mensaje
//        mqMessage.expiry = CMQC.MQEI_UNLIMITED;
//
//        mqMessage.messageType = CMQC.MQMT_DATAGRAM;
//
//        mqMessage.writeString("Este es un mensaje en formato texto.");
//
//        //  initConnection("*GCSSVD01.D.QM","SUMMER.CH1","brokerbmd",1414);
//        initConnection2("*GCSSVD01.D.QM","SUMMER.CH1","brokerbmd",1414);
//
//
//        ConectorCliente.sendAsynchronousMessageToESB("servicio","pais","compania","cadena","sucursal","numeroTransaccion","hostOrigen","token", "datos","nombreAplicacion");
//
//        return validate(mqMessage);
//    }
//
//    public void messageGroupSettings(int aleatorio, Object message) {
//        MQMessage mqMessage = (MQMessage) message;
//        mqMessage.messageFlags = CMQC.MQMF_LAST_MSG_IN_GROUP;
//        mqMessage.correlationId = "this.parametrosCliente.getGrupoMensaje()".getBytes();
//        mqMessage.groupId = new String("this.parametrosCliente.getNumeroTransaccion()" + aleatorio).getBytes();
//    }
//
//    private Mono<String> validate(MQMessage mensajeClient) throws IOException {
//        String formato = mensajeClient.format;
//        int prioridad = mensajeClient.priority;
//        int type = mensajeClient.messageType;
//        byte[] correlationId = mensajeClient.correlationId;
//        String replyToQueueName = mensajeClient.replyToQueueName;
//        int deliveryMode = mensajeClient.persistence;
//
//        // InputStream inputStream = XmlReader.class.getClassLoader().getResourceAsStream("prueba2.xml");
//
//        String directoryPath = "src\\main\\resources\\prueba2.xml";
//
//        // Crear un objeto File para el directorio
//        File directory = new File(directoryPath);
//
//        BufferedReader reader = new BufferedReader(new FileReader(directory));
//        StringBuilder line = new StringBuilder();
//        String currentLine;
//        while ((currentLine = reader.readLine()) != null) {
//            line.append(currentLine).append("\n");  // Concatenar cada línea con un salto de línea
//        }
//
//
//        ProtocoloAMPQ protocoloAMPQ = ProtocoloAMPQ.builder()
//                .header(Header.builder().deliveryMode(deliveryMode).priority(prioridad).build())
//                .properties(Properties.builder().contentType(type).correlationId(correlationId).replyTo(replyToQueueName).build())
//                .body(Body.builder().xml("prueba").build())
//                .build();
//
//        return enviarXml(protocoloAMPQ, webClient);
//
//    }
//
//    public Mono<String> enviarXml(ProtocoloAMPQ protocoloAMPQ, WebClient webClient) {
//
//
//        URI uri = UriComponentsBuilder
//                .fromHttpUrl("http://localhost:8081/prestamo") // Asegúrate de que esta URL es correcta
//                .build().toUri();
//
//        return webClient.post()
//                .uri(uri)
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .body(BodyInserters.fromValue(protocoloAMPQ))
//                .retrieve()
//                .bodyToMono(String.class);
//    }
//
//    public void initConnection(String queueManagerName, String channel, String host, int port) throws MQException {
//
//        MQEnvironment.hostname = host;
//        MQEnvironment.port = port;
//        MQEnvironment.channel = channel;
//        MQEnvironment.properties.put(MQConstants.TRANSPORT_PROPERTY, MQConstants.TRANSPORT_MQSERIES);
//
//        try {
//            //   token = MQEnvironment.addConnectionPoolToken();
//            queueManager = new MQQueueManager(queueManagerName);
//            System.out.println("Conectado al Queue Manager: " + queueManagerName);
//        } catch (MQException e) {
//            //log.fatal("Error al conectarse al queue manager " + queueManagerName,e);
//            throw e;
//        }
//
//
//    }
//
//    public void initConnection2(String queueManagerName, String channel, String host, int port) throws JMSException {
//        MQQueueConnectionFactory temp = new MQQueueConnectionFactory();
//        temp.setTransportType(WMQConstants.WMQ_CM_CLIENT);
//        temp.setHostName("localhost");
//        // temp.setQueueManager(queueManagerName);
//        temp.setPort(1414);
//        // temp.setChannel(channel);
//        // this.mqConnFactory = temp;
//    }
//
//}
