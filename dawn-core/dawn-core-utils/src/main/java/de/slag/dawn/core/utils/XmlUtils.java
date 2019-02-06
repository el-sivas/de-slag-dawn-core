package de.slag.dawn.core.utils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlUtils {

	public void out(Object o, Consumer<String> consumer) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		marshal(o, o.getClass(), baos);
		try {
			consumer.accept(baos.toString(StandardCharsets.UTF_8.name()));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T in(Class<T> type, Supplier<String> supplier) {
		byte[] buf = supplier.get().getBytes(StandardCharsets.UTF_8);
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		return type.cast(unmarshall(type, bais));

	}

	private <T> Object unmarshall(Class<T> type, ByteArrayInputStream bais) {
		try {
			return createUnmarshaller(type).unmarshal(bais);
		} catch (JAXBException e) {
			throw new XmlUtilsException(e);
		}
	}

	private void marshal(Object o, Class<? extends Object> class1, ByteArrayOutputStream baos) {
		try {
			createMarshaller(class1).marshal(o, baos);
		} catch (JAXBException e) {
			throw new XmlUtilsException(e);
		}
	}

	private Marshaller createMarshaller(Class<? extends Object> class1) {
		try {
			return newInstance(class1).createMarshaller();
		} catch (JAXBException e) {
			throw new XmlUtilsException(e);
		}
	}

	private Unmarshaller createUnmarshaller(Class<? extends Object> class1) {
		try {
			return newInstance(class1).createUnmarshaller();
		} catch (JAXBException e) {
			throw new XmlUtilsException(e);
		}
	}

	private JAXBContext newInstance(Class<? extends Object> class1) {
		try {
			return JAXBContext.newInstance(class1);
		} catch (JAXBException e) {
			throw new XmlUtilsException(e);
		}
	}

	public class XmlUtilsException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public XmlUtilsException(Throwable t) {
			super(t);
		}

	}

}
