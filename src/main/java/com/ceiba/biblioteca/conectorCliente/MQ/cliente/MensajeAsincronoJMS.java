package com.ceiba.biblioteca.conectorCliente.MQ.cliente;

import com.ceiba.biblioteca.conectorCliente.MQ.contexto.ParametrosMensajeCliente;
import com.ceiba.biblioteca.infraestructure.service.HttpService;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.ParametrosConfiguracionMQ;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.jms.JMSQueueManagerContext;
import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.jms.MQQueue;
import com.ibm.msg.client.jms.JmsConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.JMSException;
import java.io.Serializable;

/**
 * Clase que implementa el envio de mensajes As�ncronos en modalidad cliente a trav�s de JMS.
 *
 * @author echicas
 * @version 2.0
 */
public class MensajeAsincronoJMS extends EnviarMensajeJMS implements Serializable {

    private static final long serialVersionUID = 4295280638223594481L;
    private static Log log = LogFactory.getLog(MensajeAsincronoJMS.class);
    private HttpService httpService;

    public MensajeAsincronoJMS(JMSQueueManagerContext jmsContext, ParametrosConfiguracionMQ parametros, ParametrosMensajeCliente headerCliente, String datosCliente) throws JMSException {
        super(jmsContext, parametros, headerCliente, datosCliente);
    }

    public int enviarMensajeJMS(int aleatorio, int priority) {
        try {
            messagePutJMS(getQueueNameReq(), null, aleatorio, priority);
        } catch (Exception e) {
            //throw e;
            //throw new RuntimeException(e.getLinkedException()!=null?e.getLinkedException():e);
        }
        return 0;
    }

    protected void messageGetJMS(String queueRespName) {
        throw new UnsupportedOperationException("Operaci�n no permitida en envios asincronos");
    }

    protected void messagePutJMS(String queueReqName, String replyToQueueName, int aleatorio, int priority) throws Exception {
        //	QueueSender sender = null;

        //creamos el acceso a la cola
        MQQueue queue = new MQQueue();
        queue.setBooleanProperty(MQConstants.WMQ_MQMD_WRITE_ENABLED, true);
        //verificamos la persistencia del mensaje
        if (this.parametrosCliente.isMensajePersistente()) {
            queue.setPersistence(JmsConstants.DELIVERY_PERSISTENT);
        } else {
            queue.setPersistence(JmsConstants.DELIVERY_NOT_PERSISTENT);
        }

        //establecemos prioridad y otros valores por defecto para la interaccion con la cola
        queue.setPriority(priority);
        setQueueDefaults(queue);

        //creamos el mensaje
        JMSTextMessage message = new JMSTextMessage();

        //a�adimos las propiedades de usuario
        addProperties2JMSMessage(message);

        //se a�aden las propiedades de grupo
        messageGroupSettings(aleatorio, message);

        //	message.setText(this.datosGCSSIEnvelopeReq);

        //establecemos el tipo de mensaje MQ
        //		message.setIntProperty(JmsConstants.JMS_IBM_MQMD_MSGTYPE, MQConstants.MQMT_DATAGRAM);

        //abrimos el canal para el sender
        //	sender = (QueueSender) jmsContext.getSession().createSender(queue);

        //se asume que 	connection.start(); ya fue ejecutado en el constructor y se cerrar� por parte del usuario
        //se envia el mensaje
        //		sender.send(message);

        log.info("Mensaje enviado a " + this.parametrosCliente.getCodigoServicio());

        httpService = new HttpService();

        httpService.pruebaServicio(this.datosGCSSIEnvelopeReq);
    }

}
