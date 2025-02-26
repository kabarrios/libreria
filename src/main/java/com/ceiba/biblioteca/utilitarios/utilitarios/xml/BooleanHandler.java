package com.siman.Integracion.utilitarios.xml;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

import java.io.Serializable;

/**
 * The FieldHandler for the boolean class
 */
public class BooleanHandler extends GeneralizedFieldHandler implements Serializable{


	public static final String FALSE_ALIAS = "F";
	private static final String TRUE_ALIAS = "T";
	public static final String FALSE = "false";
	public static final String TRUE = "true";
	private static final long serialVersionUID = -2717745492587830791L;

	/**
     * Creates a new BooleanHandler instance
     */
    public BooleanHandler() {
        super();
    }

    /**
     * This method is used to convert the value when the
     * getValue method is called. The getValue method will
     * obtain the actual field value from given 'parent' object.
     * This convert method is then invoked with the field's
     * value. The value returned from this method will be
     * the actual value returned by getValue method.
     * @param value the object value to convert after performing a get operation
     * @return the converted value.
     */
    public Object convertUponGet(Object value) {
        if(value.toString().equals(TRUE)) return TRUE_ALIAS;
        else if(value.toString().equals(FALSE)) return FALSE_ALIAS;
        return null;
    }


    /**
     * This method is used to convert the value when the
     * setValue method is called. The setValue method will
     * call this method to obtain the converted value.
     * The converted value will then be used as the value to
     * set for the field.
     * @param value the object value to convert before performing a set operation
     * @return the converted value.
     */
    public Object convertUponSet(Object value) {
    	if(((String)value).equals(TRUE_ALIAS)) return new Boolean(TRUE);
    	else if(((String)value).equals(FALSE_ALIAS)) return new Boolean(FALSE);
    	return null;
    }

    /**
     * Returns the class type for the field that this
     * GeneralizedFieldHandler converts to and from. This
     * should be the type that is used in the
     * object model.
     * @return the class type of of the field
     */
    public Class getFieldType() {
        return boolean.class;
    }

    /**
     * Creates a new instance of the object described by
     * this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple
     *  type and cannot be instantiated
     */
    public Object newInstance( Object parent ) throws IllegalStateException{
        //-- Since it's marked as a string...just return null,
        //-- it's not needed.
        return null;
    }

}