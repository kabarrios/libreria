package com.ceiba.biblioteca.infraestructure.service;

import com.ceiba.biblioteca.conectorCliente.MQ.principal.ConectorCliente;
import com.ceiba.biblioteca.domain.model.gateway.IPrestamoService;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPoolToken;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PrestamoService implements IPrestamoService {

    protected MQQueueManager queueManager = null;
    protected MQPoolToken token = null;


    @Override
    public Mono<String> pruebaServicio(Long id) throws Exception {
        return messagePut("Prueba", 1, 2);
    }


    public Mono<String> messagePut(String queueReqName, int aleatorio, int priority) throws Exception {
        //creamos el mensaje basico
        MQMessage mqMessage = new MQMessage();
        mqMessage.clearMessage();
        mqMessage.messageId = CMQC.MQMI_NONE;
        mqMessage.format = CMQC.MQFMT_STRING;
        mqMessage.applicationIdData = "123" + "-" + "456";
        mqMessage.priority = priority;

        MQPutMessageOptions pmo = new MQPutMessageOptions();
        pmo.options = CMQC.MQOO_SET_IDENTITY_CONTEXT;

        //manejamos los grupos de mensajes
        pmo.options += CMQC.MQPMO_SYNCPOINT;
        messageGroupSettings(aleatorio, mqMessage);

        mqMessage.persistence = CMQC.MQPER_NOT_PERSISTENT;
        //seteamos la expiracion del mensaje
        mqMessage.expiry = CMQC.MQEI_UNLIMITED;

        mqMessage.messageType = CMQC.MQMT_DATAGRAM;

        mqMessage.writeString("Este es un mensaje en formato texto.");


       ConectorCliente.sendAsynchronousMessageToESB("servicio","pais","compania","cadena","sucursal","numeroTransaccion","hostOrigen","token", "datos","nombreAplicacion");

        return Mono.just("Prueba OK");
    }

    public void messageGroupSettings(int aleatorio, Object message) {
        MQMessage mqMessage = (MQMessage) message;
        mqMessage.messageFlags = CMQC.MQMF_LAST_MSG_IN_GROUP;
        mqMessage.correlationId = "this.parametrosCliente.getGrupoMensaje()".getBytes();
        mqMessage.groupId = new String("this.parametrosCliente.getNumeroTransaccion()" + aleatorio).getBytes();
    }

}
